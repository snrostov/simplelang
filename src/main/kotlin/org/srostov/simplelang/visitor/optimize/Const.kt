package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.ConstExpr
import org.srostov.simplelang.Expr
import org.srostov.simplelang.Fun
import org.srostov.simplelang.visitor.base.Transformer
import org.srostov.simplelang.visitor.eval

class ConstantPropagator : Transformer<Unit>() {
    override fun visitFun(x: Fun, a: Unit, create: (List<Expr>) -> Expr): Expr {
        val inputs = transform(x.inputs, a)
        return if (isConsts(inputs)) ConstExpr(x.eval()) else create(inputs)
    }
}

private fun isConsts(inputs: List<Expr>): Boolean {
    inputs.forEach {
        if (it !is ConstExpr) return false
    }

    return true
}