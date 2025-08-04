package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class KabooNextPlayerMenuScene(val rootService: RootService) : MenuScene(1000,1000), Refreshable{
    private val helloLabel = Label(
        width = 400,
        height = 400,
        posX = 300,
        posY = 200,
        text = "Turn of ${rootService.currentGame?.currentPlayer?.name ?: "next player"}",
        font = Font(size = 30)
    )

    private val readyButton = Button(
        width = 450, height = 100,
        posX = 275, posY = 650,
        text = "Ready",
        font = Font(size = 48)
    )/*.apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            KabooNextPlayerMenuScene
        }
    }*/

    init {
        addComponents(helloLabel, readyButton)
    }
}