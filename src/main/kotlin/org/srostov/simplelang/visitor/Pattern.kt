package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.ExprVisitor
import org.srostov.simplelang.visitor.base.Transformer

class Pattern(val match: Expr, val cause: Expr) {
    fun match(x: Expr): Matcher? {
        val m = Matcher()
        if (match.accept(m, x)) return m else return null
    }

    // pattern = x, a = to match
    class Matcher() : ExprVisitor<Boolean, Expr> {
        val matches: MutableMap<UnknownExpr, Expr> = hashMapOf()

        fun transform(cause: Expr): Expr = cause.accept(object : Transformer<Unit>() {
            override fun visitUnknownExpr(x: UnknownExpr, a: Unit) = matches[x]!!
        }, Unit)

        override fun visitConst(x: ConstExpr, a: Expr): Boolean = a is ConstExpr && a.v == x.v

        /**
         * @return true, если все аргрументы соотвествуют шаблону
         */
        private fun matchInputs(patternInputs: List<Expr>, targetInputs: List<Expr>): Boolean {
            patternInputs.forEachIndexed { i, patternInput ->
                val targetInput = targetInputs[i]
                if (!patternInput.accept(this, targetInput)) {
                    return false
                }
            }

            return true
        }

        override fun visitFunCall(x: FunCall, a: Expr): Boolean
            = a is FunCall && x.f == a.f && matchInputs(x.inputs, a.inputs)

        override fun visitUserFunInput(x: UserFun.Arg, a: Expr): Boolean = throw IllegalStateException()

        override fun visitUnknownExpr(x: UnknownExpr, a: Expr): Boolean {
            matches[x] = a
            return true
        }
    }
}