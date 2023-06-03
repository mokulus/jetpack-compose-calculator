package com.mokulus.calculator.parser

import com.mokulus.calculator.ConstantExpression

sealed class Constant(val text: String, val value : Double) {
    companion object {
        fun constants() : List<Constant> {
            return listOf(
                PiConstant(),
                EConstant(),
            )
        }
    }
    fun expression() : ConstantExpression {
        return ConstantExpression(value)
    }
}

class PiConstant : Constant("π", kotlin.math.PI)

class EConstant : Constant("e", kotlin.math.E)