package com.mokulus.calculator

import java.lang.IllegalArgumentException

sealed class Lexeme(open val text : String, open val position : Int) {}

data class LeftParen(override val position: Int) : Lexeme("(", position)

data class RightParen(override val position: Int) : Lexeme(")", position)

data class Number(override val text : String, override val position: Int) : Lexeme(text, position) {
    val value : Double = text.toDouble()
}

data class Name(override val text : String, override val position: Int) : Lexeme(text, position)

enum class OperatorType {
    Plus,
    Minus,
    Multiply,
    Divide,
    Percent,
    Factorial,
    Power
}

data class Operator(override val text : String, override val position: Int) : Lexeme(text, position) {
    companion object {
        val operators = listOf("+", "-", "*", "/", "%", "!", "^")
    }
    val type : OperatorType = when (text) {
        "+" -> OperatorType.Plus
        "-" -> OperatorType.Minus
        "*" -> OperatorType.Multiply
        "/" -> OperatorType.Divide
        "%" -> OperatorType.Percent
        "!" -> OperatorType.Factorial
        "^" -> OperatorType.Power
        else -> throw IllegalArgumentException("Unknown operator ${text}")
    }
}