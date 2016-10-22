package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.*

open class Transformer<in T> : ExprVisitor<Expr, T> {
    val cache: MutableMap<Expr, Expr> = hashMapOf()

    fun transform(x: Expr, a: T): Expr = cache.getOrPut(x) { x.accept(this, a) }

    fun transform(list: List<Expr>, a: T) = list.map { transform(it, a) }

    override fun visitConst(x: ConstExpr, a: T): Expr = x

    open fun visitFun(x: Fun, a: T, create: (List<Expr>) -> Expr): Expr = create(transform(x.inputs, a))

    override fun visitIf(x: If, a: T): Expr = visitFun(x, a) { If(it[0], it[1], it[2]) }

    override fun visitLoop(x: Loop.Call, a: T): Expr = visitFun(x, a) { Loop.Call(x.loop, it) }

    override fun visitOp(x: Operator.Call, a: T): Expr = visitFun(x, a) { Operator.Call(x.op, it) }

    override fun visitUserFun(x: UserFun.Call, a: T): Expr = visitFun(x, a) { UserFun.Call(x.f, it) }

    override fun visitUserFunInput(x: UserFun.Arg, a: T): Expr = x

    override fun visitLoopVar(x: Loop.Var, a: T): Expr = x

    override fun visitUnknownExpr(x: UnknownExpr, a: T): Expr = x
}