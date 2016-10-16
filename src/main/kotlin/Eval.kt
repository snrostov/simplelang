import java.util.*

class EvalCtx() {
    val values: MutableMap<EvalExpr, Any> = HashMap()

    companion object {
        val empty = EvalCtx()
    }
}

class Eval : ValueVisitor<Any, EvalCtx> {
    fun evalInputs(x: Fun, a: EvalCtx) = x.inputs.map { it.accept(this, a) }

    inline fun cache(x: EvalExpr, a: EvalCtx, defaultValue: () -> Any): Any =
            a.values.getOrPut(x, defaultValue)

    override fun visitConst(x: Const, a: EvalCtx): Any = x.getValue()

    override fun visitIf(x: If, a: EvalCtx): Any =
            cache(x, a) {
                if (x.condition.accept(this, a) == true) x._then.accept(this, a)
                else x._else.accept(this, a)
            }

    override fun visitOp(x: Operator.Call, a: EvalCtx): Any = cache(x, a) { x.op(evalInputs(x, a)) }

    override fun visitUserFun(x: UserFun.Call, a: EvalCtx): Any = cache(x, a) {
        val ctx = EvalCtx()
        evalInputs(x, a).forEachIndexed { i, v -> ctx.values[x.f.inputs[i]] = v }
        return x.f.result.accept(this, ctx)
    }

    override fun visitCycle(x: Cycle.Call, a: EvalCtx): Any = cache(x, a) {

    }

    override fun visitCycleVar(x: Cycle.Var, a: EvalCtx): Any = cache(x, a) { throw IllegalStateException() }

    override fun visitUserFunInput(x: UserFun.Input, a: EvalCtx): Any = cache(x, a) { throw IllegalStateException() }
}