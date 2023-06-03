package com.mokulus.calculator.parser.functions

abstract class TrigFunction : Function() {
    companion object {
        fun degToRad(deg : Double) : Double {
            return deg * kotlin.math.PI / 180
        }
        fun radToDeg(rad : Double) : Double{
            return rad * 180 / kotlin.math.PI
        }
    }
    abstract var useRadians : Boolean
}