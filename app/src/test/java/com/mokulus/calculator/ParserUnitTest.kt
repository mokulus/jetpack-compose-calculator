package com.mokulus.calculator

import com.mokulus.calculator.parser.Parser
import org.junit.Assert
import org.junit.Test

class ParserUnitTest {
    @Test
    fun parser_isCorrect() {
        val lexer = Lexer("2+(3*4)")
        val parser = Parser(lexer.getLexemes())
        Assert.assertEquals(parser.parse().eval(), 14.0, 0.001)
    }
    @Test
    fun parser_simpleMul() {
        val lexer = Lexer("3*2")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 6.0, 0.001)
    }

    @Test
    fun parser_precedence() {
        val lexer = Lexer("2+2*2")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 6.0, 0.001)
    }

    @Test
    fun parser_division() {
        val lexer = Lexer("12/3/4")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 1.0, 0.001)
    }

    @Test
    fun parser_subtract() {
        val lexer = Lexer("12-(5-3)")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 10.0, 0.001)
    }

    @Test
    fun parse_empty() {
        val lexer = Lexer("")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 0.0, 0.001)
    }
    @Test
    fun parse_power() {
        val lexer = Lexer("2^3")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 8.0, 0.001)
    }
    @Test
    fun parse_power_right_assoc() {
        val lexer = Lexer("2^2^2")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 16.0, 0.001)
    }
    @Test
    fun parse_subtraction_left_assoc() {
        val lexer = Lexer("3-5-5")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), -7.0, 0.001)
    }

    @Test
    fun parse_factorial() {
        val lexer = Lexer("1-6!")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), -719.0, 0.001)
    }

    @Test
    fun parse_percent() {
        val lexer = Lexer("15%*3")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 0.45, 0.001)
    }
    @Test
    fun parseTrigOne() {
        val lexer = Lexer("sin(e)^2+cos(e)^2")
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 1.0, 0.001)
    }
}