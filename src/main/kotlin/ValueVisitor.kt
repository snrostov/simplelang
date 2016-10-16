interface ValueVisitor<out R, in T> {
    fun visitConst(x: Const, a: T): R
    fun visitIf(x: If, a: T): R
    fun visitCycle(x: Cycle.Call, a: T): R
    fun visitOp(x: Operator.Call, a: T): R
    fun visitUserFun(x: UserFun.Call, a: T): R

    fun visitUserFunInput(x: UserFun.Input, a: T): R
    fun visitCycleVar(x: Cycle.Var, a: T): R
}