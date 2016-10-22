package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.Pattern
import org.srostov.simplelang.visitor.base.Transformer

object OptOptimizePatterns {
    val a = UnknownExpr("a")
    val b = UnknownExpr("b")
    val c = UnknownExpr("c")
    val c0 = 0.asConst
    val c1 = 1.asConst

    val leafReplacements = listOf(
            Pattern(a + c0, a),
            Pattern(a - a, c0),
            Pattern(a * c1, a),
            Pattern(a * c0, c0),
            Pattern(a / c1, a),
            Pattern(c0 * a, c0)
    )

    val variantsReplacements = listOf(
            Pattern(a + b, b + a),
            Pattern(a * b, b * a),
            Pattern((a + b) + c, a + (b + c)),
            Pattern(a * (b + c), a * b + a * c),

            Pattern(a + (b + c), (a + b) + c),
            Pattern(a * b + a * c, a * (b + c))
    )
}

class OpsOptimizer : Transformer<Unit>() {
    override fun visitOp(x: Operator.Call, a: Unit): Expr = with(x) {
        return tryVariant(x)
    }

    private fun tryVariant(call: Operator.Call, depth: Int = 1): Expr {
        if (depth > 5) return call

        val t = super.visitOp(call, Unit)
        if (t is Operator.Call) {
            OptOptimizePatterns.leafReplacements.forEach {
                val match = it.match(t)
                if (match != null) {
                    return match.transform(it.cause)
                }
            }

            OptOptimizePatterns.variantsReplacements.forEach {
                val match = it.match(t)
                if (match != null) {
                    return tryVariant(match.transform(it.cause) as Operator.Call, depth + 1)
                }
            }
        }
        return t
    }
}