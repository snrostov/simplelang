package org.srostov.simplelang.visitor

import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.base.ExprVisitor
import java.util.*

class EvalCtx() {
    val values: MutableMap<VarExpr, Any> = HashMap()

    companion object {
        val empty = EvalCtx()
    }
}

object Evaluator : ExprVisitor<Any, EvalCtx> {
    fun eval(x: Expr, a: EvalCtx) = x.accept(this, a)

    fun evalCached(x: Expr, a: EvalCtx) =
            if (x is VarExpr) a.values.getOrPut(x) { eval(x, a) }
            else (x as ConstExpr).v

    fun evalInputs(x: Fun, a: EvalCtx) = x.inputs.map { evalCached(it, a) }

    override fun visitConst(x: ConstExpr, a: EvalCtx): Any = x.v

    override fun visitIf(x: If, a: EvalCtx): Any =
            if (evalCached(x.condition, a) == true) evalCached(x._then, a)
            else evalCached(x._else, a)

    override fun visitOp(x: Operator.Call, a: EvalCtx): Any = x.op(evalInputs(x, a))

    override fun visitUserFun(x: UserFun.Call, a: EvalCtx): Any {
        val ctx = EvalCtx()
        evalInputs(x, a).forEachIndexed { i, v -> ctx.values[x.f.inputs[i]] = v }
        return evalCached(x.f.result, ctx)
    }

    override fun visitCycle(x: Cycle.Call, a: EvalCtx): Any {
        fun setVarsVal(c: EvalCtx, vals: List<Any>) {
            vals.forEachIndexed { i, v -> c.values[x.cycle.vals[i]] = v }
        }

        val vals = evalInputs(x, a)
        setVarsVal(a, vals)
        val nextVals = ArrayList(vals)
        while (x.cycle.condition.accept(this, a) == true) {
            vals.forEachIndexed { i, v ->
                nextVals[i] = eval(x.cycle.vals[i].result, a)
            }
            setVarsVal(a, nextVals)
        }
        return eval(x.cycle.result, a)
    }

    override fun visitCycleVar(x: Cycle.Var, a: EvalCtx): Any = a.values[x]!!

    override fun visitUserFunInput(x: UserFun.Input, a: EvalCtx): Any = a.values[x]!!
}

fun Expr.eval() = Evaluator.eval(this, EvalCtx())