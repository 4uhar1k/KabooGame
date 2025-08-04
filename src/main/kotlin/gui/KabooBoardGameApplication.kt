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

    /**
     * Initializes the application by displaying the [KabooStartMenuScene].
     */
    init {


        rootService.addRefreshables(
            this,
            kabooBoardGameScene,
            kabooStartMenuScene,
            kabooNextPlayerMenuScene
        )
        rootService.gameService.addPlayers("Bob", "Alice")
        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer

        this.showGameScene(kabooBoardGameScene)
        this.showMenuScene(kabooStartMenuScene, 0)
    }
    override fun refreshAfterStartGame() {
        this.hideMenuScene()
        println("Kaboo Board Game Started!")
    }

    override fun refreshAfterEachTurn() {
        //this.showMenuScene(kabooNextPlayerMenuScene)
        //this.hideMenuScene()
    }

}

