package service

import entity.*

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the GUI classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * GUI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 */
interface Refreshable{
    /**
     * The method switches from the start game screen to the normal game screen.
     */
    fun refreshAfterStartGame(){

    }

    /**
     * Refreshable, that displays [gui.KabooStartMenuScene]
     * after the player klicked on "Start new game" button
     */
    fun refreshAfterStartNewGame(){

    }

    /**
     * The method displays the main game screen after the names of both players are added.
     */
    fun refreshAfterAddPlayers(){

    }

    /**
     * The method takes the players back to start menu screen after the game is ended.
     * @param winnerMessage a String of the winner's name
     */
    fun refreshAfterEndGame(winnerMessage: String){

    }

    /**
     * The method puts the card from the deck the player has selected in his hand card position
     * @param discardable Indicates if hand card can be discarded
     * @param usablePower Indicates if hand card is a power card
     */
    fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean){

    }

    /**
     * The method removes the card from position of players' hand card
     */
    fun refreshAfterDiscard(){

    }

    /**
     * Updates GUI after SwapOther is done
     * (refreshes the positions in currentPlayer and otherPlayer deck and usedCardStack)
     * @param ownPosition position in currentPlayer Deck to swap Cards
     * @param otherPosition position in otherPlayer Deck to swap Cards
     */
    fun refreshAfterSwapOther(ownPosition: DeckPosition, otherPosition: DeckPosition){

    }

    /**
     * Updates the GUI after SwapSelf was done (update usedCardStack and currentPlayer deck at given position)
     * @param position chosen Position in current player's deck to swap Card in hand with
     */
    fun refreshAfterSwapSelf(position: DeckPosition){

    }

    /**
     * The method reveals the card at a specified position for a given player.
     * @param positionToPeak an Int indicating which position in the deck of playerToPeak should be revealed.
     * @param playerToPeak a Player whose card at position positionToPeak in their deck should be revealed.
     */
    fun refreshAfterPeakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player){

    }

    /**
     * This method will update every scene that is using it in GUI.
     * It will be called by endTurn() so the dialogue box gets displayed.
     */
    fun refreshAfterEachTurn(){

    }

    /**
     * The method is called after a player has knocked.
     * All relevant UI components are notified of the knock and update accordingly.
     */
    fun refreshAfterKnock(){

    }

    /**
     * The method is called after gameMove() and updates GUI due to game conditions
     * For example, it hides the "knock" button
     * @param canKnock indicates, if the next player can knock
     * @param canTakeUsedCard indicates, if the next player can take card from used stack
     */
    fun refreshAfterGameMove(canKnock: Boolean, canTakeUsedCard: Boolean){

    }

    /**
     * The method highlights all the cards, that can be peaked after using power card
     * @param highlightDeckPlayer1 indicates, if the deck of player1 must be highlighted
     * @param highlightDeckPlayer2 indicates, if the deck of player2 must be highlighted
     */
    fun refreshAfterUsePower(highlightDeckPlayer1: Boolean, highlightDeckPlayer2: Boolean){

    }

    /**
     * The method is called if hand card is QUEEN and player clicks on cards in deck
     */
    fun refreshAfterChooseCard(){

    }
}