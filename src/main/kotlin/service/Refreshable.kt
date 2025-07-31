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
    fun refreshAfterGameStart(){

    }
    fun refreshAfterAddPlayers(){

    }
    fun refreshAfterEndGame(winnerMessage: String){

    }
    fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean){

    }
    fun refreshAfterDiscard(){

    }
    fun refreshAfterSwapOther(){

    }
    fun refreshAfterSwapSelf(position: DeckPosition){

    }
    fun refreshAfterPeakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player){

    }
    fun refreshAfterEachTurn(){

    }
    fun refreshAfterKnock(){

    }
    fun refreshAfterGameMove(canKnock: Boolean, canTakeUsedCard: Boolean){

    }
    fun refreshAfterUsePower(highlightDeckPlayer1: Boolean, highlightDeckPlayer2: Boolean){

    }
    fun refreshAfterChooseCard(){

    }
}