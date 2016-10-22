package org.srostov.simplelang

val Int.asConst: ConstExpr get() = ConstExpr(this)
val String.asConst: ConstExpr get() = ConstExpr(this)

operator fun Fun.invoke(vararg args: Expr) = FunCall(this, args.toList())

operator fun UserFun.invoke(vararg args: Expr): FunCall {
    check(args.size == this.args.size) { "Excepted ${this.args.size} args, ${args.size} got" }
    return FunCall(this, args.toList())
}

// logical operators
infix fun Expr.less(b: Expr): Expr = Less(this, b)
infix fun Expr.more(b: Expr): Expr = More(this, b)
infix fun Expr.eq(b: Expr): Expr = Equal(this, b)
infix fun Expr.and(b: Expr): Expr = And(this, b)
infix fun Expr.or(b: Expr): Expr = Or(this, b)
fun not(b: Expr): Expr = Not(b)

// arithmetic operators
operator fun Expr.plus(b: Expr) = Plus(this, b)
operator fun Expr.minus(b: Expr) = Minus(this, b)
operator fun Expr.times(b: Expr) = Mul(this, b)
operator fun Expr.div(b: Expr) = Div(this, b)
operator fun Expr.mod(b: Expr) = Mod(this, b)

// string
infix fun Expr.append(b: Expr) = Append(this, b)

// objects
//operator fun Expr.get(index: Expr) = Get.newCall(index)
//
//val Expr.length: Expr get() = GroupLength.newCall(this)
//
//fun group(vararg items: Expr) = Group(items.toList())