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
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Stack
import kotlin.collections.get
import kotlin.text.clear

class KabooBoardGameScene(val rootService: RootService): BoardGameScene(), Refreshable {
    //val rootService: RootService = RootService()


    private val player1HandCard = LabeledStackView(posX = 195, posY = 100 )
    private val player2HandCard = LabeledStackView(posX = 1595, posY = 100)

    private val player1TopLeft = LabeledStackView(posX = 145, posY = 520 ).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player1TopRight = LabeledStackView(posX = 345, posY = 520).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player1BottomLeft = LabeledStackView(posX = 145, posY = 800 ).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player1BottomRight = LabeledStackView(posX = 345, posY = 800).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }

    private val player2TopLeft = LabeledStackView(posX = 1445, posY = 520 ).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player2TopRight = LabeledStackView(posX = 1645, posY = 520).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player2BottomLeft = LabeledStackView(posX = 1445, posY = 800 ).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }
    private val player2BottomRight = LabeledStackView(posX = 1645, posY = 800).apply{
        onMouseClicked = {
            flipCard(this.peek())
        }
    }

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

    private val nextTurnButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 450,
        text = "Next turn",
        font = Font(size = 38)

    ).apply {
        visual = ColorVisual(255, 255, 255)
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.gameService.endTurn()
            }
        }
    }
    private val usePowerButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 550,
        text = "Use Power",
        font = Font(size = 38)
    ).apply {
        visual = ColorVisual(255, 255, 255)
        onMouseClicked = {

            }
        }

    private val knockButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 650,
        text = "Knock",
        font = Font(size = 38)
    ).apply {
        visual = ColorVisual(255, 255, 255)
        onMouseClicked = {

            }
        }

    private val swapButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 550,
        text = "Swap cards",
        font = Font(size = 38)
    ).apply {
        visual = ColorVisual(255, 255, 255)
        onMouseClicked = {

            }
        }


    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)
        addComponents(player1HandCard, player2HandCard, usedStack, newStack,
            player1TopLeft, player1TopRight, player1BottomLeft, player1BottomRight,
            player2TopLeft, player2TopRight, player2BottomLeft, player2BottomRight,
            nextTurnButton, swapButton)
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
        initializePlayersDecks(game.players[0].deck, game.players[0], cardImageLoader)
        initializePlayersDecks(game.players[1].deck, game.players[1], cardImageLoader)
       // usePowerButton.isVisible = false
        //knockButton.isVisible = false
        swapButton.isVisible = false
    }

    override fun refreshAfterEachTurn() {
        swapButton.isVisible = false
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = game.currentPlayer
        val listOfCardViews1 = mutableListOf<CardView>(player1TopLeft.peek(),
            player1TopRight.peek(), player1BottomLeft.peek(), player1BottomRight.peek())
        val listOfCardViews2 = mutableListOf<CardView>(player2TopLeft.peek(),
            player2TopRight.peek(), player2BottomLeft.peek(), player2BottomRight.peek())

        if (currentPlayer == game.players[1]){
            if (player1BottomLeft.peek().currentSide == CardView.CardSide.FRONT &&
                player1BottomRight.peek().currentSide == CardView.CardSide.FRONT)
            {
                for (card in listOfCardViews1){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
            }
            else{
                for (card in listOfCardViews1){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
                for (card in listOfCardViews2){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
            }


        }
        else {
            if (player2BottomLeft.peek().currentSide == CardView.CardSide.FRONT &&
                player2BottomRight.peek().currentSide == CardView.CardSide.FRONT){
                for (card in listOfCardViews2){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
            }
            else
            {
                for (card in listOfCardViews1){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
                for (card in listOfCardViews2){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
            }

        }
        if (player1BottomLeft.peek().currentSide != CardView.CardSide.FRONT &&
        player1BottomRight.peek().currentSide != CardView.CardSide.FRONT &&
        player2BottomLeft.peek().currentSide != CardView.CardSide.FRONT &&
        player2BottomRight.peek().currentSide != CardView.CardSide.FRONT &&
        !game.players[0].knocked && !game.players[1].knocked
        ){
            nextTurnButton.isVisible = true
            nextTurnButton.text = "Knock"
            nextTurnButton.apply {onMouseClicked = {
                rootService.playerService.knock() }
            }

        }
        /*val cardImageLoader = CardImageLoader()
        initializeStackView(game.newStack, newStack, cardImageLoader)
        initializeStackView(game.usedStack, usedStack, cardImageLoader)*/

    }

    override fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean) {
        val game = rootService.currentGame
        val cardImageLoader = CardImageLoader()
        checkNotNull(game) { "No game found." }
        nextTurnButton.isVisible = false
        if (discardable){
            game.newStack.push(game.currentPlayer!!.hand!!)

            when (game.currentPlayer) {
                game.players[0] -> {
                    moveCardView(cardMap.forward(game.newStack.pop()), player1HandCard, true)
                    player1TopLeft.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.TOP_LEFT)}}
                    player1TopRight.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.TOP_RIGHT)}}
                    player1BottomLeft.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.BOTTOM_LEFT)}}
                    player1BottomRight.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.BOTTOM_RIGHT)}}

                    player2TopLeft.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player2TopRight.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player2BottomLeft.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player2BottomRight.apply { onMouseClicked = {error("You can't swap other player's card")} }
                }
                game.players[1] -> {
                    moveCardView(cardMap.forward(game.newStack.pop()), player2HandCard, true)
                    player2TopLeft.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.TOP_LEFT)}}
                    player2TopRight.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.TOP_RIGHT)}}
                    player2BottomLeft.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.BOTTOM_LEFT)}}
                    player2BottomRight.apply { onMouseClicked = {rootService.playerService.swapSelf(DeckPosition.BOTTOM_RIGHT)}}

                    player1TopLeft.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player1TopRight.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player1BottomLeft.apply { onMouseClicked = {error("You can't swap other player's card")} }
                    player1BottomRight.apply { onMouseClicked = {error("You can't swap other player's card")} }
                }
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






        if (usablePower){
            nextTurnButton.isVisible = true
            nextTurnButton.text = "Use Power"
            nextTurnButton.apply { onMouseClicked = {rootService.playerService.usePower()}}
        }
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

    override fun refreshAfterPeakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player) {
        val game = rootService.currentGame

        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]
        if (playerToPeak == player1){
            when (positionToPeak.toInt()) {
                0 -> flipCard(player1TopLeft.peek())
                1 -> flipCard(player1TopRight.peek())
                2 -> flipCard(player1BottomLeft.peek())
                3 -> flipCard(player1BottomRight.peek())
            }
        }
        else {
            when (positionToPeak.toInt()) {
                0 -> flipCard(player2TopLeft.peek())
                1 -> flipCard(player2TopRight.peek())
                2 -> flipCard(player2BottomLeft.peek())
                3 -> flipCard(player2BottomRight.peek())
            }
        }
        println("Current player: ${game.currentPlayer?.name}")
        println("ownselected: ${game.currentPlayer?.ownSelected}")
        println("otherselected: ${game.currentPlayer?.otherSelected}")
        println("Hand: ${game.currentPlayer?.hand}")
    }

    override fun refreshAfterKnock() {
        nextTurnButton.isVisible = false
    }

    override fun refreshAfterSwapSelf(position: DeckPosition) {
        val game = rootService.currentGame

        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]
        if (game.currentPlayer == player1){
            when(position.toInt()){
                0 -> {
                    moveCardView(player1TopLeft.peek(), usedStack, true)
                    moveCardView(player1HandCard.peek(), player1TopLeft, true)
                }
                1 -> {
                    moveCardView(player1TopRight.peek(), usedStack, true)
                    moveCardView(player1HandCard.peek(), player1TopRight, true)
                }
                2 -> {
                    moveCardView(player1BottomLeft.peek(), usedStack, true)
                    moveCardView(player1HandCard.peek(), player1BottomLeft, true)
                }
                3 ->  {
                    moveCardView(player1BottomRight.peek(), usedStack, true)
                    moveCardView(player1HandCard.peek(), player1BottomRight, true)
                }
            }
        }
        else{
            when(position.toInt()){
                0 -> {
                    moveCardView(player2TopLeft.peek(), usedStack, true)
                    moveCardView(player2HandCard.peek(), player2TopLeft, true)
                }
                1 -> {
                    moveCardView(player2TopRight.peek(), usedStack, true)
                    moveCardView(player2HandCard.peek(), player2TopRight, true)
                }
                2 -> {
                    moveCardView(player2BottomLeft.peek(), usedStack, true)
                    moveCardView(player2HandCard.peek(), player2BottomLeft, true)
                }
                3 ->  {
                    moveCardView(player2BottomRight.peek(), usedStack, true)
                    moveCardView(player2HandCard.peek(), player2BottomRight, true)
                }
            }
        }
        cardMap.clear()
        val cardImageLoader = CardImageLoader()
        initializeStackView(game.newStack, newStack, cardImageLoader)
        initializeStackView(game.usedStack, usedStack, cardImageLoader)
    }

    override fun refreshAfterSwapOther(ownPosition: DeckPosition, otherPosition: DeckPosition) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]
        var listOfPositions1 = mutableListOf<CardView>(player1TopLeft.peek(), player1TopRight.peek(),
            player1BottomLeft.peek(), player1BottomRight.peek())
        var listOfPositions2 = mutableListOf<CardView>(player2TopLeft.peek(), player2TopRight.peek(),
            player2BottomLeft.peek(), player2BottomRight.peek())
        var listOfStacks1 = mutableListOf<LabeledStackView>(player1TopLeft, player1TopRight, player1BottomLeft, player1BottomRight)
        var listOfStacks2 = mutableListOf<LabeledStackView>(player2TopLeft, player2TopRight, player2BottomLeft, player2BottomRight)

        var blindPeek: Boolean = false
        println("Current player: ${game.currentPlayer?.name}")
        println("ownselected: ${game.currentPlayer?.ownSelected}")
        println("otherselected: ${game.currentPlayer?.otherSelected}")
        println("Hand: ${game.currentPlayer?.hand}")
        if (game.currentPlayer!!.hand!!.value.toString() == "Q")
            blindPeek = true
        else if (game.currentPlayer!!.hand!!.value.toString() == "J")
            blindPeek = false
        if (game.currentPlayer == player1){
            val cardViewToExchange = listOfPositions2[ownPosition.toInt()]
            moveCardView(listOfPositions1[ownPosition.toInt()], listOfStacks2[otherPosition.toInt()], blindPeek)
            moveCardView(cardViewToExchange, listOfStacks1[ownPosition.toInt()], blindPeek)
        }
        else{
            val cardViewToExchange = listOfPositions1[ownPosition.toInt()]
            moveCardView(listOfPositions2[ownPosition.toInt()], listOfStacks1[otherPosition.toInt()], blindPeek)
            moveCardView(cardViewToExchange, listOfStacks2[ownPosition.toInt()], blindPeek)
        }

    }

    override fun refreshAfterUsePower(highlightDeckPlayer1: Boolean, highlightDeckPlayer2: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]
        if (highlightDeckPlayer1 && !highlightDeckPlayer2){
            player1TopLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_LEFT, player1)}}
            player1TopRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_RIGHT, player1)}}
            player1BottomLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_LEFT, player1)}}
            player1BottomRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_RIGHT, player1)}}

            if (game.currentPlayer!!.hand!!.value.toString() == "Q" || game.currentPlayer!!.hand!!.value.toString() == "J"){
                player2TopLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_LEFT, player2)}}
                player2TopRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_RIGHT, player2)}}
                player2BottomLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_LEFT, player2)}}
                player2BottomRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_RIGHT, player2)}}

            }
            else{
                player2TopLeft.apply { onMouseClicked = {error("You can't see this card")} }
                player2TopRight.apply { onMouseClicked = {error("You can't see this card")} }
                player2BottomLeft.apply { onMouseClicked = {error("You can't see this card")} }
                player2BottomRight.apply { onMouseClicked = {error("You can't see this card")} }
            }


            nextTurnButton.text = "Next turn"
            nextTurnButton.onMouseClicked = {rootService.gameService.endTurn()}
            //rootService.playerService.peakCardPlayer(, player1)
        }
        else if (!highlightDeckPlayer1 && highlightDeckPlayer2){
            player2TopLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_LEFT, player2)}}
            player2TopRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_RIGHT, player2)}}
            player2BottomLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_LEFT, player2)}}
            player2BottomRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_RIGHT, player2)}}

            if (game.currentPlayer!!.hand!!.value.toString() == "Q" || game.currentPlayer!!.hand!!.value.toString() == "J"){
                player1TopLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_LEFT, player1)}}
                player1TopRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.TOP_RIGHT, player1)}}
                player1BottomLeft.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_LEFT, player1)}}
                player1BottomRight.apply { onMouseClicked = {checkIfCardFromDeckIsFront(this, DeckPosition.BOTTOM_RIGHT, player1)}}
                swapButton.isVisible = true
            }
            else {
                player1TopLeft.apply { onMouseClicked = {error("You can't see this card")} }
                player1TopRight.apply { onMouseClicked = {error("You can't see this card")} }
                player1BottomLeft.apply { onMouseClicked = {error("You can't see this card")} }
                player1BottomRight.apply { onMouseClicked = {error("You can't see this card")} }
            }


            nextTurnButton.text = "Next turn"
            nextTurnButton.onMouseClicked = {rootService.gameService.endTurn()}
        }

    }

    override fun refreshAfterChooseCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        if (game.currentPlayer!!.ownSelected != DeckPosition.NOT_SELECTED && game.currentPlayer!!.otherSelected != DeckPosition.NOT_SELECTED){
            swapButton.isVisible = true
            swapButton.apply { onMouseClicked = {rootService.playerService.swapOther(game.currentPlayer!!.ownSelected, game.currentPlayer!!.otherSelected)} }
        }

    }

    private fun checkIfCardFromDeckIsFront(stackView: LabeledStackView, deckPosition: DeckPosition, playerToPeak: Player){
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]
        if (stackView.peek().currentSide == CardView.CardSide.FRONT){
            error("This card is already in front")
        }
        var listOfOtherPositions = mutableListOf<CardView>()
        if (playerToPeak == player1){
            listOfOtherPositions.add(player1TopLeft.peek())
            listOfOtherPositions.add(player1TopRight.peek())
            listOfOtherPositions.add(player1BottomLeft.peek())
            listOfOtherPositions.add(player1BottomRight.peek())
            listOfOtherPositions.remove(stackView.peek())
            if (game.currentPlayer!!.ownSelected != DeckPosition.NOT_SELECTED){
                "You have already peaked one card from this deck"
            }
        }
        else{
            listOfOtherPositions.add(player2TopLeft.peek())
            listOfOtherPositions.add(player2TopRight.peek())
            listOfOtherPositions.add(player2BottomLeft.peek())
            listOfOtherPositions.add(player2BottomRight.peek())
            listOfOtherPositions.remove(stackView.peek())
            if (game.currentPlayer!!.otherSelected != DeckPosition.NOT_SELECTED){
                "You have already peaked one card from this deck"
            }
        }
        for (i in 0..2){
            if (listOfOtherPositions[i].currentSide == CardView.CardSide.FRONT){
                error("You have already peaked one card from this deck")
            }
        }
        if (game.currentPlayer!!.hand!!.value.toString() != "J"){
            rootService.playerService.peakCardPlayer(deckPosition, playerToPeak)
        }
        if (game.currentPlayer!!.hand!!.value.toString() == "Q" || game.currentPlayer!!.hand!!.value.toString() == "J"){
            rootService.playerService.chooseCard(deckPosition, playerToPeak)
        }
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
        if (stackView == usedStack){
            for (card in stackView.components){
                card.currentSide = CardView.CardSide.FRONT
            }
        }
    }

    private fun initializePlayersDecks(deck: MutableList<Card>, player: Player, cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        val player1 = game!!.players[0]
        val player2 = game.players[1]
        /*for (card in deck){
            val cardView = CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            )
        }*/
        if (player == player1){
            player1TopLeft.add(CardView(
            height = 250,
            width = 162.5,
            front = cardImageLoader.frontImageFor(player1.deck[0].suit, player1.deck[0].value),
            back = cardImageLoader.backImage
            ))

            player1TopRight.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player1.deck[1].suit, player1.deck[1].value),
                back = cardImageLoader.backImage
            ))

            player1BottomLeft.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player1.deck[2].suit, player1.deck[2].value),
                back = cardImageLoader.backImage
            ))

            player1BottomRight.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player1.deck[3].suit, player1.deck[3].value),
                back = cardImageLoader.backImage
            ))
        }
        else{
            player2TopLeft.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player2.deck[0].suit, player2.deck[0].value),
                back = cardImageLoader.backImage
            ))

            player2TopRight.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player2.deck[1].suit, player2.deck[1].value),
                back = cardImageLoader.backImage
            ))

            player2BottomLeft.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player2.deck[2].suit, player2.deck[2].value),
                back = cardImageLoader.backImage
            ))

            player2BottomRight.add(CardView(
                height = 250,
                width = 162.5,
                front = cardImageLoader.frontImageFor(player2.deck[3].suit, player2.deck[3].value),
                back = cardImageLoader.backImage
            ))
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

    private fun flipCard(cardView: CardView) {

        when (cardView.currentSide) {
            CardView.CardSide.BACK -> cardView.showFront()
            CardView.CardSide.FRONT -> cardView.showBack()
        }

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