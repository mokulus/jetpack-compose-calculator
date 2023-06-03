package com.mokulus.calculator.parser.functions

class SquareRootFunction : Function() {
    override val name = "sqrt"

    override fun invoke(x: Double): Double {
        return kotlin.math.sqrt(x)
    }

    override fun inverse(): Function? {
        return null
    }
}