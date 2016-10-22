package org.srostov.simplelang.visitor.base

import org.srostov.simplelang.ConstExpr
import org.srostov.simplelang.FunCall
import org.srostov.simplelang.UnknownExpr
import org.srostov.simplelang.UserFun

interface ExprVisitor<out R, in T> {
    fun visitConst(x: ConstExpr, a: T): R
    fun visitFunCall(x: FunCall, a: T): R
    fun visitUserFunInput(x: UserFun.Arg, a: T): R
    fun visitUnknownExpr(x: UnknownExpr, a: T): R
}