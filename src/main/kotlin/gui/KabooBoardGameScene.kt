package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class KabooBoardGameScene(val rootService: RootService): BoardGameScene(), Refreshable {
    //val rootService: RootService = RootService()

    var helloLabel = Label(
        width = 400,
        height = 100,
        posX = 760,
        posY = 200,
        text = "Test",
        font = Font(size = 56)
    )
    init {
        background = ColorVisual(108, 168, 59)
        addComponents(helloLabel)
    }
    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        helloLabel.text = "Tested fine"

    }


}