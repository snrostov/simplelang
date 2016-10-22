package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.Expr
import org.srostov.simplelang.UserFun
import org.srostov.simplelang.visitor.base.Transformer

open class InlineRecursive(val maxDepth: Int) : Transformer<Unit>() {
    override fun visitUserFun(x: UserFun.Call, a: Unit): Expr {
        val c = (super.visitUserFun(x, a) as UserFun.Call)
        return if (maxDepth > 0) c.inlineRecursive(maxDepth - 1) else c
    }
}

open class ReplaceFunInputs(val call: UserFun.Call, maxDepth: Int) : InlineRecursive(maxDepth) {
    override fun visitUserFunInput(x: UserFun.Arg, a: Unit): Expr {
        return call.inputs[x.i]
    }
}

fun Expr.inlineRecursive(maxDepth: Int): Expr
        = accept(InlineRecursive(maxDepth), Unit)

fun UserFun.Call.inlineRecursive(maxDepth: Int): Expr
        = f.result.accept(ReplaceFunInputs(this, maxDepth), Unit)