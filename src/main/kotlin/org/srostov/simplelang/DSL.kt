package org.srostov.simplelang

fun Operator.newCall(vararg args: Expr) = Operator.Call(this, args.toList())

operator fun UserFun.invoke(vararg args: Expr): UserFun.Call {
    check(args.size == this.args.size) { "Excepted ${this.args.size} args, ${args.size} got" }
    return UserFun.Call(this, args.toList())
}

//operator fun Expr.get(index: Expr) = Get.newCall(index)
//
//val Expr.length: Expr get() = GroupLength.newCall(this)

infix fun Expr.plus(b: Expr) = Plus.newCall(this, b)

infix fun Expr.append(b: Expr) = Append.newCall(this, b)

infix fun Expr.less(b: Expr): Expr = Less.newCall(this, b)

//fun group(vararg items: Expr) = Group(items.toList())

fun cycle(b: CycleBuilder.() -> Expr): Expr {
    val b1 = CycleBuilder()
    b1.result = b(CycleBuilder())
    return b1.build()
}

class CycleBuilder {
    fun newVar(v: VarExpr): Cycle.Var = Cycle.Var("", v)
    var condition: Expr? = null
    var result: Expr? = null
    fun build(): Expr {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}