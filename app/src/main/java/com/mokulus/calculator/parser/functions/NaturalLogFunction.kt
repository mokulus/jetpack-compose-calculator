package com.mokulus.calculator.parser.functions

class NaturalLogFunction : Function() {
    override val name = "ln"

    override fun invoke(x: Double): Double {
        return kotlin.math.ln(x)
    }

    override fun inverse(): Function {
        return ExpFunction()
    }
}