package gui

import service.Refreshable
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import kotlin.text.clear
import entity.*
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Stack
import kotlin.collections.get
import kotlin.text.clear

class KabooBoardGameScene(val rootService: RootService): BoardGameScene(), Refreshable {
    //val rootService: RootService = RootService()


    private val player1HandCard = LabeledStackView(posX = 195, posY = 100 )
    private val player2HandCard = LabeledStackView(posX = 1595, posY = 100)

    private val player1TopLeft = LabeledStackView(posX = 145, posY = 520 )
    private val player1TopRight = LabeledStackView(posX = 345, posY = 520)
    private val player1BottomLeft = LabeledStackView(posX = 145, posY = 800 )
    private val player1BottomRight = LabeledStackView(posX = 345, posY = 800)

    private val player2TopLeft = LabeledStackView(posX = 1445, posY = 520 )
    private val player2TopRight = LabeledStackView(posX = 1645, posY = 520)
    private val player2BottomLeft = LabeledStackView(posX = 1445, posY = 800 )
    private val player2BottomRight = LabeledStackView(posX = 1645, posY = 800)

    private val newStack = LabeledStackView(posX = 800, posY = 100 ).apply {
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                rootService.playerService.drawCard(false)
            }
        }
    }
    private val usedStack = LabeledStackView(posX = 1000, posY = 100).apply {
        onMouseClicked = {
            if (rootService.currentGame!!.currentPlayer!!.hand == null)
            {
                rootService.playerService.drawCard(true)
                //this.pop()
            }
            else{
                rootService.currentGame?.let { game ->
                    rootService.playerService.discard()
                }
            }

        }
    }

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)
        addComponents(player1HandCard, player2HandCard, usedStack, newStack,
            player1TopLeft, player1TopRight, player1BottomLeft, player1BottomRight,
            player2TopLeft, player2TopRight, player2BottomLeft, player2BottomRight)
    }
    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        cardMap.clear()
        val cardImageLoader = CardImageLoader()

        initializeStackView(game.newStack, newStack, cardImageLoader)
        initializeStackView(game.usedStack, usedStack, cardImageLoader)
        player1HandCard.clear()
        player2HandCard.clear()

    }

    override fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean) {
        val game = rootService.currentGame
        val cardImageLoader = CardImageLoader()
        checkNotNull(game) { "No game found." }
        if (discardable){
            game.newStack.push(game.currentPlayer!!.hand!!)

            when (game.currentPlayer) {
                game.players[0] -> moveCardView(cardMap.forward(game.newStack.pop()), player1HandCard, true)
                game.players[1] -> moveCardView(cardMap.forward(game.newStack.pop()), player2HandCard, true)
            }
            println("drawed card ${player1HandCard.numberOfComponents()} ${usedStack.numberOfComponents()}")
            checkAllStackViews(game)
        }
        else{
            game.usedStack.push(game.currentPlayer!!.hand!!)

            when (game.currentPlayer) {
                game.players[0] -> moveCardView(cardMap.forward(game.usedStack.pop()), player1HandCard, false)
                game.players[1] -> moveCardView(cardMap.forward(game.usedStack.pop()), player2HandCard, false)
            }
            println("drawed card ${player1HandCard.numberOfComponents()} ${usedStack.numberOfComponents()}")
            checkAllStackViews(game)
        }
        //initializeStackView(game.newStack, newStack, cardImageLoader)
        //initializeStackView(game.usedStack, usedStack, cardImageLoader)

    }

    override fun refreshAfterDiscard() {
        val game = rootService.currentGame

        checkNotNull(game) { "No game found." }

        when (game.currentPlayer) {
            game.players[0] -> {
                moveCardView(cardMap.forward(game.players[0].hand!!), usedStack, false)
                player1HandCard.clear()
            }
            game.players[1] -> {
                moveCardView(cardMap.forward(game.players[1].hand!!), usedStack, false)
                player2HandCard.clear()
            }
        }
        println("discarded card ${newStack.numberOfComponents()} ${usedStack.numberOfComponents()}")

    }

    private fun initializeStackView(stack: Stack<Card>, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        stack.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }

        cardView.removeFromParent()

        toStack.add(cardView)
    }

    private fun checkAllStackViews(game: Kaboo) {
        checkStackView(game.newStack, newStack)
        checkStackView(game.usedStack, usedStack)

    }

    private fun checkStackView(stack: Stack<Card>, stackView: LabeledStackView) {

        check(stack.size == stackView.components.size) {
            "Stack size (${stack.size}) is not equal to view size (${stackView.components.size})"
        }

        val stackContents = stack.peekAll().reversed()

        for (i in 0 until stack.size) {
            val cardInView = cardMap.backward(stackView.components[i])
            val cardOnStack = stackContents[i]
            check(cardOnStack == cardInView) {
                "Card on stack ($cardOnStack) is not equal to card in view ($cardInView)"
            }
        }
    }



}