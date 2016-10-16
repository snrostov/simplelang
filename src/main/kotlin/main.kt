interface TplEntry


fun applyTpl(tpl: List<TplEntry>, data: List<*>) {

}

fun main(args: Array<String>) {
    val f = UserFun("f", 2) {
        val (i, n) = inputs
        val f = this

        If(
                i less n,
                _then = Const("number ")
                        append i
                        append Const(", ")
                        append f(i plus Const(1), n),
                _else = Const("")
        )
    }

    val f1 = f(Const(1), Const(10))

    val accept = f1.accept(ConstantPropagator(), 1)

    val message = f1.getValue()
    println(message)
}

fun test(int: Int) {
    var x = ""

    for (it in 1..int) {
        x += "number $it!, "
    }

    println(x)
}

fun test_recursive(i: Int, max: Int): String =
        if (i < max) "number $i, " + test_recursive(i + 1, max) else ""

fun test_recursive2(i: Int, max: Int, result: String): String =
        if (i < max) test_recursive2(i + 1, max, result + "number $i, ") else result