import java.util.*

class Cycle(val condition: Var, val vals: List<Var>, val result: Expr) {
    class Var(val result: EvalExpr) : PreDefExpr() {
        override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitCycleVar(this, a)
    }

    class Call(val cycle: Cycle, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ValueVisitor<R, T>, a: T): R = v.visitCycle(this, a)

        override fun eval(c: EvalCtx, vals: List<Any>): Any {
            setVals(c, vals)
            val nextVals = ArrayList(vals)
            while (cycle.condition.getValue(c) == true) {
                vals.forEachIndexed { i, v ->
                    nextVals[i] = cycle.vals[i].result.eval(c)
                }
                setVals(c, nextVals)
            }
            return cycle.result.getValue(c)
        }

        private fun setVals(c: EvalCtx, vals: List<Any>) {
            vals.forEachIndexed { i, v -> c.values[cycle.vals[i]] = v }
        }
    }
}

fun cycle(b: CycleBuilder.() -> Expr): Expr {
    val b1 = CycleBuilder()
    b1.result = b(CycleBuilder())
    return b1.build()
}

class CycleBuilder {
    fun newVar(v: EvalExpr): Cycle.Var = Cycle.Var(v)
    var condition: Expr? = null
    var result: Expr? = null
    fun build(): Expr {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
