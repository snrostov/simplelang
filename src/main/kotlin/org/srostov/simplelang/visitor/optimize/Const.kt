package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.ConstExpr
import org.srostov.simplelang.Expr
import org.srostov.simplelang.Fun
import org.srostov.simplelang.If
import org.srostov.simplelang.visitor.base.Transformer
import org.srostov.simplelang.visitor.eval

class ConstantPropagator : Transformer() {
    override fun visitIf(x: If, a: Any): Expr {
        val cond = transform(x.condition)
        return if (cond is ConstExpr) {
            if (cond.v == true) transform(x._then) else transform(x._else)
        } else If(cond, transform(x._then), transform(x._else))
    }

    override fun visitFun(x: Fun, create: (List<Expr>) -> Expr): Expr {
        val inputs = transform(x.inputs)
        return if (isConsts(inputs)) ConstExpr(x.eval()) else create(inputs)
    }
}

private fun isConsts(inputs: List<Expr>) = inputs.map { it is ConstExpr }.reduce { a, b -> a && b }