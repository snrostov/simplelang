package org.srostov.simplelang

import org.srostov.simplelang.visitor.base.ExprVisitor

abstract class Operator() : (List<Any>) -> Any {
    class Call(val op: Operator, inputs: List<Expr>) : Fun(inputs) {
        override fun <R, T> accept(v: ExprVisitor<R, T>, a: T): R = v.visitOp(this, a)
    }

    abstract fun toString(inputs: List<Expr>): String
}

abstract class UnOp<A, R>(val symbol: String) : Operator() {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(args: List<Any>): Any = invokeUn(args[0] as A) as Any

    abstract fun invokeUn(a: A): R

    override fun toString(inputs: List<Expr>): String = "$symbol ${inputs[0]}"
}

abstract class BinOp<A, B, R>(val symbol: String) : Operator() {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(args: List<Any>): Any = invoke(args[0] as A, args[1] as B) as Any

    abstract fun invoke(a: A, b: B): R

    override fun toString(inputs: List<Expr>): String = "${inputs[0]} $symbol ${inputs[1]}"
}

interface Commutative

object Less : BinOp<Int, Int, Boolean>("<") {
    override fun invoke(a: Int, b: Int): Boolean = a < b
}

object More : BinOp<Int, Int, Boolean>(">") {
    override fun invoke(a: Int, b: Int): Boolean = a > b
}

object Equal : BinOp<Any, Any, Boolean>("="), Commutative {
    override fun invoke(a: Any, b: Any): Boolean = a == b
}

object And : BinOp<Boolean, Boolean, Boolean>("and"), Commutative {
    override fun invoke(a: Boolean, b: Boolean): Boolean = a && b
}

object Or : BinOp<Boolean, Boolean, Boolean>("or"), Commutative {
    override fun invoke(a: Boolean, b: Boolean): Boolean = a || b
}

object Not : UnOp<Boolean, Boolean>("not") {
    override fun invokeUn(a: Boolean): Boolean = !a
}

object Append : BinOp<Any, Any, String>("append") {
    override fun invoke(a: Any, b: Any): String = a.toString() + b.toString()
}

object Plus : BinOp<Int, Int, Int>("+"), Commutative {
    override fun invoke(a: Int, b: Int): Int = a + b
}

object Minus : BinOp<Int, Int, Int>("-") {
    override fun invoke(a: Int, b: Int): Int = a - b
}

object Mul : BinOp<Int, Int, Int>("*"), Commutative {
    override fun invoke(a: Int, b: Int): Int = a * b
}

object Div : BinOp<Int, Int, Int>("/") {
    override fun invoke(a: Int, b: Int): Int = a / b
}

object Mod : BinOp<Int, Int, Int>("%") {
    override fun invoke(a: Int, b: Int): Int = a % b
}

object Group : Operator() {
    override fun toString(inputs: List<Expr>): String = "[${inputs.joinToString()}]"

    override fun invoke(args: List<Any>): Any = args
}

object Get : Operator() {
    override fun toString(inputs: List<Expr>): String = "${inputs[0]}[${inputs[1]}]"

    override fun invoke(args: List<Any>): Any = (args[0] as List<*>)[args[1] as Int]!!
}

object GroupLength : Operator() {
    override fun toString(inputs: List<Expr>): String = "${inputs[0]}.size"

    override fun invoke(args: List<Any>): Any = (args[0] as List<*>).size
}
