package ui.assignments.connectfour.ui

import javafx.animation.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.util.Duration
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Piece

data class DragInfo(var target : ImageView? = null,
                    var anchorX: Double = 0.0,
                    var anchorY: Double = 0.0,
                    var initialX: Double = 0.0,
                    var initialY: Double = 0.0)

const val discHBoxHMargin = 30.0
const val sceneWidth = 1100.0
const val discDiameter = 80.0
const val margin = 2.0
const val yMargin = 10.0
const val restoreDuration = 300.0

// Calculated the lower and upper x coor for each column in the grid
fun getDiscColRange(initX: Double): MutableList<Pair<Double, Double>> {
    val colRange = mutableListOf<Pair<Double, Double>>()
    val singleRangeWidth = boardWidth / Model.width
    var lowerBound = initX
    var upperBound = initX + singleRangeWidth
    for (i in 0 until Model.width) {
        colRange.add(Pair(lowerBound, upperBound))
        lowerBound += singleRangeWidth
        upperBound += singleRangeWidth
    }
    return colRange
}

val redDiscColRange = getDiscColRange((sceneWidth - 2 * discHBoxHMargin) / 2 - boardWidth / 2)
val yellowDiscColRange = getDiscColRange(-(boardWidth / 2 + sceneWidth / 2 - discHBoxHMargin - discDiameter))

class RedDiscView(): ImageView() {
    private var dragInfo = DragInfo()
    private var dropColIdx = -1
    private val restoreAnimation = Timeline(
        KeyFrame(Duration.millis(restoreDuration),
                 KeyValue(this.translateXProperty(), 0.0, Interpolator.EASE_BOTH),
                 KeyValue(this.translateYProperty(), 0.0, Interpolator.EASE_BOTH))).apply {
        isAutoReverse = false
        cycleCount = 1
    }

    init {
        image = Image(javaClass.getResource("/ui/assignments/connectfour/piece_red.png").toString())
        addEventFilter(MouseEvent.MOUSE_PRESSED) {
            dragInfo = DragInfo(this, it.sceneX, it.sceneY, translateX, translateY)
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) {
            val lowestX = -discHBoxHMargin + margin
            val highestX = sceneWidth - discDiameter - discHBoxHMargin - margin
            val lowestY = -yMargin + margin
            val highestY = 2 * yMargin - margin
            var x = dragInfo.initialX + it.sceneX - dragInfo.anchorX
            var y = dragInfo.initialY + it.sceneY - dragInfo.anchorY

            // Make sure disc is not out of bound
            if (x < lowestX) x = lowestX
            if (x > highestX) x = highestX
            if (y < lowestY) y = lowestY
            if (y > highestY) y = highestY

            // Check which column to snap to
            for (i in 0 until Model.width) {
                // Use disc's center x coordinate to determine above which column it should snap to
                if (redDiscColRange[i].first <= x + discDiameter / 2 && x + discDiameter / 2 < redDiscColRange[i].second) {
                    x = (redDiscColRange[i].first + redDiscColRange[i].second) / 2 - discDiameter / 2
                    dropColIdx = i
                    break
                }
            }

            if (x + discDiameter / 2 < redDiscColRange[0].first || x + discDiameter / 2 >= redDiscColRange[Model.width - 1].second) {
                dropColIdx = -1
            }
            translateX = x
            translateY = y
        }
        addEventFilter(MouseEvent.MOUSE_RELEASED) {
            if (dropColIdx >= 0) {
                // Records current value of translateX Y
                Model.transXBeforeDrop = translateX
                Model.transYBeforeDrop = translateY
                Model.dropPiece(dropColIdx)
            }
            restoreAnimation.play()
        }
    }
}

class YellowDiscView(): ImageView() {
    private var dragInfo = DragInfo()
    private var dropColIdx = -1
    private val restoreAnimation = Timeline(
        KeyFrame(Duration.millis(restoreDuration),
                 KeyValue(this.translateXProperty(), 0.0, Interpolator.EASE_BOTH),
                 KeyValue(this.translateYProperty(), 0.0, Interpolator.EASE_BOTH))).apply {
        isAutoReverse = false
    }

    init {
        image = Image(javaClass.getResource("/ui/assignments/connectfour/piece_yellow.png").toString())
        addEventFilter(MouseEvent.MOUSE_PRESSED) {
            dragInfo = DragInfo(this, it.sceneX, it.sceneY, translateX, translateY)
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) {
            val lowestX = -(sceneWidth - discHBoxHMargin - discDiameter) + margin
            val highestX = discHBoxHMargin - margin
            val lowestY = -yMargin + margin
            val highestY = 2 * yMargin - margin
            var x = dragInfo.initialX + it.sceneX - dragInfo.anchorX
            var y = dragInfo.initialY + it.sceneY - dragInfo.anchorY

            // Make sure disc is not out of bound
            if (x < lowestX) x = lowestX
            if (x > highestX) x = highestX
            if (y < lowestY) y = lowestY
            if (y > highestY) y = highestY

            // Check which column to snap to
            for (i in 0 until Model.width) {
                // Use disc's center x coordinate to determine above which column it should snap to
                if (yellowDiscColRange[i].first <= x + discDiameter / 2 && x + discDiameter / 2 < yellowDiscColRange[i].second) {
                    x = (yellowDiscColRange[i].first + yellowDiscColRange[i].second) / 2 - discDiameter / 2
                    dropColIdx = i
                    break
                }
            }

            if (x + discDiameter / 2 < yellowDiscColRange[0].first || x + discDiameter / 2 >= yellowDiscColRange[Model.width - 1].second) {
                dropColIdx = -1
            }
            translateX = x
            translateY = y
        }
        addEventFilter(MouseEvent.MOUSE_RELEASED) {
            if (dropColIdx >= 0) {
                // Change transX to follow red disc's translate origin
                Model.transXBeforeDrop = translateX + sceneWidth - 2 * discHBoxHMargin - discDiameter
                Model.transYBeforeDrop = translateY
                Model.dropPiece(dropColIdx)
            }
            restoreAnimation.play()
        }
    }
}