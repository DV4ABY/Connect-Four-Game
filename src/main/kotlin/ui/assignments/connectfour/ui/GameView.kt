package ui.assignments.connectfour.ui

import javafx.beans.Observable
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.effect.Shadow
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player

class GameView: VBox(), ChangeListener<Player> {
    private val roundInfo = RoundInfo()
    private val startGameBtn : Button = Button("Click here to start game!").apply {
        background = Background(BackgroundFill(Color.LIGHTGREEN, null, null))
        alignment = Pos.CENTER
        font = Font("Verdana", 25.0)
        prefWidth = 600.0
        prefHeight = discDiameter + yMargin
        onAction = EventHandler {
            Model.startGame()
            this@GameView.children.clear()
            this@GameView.children.addAll(roundInfo, discWrap, gameBoard)
            VBox.setMargin(discWrap, Insets(0.0, discHBoxHMargin, 2 * yMargin, discHBoxHMargin))
        }
    }
    private val redDisc = RedDiscView()
    private val yellowDisc = YellowDiscView()
    private val gameBoard = BoardView()
    private val discWrap = HBox(redDisc).apply {
        alignment = Pos.CENTER_LEFT
    }

    // For winning pop-up
    private val winListener = { _: Observable, _: Player?, new: Player ->
        val winMessage = Button("Player #${if (new == Player.ONE) "1" else "2"} has won!!!").apply {
            alignment = Pos.CENTER
            font = Font("Verdana", 25.0)
            prefWidth = 600.0
            minHeight = discDiameter + yMargin
            effect = DropShadow()
            background = Background(BackgroundFill(if (new == Player.ONE) Color.RED else Color.YELLOW, CornerRadii(10.0), null))
        }
        this@GameView.children.clear()
        this@GameView.children.addAll(roundInfo, winMessage, gameBoard)
        VBox.setMargin(winMessage, Insets(0.0, 0.0, yMargin, 0.0))
    }

    // For draw pop-up
    private val drawListener = { _: Observable, _: Boolean, _: Boolean ->
        val drawMessage = Button("Draw!!!").apply {
            alignment = Pos.CENTER
            font = Font("Verdana", 25.0)
            prefWidth = 600.0
            minHeight = discDiameter + yMargin
            effect = DropShadow()
            background = Background(BackgroundFill(Color.GREY, CornerRadii(10.0), null))
        }
        this@GameView.children.clear()
        this@GameView.children.addAll(roundInfo, drawMessage, gameBoard)
        VBox.setMargin(drawMessage, Insets(0.0, 0.0, yMargin, 0.0))
    }

    init {
        alignment = Pos.TOP_CENTER
        children.addAll(roundInfo, startGameBtn, gameBoard)
        VBox.setMargin(roundInfo, Insets(yMargin))
        VBox.setMargin(startGameBtn, Insets(0.0, 0.0, yMargin, 0.0))
        Model.onNextPlayer.addListener(this)
        Model.onGameWin.addListener(winListener)
        Model.onGameDraw.addListener(drawListener)
        changed(null, null, Model.onNextPlayer.value)
    }

    private fun changeDisc(newPlayer: Player?) {
        if (newPlayer == null) return
        discWrap.children.clear()
        if (newPlayer == Player.ONE) {
            discWrap.children.add(redDisc)
            discWrap.alignment = Pos.CENTER_LEFT
        } else if (newPlayer == Player.TWO) {
            discWrap.children.add(yellowDisc)
            discWrap.alignment = Pos.CENTER_RIGHT
        }
    }

    override fun changed(observable: ObservableValue<out Player>?, oldValue: Player?, newValue: Player?) {
        changeDisc(newValue)    // Switch to new current player's disc
    }
}