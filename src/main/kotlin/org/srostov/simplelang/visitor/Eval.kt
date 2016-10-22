package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.ExprVisitor
import java.util.*

private class EvalCtx() {
    val values: MutableMap<VarExpr, Any> = HashMap()

    companion object {
        val empty = EvalCtx()
    }
}

private object Evaluator : ExprVisitor<Any, EvalCtx> {
    fun eval(x: Expr, a: EvalCtx) = x.accept(this, a)

    fun evalCached(x: Expr, a: EvalCtx) =
            if (x is VarExpr) a.values.getOrPut(x) { eval(x, a) }
            else (x as ConstExpr).v

    fun evalInputs(x: FunCall, a: EvalCtx) = x.inputs.map { evalCached(it, a) }

    override fun visitFunCall(x: FunCall, a: EvalCtx): Any {
        if (x.f is UserFun) {
            val ctx = EvalCtx()
            evalInputs(x, a).forEachIndexed { i, v -> ctx.values[x.f.args[i]] = v }
            return evalCached(x.f.result, ctx)
        } else {
            return (x.f as BaseFun).invoke(evalInputs(x, a))
        }
    }

    override fun visitConst(x: ConstExpr, a: EvalCtx): Any = x.v

    override fun visitUserFunInput(x: UserFun.Arg, a: EvalCtx): Any = a.values[x]!!

    override fun visitUnknownExpr(x: UnknownExpr, a: EvalCtx): Any = "unknown"
}

fun Expr.eval() = Evaluator.eval(this, EvalCtx.empty)