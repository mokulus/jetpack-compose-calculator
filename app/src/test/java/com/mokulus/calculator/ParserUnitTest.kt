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
            ret = ret.replace(Regex("""\be\b"""), EConstant().text)
            ret = ret.replace(Regex("""\bpi\b"""), PiConstant().text)
            return ret
        }

        private fun assertResult(equation : String, actual : Double) {
            println(processText(equation))
            val lexer = Lexer(processText(equation))
            println(lexer.getLexemes())
            val parser = Parser(lexer.getLexemes())
            println(parser.parse())
            Assert.assertEquals(parser.parse().eval(), actual, 0.001)
        }
    }
    @Test
    fun parser_isCorrect() {
        assertResult("2+(3*4)", 14.0)
    }
    @Test
    fun parser_simpleMul() {
        assertResult("3*2", 6.0)
    }

    @Test
    fun parser_precedence() {
        assertResult("2+2*2", 6.0)
    }

    @Test
    fun parser_division() {
        assertResult("12/3/4", 1.0)
    }

    @Test
    fun parser_subtract() {
        assertResult("12-(5-3)", 10.0)
    }

    @Test
    fun parse_empty() {
        assertResult("", 0.0)
    }

    @Test
    fun parse_power() {
        assertResult("2^3", 8.0)
    }

    @Test
    fun parse_power_right_assoc() {
        assertResult("2^2^2", 16.0)
    }

    @Test
    fun parse_subtraction_left_assoc() {
        assertResult("3-5-5", -7.0)
    }

    @Test
    fun parse_factorial() {
        assertResult("1-6!", -719.0)
    }

    @Test
    fun parse_percent() {
        assertResult("15%*3", 0.45)
    }
    @Test
    fun parseTrigOne() {
        assertResult("sin(e)^2+cos(e)^2", 1.0)
    }

    @Test
    fun parsePiByPi() {
        assertResult("pi/pi", 1.0)
    }

    @Test
    fun parseMultiple1() {
        assertResult("sqrt(36) + ln(exp(2)) - sin(pi/6)", 7.5)
    }

    @Test
    fun parseMultiple2() {
        assertResult("(10 * 3 - 8) / cos(0) + tan(pi/4)", 23.0)
    }

    @Test
    fun parseMultiple3() {
        assertResult("(asin(sqrt(2)/2) + acos(sqrt(3)/2) + atan(1)) - 2/3*pi", 0.0)
    }
}
