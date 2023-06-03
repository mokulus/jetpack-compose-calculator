package com.mokulus.calculator

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mokulus.calculator.parser.Constant
import com.mokulus.calculator.parser.Parser
import com.mokulus.calculator.parser.functions.Function

class CalculatorViewModel : ViewModel() {
    var text : String by mutableStateOf("")
        private set
    var useRadians by mutableStateOf(true)
        private set
    var inverse by mutableStateOf(false)
        private set

    var lexer = Lexer("")
    val decimalFormat : DecimalFormat

    init {
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.groupingSeparator = ' '
        decimalFormat = DecimalFormat("###,###,###,###,##0.#########", decimalFormatSymbols)
    }

    fun pushKey(digit : Int) {
        pushRaw(digit.toString())
    }

    fun pushDot() {
        val last = getLastLexeme()
        if (last != null && last is Number && last.text.contains("."))
            return
        pushRaw(".")
    }

    fun pushOperator(operatorType: OperatorType) {
        val operator = Operator(operatorType.getSymbol(), text.length)
        if (lexer.getLexemes().isEmpty()) {
            if (operator.type == OperatorType.Minus) {
                pushRaw(operator.text)
            }
        } else if (lexer.getLexemes().size == 1) {
            val last = getLastLexeme()!!
            if (last !is Operator)
                pushRaw(operator.text)
        } else {
            val last = getLastLexeme()!!
            val secondToLast = lexer.getLexemes()[lexer.getLexemes().size - 2]
            if (secondToLast is Operator) {
                if (last is Operator) {
                    pop()
                    pop()
                    pushRaw(operator.text)
                } else {
                    pushRaw(operator.text)
                }
            } else {
                if (last is Operator) {
                    if (operator.type != OperatorType.Minus)
                        pop()
                    pushRaw(operator.text)
                } else {
                    pushRaw(operator.text)
                }
            }
        }
    }

    fun pushConstant(constant : Constant) {
        pushName(Name(constant.text, text.length))
    }

    fun pushFunction(function : Function) {
        pushName(Name(function.name, text.length))
        pushLeftParen()
    }

    fun pushLeftParen() {
        val last = getLastLexeme()
        if (last == null || last !is Number)
            pushRaw("(")
    }

    fun pushRightParen() {
        val openCount = text.count { it == '(' }
        val closeCount = text.count { it == ')' }
        if (openCount > closeCount)
            pushRaw(")")
    }

    fun calculate() {
        val value = Parser(Lexer(text).getLexemes(), useDegrees = !useRadians).parse().eval()
        text = decimalFormat.format(value)
        lexer = Lexer(text)
    }

    fun toggleUseRadians() {
        useRadians = !useRadians
    }

    fun toggleInverse() {
        inverse = !inverse
    }

    fun pop() {
        val last = getLastLexeme()
        if (last != null && last is Number) {
            val newText = text.substring(0, text.length - 1)
            lexer = Lexer(newText)
        } else {
            lexer.popLexeme()
        }
        updateText()
    }

    private fun getLastLexeme() : Lexeme? {
        return lexer.getLexemes().lastOrNull()
    }

    private fun pushName(name : Name) {
        val last = getLastLexeme()
        if (last != null && last is Name)
            pop()
        if (last == null || last !is Number)
            pushRaw(name.text)
    }

    private fun pushRaw(raw : String) {
        try {
            lexer = Lexer(text + raw)
            updateText()
        } catch (_: InvalidLexeme) {

        }
    }

    private fun updateText() {
        text = lexer.format(decimalFormat)
    }
}