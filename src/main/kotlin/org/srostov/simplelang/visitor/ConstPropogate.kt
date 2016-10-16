package org.srostov.simplelang.visitor

import org.srostov.simplelang.*

class ConstantPropagator : ExprVisitor<Expr, Any> {
    val cache: MutableMap<Expr, Expr> = hashMapOf()

    fun transform(x: Expr): Expr = cache.getOrPut(x) { x.accept(this, 0) }

    override fun visitConst(x: ConstExpr, a: Any): Expr = x

    override fun visitIf(x: If, a: Any): Expr {
        val cond = transform(x.condition)
        return if (cond is ConstExpr) {
            if (cond.v == true) transform(x._then) else transform(x._else)
        } else If(cond, transform(x._then), transform(x._else))
    }

    private inline fun visitFun(x: Fun, create: (List<Expr>) -> Expr): Expr {
        val inputs = x.inputs.map { transform(it) }

        return if (isConsts(inputs)) ConstExpr(x.eval()) else create(inputs)
    }

    override fun visitCycle(x: Cycle.Call, a: Any): Expr = visitFun(x) { Cycle.Call(x.cycle, it) }

    override fun visitOp(x: Operator.Call, a: Any): Expr = visitFun(x) { Operator.Call(x.op, it) }

    override fun visitUserFun(x: UserFun.Call, a: Any): Expr = visitFun(x) { UserFun.Call(x.f, it) }

    override fun visitUserFunInput(x: UserFun.Input, a: Any): Expr = x

    override fun visitCycleVar(x: Cycle.Var, a: Any): Expr = x
}

private fun isConsts(inputs: List<Expr>) = inputs.map { it is ConstExpr }.reduce { a, b -> a && b }