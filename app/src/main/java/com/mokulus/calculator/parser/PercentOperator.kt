package com.mokulus.calculator.parser

import com.mokulus.calculator.Expression
import com.mokulus.calculator.UnaryExpression

class PercentOperator : UnaryPostfixOperator() {
    override fun apply(expr: Expression): Expression {
        return UnaryExpression({it / 100.0}, expr)
    }
}