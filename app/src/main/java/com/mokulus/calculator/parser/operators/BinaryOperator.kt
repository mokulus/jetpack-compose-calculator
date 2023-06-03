package com.mokulus.calculator.parser.operators

import com.mokulus.calculator.Expression
import com.mokulus.calculator.OperatorType

enum class Associativity {
    Left,
    Right
}

sealed class BinaryOperator {
    abstract fun apply(left : Expression, right : Expression) : Expression
    open fun associativity() : Associativity {
        return Associativity.Left
    }

    companion object {
        val operators = listOf(
            OperatorType.Plus,
            OperatorType.Minus,
            OperatorType.Multiply,
            OperatorType.Divide,
            OperatorType.Power)
        val precedence = listOf(
            listOf(
                OperatorType.Power
            ),
            listOf(
                OperatorType.Multiply,
                OperatorType.Divide
            ),
            listOf(
                OperatorType.Plus,
                OperatorType.Minus
            )
        )
        fun get(type : OperatorType) : BinaryOperator {
            return when(type) {
                OperatorType.Plus -> AdditionOperator()
                OperatorType.Minus -> SubtractionOperator()
                OperatorType.Multiply -> MultiplicationOperator()
                OperatorType.Divide -> DivisionOperator()
                OperatorType.Power -> PowerOperator()
                else -> throw IllegalArgumentException("Invalid operator type $type")
            }
        }
    }
}