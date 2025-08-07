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
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Stack
import javax.swing.text.DefaultHighlighter
import kotlin.collections.get
import kotlin.text.clear
/**
 * This is the main thing for the Kaboo game. The scene shows the complete table at once.
 * Each player got 4 cards in his deck. Deck of the Player1 is on the left, of the Player2
 * on the right. Each player also got a place for hand card which can be drawn either from
 * new pile (left in the middle) or discard pile(right in the middle)
 *
 * @param rootService The [RootService] instance to access the other service methods and entity layer
 */

class KabooBoardGameScene(val rootService: RootService): BoardGameScene(), Refreshable {
    //val rootService: RootService = RootService()


    private val player1HandCard = LabeledStackView(posX = 195, posY = 100 ).apply{


    }
    private val player2HandCard = LabeledStackView(posX = 1595, posY = 100)

    private val player1TopLeft = LabeledStackView(posX = 145, posY = 450 )

    private val player1TopRight = LabeledStackView(posX = 345, posY = 450)
    private val player1BottomLeft = LabeledStackView(posX = 145, posY = 730 )
    private val player1BottomRight = LabeledStackView(posX = 345, posY = 730)

    private val player2TopLeft = LabeledStackView(posX = 1445, posY = 450 )
    private val player2TopRight = LabeledStackView(posX = 1645, posY = 450)
    private val player2BottomLeft = LabeledStackView(posX = 1445, posY = 730 )
    private val player2BottomRight = LabeledStackView(posX = 1645, posY = 730)

    private val player1NameLabel = Label(
        width = 400,
        height = 100,
        posX = 125,
        posY = 980,
        text = "player1",
        font = Font(size = 38),

    )
    private val player2NameLabel = Label(
        width = 400,
        height = 100,
        posX = 1425,
        posY = 980,
        text = "player2",
        font = Font(size = 38)
    )

    private val newStack = LabeledStackView(posX = 800, posY = 100 ).apply {
        onMouseClicked = {
            //rootService.currentGame?.let { game ->
                //rootService.playerService.drawCard(false)
            //}
        }
    }

    /**
     * if a player has a hand card, that means that he wants to discard the card
     * if not, he wants to pick a card from discard pile
     */
    private val usedStack = LabeledStackView(posX = 1000, posY = 100).apply {
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game) { "No game found." }
            val currentPlayer = game.currentPlayer
            checkNotNull(currentPlayer) {"No current player found."}
            if (currentPlayer.hand == null)
            {
                rootService.playerService.drawCard(true)
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
                rootService.gameService.openNextPlayerWindow()
            }
        }
    }


    private val swapButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 550,
        text = "Swap cards",
        font = Font(size = 38)
    ).apply {
        visual = ColorVisual(255, 255, 255)
    }

    /**
     * This button appears after player decided to view cards after end of the game
     * It calls [KabooStartMenuScene]
     */
    public val startNewGameButton = Button(
        width = 350, height = 75,
        posX = 785, posY = 450,
        text = "Start new game",
        font = Font(size = 38)
    ).apply {
        visual = ColorVisual(255, 255, 255)
    }
    private val logLabel = Label(
        width = 700,
        height = 100,
        posX = 585,
        posY = 700,
        text = "",
        font = Font(size = 28),
        isWrapText = true
    )


    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()
    /**
     * Initializes the scene by setting the background color and adding the label.
     */
    init {
        background = ColorVisual(108, 168, 59)
        addComponents(player1HandCard, player2HandCard, usedStack, newStack,
            player1TopLeft, player1TopRight, player1BottomLeft, player1BottomRight,
            player2TopLeft, player2TopRight, player2BottomLeft, player2BottomRight,
            nextTurnButton, swapButton, player1NameLabel, player2NameLabel, startNewGameButton,
            logLabel)
    }

    /**
     * The method switches from the start game screen to the normal game screen,
     * initializes new and discard piles and each player's deck
     */
    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer)
        if (currentPlayer == game.players[1]){
            player2NameLabel.font = Font(size = 38, color = Color.RED)
            player1NameLabel.font = Font(size = 38, color = Color.BLACK)
        }
        else{
            player1NameLabel.font = Font(size = 38, color = Color.RED)
            player2NameLabel.font = Font(size = 38, color = Color.BLACK)
        }

        cardMap.clear()
        val cardImageLoader = CardImageLoader()

        initializeStackView(game.newStack, newStack, cardImageLoader)
        initializeStackView(game.usedStack, usedStack, cardImageLoader)
        player1HandCard.clear()
        player2HandCard.clear()
        initializePlayersDecks(game.players[0].deck, game.players[0], cardImageLoader)
        initializePlayersDecks(game.players[1].deck, game.players[1], cardImageLoader)
        nextTurnButton.isVisible = true
        nextTurnButton.text = "Next turn"
        nextTurnButton.onMouseClicked = {rootService.gameService.openNextPlayerWindow()}
        swapButton.isVisible = false
        startNewGameButton.isVisible = false
        logLabel.text = game.log
    }

    override fun refreshAfterAddPlayers() {
        val game = rootService.currentGame
        //checkNotNull(game)
        if (game != null){
            player1NameLabel.text = game.players[0].name
            player2NameLabel.text = game.players[1].name
        }

    }

    /**
     * This method refreshes GUI after each turn
     * Besides the first round, when a player can view his bottom cards,
     * it reverses all the cards back. If it's first round, only the cards
     * of other player. Also it shows button "Knock", if a player has not picked
     * any card yet.
     */
    override fun refreshAfterEachTurn() {
        swapButton.isVisible = false

        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        logLabel.text = game.log
        game.log = ""
        val listOfCardViews1 = mutableListOf<CardView>(player1TopLeft.peek(),
            player1TopRight.peek(), player1BottomLeft.peek(), player1BottomRight.peek())
        val listOfCardViews2 = mutableListOf<CardView>(player2TopLeft.peek(),
            player2TopRight.peek(), player2BottomLeft.peek(), player2BottomRight.peek())

        if (currentPlayer.viewedCards){
            newStack.onMouseClicked = {rootService.playerService.drawCard(false)}

        }

        if (currentPlayer == game.players[1]){
            player2NameLabel.font = Font(size = 38, color = Color.RED)
            player1NameLabel.font = Font(size = 38, color = Color.BLACK)
            if (player1BottomLeft.peek().currentSide == CardView.CardSide.FRONT &&
                player1BottomRight.peek().currentSide == CardView.CardSide.FRONT){
                for (card in listOfCardViews1){
                    if (card.currentSide == CardView.CardSide.FRONT){
                        flipCard(card)
                    }
                }
            }
            else{
                //newStack.onMouseClicked = {rootService.playerService.drawCard(false)}
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
            player1NameLabel.font = Font(size = 38, color = Color.RED)
            player2NameLabel.font = Font(size = 38, color = Color.BLACK)
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
                //newStack.onMouseClicked = {rootService.playerService.drawCard(false)}
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
            nextTurnButton.onMouseClicked = {
                rootService.playerService.knock() }

        }
    }

    /**
     * This method refreshes GUI after drawing a card.
     * It allows the player to select a card from his deck to swap with hand card
     * and checks that the current player can't swap from opponent's deck
     * @param discardable Indicates if hand card can be discarded
     * @param usablePower Indicates if hand card is a power card
     */
    override fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand) {"No current player's hand card found."}
        nextTurnButton.isVisible = false
        if (discardable){
            game.newStack.push(currentPlayerHand)

            when (currentPlayer) {
                game.players[0] -> {
                    moveCardView(cardMap.forward(game.newStack.pop()), player1HandCard, true)
                }
                game.players[1] -> {
                    moveCardView(cardMap.forward(game.newStack.pop()), player2HandCard, true)
                }
            }
        }
        else{
            game.usedStack.push(currentPlayerHand)

            when (currentPlayer) {
                game.players[0] -> {
                    moveCardView(cardMap.forward(game.usedStack.pop()), player1HandCard, false)
                }
                game.players[1] -> {
                    moveCardView(cardMap.forward(game.usedStack.pop()), player2HandCard, false)
                }
            }
        }
        when (currentPlayer) {
            game.players[0] -> {
                player1TopLeft.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.TOP_LEFT) }
                player1TopRight.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.TOP_RIGHT) }
                player1BottomLeft.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.BOTTOM_LEFT) }
                player1BottomRight.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.BOTTOM_RIGHT) }

                player2TopLeft.onMouseClicked = { error("You can't swap other player's card") }
                player2TopRight.onMouseClicked = { error("You can't swap other player's card") }
                player2BottomLeft.onMouseClicked = { error("You can't swap other player's card") }
                player2BottomRight.onMouseClicked = { error("You can't swap other player's card") }
            }

            game.players[1] -> {
                player2TopLeft.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.TOP_LEFT) }
                player2TopRight.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.TOP_RIGHT) }
                player2BottomLeft.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.BOTTOM_LEFT) }
                player2BottomRight.onMouseClicked = { rootService.playerService.swapSelf(DeckPosition.BOTTOM_RIGHT) }

                player1TopLeft.onMouseClicked = { error("You can't swap other player's card") }
                player1TopRight.onMouseClicked = { error("You can't swap other player's card") }
                player1BottomLeft.onMouseClicked = { error("You can't swap other player's card") }
                player1BottomRight.onMouseClicked = { error("You can't swap other player's card") }
            }
        }
        checkAllStackViews(game)
        if (usablePower && discardable){
            nextTurnButton.isVisible = true
            nextTurnButton.text = "Use Power"
            nextTurnButton.onMouseClicked = {rootService.playerService.usePower()}
        }
    }

    /**
     * The method refreshes GUI after discarding a card.
     * It moves a card from player's hand to discard pile and
     * clears player's hand card position
     */
    override fun refreshAfterDiscard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand) {"No current player's hand card found."}
        moveCardView(cardMap.forward(currentPlayerHand), usedStack, false)
        when (currentPlayer) {
            game.players[0] -> {
                player1HandCard.clear()
            }
            game.players[1] -> {
                player2HandCard.clear()
            }
        }
        if(!game.log.contains("viewed") && !game.log.contains("swapped your card"))
            game.log = "${currentPlayer.name} has discarded the card"
        if (currentPlayerHand.value.toString() == "Q") {
                game.log = "${currentPlayer.name} viewed your card on position " +
                        "${currentPlayer.otherSelected} and his on position ${currentPlayer.ownSelected} " +
                        "but didn't swap"
        }
    }

    /**
     * The method reverses a card, that a player has selected (will be used
     * by Use Power of 7,8,9,10 and Queen)
     * @param positionToPeak an Int indicating which position in the deck of playerToPeak should be revealed.
     * @param playerToPeak a Player whose card at position positionToPeak in their deck should be revealed.
     */
    override fun refreshAfterPeakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        //checkNotNull(currentPlayerHand) {"No current player's hand card found."}
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
        if (currentPlayerHand?.value.toString() != "Q"){
            nextTurnButton.isVisible = true
            nextTurnButton.text = "Next turn"
            nextTurnButton.onMouseClicked = {rootService.gameService.openNextPlayerWindow()}
            if (playerToPeak == currentPlayer){
                game.log = "${currentPlayer.name} has viewed his card on position $positionToPeak"
            }
            else{
                game.log = "${currentPlayer.name} has viewed your card on position $positionToPeak"
            }
        }
        if (!currentPlayer.viewedCards){
            game.log = "${currentPlayer.name} has seen his bottom cards"
            //logLabel.text = game.log
        }
    }

    /**
     * The method refreshes GUI after a player has knocked.
     * It removes the button "Knock"
     */
    override fun refreshAfterKnock() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        nextTurnButton.isVisible = false
        game.log = "${currentPlayer.name} has knocked"
    }

    /**
     * The method refreshes GUI after swapSelf()
     * It removes selected card from player's deck to discard pile
     * and hand card to its previous position
     * @param position chosen Position in current player's deck to swap Card in hand with
     */
    override fun refreshAfterSwapSelf(position: DeckPosition) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}

        val player1 = game.players[0]
        val player2 = game.players[1]
        if (currentPlayer == player1){
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
        game.log = "${currentPlayer.name} has swapped his card on position $position"
    }

    /**
     * The method refreshes GUI after swapOther()
     * It changes selected cards from current player's and his opponents decks.
     * If it will be provided by Jack, swap works blindly, if not, player should first
     * see the cards
     * @param ownPosition position in currentPlayer Deck to swap Cards
     * @param otherPosition position in otherPlayer Deck to swap Cards
     */
    override fun refreshAfterSwapOther(ownPosition: DeckPosition, otherPosition: DeckPosition) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand) {"No current player's hand card found."}
        val player1 = game.players[0]
        val player2 = game.players[1]
        val listOfPositions1 = mutableListOf<CardView>(player1TopLeft.peek(), player1TopRight.peek(),
            player1BottomLeft.peek(), player1BottomRight.peek())
        val listOfPositions2 = mutableListOf<CardView>(player2TopLeft.peek(), player2TopRight.peek(),
            player2BottomLeft.peek(), player2BottomRight.peek())
        val listOfStacks1 = mutableListOf<LabeledStackView>(player1TopLeft, player1TopRight,
            player1BottomLeft, player1BottomRight)
        val listOfStacks2 = mutableListOf<LabeledStackView>(player2TopLeft, player2TopRight,
            player2BottomLeft, player2BottomRight)

        var blindPeek: Boolean = false
        if (currentPlayerHand.value.toString() == "Q")
            blindPeek = true
        else if (currentPlayerHand.value.toString() == "J")
            blindPeek = false
        if (currentPlayer == player1){
            val cardViewToExchange = listOfPositions2[otherPosition.toInt()]
            moveCardView(listOfPositions1[ownPosition.toInt()], listOfStacks2[otherPosition.toInt()], blindPeek)
            moveCardView(cardViewToExchange, listOfStacks1[ownPosition.toInt()], blindPeek)
        }
        else{
            val cardViewToExchange = listOfPositions1[otherPosition.toInt()]
            moveCardView(listOfPositions2[ownPosition.toInt()], listOfStacks1[otherPosition.toInt()], blindPeek)
            moveCardView(cardViewToExchange, listOfStacks2[ownPosition.toInt()], blindPeek)
        }

    }

    /**
     * The method updates GUI after usePower(). It allows to flip cards from any of player's decks
     * depending on power card. If 7,8 - own deck, 9,10 - opponent's deck,
     * Jack or Queen - both
     * @param highlightDeckPlayer1 indicates, if the deck of player1 must be highlighted
     * @param highlightDeckPlayer2 indicates, if the deck of player2 must be highlighted
     */
    override fun refreshAfterUsePower(highlightDeckPlayer1: Boolean, highlightDeckPlayer2: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand) {"No current player's hand card found."}
        val player1 = game.players[0]
        val player2 = game.players[1]
        nextTurnButton.isVisible = false
        if (currentPlayerHand.value.toString() == "Q" || currentPlayerHand.value.toString() == "J") {
            player1TopLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player1TopLeft,
                DeckPosition.TOP_LEFT, player1)}
            player1TopRight.onMouseClicked = {checkIfCardFromDeckIsFront(player1TopRight,
                DeckPosition.TOP_RIGHT, player1)}
            player1BottomLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player1BottomLeft,
                DeckPosition.BOTTOM_LEFT, player1)}
            player1BottomRight.onMouseClicked = {checkIfCardFromDeckIsFront(player1BottomRight,
                DeckPosition.BOTTOM_RIGHT, player1)}
            player2TopLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player2TopLeft,
                DeckPosition.TOP_LEFT, player2)}
            player2TopRight.onMouseClicked = {checkIfCardFromDeckIsFront(player2TopRight,
                DeckPosition.TOP_RIGHT, player2)}
            player2BottomLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player2BottomLeft,
                DeckPosition.BOTTOM_LEFT, player2)}
            player2BottomRight.onMouseClicked = {checkIfCardFromDeckIsFront(player2BottomRight,
                DeckPosition.BOTTOM_RIGHT, player2)}
        }
        else{
            if (highlightDeckPlayer1 && !highlightDeckPlayer2){
                player1TopLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player1TopLeft,
                    DeckPosition.TOP_LEFT, player1)}
                player1TopRight.onMouseClicked = {checkIfCardFromDeckIsFront(player1TopRight,
                    DeckPosition.TOP_RIGHT, player1)}
                player1BottomLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player1BottomLeft,
                    DeckPosition.BOTTOM_LEFT, player1)}
                player1BottomRight.onMouseClicked = {checkIfCardFromDeckIsFront(player1BottomRight,
                    DeckPosition.BOTTOM_RIGHT, player1)}
                player2TopLeft.onMouseClicked = {error("You can't see this card")}
                player2TopRight.onMouseClicked = {error("You can't see this card")}
                player2BottomLeft.onMouseClicked = {error("You can't see this card")}
                player2BottomRight.onMouseClicked = {error("You can't see this card")}
            }
            else if (!highlightDeckPlayer1 && highlightDeckPlayer2){
                player2TopLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player2TopLeft,
                    DeckPosition.TOP_LEFT, player2)}
                player2TopRight.onMouseClicked = {checkIfCardFromDeckIsFront(player2TopRight,
                    DeckPosition.TOP_RIGHT, player2)}
                player2BottomLeft.onMouseClicked = {checkIfCardFromDeckIsFront(player2BottomLeft,
                    DeckPosition.BOTTOM_LEFT, player2)}
                player2BottomRight.onMouseClicked = {checkIfCardFromDeckIsFront(player2BottomRight,
                    DeckPosition.BOTTOM_RIGHT, player2)}
                player1TopLeft.onMouseClicked = {error("You can't see this card")}
                player1TopRight.onMouseClicked = {error("You can't see this card")}
                player1BottomLeft.onMouseClicked = {error("You can't see this card")}
                player1BottomRight.onMouseClicked = {error("You can't see this card")}
            }
        }


    }

    /**
     * The method refreshes GUI after chooseCard()
     * If both cards for swapOther() are already selected,
     * it shows "Swap" button.
     */
    override fun refreshAfterChooseCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand)
        if (currentPlayer.ownSelected != DeckPosition.NOT_SELECTED &&
            currentPlayer.otherSelected != DeckPosition.NOT_SELECTED){
            nextTurnButton.isVisible = true
            nextTurnButton.text = "Next turn"
            nextTurnButton.onMouseClicked = {rootService.gameService.openNextPlayerWindow();
                game.log = "${currentPlayer.name} has discarded the card"}
            swapButton.isVisible = true
            swapButton.onMouseClicked = {rootService.playerService.swapOther(currentPlayer.ownSelected,
                currentPlayer.otherSelected); game.log = "${currentPlayer.name} swapped your card on position " +
                    "${currentPlayer.otherSelected} with his on position ${currentPlayer.ownSelected}"}
            if (currentPlayerHand.value.toString() == "Q"){
                nextTurnButton.onMouseClicked = {rootService.gameService.openNextPlayerWindow();
                    game.log = "${currentPlayer.name} viewed your card on position " +
                            "${currentPlayer.otherSelected} and his on position ${currentPlayer.ownSelected} " +
                            "but didn't swap"}
            }

        }

    }

    override fun refreshAfterEndGame(winnerMessage: String) {
        player2NameLabel.font = Font(size = 38, color = Color.BLACK)
        player1NameLabel.font = Font(size = 38, color = Color.BLACK)
        if (player1TopLeft.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player1TopLeft.peek())
        if (player1TopRight.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player1TopRight.peek())
        if (player1BottomLeft.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player1BottomLeft.peek())
        if (player1BottomRight.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player1BottomRight.peek())
        if (player2TopLeft.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player2TopLeft.peek())
        if (player2TopRight.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player2TopRight.peek())
        if (player2BottomLeft.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player2BottomLeft.peek())
        if (player2BottomRight.peek().currentSide== CardView.CardSide.BACK)
            flipCard(player2BottomRight.peek())
        startNewGameButton.isVisible = true
        if (winnerMessage != "Draw")
            logLabel.text = "$winnerMessage is a winner!"
        else
            logLabel.text = "It's a draw!"
    }

    /**
     * The method restricts, that player can flip more than one card from one deck
     * @param stackView Selected card of player as LabeledStackView
     * @param deckPosition Selected card of player as DeckPosition
     * @param playerToPeak Player, which card was selected to flip
     */
    private fun checkIfCardFromDeckIsFront(stackView: LabeledStackView,
                                           deckPosition: DeckPosition, playerToPeak: Player){
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val currentPlayer = game.currentPlayer
        checkNotNull(currentPlayer) {"No current player found."}
        val currentPlayerHand = currentPlayer.hand
        checkNotNull(currentPlayerHand) {"No current player's hand card found."}
        val player1 = game.players[0]
        val player2 = game.players[1]
        if (stackView.peek().currentSide == CardView.CardSide.FRONT){
            error("This card is already in front")
        }
        val listOfOtherPositions = mutableListOf<CardView>()
        if (currentPlayer == player1){
            if (playerToPeak == player1){
                listOfOtherPositions.add(player1TopLeft.peek())
                listOfOtherPositions.add(player1TopRight.peek())
                listOfOtherPositions.add(player1BottomLeft.peek())
                listOfOtherPositions.add(player1BottomRight.peek())
                listOfOtherPositions.remove(stackView.peek())
                if (currentPlayer.ownSelected != DeckPosition.NOT_SELECTED){
                    error("You have already peaked one card from this deck")
                }
            }
            else{
                listOfOtherPositions.add(player2TopLeft.peek())
                listOfOtherPositions.add(player2TopRight.peek())
                listOfOtherPositions.add(player2BottomLeft.peek())
                listOfOtherPositions.add(player2BottomRight.peek())
                listOfOtherPositions.remove(stackView.peek())
                if (currentPlayer.otherSelected != DeckPosition.NOT_SELECTED){
                    error("You have already peaked one card from this deck")
                }
            }
        }
        else{
            if (playerToPeak == player1){
                listOfOtherPositions.add(player1TopLeft.peek())
                listOfOtherPositions.add(player1TopRight.peek())
                listOfOtherPositions.add(player1BottomLeft.peek())
                listOfOtherPositions.add(player1BottomRight.peek())
                listOfOtherPositions.remove(stackView.peek())
                if (currentPlayer.otherSelected != DeckPosition.NOT_SELECTED){
                    error("You have already peaked one card from this deck")
                }
            }
            else{
                listOfOtherPositions.add(player2TopLeft.peek())
                listOfOtherPositions.add(player2TopRight.peek())
                listOfOtherPositions.add(player2BottomLeft.peek())
                listOfOtherPositions.add(player2BottomRight.peek())
                listOfOtherPositions.remove(stackView.peek())
                if (currentPlayer.ownSelected != DeckPosition.NOT_SELECTED){
                    error("You have already peaked one card from this deck")
                }
            }
        }

        for (i in 0..2){
            if (listOfOtherPositions[i].currentSide == CardView.CardSide.FRONT){
                error("You have already peaked one card from this deck")
            }
        }
        if (currentPlayerHand.value.toString() != "J"){
            rootService.playerService.peakCardPlayer(deckPosition, playerToPeak)
        }
        if (currentPlayerHand.value.toString() == "Q" || currentPlayerHand.value.toString() == "J"){
            rootService.playerService.chooseCard(deckPosition, playerToPeak)
        }
    }

    /**
     * clears [stackView], adds a new [CardView] for each
     * element of [stack] onto it, and adds the newly created view/card pair
     * to the global [cardMap].
     */
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

    /**
     * Initializes decks for each player
     */
    private fun initializePlayersDecks(deck: MutableList<Card>, player: Player, cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        val player1 = game.players[0]
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
    /**
     * moves a [cardView] from current container on top of [toStack].
     *
     * @param flip if true, the view will be flipped from [CardView.CardSide.FRONT] to
     * [CardView.CardSide.BACK] and vice versa.
     */
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

    /**
     * Reverses selected card upside down
     * @param cardView selected card
     */
    private fun flipCard(cardView: CardView) {

        when (cardView.currentSide) {
            CardView.CardSide.BACK -> cardView.showFront()
            CardView.CardSide.FRONT -> cardView.showBack()
        }

    }
    /**
     * As GUI is complicated to test, this method provides a way to constantly runtime-check
     * if the [CardView]s contained in the players' [LabeledStackView] UI components match with
     * what currently is the state of the entity layer.
     *
     * This is just a convenience wrapper that calls [checkStackView] with all four stacks of each player.
     */
    private fun checkAllStackViews(game: Kaboo) {
        checkStackView(game.newStack, newStack)
        checkStackView(game.usedStack, usedStack)

    }
    /**
     * Checks if the given [stackView] contains (in the correct order)
     * [CardView]s for all [WarCard]s currently in [stack].
     *
     * @throws IllegalStateException if a mismatch is found
     */
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