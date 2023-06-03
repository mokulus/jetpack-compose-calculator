package com.mokulus.calculator.parser

import com.mokulus.calculator.ConstantExpression
import com.mokulus.calculator.ConstantType
import com.mokulus.calculator.Expression
import com.mokulus.calculator.LeftParen
import com.mokulus.calculator.Lexeme
import com.mokulus.calculator.Name
import com.mokulus.calculator.Number
import com.mokulus.calculator.NumberExpression
import com.mokulus.calculator.Operator
import com.mokulus.calculator.OperatorType
import com.mokulus.calculator.RightParen
import com.mokulus.calculator.UnaryExpression

abstract class ParserException(msg: String) : Exception(msg) {}

class UnexpectedTokenException(val token : Lexeme) : ParserException("Unexpected token ${token.text}")

class NotEnoughTokensException() : ParserException("Unfinished expression")

class DomainError(msg: String) : ParserException(msg)

class Parser(val lexemes: List<Lexeme>, val useDegrees : Boolean = false) {
    val value = 0f
    var position = 0
    private val degToRad = kotlin.math.PI / 180
    private val radToDeg = 1 / degToRad
    val funcMap = mapOf<String, (Double) -> Double>(
        "sin" to if (!useDegrees) { { kotlin.math.sin(it) } } else { { kotlin.math.sin(degToRad * it) } },
        "cos" to if (!useDegrees) { { kotlin.math.cos(it) } } else { { kotlin.math.cos(degToRad * it) } },
        "tan" to if (!useDegrees) { { kotlin.math.tan(it) } } else { { kotlin.math.tan(degToRad * it) } },

        "asin" to if (!useDegrees) { { kotlin.math.asin(it) } } else { { radToDeg * kotlin.math.asin(it) } },
        "acos" to if (!useDegrees) { { kotlin.math.acos(it) } } else { { radToDeg * kotlin.math.acos(it) } },
        "atan" to if (!useDegrees) { { kotlin.math.atan(it) } } else { { radToDeg * kotlin.math.atan(it) } },

        "exp" to { kotlin.math.exp(it) },
        "ln" to { kotlin.math.ln(it) },

        "log" to { kotlin.math.log10(it) },

        "sqrt" to { kotlin.math.sqrt(it) },
    )
    val variableMap = mapOf(
        "pi" to ConstantType.PI,
        "e" to ConstantType.E)

    private fun peek() : Lexeme? {
        if (lexemes.size != position)
            return lexemes[position]
        else
            return null
    }

    private fun expect(cond: (Lexeme) -> Boolean) {
        if (lexemes.size == position) {
            throw NotEnoughTokensException()
        }
        if (cond(lexemes[position])) {
            position++
            return
        }
        throw UnexpectedTokenException(lexemes[position])
    }

    private fun primaryExpression(): Expression {
        var ret : Expression? = null
        if (position == lexemes.size)
            throw NotEnoughTokensException()
        val lexeme = lexemes[position++]
        if (lexeme is Number) {
            ret = NumberExpression(lexeme.value)
        } else if (lexeme is Name) {
            if (lexeme.text in funcMap.keys) {
                expect { it is LeftParen }
                val expr = operatorExpression()
                expect { it is RightParen }
                ret =  UnaryExpression(funcMap[lexeme.text]!!, expr)
            } else if (lexeme.text in variableMap.keys) {
                ret = ConstantExpression(variableMap[lexeme.text]!!)
            } else
                throw UnexpectedTokenException(lexeme)
        } else if (lexeme is LeftParen) {
            val expr = operatorExpression()
            expect { it is RightParen }
            ret = expr
        } else if (lexeme is Operator && lexeme.type == OperatorType.Minus) {
            val expr = operatorExpression()
            ret = UnaryExpression({ -it }, expr)
        }
        if (ret == null)
            throw UnexpectedTokenException(lexeme)
        val current = peek()
        if (current != null && current is Operator && current.type in UnaryPostfixOperator.operators) {
            ret = UnaryPostfixOperator.get(current.type).apply(ret)
            position++
        }
        return ret
    }

    private fun operatorExpression(depth: Int = 0) : Expression {
        if (depth == BinaryOperator.precedence.size)
            return primaryExpression()
        var expr = operatorExpression(depth + 1)
        if (position == lexemes.size)
            return expr
        var lexeme = lexemes[position]
        while (lexeme is Operator && (lexeme.type in BinaryOperator.precedence.reversed()[depth])) {
            position++
            val op = BinaryOperator.get(lexeme.type)
            if (op.associativity() == Associativity.Left) {
                val rhs = operatorExpression(depth + 1)
                expr = op.apply(expr, rhs)
            } else {
                val rhs = operatorExpression(depth)
                expr = op.apply(expr, rhs)
            }
            if (position == lexemes.size)
                break
            lexeme = lexemes[position]
        }
        return expr
    }

    fun parse(): Expression {
        if (lexemes.isEmpty())
            return NumberExpression(0.0)
        position = 0
        val expr = operatorExpression()
        if (position != lexemes.size)
            throw UnexpectedTokenException(lexemes[position])
        return expr
    }
}