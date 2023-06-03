package com.mokulus.calculator.parser.functions

class AsinFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "asin"

    override fun invoke(x: Double): Double {
        return if (useRadians)
            kotlin.math.asin(x)
        else
            radToDeg(kotlin.math.asin(x))
    }

    override fun inverse(): Function {
        return SinFunction(useRadians)
    }
}