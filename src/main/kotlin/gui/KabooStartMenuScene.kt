package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * Represents an example scene with a greeting label.
 * This scene has a default size of 1920x1080 pixels and a green background.
 */
class KabooStartMenuScene() : MenuScene(), Refreshable {
    val rootService: RootService = RootService()
    /**
     * A label displaying the text "Hello, SoPra!" centered within the scene.
     */
    private val helloLabel = Label(
        width = 400,
        height = 100,
        posX = 760,
        posY = 200,
        text = "Start new game",
        font = Font(size = 56)
    )

    private val p1Label = Label(
        width = 200, height = 35,
        posX = 690, posY = 350,
        text = "Player 1:",
        font = Font(size = 28)
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 450, height = 50,
        posX = 735, posY = 400,
        prompt = "Player 1",
        font = Font(size = 28)
    ).apply {
        onKeyPressed = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    private val p2Label = Label(
        width = 200, height = 35,
        posX = 690, posY = 500,
        text = "Player 2:",
        font = Font(size = 28)
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 450, height = 50,
        posX = 735, posY = 550,
        prompt = "Player 2",
        font = Font(size = 28)
    ).apply {
        onKeyPressed = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val startButton = Button(
        width = 450, height = 100,
        posX = 735, posY = 650,
        text = "Start",
        font = Font(size = 48)
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.gameService.addPlayers(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    /**
     * Initializes the scene by setting the background color and adding the label.
     */
    init {
        background = ColorVisual(108, 168, 59)
        addComponents(helloLabel, p1Label, p1Input,
            p2Label, p2Input, startButton)
    }

}