package com.mokulus.calculator.parser.functions

class ExpFunction : Function() {
    override val name = "exp"

    override fun invoke(x: Double): Double {
        return kotlin.math.exp(x)
    }

    override fun inverse(): Function {
        return NaturalLogFunction()
    }
}