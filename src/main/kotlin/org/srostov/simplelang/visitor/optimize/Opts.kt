package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.Expr
import org.srostov.simplelang.Operator
import org.srostov.simplelang.visitor.base.Transformer

class OptsOptimizer : Transformer() {
    override fun visitOp(x: Operator.Call, a: Any): Expr {
        return super.visitOp(x, a)
    }
}