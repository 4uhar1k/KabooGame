package gui

import service.AbstractRefreshingService
import tools.aqua.bgw.core.BoardGameApplication
import service.RootService
import service.Refreshable
import java.lang.Thread.sleep


/**
 * Represents the main application for the SoPra board game.
 * The application initializes the [RootService] and displays the scenes.
 */
class KabooBoardGameApplication : BoardGameApplication("Kaboo"), Refreshable {

    /**
     * The root service instance. This is used to call service methods and access the entity layer.
     */
    val rootService: RootService = RootService()

    /**
     * The main game scene displayed in the application.
     */
    private val kabooStartMenuScene = KabooStartMenuScene(rootService)
    private val kabooBoardGameScene = KabooBoardGameScene(rootService)
    private val kabooNextPlayerMenuScene = KabooNextPlayerMenuScene(rootService)
    private var kabooEndGameMenuScene = KabooEndGameMenuScene(rootService, "bb")

    /**
     * Initializes the application by displaying the [KabooStartMenuScene].
     */
    init {


        rootService.addRefreshables(
            this,
            kabooBoardGameScene,
            kabooStartMenuScene,
            kabooNextPlayerMenuScene,
            kabooEndGameMenuScene
        )
        rootService.gameService.addPlayers("Bob", "Alice")
        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer

        this.showGameScene(kabooBoardGameScene)
        this.showMenuScene(kabooStartMenuScene, 0)
    }

    /**
     * Hides [KabooStartMenuScene]
     */
    override fun refreshAfterStartGame() {
        this.hideMenuScene()
        println("Kaboo Board Game Started!")
    }

    /**
     * Shows [KabooNextPlayerMenuScene]
     */
    override fun refreshAfterGameMove(canKnock: Boolean, canTakeUsedCard: Boolean) {
        this.showMenuScene(kabooNextPlayerMenuScene)
        //this.hideMenuScene()
    }

    /**
     * Hides [KabooNextPlayerMenuScene]
     */
    override fun refreshAfterEachTurn() {
        this.hideMenuScene()
    }

    /**
     * Shows [KabooEndGameMenuScene] with a message with winner of the game
     * @param winnerMessage - name of a winner or "Draw"
     */
    override fun refreshAfterEndGame(winnerMessage: String) {
        if (winnerMessage!="Draw"){
            kabooEndGameMenuScene = KabooEndGameMenuScene(rootService, "${winnerMessage} is a winner!")
        }
        else{
            kabooEndGameMenuScene = KabooEndGameMenuScene(rootService, "It's a draw!")

        }
        this.showMenuScene(kabooEndGameMenuScene)
    }

}

