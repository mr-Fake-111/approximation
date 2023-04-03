package src_current

import kotlin.math.pow
import kotlin.math.sin

class UserFunction() {
   companion object {

       fun calculateAt(x: Double): Double {
           return (x.pow(2) +  sin(x))
       }

       fun getNodes(xValues: MutableList<Double>): Array<DoubleArray> {
           val nodes = Array<DoubleArray>(xValues.size) {DoubleArray(2)}
           for(i in 0..(xValues.size-1)) {
               nodes[i][0] = xValues[i]
               nodes[i][1] = calculateAt(xValues[i])
           }
           return nodes
       }
   }
}