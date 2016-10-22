package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.Expr
import org.srostov.simplelang.FunCall
import org.srostov.simplelang.UserFun
import org.srostov.simplelang.visitor.base.Transformer

open class InlineRecursive(val maxDepth: Int) : Transformer<Unit>() {
    override fun visitFunCall(x: FunCall, a: Unit): Expr {
        val c = (super.visitFunCall(x, a) as FunCall)
        return if (x.f is UserFun && maxDepth > 0) c.inlineRecursive(maxDepth - 1) else c
    }
}

open class ReplaceFunInputs(val call: FunCall, maxDepth: Int) : InlineRecursive(maxDepth) {
    override fun visitUserFunInput(x: UserFun.Arg, a: Unit): Expr {
        return call.inputs[x.i]
    }
}

fun Expr.inlineRecursive(maxDepth: Int): Expr
        = accept(InlineRecursive(maxDepth), Unit)

fun FunCall.inlineRecursive(maxDepth: Int): Expr
        = (f as UserFun).result.accept(ReplaceFunInputs(this, maxDepth), Unit)