abstract class Operator() : (List<Any>) -> Any {
    class Call(val op: Operator, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitOp(this, a)

        override fun eval(c: EvalCtx, vals: List<Any>) = op(vals)

        override fun toString(): String = op.toString(inputs)
    }

    abstract fun toString(inputs: List<Expr>): String
}

abstract class BinOp<A, B, R>(val symbol: String): Operator() {

    @Suppress("UNCHECKED_CAST")
    override fun invoke(args: List<Any>): Any = invoke(args[0] as A, args[1] as B) as Any

    abstract fun invoke(a: A, b: B): R

    override fun toString(inputs: List<Expr>): String = "${inputs[0]} $symbol ${inputs[1]}"
}

object Less : BinOp<Int, Int, Boolean>("<") {
    override fun invoke(a: Int, b: Int): Boolean = a < b
}

object Append : BinOp<Any, Any, String>("append") {
    override fun invoke(a: Any, b: Any): String = a.toString() + b.toString()
}

object Plus : BinOp<Int, Int, Int>("+") {
    override fun invoke(a: Int, b: Int): Int = a + b
}

object Group : Operator() {
    override fun toString(inputs: List<Expr>): String = "[${inputs.joinToString()}]"

    override fun invoke(args: List<Any>): Any = args
}

object Get : Operator() {
    override fun toString(inputs: List<Expr>): String = "${inputs[0]}[${inputs[1]}]"

    override fun invoke(args: List<Any>): Any = (args[0] as List<*>)[args[1] as Int]!!
}

object GroupLength : Operator() {
    override fun toString(inputs: List<Expr>): String = "${inputs[0]}.size"

    override fun invoke(args: List<Any>): Any = (args[0] as List<*>).size
}

operator fun Expr.get(index: Expr) = Operator.Call(Get, arrayListOf(this, index))

val Expr.length: Expr get() = Operator.Call(GroupLength, arrayListOf(this))

infix fun Expr.plus(b: Expr) = Operator.Call(Plus, arrayListOf(this, b))

infix fun Expr.append(b: Expr) = Operator.Call(Append, arrayListOf(this, b))

infix fun Expr.less(b: Expr): Expr = Operator.Call(Less, arrayListOf(this, b))

fun group(vararg items: Expr) = Group(items.toList())
