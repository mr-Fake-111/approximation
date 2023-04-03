package src_current

import src_current.UserFunction
import kotlin.math.abs
import kotlin.math.absoluteValue

class SpotGenerator {
    companion object {
        fun createSpots(nodes: Array<Double>, mismatch: Double): Array<DoubleArray> {

            val spots = Array<DoubleArray>(nodes.size) { DoubleArray(2) { 0.0 } }
            for (i in nodes.indices) {
                spots[i][0] = nodes[i]
                spots[i][1] = UserFunction.calculateAt(spots[i][0]) + (Math.random() * (mismatch) - mismatch / 2)
            }

            return spots
        }
    }
}