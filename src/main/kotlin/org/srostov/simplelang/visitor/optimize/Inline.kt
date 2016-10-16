package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.Expr
import org.srostov.simplelang.UserFun
import org.srostov.simplelang.visitor.base.Transformer

class UserFunInliner(val call: UserFun.Call) : Transformer() {
    override fun visitUserFunInput(x: UserFun.Arg, a: Any): Expr {
        return call.inputs[x.i]
    }
}