package org.srostov.simplelang

val Int.asConst: ConstExpr get() = ConstExpr(this)
val String.asConst: ConstExpr get() = ConstExpr(this)

fun Operator.newCall(vararg args: Expr) = Operator.Call(this, args.toList())

operator fun UserFun.invoke(vararg args: Expr): UserFun.Call {
    check(args.size == this.args.size) { "Excepted ${this.args.size} args, ${args.size} got" }
    return UserFun.Call(this, args.toList())
}

// logical operators
infix fun Expr.less(b: Expr): Expr = Less.newCall(this, b)
infix fun Expr.more(b: Expr): Expr = More.newCall(this, b)
infix fun Expr.eq(b: Expr): Expr = Equal.newCall(this, b)
infix fun Expr.and(b: Expr): Expr = And.newCall(this, b)
infix fun Expr.or(b: Expr): Expr = Or.newCall(this, b)
fun not(b: Expr): Expr = Not.newCall(b)

// arithmetic operators
operator fun Expr.plus(b: Expr) = Plus.newCall(this, b)
operator fun Expr.minus(b: Expr) = Minus.newCall(this, b)
operator fun Expr.times(b: Expr) = Mul.newCall(this, b)
operator fun Expr.div(b: Expr) = Div.newCall(this, b)
operator fun Expr.mod(b: Expr) = Mod.newCall(this, b)

// string
infix fun Expr.append(b: Expr) = Append.newCall(this, b)

// objects
//operator fun Expr.get(index: Expr) = Get.newCall(index)
//
//val Expr.length: Expr get() = GroupLength.newCall(this)
//
//fun group(vararg items: Expr) = Group(items.toList())

fun cycle(b: CycleBuilder.() -> Expr): Expr {
    val b1 = CycleBuilder()
    b1.result = b(CycleBuilder())
    return b1.build()
}

class CycleBuilder {
    fun newVar(v: VarExpr): Loop.Var = Loop.Var("", v)
    var condition: Expr? = null
    var result: Expr? = null
    fun build(): Expr {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}