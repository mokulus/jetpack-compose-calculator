package com.mokulus.calculator.parser.functions

class AtanFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "atan"

    override fun invoke(x: Double): Double {
        return if (useRadians)
            kotlin.math.atan(x)
        else
            radToDeg(kotlin.math.atan(x))
    }

    override fun inverse(): Function {
        return TanFunction(useRadians)
    }
}