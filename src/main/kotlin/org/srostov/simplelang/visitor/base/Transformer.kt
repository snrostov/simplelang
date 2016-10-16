package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.*

open class Transformer : ExprVisitor<Expr, Any> {
    val cache: MutableMap<Expr, Expr> = hashMapOf()

    fun transform(x: Expr): Expr = cache.getOrPut(x) { x.accept(this, 0) }

    fun transform(list: List<Expr>) = list.map { transform(it) }

    override fun visitConst(x: ConstExpr, a: Any): Expr = x

    override fun visitIf(x: If, a: Any): Expr
            = If(transform(x.condition), transform(x._then), transform(x._else))

    open fun visitFun(x: Fun, create: (List<Expr>) -> Expr): Expr = create(transform(x.inputs))

    override fun visitCycle(x: Cycle.Call, a: Any): Expr = visitFun(x) { Cycle.Call(x.cycle, it) }

    override fun visitOp(x: Operator.Call, a: Any): Expr = visitFun(x) { Operator.Call(x.op, it) }

    override fun visitUserFun(x: UserFun.Call, a: Any): Expr = visitFun(x) { UserFun.Call(x.f, it) }

    override fun visitUserFunInput(x: UserFun.Input, a: Any): Expr = x

    override fun visitCycleVar(x: Cycle.Var, a: Any): Expr = x
}