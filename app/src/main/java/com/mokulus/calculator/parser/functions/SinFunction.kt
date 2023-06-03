package com.mokulus.calculator.parser.functions

class SinFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "sin"

    override fun invoke(x: Double) : Double {
        return if (useRadians)
            kotlin.math.sin(x)
        else
            kotlin.math.sin(degToRad(x))
    }

    override fun inverse(): Function {
        return AsinFunction(useRadians)
    }
}