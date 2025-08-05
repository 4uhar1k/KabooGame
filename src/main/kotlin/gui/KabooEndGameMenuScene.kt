package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font

class KabooEndGameMenuScene(val rootService: RootService, var winnerMessage: String) : MenuScene(1000,1000), Refreshable {
    private val helloLabel = Label(
        width = 400,
        height = 400,
        posX = 300,
        posY = 200,
        text = winnerMessage,
        font = Font(size = 30)
    )
    val game = rootService.currentGame

    private val readyButton = Button(
        width = 450, height = 100,
        posX = 275, posY = 650,
        text = "Start new game",
        font = Font(size = 48)
    ).apply {
        //visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            checkNotNull(game) {"The game is already over"}
            rootService.gameService.addPlayers(game.players[0].name, game.players[1].name)
        }
    }

    init {
        addComponents(helloLabel, readyButton)
    }
}