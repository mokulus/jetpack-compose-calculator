package com.mokulus.calculator

import com.mokulus.calculator.parser.EConstant
import com.mokulus.calculator.parser.Parser
import com.mokulus.calculator.parser.PiConstant
import org.junit.Assert
import org.junit.Test

class ParserUnitTest {

    companion object {
        private fun processText(text : String) : String {
            var ret = text
            ret = ret.replace("*", OperatorType.Multiply.getSymbol())
            ret = ret.replace("/", OperatorType.Divide.getSymbol())
            ret = ret.replace("e", EConstant().text)
            ret = ret.replace("pi", PiConstant().text)
            return ret
        }
    }
    @Test
    fun parser_isCorrect() {
        val lexer = Lexer(processText("2+(3*4)"))
        val parser = Parser(lexer.getLexemes())
        Assert.assertEquals(parser.parse().eval(), 14.0, 0.001)
    }
    @Test
    fun parser_simpleMul() {
        val lexer = Lexer(processText("3*2"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 6.0, 0.001)
    }

    @Test
    fun parser_precedence() {
        val lexer = Lexer(processText("2+2*2"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 6.0, 0.001)
    }

    @Test
    fun parser_division() {
        val lexer = Lexer(processText("12/3/4"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 1.0, 0.001)
    }

    @Test
    fun parser_subtract() {
        val lexer = Lexer(processText("12-(5-3)"))
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
        val lexer = Lexer(processText("2^3"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 8.0, 0.001)
    }
    @Test
    fun parse_power_right_assoc() {
        val lexer = Lexer(processText("2^2^2"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 16.0, 0.001)
    }
    @Test
    fun parse_subtraction_left_assoc() {
        val lexer = Lexer(processText("3-5-5"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), -7.0, 0.001)
    }

    @Test
    fun parse_factorial() {
        val lexer = Lexer(processText("1-6!"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), -719.0, 0.001)
    }

    @Test
    fun parse_percent() {
        val lexer = Lexer(processText("15%*3"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 0.45, 0.001)
    }
    @Test
    fun parseTrigOne() {
        val lexer = Lexer(processText("sin(e)^2+cos(e)^2"))
        println(lexer.getLexemes())
        val parser = Parser(lexer.getLexemes())
        println(parser.parse())
        Assert.assertEquals(parser.parse().eval(), 1.0, 0.001)
    }
}