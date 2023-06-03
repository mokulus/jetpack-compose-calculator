package com.mokulus.calculator.parser.functions

abstract class Function {
    abstract val name : String
    abstract fun invoke(x : Double) : Double
    abstract fun inverse() : Function?
}