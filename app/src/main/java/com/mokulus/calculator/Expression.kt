package com.mokulus.calculator

abstract class Expression {
    abstract fun eval() : Double
}

class NumberExpression(private val value : Double) : Expression() {
    override fun eval(): Double {
        return value
    }
}

class ConstantExpression(val value : Double) : Expression() {
    override fun eval(): Double {
        return value
    }
}

class UnaryExpression(private val func : (Double) -> Double,
                      private val child : Expression,
                      private val domain: ((Double) -> Unit)? = null) : Expression() {
    override fun eval(): Double {
        val childEval = child.eval()
        domain?.invoke(childEval)
        return func(childEval)
    }
}

class BinaryExpression(private val func: (Double, Double) -> Double,
                       private val left: Expression, private val right: Expression,
                       private val domain: ((Double, Double) -> Unit)? = null) : Expression() {
    override fun eval(): Double {
        val evalLeft = left.eval()
        val evalRight = right.eval()
        domain?.invoke(evalLeft, evalRight)
        return func(evalLeft, evalRight)
    }
}