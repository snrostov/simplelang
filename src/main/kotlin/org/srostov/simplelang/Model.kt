/**
 * Общая иеерархия:
 *
 * Expr - Любое выражение
 *      Const - Константа
 *      Var - Выражение значение которого не известно на этапе компиляции
 *          Ref - Ссылка на входящий параметр
 *              UserFun.Arg - Параметр пользовательской функции
 *              Cycle.Var   - Переменная, значение которой обновляется в цикле
 *              UnknownExpr - Используется для дебага
 *          Fun - Выражение, значение котрого зависит от входных параметров
 *              Op.Call - Базовая операция (вызов оператора)
 *              If - Выражение значение котрого зависит от указанного условия
 *              UserFun.Call - Вызов пользовательской функции
 *              Cycle.Call - Функция выполняещеся в цикле (частный случай функции)
 */
package org.srostov.simplelang

import org.srostov.simplelang.visitor.base.ExprVisitor
import org.srostov.simplelang.visitor.base.PrinterBase
import org.srostov.simplelang.visitor.toStr

abstract class Expr {
    abstract fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R

    override fun toString(): String = this.toStr()
}

abstract class VarExpr : Expr() {
    val usages: MutableList<FunCall> = arrayListOf()
}

class ConstExpr(val v: Any) : Expr() {
    override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitConst(this, a)
}

abstract class RefExpr : VarExpr()

class FunCall(val f: Fun, val inputs: List<Expr>) : VarExpr() {
    init {
        require(inputs.size == f.arity) { "Incorrect call of $f with (${inputs.joinToString()})" }
    }

    override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitFunCall(this, a)
}

abstract class Fun(val arity: Int)

abstract class BaseFun(arity: Int) : Fun(arity), (List<Any>) -> Any {
    abstract fun print(inputs: List<Expr>, p: PrinterBase)
}

class UserFun(val name: String, vararg args: String, resultBuilder: UserFun.() -> Expr) : Fun(args.size) {
    val args: List<Arg> = args.mapIndexed { i, s -> Arg(this, i, s) }
    var result: Expr = resultBuilder(this)

    class Arg(val f: UserFun, val i: Int, val name: String) : RefExpr() {
        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitUserFunInput(this, a)
    }

    override fun toString(): String = "$name(${args.joinToString()})"
}

class UnknownExpr(val name: String) : RefExpr() {
    override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitUnknownExpr(this, a)
}