package ui.assignments.connectfour.ui

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player

class RoundInfo() : HBox(), ChangeListener<Player> {
    private val notCurRoundColor = Color.LIGHTGRAY
    private val player1 = Label("Player #1").apply {
        alignment = Pos.TOP_LEFT
        textFill = notCurRoundColor
        font = Font("Verdana", 25.0)
    }

    private val player2 = Label("Player #2").apply {
        alignment = Pos.TOP_RIGHT
        textFill = notCurRoundColor
        font = Font("Verdana", 25.0)
    }

    init {
        val spaceFiller = Region()
        this.children.addAll(player1, spaceFiller, player2)
        HBox.setHgrow(spaceFiller, Priority.ALWAYS)
        Model.onNextPlayer.addListener(this)
        changed(null, null, Model.onNextPlayer.value)
    }

    private fun nextPlayer(newPlayer: Player?) {
        if (newPlayer == Player.ONE) {
            player1.textFill = Color.BLACK
            player2.textFill = notCurRoundColor
        } else if (newPlayer == Player.TWO) {
            player2.textFill = Color.BLACK
            player1.textFill = notCurRoundColor
        }
    }

    override fun changed(observable: ObservableValue<out Player>?, oldValue: Player?, newValue: Player?) {
        nextPlayer(newValue)
    }
}