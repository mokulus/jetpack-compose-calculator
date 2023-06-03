package com.mokulus.calculator.parser.operators

import com.mokulus.calculator.BinaryExpression
import com.mokulus.calculator.Expression
import com.mokulus.calculator.parser.DomainError
import kotlin.math.pow

class PowerOperator : BinaryOperator() {
    override fun apply(left: Expression, right: Expression): Expression {
        return BinaryExpression({ a, b -> a.pow(b) }, left, right, {
            a, b -> if (a < 0 && b % 1.0 != 0.0)
                throw DomainError("Non integer power of negative value $a^$b")
        })
    }

    override fun associativity(): Associativity {
        return Associativity.Right
    }
}
