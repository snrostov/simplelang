package org.srostov.simplelang.visitor.optimize

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.Pattern
import org.srostov.simplelang.visitor.base.Transformer

object OptOptimizePatterns {
    val a = UnknownExpr("a")
    val b = UnknownExpr("b")
    val c = UnknownExpr("c")
    val d = UnknownExpr("d")
    val e = UnknownExpr("e")
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

    val compositionReplacements = listOf(
            Pattern(If(a, b, c),
                    If(not(a), c, b)),
            Pattern(If(a, If(b, c, c0), c0),
                    If(a and b, c, c0))
    )
}

class PatternsApplyer : Transformer<Unit>() {
    override fun visitFunCall(x: FunCall, a: Unit): Expr {
        return tryVariant(x)
    }

    private fun tryVariant(call: FunCall, depth: Int = 1): Expr {
        if (depth > 5) return call

        val t = super.visitFunCall(call, Unit)
        if (t is FunCall) {
            OptOptimizePatterns.leafReplacements.forEach {
                val match = it.match(t)
                if (match != null) {
                    return match.transform(it.cause)
                }
            }

            OptOptimizePatterns.variantsReplacements.forEach {
                val match = it.match(t)
                if (match != null) {
                    return tryVariant(match.transform(it.cause) as FunCall, depth + 1)
                }
            }
        }
        return t
    }
}