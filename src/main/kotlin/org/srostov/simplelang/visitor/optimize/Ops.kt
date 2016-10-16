@file:Suppress("NAME_SHADOWING")

package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.Transformer
import java.util.function.BinaryOperator

class OpsOptimizer : Transformer() {
    override fun visitOp(x: Operator.Call, a: Any): Expr = with(x) {
        if (op is BinaryOperator<*>) {
            var (a, b) = inputs

            if (op is Commutative) {
                // a - simple val, b - fun
                if (a is Fun && b !is Fun) {
                    val t = b; b = a; a = t // swap
                }
            }

            if (op is Plus) {
                if (a is ConstExpr && a.v == 0) return transform(b)
            }

            if (op is Minus) {
                if (a == b) return ConstExpr(0)
                if (b is ConstExpr && b.v == 0) return transform(b)
            }

            if (op is Mul) {
                if (a is ConstExpr && a.v == 0) return transform(ConstExpr(0))
                if (a is ConstExpr && a.v == 1) return transform(b)
            }

            if (op is Div) {
                if (a == b) return ConstExpr(1)
                if (b is ConstExpr && b.v == 1) return transform(a)
            }
        }

        return super.visitOp(x, a)
    }
}