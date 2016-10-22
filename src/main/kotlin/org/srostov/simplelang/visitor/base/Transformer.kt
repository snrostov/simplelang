package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.*

open class Transformer<in T> : ExprVisitor<Expr, T> {
    val cache: MutableMap<Expr, Expr> = hashMapOf()

    fun transform(x: Expr, a: T): Expr = cache.getOrPut(x) { x.accept(this, a) }

    fun transform(list: List<Expr>, a: T) = list.map { transform(it, a) }

    override fun visitFunCall(x: FunCall, a: T): Expr = FunCall(x.f, transform(x.inputs, a))

    override fun visitConst(x: ConstExpr, a: T): Expr = x

    override fun visitUserFunInput(x: UserFun.Arg, a: T): Expr = x

    override fun visitUnknownExpr(x: UnknownExpr, a: T): Expr = x
}