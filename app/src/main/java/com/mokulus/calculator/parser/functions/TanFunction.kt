package com.mokulus.calculator.parser.functions

class TanFunction(override var useRadians: Boolean) : TrigFunction() {
    override val name = "tan"

    override fun invoke(x: Double) : Double {
        return if (useRadians)
            kotlin.math.tan(x)
        else
            kotlin.math.tan(degToRad(x))
    }

    override fun inverse(): Function {
        return AtanFunction(useRadians)
    }
}