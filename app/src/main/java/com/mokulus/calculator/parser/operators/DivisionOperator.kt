package com.mokulus.calculator.parser.operators

import com.mokulus.calculator.BinaryExpression
import com.mokulus.calculator.Expression
import com.mokulus.calculator.parser.DomainError

class DivisionOperator : BinaryOperator() {
    override fun apply(left: Expression, right: Expression): Expression {
        return BinaryExpression({ a, b -> a / b }, left, right)
            { a, b -> if (b == 0.0) throw DomainError("Divide by zero") }
    }
}

