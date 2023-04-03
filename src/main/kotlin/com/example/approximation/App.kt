package com.example.approximation

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.effect.Light.Spot
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage
import javafx.stage.Window
import src_current.Approximator
import src_current.SpotGenerator
import kotlin.math.pow

class App: Application() {

    override fun start(primaryStage: Stage?) {
        val group = Group()
        val WIDGTH = 1080.0
        val HEIGHT = 720.0
        val scene = Scene(group, WIDGTH, HEIGHT)

        group.children.addAll(
            Line(0.0, HEIGHT/2, WIDGTH, HEIGHT/2),
           // Line(WIDGTH/2, 0.0, WIDGTH/2, HEIGHT)
        )
        for(i in 1 .. 5) {
            val line1 = Line(WIDGTH*(2*i)/12 - 80, 0.0, WIDGTH*(2*i)/12 - 80, HEIGHT)
            line1.stroke = Color.RED
            val line2 = Line(WIDGTH*(2*i)/12 + 80, 0.0, WIDGTH*(2*i)/12 + 80, HEIGHT)
            line1.stroke = Color.VIOLET
            line2.stroke = Color.VIOLET

            group.children.addAll(
                line1,
                line2,
                Line(WIDGTH*(2*i)/12, 0.0, WIDGTH*(2*i)/12, HEIGHT)
            )
        }

        val nodes = Array<Double>(100) {0.0}
        for(i in nodes.indices step 5) {

            for(j in 0.. 4) {
                nodes[i+j] = -1+  2.0 / nodes.size * i
            }

        }
        val spots = SpotGenerator.createSpots(nodes, mismatch = (10.0).pow(-1))

        for(i in 1..5) {
            val polyByNorm = Approximator.polyByNormalEquations(spots, i)
            val polyByOrt = Approximator.polyByOrtPolys(spots, i)

            var minSquareMistakeByNorm = 0.0
            var minSquareMistakeByOrt = 0.0
            for (i in spots.indices) {
                minSquareMistakeByNorm += (spots[i][1] - polyByNorm.calculateAt(spots[i][0])).pow(2)
                minSquareMistakeByOrt += (spots[i][1] - polyByOrt.calculateAt(spots[i][0])).pow(2)
            }
            println("$i, $minSquareMistakeByNorm, $minSquareMistakeByOrt")


            var iter = -1.0
            while(iter < 1) {
                val line = Line(
                    80*iter + WIDGTH*(2*i)/12,
                    -80* polyByNorm.calculateAt(iter) + HEIGHT/2,
                    80*(iter + 0.01) + WIDGTH*(2*i)/12,
                    -80* polyByNorm.calculateAt(iter+0.01) + HEIGHT/2,
                )
                line.stroke = Color.BLUE
                group.children.addAll(
                    line
                )
                iter += 0.01
            }

            for(j in spots.indices) {
                group.children.addAll(
                    Circle(
                        80*spots[j][0] + WIDGTH*(2*i)/12,
                        -80* spots[j][1] + HEIGHT/2,
                        3.0,
                        Color.RED
                    )
                )
            }

        }


        primaryStage?.title = "Approximation"
        primaryStage?.scene = scene
        primaryStage?.show()
    }

    fun launchApp() {
        launch()
    }

}

fun main(args: Array<String>) {
    val app = App()
    app.launchApp()
}