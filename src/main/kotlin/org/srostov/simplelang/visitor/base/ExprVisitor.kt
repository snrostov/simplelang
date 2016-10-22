package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.*

interface ExprVisitor<out R, in T> {
    fun visitConst(x: ConstExpr, a: T): R
    fun visitIf(x: If, a: T): R
    fun visitLoop(x: Loop.Call, a: T): R
    fun visitOp(x: Operator.Call, a: T): R
    fun visitUserFun(x: UserFun.Call, a: T): R

    fun visitUserFunInput(x: UserFun.Arg, a: T): R
    fun visitLoopVar(x: Loop.Var, a: T): R

    fun visitUnknownExpr(x: UnknownExpr, a: T): R
}