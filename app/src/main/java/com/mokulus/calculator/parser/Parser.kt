package com.mokulus.calculator.parser

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
import com.mokulus.calculator.parser.functions.AcosFunction
import com.mokulus.calculator.parser.functions.AsinFunction
import com.mokulus.calculator.parser.functions.AtanFunction
import com.mokulus.calculator.parser.functions.CosFunction
import com.mokulus.calculator.parser.functions.ExpFunction
import com.mokulus.calculator.parser.functions.Log10Function
import com.mokulus.calculator.parser.functions.NaturalLogFunction
import com.mokulus.calculator.parser.functions.SinFunction
import com.mokulus.calculator.parser.functions.SquareRootFunction
import com.mokulus.calculator.parser.functions.TanFunction
import com.mokulus.calculator.parser.operators.Associativity
import com.mokulus.calculator.parser.operators.BinaryOperator
import com.mokulus.calculator.parser.operators.UnaryPostfixOperator

abstract class ParserException(msg: String) : Exception(msg) {}

class UnexpectedTokenException(val token : Lexeme) : ParserException("Unexpected token ${token.text}")

class NotEnoughTokensException : ParserException("Unfinished expression")

class DomainError(msg: String) : ParserException(msg)

class Parser(val lexemes: List<Lexeme>, val useDegrees : Boolean = false) {
    val value = 0f
    var position = 0

    val funcionList = listOf(
        SinFunction(useRadians = !useDegrees),
        CosFunction(useRadians = !useDegrees),
        TanFunction(useRadians = !useDegrees),
        AsinFunction(useRadians = !useDegrees),
        AcosFunction(useRadians = !useDegrees),
        AtanFunction(useRadians = !useDegrees),
        ExpFunction(),
        NaturalLogFunction(),
        Log10Function(),
        SquareRootFunction()
    )

    val constants = Constant.constants()

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
            if (lexeme.text in funcionList.map { it.name }) {
                expect { it is LeftParen }
                val expr = operatorExpression()
                expect { it is RightParen }
                val function = funcionList.find { lexeme.text == it.name }!!
                ret =  UnaryExpression({function.invoke(it)}, expr)
            } else if (lexeme.text in constants.map { it.text }) {
                val constant = constants.find { lexeme.text == it.text }!!
                ret = constant.expression()
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
                val rhs = operatorExpression(BinaryOperator.precedence.size - 1)
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