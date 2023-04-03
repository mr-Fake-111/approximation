package src_current

import kotlin.math.pow

class Polynomial() {

    fun calculateAt(x:Double): Double {
        var result: Double = 0.0;
        for(i in 0..(power)) {
            result += x.pow(power - i)*coefficients[i]
        }
        return result
    }

    override fun toString(): String {
        var s = ""
        for(i in 0..power-1) {
            s += "(x^${(power - i)}) * (${"%.8f".format(coefficients[i])}) +"
        }
        s+= "(${"%.8f".format(coefficients[power])})"

        return s
    }

    var coefficients: MutableList<Double> = mutableListOf()
        get() = field
        set(givenCoefficients) {
            field = givenCoefficients
            power = givenCoefficients.size -1
        }

    var power: Int = this.coefficients.size -1
        get() = field

    constructor(coefficients: MutableList<Double>) : this() {
        this.coefficients = coefficients
        this.power = coefficients.size -1
    }
}