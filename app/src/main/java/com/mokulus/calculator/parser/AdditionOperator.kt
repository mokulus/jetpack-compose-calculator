package com.mokulus.calculator.parser

import com.mokulus.calculator.BinaryExpression
import com.mokulus.calculator.Expression

class AdditionOperator : BinaryOperator() {
    override fun apply(left: Expression, right : Expression): Expression {
        return BinaryExpression({ a, b -> a + b}, left, right)
    }
}