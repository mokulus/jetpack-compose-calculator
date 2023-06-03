package com.mokulus.calculator

import org.junit.Test

class LexerUnitTest {
    @Test
    fun lexer_isCorrect() {
        val lexer = Lexer("sin(2+(3*5))")
        println(lexer.getLexemes())
    }
}