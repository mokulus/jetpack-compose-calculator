package com.mokulus.calculator.parser.operators

import com.mokulus.calculator.BinaryExpression
import com.mokulus.calculator.Expression

class SubtractionOperator : BinaryOperator() {
    override fun apply(left: Expression, right: Expression): Expression {
        return BinaryExpression({ a, b -> a - b}, left, right)
    }
}