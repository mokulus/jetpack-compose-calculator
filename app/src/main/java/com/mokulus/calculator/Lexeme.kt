package com.mokulus.calculator

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
    Power;

    fun getSymbol() : String {
        return when (this) {
            Plus -> "+"
            Minus -> "-"
            Multiply -> "×"
            Divide -> "÷"
            Percent -> "%"
            Factorial -> "!"
            Power -> "^"
        }
    }
}

data class Operator(override val text : String, override val position: Int) : Lexeme(text, position) {
    companion object {
        val operators = OperatorType.values().map { it.getSymbol() }
    }

    val type : OperatorType
    init {
        val map = OperatorType.values().associateWith { it.getSymbol() }
        val match = map.filter { it.value == text }.keys.firstOrNull()
        if (match == null)
            throw IllegalArgumentException("Unknown operator ${text}")
        else
            type = match
    }
}