package com.mokulus.calculator.parser.functions

class AcosFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "acos"

    override fun invoke(x: Double): Double {
        return if (useRadians)
            kotlin.math.acos(x)
        else
            radToDeg(kotlin.math.acos(x))
    }

    override fun inverse(): Function {
        return CosFunction(useRadians)
    }
}