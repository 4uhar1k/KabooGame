package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game.
 * Also, there is a button: for starting a new game with the same players' names.
 *
 * @param rootService [RootService] instance to access the service methods and entity layer
 */
class KabooEndGameMenuScene(val rootService: RootService, var winnerMessage: String)
    : MenuScene(1000,1000), Refreshable {
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
    /**
     * Initializes the scene by setting the background color and adding the label.
     */
    init {
        addComponents(helloLabel, readyButton)
    }
}