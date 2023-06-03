package com.mokulus.calculator.parser.functions

class CosFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "cos"

    override fun invoke(x: Double) : Double {
        return if (useRadians)
            kotlin.math.cos(x)
        else
            kotlin.math.cos(degToRad(x))
    }

    override fun inverse(): Function {
        return AcosFunction(useRadians)
    }
}