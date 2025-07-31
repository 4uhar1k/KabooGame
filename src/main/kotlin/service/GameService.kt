package service
import entity.*
import tools.aqua.bgw.util.Stack
import java.util.Collections.addAll

class GameService (private val rootService: RootService){
    var player1 = Player()
    var player2 = Player()
    fun StartGame(){
        var kaboo = Kaboo()
        kaboo.currentPlayer = player1
        kaboo.players.add(player1)
        kaboo.players.add(player2)
        kaboo.newStack = randomizeCards()
        rootService.currentGame = kaboo
    }
    fun addPlayers(namePlayer1: String, namePlayer2: String){
        player1 = Player(namePlayer1)
        player2 = Player(namePlayer2)
    }

    private fun randomizeCards() : Stack<Card> {
        var randomListOfCards = List(52){ index ->
            Card(
                CardSuit.values()[index / 13],
                CardValue.values()[index % 13]
            )
        }.shuffled().toMutableList()
        player1.deck = randomListOfCards.subList(0,4)
        player1.deck = randomListOfCards.subList(4,8)
        randomListOfCards.removeAll(randomListOfCards.subList(0,8))
        return Stack<Card>().apply { addAll(randomListOfCards) }
    }
}