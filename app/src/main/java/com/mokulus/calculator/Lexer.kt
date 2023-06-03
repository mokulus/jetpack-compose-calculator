package com.mokulus.calculator

import android.icu.text.DecimalFormat

class InvalidLexeme(val position: Int, val char: Char) : Exception("Invalid lexeme at ${position}: $char")

class Lexer(var text: String) {
    private val lexemes = mutableListOf<Lexeme>()
    init {
        text = text.filter { !it.isWhitespace() }
        val iter = text.withIndex().iterator()
        val nameRegex = Regex("[a-zA-Z]+")
        val numberRegex = Regex("""[+-]?[0-9]+(\.[0-9]*)?""")
        while (iter.hasNext()) {
            val (i, c) = iter.next()
            val tail = text.slice(i until text.length)
            when {
                c == '(' -> lexemes.add(LeftParen(i))
                c == ')' -> lexemes.add(RightParen(i))
                c.toString() in Operator.operators -> lexemes.add(Operator(c.toString(), i))
                nameRegex.matchesAt(tail, 0) -> {
                    val match = nameRegex.find(tail)!!.value
                    lexemes.add(Name(match, i))
                    for (m in 1 until match.length)
                        iter.next()
                }
                numberRegex.matchesAt(tail, 0) -> {
                    val match = numberRegex.find(tail)!!.value
                    lexemes.add(Number(match, i))
                    for (m in 1 until match.length)
                        iter.next()
                }
                else -> throw InvalidLexeme(i, c)
            }
        }
    }
    fun getLexemes() : List<Lexeme> {
        return lexemes
    }
    fun popLexeme() : Lexeme? {
        if (lexemes.isEmpty())
            return null
        else {
            return lexemes.removeLast()
        }
    }
    fun pushLexeme(lex : Lexeme) {
        lexemes.add(lex)
    }
    fun format(decimalFormat: DecimalFormat) : String {
        return getLexemes().map {
            when (it) {
                is Number -> {
                    val regex = Regex("""\.0*$""").find(it.text)
                    val text = decimalFormat.format(it.value)
                    if (regex != null)
                         text + regex.value
                    else
                        text
                }
                else -> it.text
            }
        }.joinToString (separator = ""){ it }
    }
}