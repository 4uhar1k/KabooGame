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
        if (kaboo == null) {
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        val hand = currentPlayer.hand
        if (hand == null)
        {
            throw IllegalStateException("There is no card on player's hand")
        }
        kaboo.usedStack.push(currentPlayer.deck[position.toInt()])
        currentPlayer.deck[position.toInt()] = hand
        currentPlayer.hand = null
        onAllRefreshables { refreshAfterSwapSelf(position) }
    }
    fun swapOther(ownPosition: DeckPosition, otherPosition: DeckPosition){
        if (kaboo == null) {
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        val hand = currentPlayer.hand
        if (hand == null)
        {
            throw IllegalStateException("There is no card on player's hand")
        }
        var otherPlayer = Player()
        if (kaboo.currentPlayer == kaboo.players[0])
            otherPlayer = kaboo.players[1]
        else
            otherPlayer = kaboo.players[0]
        kaboo.usedStack.push(hand)
        currentPlayer.hand = null
        val cardToChange : Card = currentPlayer.deck[ownPosition.toInt()]
        currentPlayer.deck[ownPosition.toInt()] = otherPlayer.deck[otherPosition.toInt()]
        otherPlayer.deck[otherPosition.toInt()] = cardToChange
        onAllRefreshables { refreshAfterSwapOther() }

    }
    fun usePower(){
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        val hand = currentPlayer.hand

        if (hand == null)
        {
            throw IllegalStateException("There is no card on player's hand")
        }
        var otherPlayer = Player()
        if (kaboo.currentPlayer == kaboo.players[0])
            otherPlayer = kaboo.players[1]
        else
            otherPlayer = kaboo.players[0]
        if (hand.value == CardValue.SEVEN || hand.value == CardValue.EIGHT){
            onAllRefreshables { refreshAfterUsePower(true,false) }
            var selectedPosition : DeckPosition = DeckPosition.TOP_LEFT // to identify with gui
            peakCardPlayer(selectedPosition, currentPlayer)
            onAllRefreshables { refreshAfterPeakCardPlayer(selectedPosition, currentPlayer) }
        }
        else if (hand.value == CardValue.NINE || hand.value == CardValue.TEN){
            onAllRefreshables { refreshAfterUsePower(false,true) }
            var selectedPosition : DeckPosition = DeckPosition.TOP_LEFT // to identify with gui
            peakCardPlayer(selectedPosition, otherPlayer)
            onAllRefreshables { refreshAfterPeakCardPlayer(selectedPosition, otherPlayer) }
        }
        else if (hand.value == CardValue.JACK){
            var ownSelectedPosition : DeckPosition = DeckPosition.TOP_LEFT // to identify with gui
            var otherSelectedPosition : DeckPosition = DeckPosition.TOP_LEFT // to identify with gui
            swapOther(ownSelectedPosition, otherSelectedPosition)
            onAllRefreshables { refreshAfterSwapOther() }
        }
    }
    fun knock(){
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        currentPlayer.knocked = true
        onAllRefreshables { refreshAfterKnock() }
    }
    fun peakCardPlayer(positionToPeak: DeckPosition, playerToPeak: Player){
        onAllRefreshables { refreshAfterPeakCardPlayer(positionToPeak, playerToPeak) }
    }
    fun peakCardsFirstRound(){
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }
        if (!currentPlayer.viewedCards){
            currentPlayer.viewedCards = true
        }
        else {
            throw IllegalStateException("Player has already viewed cards")
        }
    }
    fun chooseCard(chosenCardPosition: DeckPosition, cardOfPlayer: Player){
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        val currentPlayer = kaboo.currentPlayer
        if (currentPlayer == null)
        {
            throw IllegalStateException("There is no current player in this game")
        }

        if (cardOfPlayer == currentPlayer){
            currentPlayer.ownSelected = chosenCardPosition
        }
        else {
            currentPlayer.otherSelected = chosenCardPosition
        }
        if (currentPlayer.hand?.value == CardValue.JACK){
            swapOther(currentPlayer.ownSelected, currentPlayer.otherSelected)
        }
        else if (currentPlayer.hand?.value == CardValue.QUEEN){
            onAllRefreshables { refreshAfterChooseCard() }
        }

    }
}