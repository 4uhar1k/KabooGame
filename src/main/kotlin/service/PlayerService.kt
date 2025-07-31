package service
import entity.*


class PlayerService(private val rootService: RootService): AbstractRefreshingService() {
    val kaboo = rootService.currentGame

    fun drawCard(used: Boolean){
        var usablePower: Boolean = false
        val listOfUsablePowers = mutableListOf<String>("QUEEN", "JACK", "TEN", "NINE", "EIGHT", "SEVEN")
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        if (used){
            kaboo.currentPlayer?.hand = kaboo.usedStack.peek()
            kaboo.usedStack.pop()
        }
        else
        {
            kaboo.currentPlayer?.hand = kaboo.newStack.peek()
            kaboo.newStack.pop()
        }
        if (listOfUsablePowers.find { it == kaboo.currentPlayer?.hand?.value.toString() } != null){
            usablePower = true
        }
        onAllRefreshables { refreshAfterDraw(!used, usablePower) }
    }
    fun discard(){
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        val hand = currentPlayer.hand

        if (hand == null){
            throw IllegalStateException("No hand card")
        }

        kaboo.usedStack.push(hand)
        currentPlayer.hand = null



    }
    fun swapSelf(position: DeckPosition){

    }
    fun swapOther(ownPosition: DeckPosition, otherPosition: DeckPosition){

    }
    fun usePower(){

    }
    fun knock(){
        onAllRefreshables { refreshAfterKnock() }
    }
    fun peakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player){

    }
    fun peakCardsFirstRound(){

    }
    fun chooseCard(chosenCardPosition: DeckPosition, cardOfPlayer: Player){

    }
}