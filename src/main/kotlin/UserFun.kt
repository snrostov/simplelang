class UserFun(val name: String, args: Int, resultBuilder: UserFun.() -> Expr) {
    val inputs: List<Input> = (0..args).map { Input(this, it) }
    val result: Expr = resultBuilder(this)

    operator fun invoke(vararg args: Expr) = Call(this, args.toList())

    override fun toString(): String = "Function $name(${inputs.size})"

    class Input(val f: UserFun, val i: Int) : PreDefExpr() {
        override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitUserFunInput(this, a)

        override fun toString(): String = "arg${i+1}"
    }

    class Call(val f: UserFun, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitUserFun(this, a)

        override fun eval(c: EvalCtx, vals: List<Any>): Any {
            val ctx = EvalCtx()
            vals.forEachIndexed { i, v -> ctx.values[f.inputs[i]] = v }
            return f.result.getValue(ctx)
        }

        override fun toString(): String = "${f.name}(${inputs.joinToString()})"
    }
}