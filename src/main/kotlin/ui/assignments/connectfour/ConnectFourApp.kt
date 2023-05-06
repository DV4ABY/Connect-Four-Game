package ui.assignments.connectfour

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import ui.assignments.connectfour.ui.GameView

class ConnectFourApp : Application() {
    override fun start(stage: Stage) {
        val root = GameView()
        val scene = Scene(root, 1100.0, 880.0)
        stage.title = "Connect Four"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}