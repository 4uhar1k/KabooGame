package service
import entity.*


/**
 * [PlayerService] is an entity, which provides all logic, performed by players during the game
 * @param rootService The [RootService] instance to access the other service methods and entity layer
 */
class PlayerService(private val rootService: RootService): AbstractRefreshingService() {


    /**
     * This method allows the player to take the card from either the draw pile or the discard pile
     * @param used the Boolean parameter,
     * that shows whether the card should be taken from draw (false) or discard pile(true).
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null)
     */
    fun drawCard(used: Boolean){
        val kaboo = rootService.currentGame
        var usablePower = false
        val listOfUsablePowers = mutableListOf<String>("Q", "J", "10", "9", "8", "7")
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet")}
        checkNotNull(kaboo.currentPlayer)
        check(kaboo.currentPlayer!!.hand == null){ throw IllegalStateException("You can't draw two cards")}
        if (used){
            if (kaboo.usedStack.size == 0)
                IllegalStateException("Used stack is empty")
            kaboo.currentPlayer?.hand = kaboo.usedStack.pop()

        }
        else
        {
            kaboo.currentPlayer?.hand = kaboo.newStack.pop()
        }
        if (listOfUsablePowers.any { it == kaboo.currentPlayer?.hand?.value.toString() }){
            usablePower = true
        }
        onAllRefreshables { refreshAfterDraw(!used, usablePower) }
    }

    /**
     * The method draws the card in player's hand into the discard pile
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null),
     * if there is no current player or if current player got no hand card
     *
     */
    fun discard(){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer){ throw IllegalStateException("There is no current player in this game") }
        val hand = currentPlayer.hand
        checkNotNull(hand)
        kaboo.usedStack.push(hand)
        onAllRefreshables { refreshAfterDiscard() }

        currentPlayer.hand = null
        if (kaboo.newStack.size == 0)
            rootService.gameService.endGame()
        else
            rootService.gameService.openNextPlayerWindow()
    }

    /**
     * The Method gives the Player a way to swap the Card in his hand with one Card in his deck
     * @param position chosen Position in currentPlayer Deck to swap Card in hand with
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null),
     * if there is no current player or if current player got no hand card
     */
    fun swapSelf(position: DeckPosition){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer){ throw IllegalStateException("There is no current player in this game") }
        val hand = currentPlayer.hand
        checkNotNull(hand)
        kaboo.usedStack.push(currentPlayer.deck[position.toInt()])
        currentPlayer.deck[position.toInt()] = hand

        onAllRefreshables { refreshAfterSwapSelf(position) }
        currentPlayer.hand = null
        rootService.gameService.openNextPlayerWindow()
    }

    /**
     * The Method swaps the Card from currentPlayer deck at ownPosition with the Card from otherPlayer at otherPosition.
     * @param ownPosition position in currentPlayer Deck to swap Cards with
     * @param otherPosition position in otherPlayer Deck to swap Cards with
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null),
     * if there is no current player or if current player got no hand card
     */
    fun swapOther(ownPosition: DeckPosition, otherPosition: DeckPosition){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer){ throw IllegalStateException("There is no current player in this game") }
        val hand = currentPlayer.hand
        checkNotNull(hand)
        var otherPlayer = Player()
        if (kaboo.currentPlayer == kaboo.players[0])
            otherPlayer = kaboo.players[1]
        else
            otherPlayer = kaboo.players[0]
        //kaboo.usedStack.push(hand)

        val cardToChange : Card = currentPlayer.deck[ownPosition.toInt()]
        currentPlayer.deck[ownPosition.toInt()] = otherPlayer.deck[otherPosition.toInt()]
        otherPlayer.deck[otherPosition.toInt()] = cardToChange
        onAllRefreshables { refreshAfterSwapOther(ownPosition, otherPosition) }
        discard()
    }

    /**
     * The method is called when a player draws a power card and wants to use its effect.
     * The power card must have been drawn directly from the draw pile, without being swapped with another card.
     * Depending on the value of the drawn card, a specific special effect is triggered:
     * Jack: Blindly swap with another player's card
     * Queen: View and/or swap own and opponent's cards
     * 7 or 8: View one of your own cards
     * 9 or 10: View one of the opponent’s cards After the effect has been executed,
     * the power card is placed face-up on the discard pile.
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null),
     * if there is no current player or if current player got no hand card, or if
     * player's hand card is not a power card
     */
    fun usePower(){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer)
        val hand = currentPlayer.hand
        checkNotNull(hand)
        var otherPlayer = Player()
        if (kaboo.currentPlayer == kaboo.players[0])
            otherPlayer = kaboo.players[1]
        else
            otherPlayer = kaboo.players[0]
        if (hand.value == CardValue.SEVEN || hand.value == CardValue.EIGHT){
            if (kaboo.currentPlayer == kaboo.players[0]){
                onAllRefreshables { refreshAfterUsePower(true,false) }
            }
            else{
                onAllRefreshables { refreshAfterUsePower(false,true) }
            }
        }
        else if (hand.value == CardValue.NINE || hand.value == CardValue.TEN){
            if (kaboo.currentPlayer == kaboo.players[0]){
                onAllRefreshables { refreshAfterUsePower(false,true) }
            }
            else{
                onAllRefreshables { refreshAfterUsePower(true,false) }
            }
        }
        else if (hand.value == CardValue.JACK ){
            onAllRefreshables { refreshAfterUsePower(true,false) }
            onAllRefreshables { refreshAfterUsePower(false,true) }
        }
        else if (hand.value == CardValue.QUEEN){
            onAllRefreshables { refreshAfterUsePower(true,false) }
            onAllRefreshables { refreshAfterUsePower(false,true) }
        }
        else
        {
            throw IllegalStateException("Hand card is not a power card")
        }
    }

    /**
     * This method is called after a player knocks. Once a player has knocked,
     * the other player is no longer allowed to knock and only has one final turn.
     * After that, the game ends automatically.
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null)
     * or if there is no current player
     */
    fun knock(){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer)
        var otherPlayer = Player()
        if (kaboo.currentPlayer == kaboo.players[0])
            otherPlayer = kaboo.players[1]
        else
            otherPlayer = kaboo.players[0]
        if (otherPlayer.knocked)
            throw IllegalStateException("Other player has already knocked")
        currentPlayer.knocked = true
        onAllRefreshables { refreshAfterKnock() }
        rootService.gameService.openNextPlayerWindow()
    }

    /**
     * The method checks the parameters and instructs the GUI
     * to reveal the card at the specified position in the deck of the given player.
     * @param positionToPeak a DeckPosition indicating which position in the deck of playerToPeak should be revealed.
     * @param playerToPeak a Player whose card at position positionToPeak in their deck should be revealed.
     */
    fun peakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player){
        onAllRefreshables { refreshAfterPeakCardPlayer(positionToPeak, playerToPeak) }
    }

    /**
     * The method calls the method peakCardPlayer twice using
     * indices for the bottom two cards and the current player as arguments.
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null),
     * if there is no current player or if the player has already viewed his bottom cards
     */
    fun peakCardsFirstRound(){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer)
        if (!currentPlayer.viewedCards){
            peakCardPlayer(DeckPosition.BOTTOM_LEFT, currentPlayer)
            peakCardPlayer(DeckPosition.BOTTOM_RIGHT, currentPlayer)
            currentPlayer.viewedCards = true
        }
        else {
            throw IllegalStateException("Player has already viewed cards")
        }
    }

    /**
     * This method is called, whenever a player chooses a card to swap when having a jack or queen.
     * It stores the chosen card in either the ownSelected or the otherSelected variable of the current player,
     * depending on the cardOfPlayer.
     * When both variables (ownSelected and otherSelected) are set and the current hand card is a Queen
     * it calls the refreshAfterChooseCard to show the swap button.
     * If the current hand card is a Jack, it calls the swapOther() method to swap the selected cards immediately.
     * @param chosenCardPosition The position of the card in the Deck that the player has chosen to swap.
     * @param cardOfPlayer A Player that is part of the game.
     * @exception IllegalStateException Is thrown if the game is not started (kaboo == null)
     * or if there is no current player
     */
    fun chooseCard(chosenCardPosition: DeckPosition, cardOfPlayer: Player){
        val kaboo = rootService.currentGame
        checkNotNull(kaboo){throw IllegalStateException("Game not started yet") }
        val currentPlayer = kaboo.currentPlayer
        checkNotNull (currentPlayer){ throw IllegalStateException("There is no current player in this game") }

        if (cardOfPlayer == currentPlayer){
            currentPlayer.ownSelected = chosenCardPosition
        }
        else {
            currentPlayer.otherSelected = chosenCardPosition
        }

        if (currentPlayer.ownSelected != DeckPosition.NOT_SELECTED &&
            currentPlayer.otherSelected != DeckPosition.NOT_SELECTED){
            if (currentPlayer.hand?.value == CardValue.JACK){
                //swapOther(currentPlayer.ownSelected, currentPlayer.otherSelected)
                //onAllRefreshables { refreshAfterSwapOther(currentPlayer.ownSelected, currentPlayer.otherSelected) }
                onAllRefreshables { refreshAfterChooseCard() }
            }
            else if (currentPlayer.hand?.value == CardValue.QUEEN){
                onAllRefreshables { refreshAfterChooseCard() }
                //swapOther(currentPlayer.ownSelected, currentPlayer.otherSelected)
                //onAllRefreshables { refreshAfterSwapOther(currentPlayer.ownSelected, currentPlayer.otherSelected) }
            }
        }

    }
}