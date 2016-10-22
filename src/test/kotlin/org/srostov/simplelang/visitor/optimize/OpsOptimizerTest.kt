package org.srostov.simplelang.visitor.optimize

import org.junit.Assert
import org.junit.Test
import org.srostov.simplelang.*
import org.srostov.simplelang.visitor.toStr

class OpsOptimizerTest : BaseTest() {
    val x = UnknownExpr("x")
    val y = UnknownExpr("y")

    @Test
    fun test1() = Assert.assertEquals("x + x", optimize(x + 0.asConst + x + 0.asConst))

    @Test
    fun test2() = Assert.assertEquals("x", optimize(x * 0.asConst + x + 0.asConst))


    @Test
    fun test3() = Assert.assertEquals("y * x", optimize(x * (0.asConst + y)))

    private fun optimize(expr: Expr) = expr.accept(OpsOptimizer(), Unit).toStr()
}