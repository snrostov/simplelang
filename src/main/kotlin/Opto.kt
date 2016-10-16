class ConstantPropagator : ValueVisitor<Expr, Any> {
    override fun visitConst(x: Const, a: Any): Expr = x

    override fun visitIf(x: If, a: Any): Expr {
        val cond = x.condition.accept(this, a)
        return if (cond is Const) {
            if (cond.v == true) x._then.accept(this, a)
            else x._else.accept(this, a)
        } else If(cond, x._then.accept(this, a), x._else.accept(this, a))
    }

    inline fun visitExpr(x: Fun, a: Any, factory: (List<Expr>) -> Expr): Expr {
        val inputs = x.inputs.map {
            it.accept(this, a)
        }

        return if (allIsConst(inputs)) Const(x.getValue())
        else factory(inputs)
    }

    override fun visitCycle(x: Cycle.Call, a: Any): Expr = visitExpr(x, a) { Cycle.Call(x.cycle, it) }

    override fun visitOp(x: Operator.Call, a: Any): Expr = visitExpr(x, a) { Operator.Call(x.op, it) }

    override fun visitUserFun(x: UserFun.Call, a: Any): Expr = visitExpr(x, a) { UserFun.Call(x.f, it) }

    override fun visitUserFunInput(x: UserFun.Input, a: Any): Expr = x

    override fun visitCycleVar(x: Cycle.Var, a: Any): Expr = x
}

fun allIsConst(inputs: List<Expr>) = inputs.map { it is Const }.reduce { a, b -> a && b }

interface PossibleValues

class PossibleValue : PossibleValues
class Union(val list: List<PossibleValue>) : PossibleValues
class Range(val min: Int, val max: Int) : PossibleValues