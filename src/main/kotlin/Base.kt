abstract class Expr {
    abstract fun getValue(c: EvalCtx = EvalCtx.empty): Any

    abstract fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R
}

abstract class EvalExpr : Expr() {
    override fun getValue(c: EvalCtx): Any = c.values.getOrPut(this) { eval(c) }

    abstract internal fun eval(c: EvalCtx): Any

    val usages: MutableList<Fun> = arrayListOf()
}

abstract class PreDefExpr : EvalExpr() {
    override fun eval(c: EvalCtx): Any {
        throw IllegalStateException("$this should be defined in call site")
    }
}

class Const(val v: Any) : Expr() {
    override fun getValue(c: EvalCtx) = v

    override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitConst(this, a)

    override fun toString(): String =
        if (v is String) "\"$v\"" else v.toString()
}

abstract class Fun(val inputs: List<Expr>) : EvalExpr() {
    init {
        inputs.forEach {
            if (it is EvalExpr) it.usages.add(this)
        }
    }

    override fun eval(c: EvalCtx): Any = eval(c, inputs.map { it.getValue(c) })

    abstract fun eval(c: EvalCtx, vals: List<Any>): Any
}

class If(val condition: Expr, val _then: Expr, val _else: Expr) : EvalExpr() {
    override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitIf(this, a)

    override fun eval(c: EvalCtx): Any
            = if (condition.getValue(c) == true) _then.getValue(c) else _else.getValue(c)

    override fun toString(): String {
        return "if ($condition) $_then else $_else"
    }
}
