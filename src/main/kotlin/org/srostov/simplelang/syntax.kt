package org.srostov.simplelang

operator fun UserFun.invoke(vararg args: Expr) = UserFun.Call(this, args.toList())

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