package com.mokulus.calculator.parser

import com.mokulus.calculator.BinaryExpression
import com.mokulus.calculator.Expression
import kotlin.math.pow

class PowerOperator : BinaryOperator() {
    override fun apply(left: Expression, right: Expression): Expression {
        return BinaryExpression({ a, b -> a.pow(b) }, left, right, {
            a, b -> if (a < 0 && b % 0 != 0.0)
                throw DomainError("Non integer power of negative value")
        })
    }

    override fun associativity(): Associativity {
        return Associativity.Right
    }
}
