package src_current

import SLAE_methods_functions.MathFunctions
import SLAE_methods_functions.SLAEAccurateMethods
import java.time.Duration
import kotlin.math.pow

class Approximator {
    companion object {
        fun polyByNormalEquations(spots: Array<DoubleArray>, polyPower: Int): Polynomial {

            val matrixE = Array<DoubleArray>(spots.size) { DoubleArray(polyPower + 1) }
            val vectorF = Array<DoubleArray>(spots.size) { DoubleArray(1) { 0.0 } }

            for (i in spots.indices) {
                for (j in 0..polyPower) {
                    matrixE[i][j] = spots[i][0].pow(j)
                }
                vectorF[i][0] = spots[i][1]
            }


            val preCoefficients = SLAEAccurateMethods.GaussMethod(
                MathFunctions.multiplyMatrices(MathFunctions.transpondMatrix(matrixE), matrixE),
                MathFunctions.multiplyMatrices(MathFunctions.transpondMatrix(matrixE), vectorF)
            )
            val coefficients = MutableList<Double>(preCoefficients.size) { 0.0 }
            for (i in coefficients.indices) {
                coefficients[i] = preCoefficients[i][0]
            }

            return Polynomial(coefficients.reversed().toMutableList())
        }

        fun polyByOrtPolys(spots: Array<DoubleArray>, polyPower: Int): Polynomial {

            val polys = Array<Polynomial>(polyPower + 1) { Polynomial() }

            if(polyPower >= 0) {
                polys[0] = Polynomial(MutableList<Double>(1) {1.0})
                if(polyPower == 0) return polys[0]
            }
            if(polyPower >= 1) {
                var alpha = 0.0;
                for (i in spots.indices) {
                    alpha += spots[i][0]
                }
                alpha /= spots.size

                polys[1] = Polynomial(mutableListOf(1.0, -1.0*alpha))
                if(polyPower == 1) return polys[1]
            }

            for(i in 2..polyPower) {
                val coefficients = MutableList<Double>(i + 1) {0.0}

                var alpha1 = 0.0
                var beta1 = 0.0
                var alpha2 = 0.0
                var beta2 = 0.0

                for (j in spots.indices) {

                    val currOrtPoly = polys[i-1].calculateAt(spots[j][0])
                    val prevOrtPoly = polys[i-2].calculateAt(spots[j][0])

                    alpha1 += spots[j][0] * currOrtPoly.pow(2)
                    alpha2 += currOrtPoly.pow(2)

                    beta1 += spots[j][0] * currOrtPoly * prevOrtPoly
                    beta2 += prevOrtPoly.pow(2)
                }

                val alpha = alpha1/alpha2
                val beta = beta1/beta2

                for(k in 0..coefficients.size-2) {
                    coefficients[k] += polys[i-1].coefficients[k]
                    coefficients[k+1] -= alpha*polys[i-1].coefficients[k]
                }
                for(k in 0..coefficients.size-3) {
                    coefficients[k+2] -= beta*polys[i-2].coefficients[k]
                }

                polys[i] = Polynomial(coefficients)
            }

            val coefficientsA =  MutableList<Double>(polyPower + 1) {0.0}

            for (i in 0 ..polyPower) {
                var a1 = 0.0
                var a2 = 0.0
                for (j in spots.indices) {
                    a1 += polys[i].calculateAt(spots[j][0]) * spots[j][1]
                    a2 += polys[i].calculateAt(spots[j][0]).pow(2)
                }
                coefficientsA[i] = a1 / a2
            }

            val coefficients =  MutableList<Double>(polyPower + 1) {0.0}

            for(i in polyPower downTo 0) {
                for(j in 0..i) {
                    coefficients[polyPower - i + j] += coefficientsA[i]*polys[i].coefficients[j]
                }
            }

            return Polynomial(coefficients)
        }

    }
}

/*
fun polyByOrtPolys(spots: Array<DoubleArray>, polyPower: Int): Polynomial {

            var coefficients = MutableList<Double>(polyPower + 1) { 0.0 }

            for (i in 0 ..polyPower) {
                var a1 = 0.0
                var a2 = 0.0
                for (j in spots.indices) {
                    val calcOrtPoly = calcOrtPoly(spots[j][0], spots, i)
                    a1 += calcOrtPoly * spots[j][1]
                    a2 += calcOrtPoly.pow(2)
                }
                coefficients[i] = a1 / a2
            }

            return Polynomial(coefficients)
        }

        private fun calcOrtPoly(valX: Double, spots: Array<DoubleArray>, index: Int): Double {
            if (index == 0) return 1.0
            if (index == 1) {
                var alpha = 0.0;
                for (i in spots.indices) {
                    alpha += spots[i][0]
                }
                alpha /= spots.size

                return (valX - alpha)
            }

            var alpha1 = 0.0
            var beta1 = 0.0
            var alpha2 = 0.0
            var beta2 = 0.0

            for (i in spots.indices) {

                val currOrtPoly = calcOrtPoly(spots[i][0], spots, index - 1)
                val prevOrtPoly = calcOrtPoly(spots[i][0], spots, index - 2)

                alpha1 += spots[i][0] * currOrtPoly.pow(2)
                alpha2 += currOrtPoly.pow(2)

                beta1 += spots[i][0] * currOrtPoly * prevOrtPoly
                beta2 += prevOrtPoly.pow(2)
            }

            return ((valX - alpha1 / alpha2) * calcOrtPoly(valX, spots, index - 1) - beta1 / beta2 * calcOrtPoly(valX, spots, index - 2))
        } */