/**
 * Expr
 *      Const
 *      Var
 *          Ref
 *              UserFun.Arg
 *              Cycle.Var
 *          Fun
 *              If
 *              Cycle
 *              UserFun
 */
package org.srostov.simplelang

import org.srostov.simplelang.visitor.base.ExprVisitor

abstract class Expr {
    abstract fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R
}

abstract class VarExpr : Expr() {
    val usages: MutableList<Fun> = arrayListOf()
}

class ConstExpr(val v: Any) : Expr() {
    override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitConst(this, a)

}

abstract class RefExpr : VarExpr()

abstract class Fun(val inputs: List<Expr>) : VarExpr() {
    init {
        inputs.forEach {
            if (it is VarExpr) it.usages.add(this)
        }
    }
}

class If(val condition: Expr, val _then: Expr, val _else: Expr) : Fun(listOf(condition, _then, _else)) {
    override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitIf(this, a)
}

class UserFun(val name: String, args: Int, resultBuilder: UserFun.() -> Expr) {
    val args: List<Arg> = (0..args).map { Arg(this, it) }
    val result: Expr = resultBuilder(this)

    class Arg(val f: UserFun, val i: Int) : RefExpr() {
        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitUserFunInput(this, a)
    }

    class Call(val f: UserFun, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitUserFun(this, a)
    }
}

class Cycle(val condition: Var, val vals: List<Var>, val result: Expr) {
    class Var(val name: String, val result: VarExpr) : RefExpr() {
        override fun toString(): String {
            return name
        }

        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitCycleVar(this, a)
    }

    class Call(val cycle: Cycle, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitCycle(this, a)
    }
}