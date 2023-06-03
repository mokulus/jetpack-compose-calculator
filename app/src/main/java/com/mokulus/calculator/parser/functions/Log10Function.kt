package com.mokulus.calculator.parser.functions

class Log10Function : Function() {
    override val name = "log"

    override fun invoke(x: Double): Double {
        return kotlin.math.log10(x)
    }

    override fun inverse(): Function? {
        return null
    }

}