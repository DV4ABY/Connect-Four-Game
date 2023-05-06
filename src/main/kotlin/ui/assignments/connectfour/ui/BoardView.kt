package ui.assignments.connectfour.ui

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Piece
import ui.assignments.connectfour.model.Player

const val boardWidth = 800.0
const val boardHeight = 700.0
const val dropSpeed = 3.0

class BoardView(): VBox(), ChangeListener<Piece?> {
    private val cellHeight = boardHeight / Model.height
    private val gameBoard = ImageView(javaClass.getResource("/ui/assignments/connectfour/grid_8x7.png").toString())
    init {
        alignment = Pos.TOP_CENTER
        children.add(gameBoard)
        Model.onPieceDropped.addListener(this)
    }

    override fun changed(observable: ObservableValue<out Piece?>?, oldValue: Piece?, newValue: Piece?) {
        if (newValue != null) {
            var droppedDisc = Circle(sceneWidth / 2, 0.0, discDiameter / 2, Color.RED)
            if (newValue.player == Player.TWO) droppedDisc = Circle(sceneWidth / 2, 0.0, discDiameter / 2, Color.YELLOW)
            // Shift everything up a disc's diameter so that board don't get push down
            children.forEach {
                it.translateY -= discDiameter
            }
            children.add(0, droppedDisc)
            // Subtract offset of translation for disc in the above HBox
            droppedDisc.translateX = Model.transXBeforeDrop - (sceneWidth / 2 - discHBoxHMargin - 0.5 * discDiameter)
            droppedDisc.translateY = Model.transYBeforeDrop - discDiameter - 2 * yMargin
            val dropYEndVal = newValue.y * cellHeight + (cellHeight - discDiameter) / 2
            val dropDuration = (dropYEndVal - droppedDisc.translateY) / dropSpeed
            val dropAnimation = Timeline(
                KeyFrame(
                    Duration.millis(dropDuration),
                    KeyValue(droppedDisc.translateYProperty(), dropYEndVal, Interpolator.EASE_IN)
                )
            ).apply {
                isAutoReverse = false
                cycleCount = 1
            }
            dropAnimation.play()
        }
    }
}