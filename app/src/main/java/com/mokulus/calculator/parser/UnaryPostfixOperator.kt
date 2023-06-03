package com.mokulus.calculator.parser

import com.mokulus.calculator.Expression
import com.mokulus.calculator.OperatorType
import java.lang.IllegalArgumentException

sealed class UnaryPostfixOperator {
    abstract fun apply(expr : Expression) : Expression
    companion object {
        val operators = listOf(OperatorType.Percent, OperatorType.Factorial)
        fun get(type : OperatorType) : UnaryPostfixOperator {
            return when(type) {
                OperatorType.Percent -> PercentOperator()
                OperatorType.Factorial -> FactorialOperator()
                else -> throw IllegalArgumentException("Invalid operator type $type")
            }
        }
    }
}