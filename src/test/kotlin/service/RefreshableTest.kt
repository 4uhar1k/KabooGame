package service

import entity.DeckPosition
import entity.Player

/**
 * This class helps us to test, if the refreshables would be called in methods
 */
class RefreshableTest: Refreshable {
    var refreshAfterDrawCalled: Boolean = false
        private set
    var refreshAfterDiscardCalled: Boolean = false
        private set
    var refreshAfterEachTurnCalled: Boolean = false
        private set
    var refreshAfterSwapSelfCalled: Boolean = false
        private set
    var refreshAfterSwapOtherCalled: Boolean = false
        private set
    var refreshAfterUsePowerTrueFalseCalled: Boolean = false
        private set
    var refreshAfterUsePowerFalseTrueCalled: Boolean = false
        private set
    var refreshAfterKnockCalled: Boolean = false
        private set
    var refreshAfterPeakCardPlayerCalled: Boolean = false
        private set
    var refreshAfterChooseCardCalled: Boolean = false
        private set
    var refreshAfterAddPlayerCalled: Boolean = false
        private set
    var refreshAfterStartGameCalled: Boolean = false
        private set
    var refreshAfterEndGameCalled: Boolean = false
        private set

    fun reset(){
        refreshAfterDrawCalled = false
        refreshAfterDiscardCalled = false
        refreshAfterEachTurnCalled = false
        refreshAfterSwapSelfCalled = false
        refreshAfterSwapOtherCalled = false
        refreshAfterUsePowerTrueFalseCalled = false
        refreshAfterUsePowerFalseTrueCalled = false
        refreshAfterKnockCalled = false
        refreshAfterPeakCardPlayerCalled = false
        refreshAfterChooseCardCalled = false
        refreshAfterAddPlayerCalled = false
        refreshAfterStartGameCalled = false
        refreshAfterEndGameCalled = false
    }

    override fun refreshAfterDraw(discardable: Boolean, usablePower: Boolean) {
        refreshAfterDrawCalled = true
    }

    override fun refreshAfterDiscard() {
        refreshAfterDiscardCalled = true
    }
    override fun refreshAfterEachTurn() {
        refreshAfterEachTurnCalled = true
    }

    override fun refreshAfterSwapSelf(position: DeckPosition) {
        refreshAfterSwapSelfCalled = true
    }

    override fun refreshAfterSwapOther(ownPosition: DeckPosition, otherPosition: DeckPosition) {
        refreshAfterSwapOtherCalled = true
    }

    override fun refreshAfterUsePower(highlightDeckPlayer1: Boolean, highlightDeckPlayer2: Boolean) {
        if (highlightDeckPlayer1 && !highlightDeckPlayer2)
            refreshAfterUsePowerTrueFalseCalled = true
        else if (highlightDeckPlayer2 && !highlightDeckPlayer1)
            refreshAfterUsePowerFalseTrueCalled = true
    }

    override fun refreshAfterKnock() {
        refreshAfterKnockCalled = true
    }

    override fun refreshAfterPeakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player) {
        refreshAfterPeakCardPlayerCalled = true
    }

    override fun refreshAfterChooseCard() {
        refreshAfterChooseCardCalled = true
    }

    override fun refreshAfterAddPlayers() {
        refreshAfterAddPlayerCalled = true
    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    override fun refreshAfterEndGame(winnerMessage: String) {
        refreshAfterEndGameCalled = true
    }
}