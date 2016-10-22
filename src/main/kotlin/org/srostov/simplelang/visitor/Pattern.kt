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

        private fun visitInputs(patternInputs: List<Expr>, targetInputs: List<Expr>): Boolean {
            patternInputs.forEachIndexed { i, patternInput ->
                val targetInput = targetInputs[i]
                if (!patternInput.accept(this, targetInput)) {
                    return false
                }
            }

            return true
        }

        override fun visitOp(x: Operator.Call, a: Expr): Boolean
                = a is Operator.Call && a.op == x.op && visitInputs(x.inputs, a.inputs)

        override fun visitUserFun(x: UserFun.Call, a: Expr): Boolean
                = a is UserFun.Call && a.f == x.f && visitInputs(x.inputs, a.inputs)

        override fun visitIf(x: If, a: Expr): Boolean
                = a is If && x.condition.accept(this, a) && x._then.accept(this, a) && x._else.accept(this, a)

        override fun visitUserFunInput(x: UserFun.Arg, a: Expr): Boolean = throw IllegalStateException()

        override fun visitUnknownExpr(x: UnknownExpr, a: Expr): Boolean {
            matches[x] = a
            return true
        }

        override fun visitLoop(x: Loop.Call, a: Expr): Boolean = throw UnsupportedOperationException()

        override fun visitLoopVar(x: Loop.Var, a: Expr): Boolean = throw UnsupportedOperationException()
    }
}