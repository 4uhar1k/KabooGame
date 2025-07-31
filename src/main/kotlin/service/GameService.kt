package service
import entity.*
import tools.aqua.bgw.util.Stack
import java.util.Collections.addAll
import kotlin.IllegalStateException

class GameService (private val rootService: RootService): AbstractRefreshingService() {

    var player1 = Player()
    var player2 = Player()
    fun startGame(){
        val kaboo = Kaboo()
        rootService.currentGame = kaboo
        kaboo.currentPlayer = player1
        kaboo.players.add(player1)
        kaboo.players.add(player2)
        kaboo.newStack = createDeck()
        giveStartCards()
    }
    fun addPlayers(namePlayer1: String, namePlayer2: String){
        player1 = Player(namePlayer1)
        player2 = Player(namePlayer2)
    }
    fun createDeck() : Stack<Card> {
        val randomListOfCards = List(52){ index ->
            Card(
                CardSuit.entries[index / 13],
                CardValue.entries[index % 13]
            )
        }.shuffled().toMutableList()
        return Stack<Card>().apply { addAll(randomListOfCards) }
    }
    fun giveStartCards(){
        val kaboo = rootService.currentGame
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        for (i in 0..3){
            player1.deck.add(kaboo.newStack.peek())
            kaboo.newStack.pop()
        }
        for (i in 0..3){
            player2.deck.add(kaboo.newStack.peek())
            kaboo.newStack.pop()
        }
    }
    fun gameMove(){
        val kaboo = rootService.currentGame
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        var otherPlayer = Player()
        if (kaboo.currentPlayer == player1)
            otherPlayer = player2
        else
            otherPlayer = player1

        if (kaboo.currentPlayer?.knocked == true){
            endGame()
        }
        if (otherPlayer.knocked == true){
            //knocked not allowed anymore
        }
        else
        {
            val knocked : Boolean = false // should be updated by interactions in gui
            if (knocked) {
                rootService.playerService.knock()

            }

        }

        val used: Boolean = false // should be updated by interactions in gui
        rootService.playerService.drawCard(used)
    }


    fun endTurn(){
        val kaboo = rootService.currentGame
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        if (kaboo.currentPlayer == player1)
            kaboo.currentPlayer = player2
        else
            kaboo.currentPlayer = player1
        onAllRefreshables { refreshAfterEachTurn() }
    }

    fun endGame(): String{
        val kaboo = rootService.currentGame
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        var sum1 = 0
        var sum2 = 0
        for (i in 0..3){
            sum1 += player1.deck[i].value.toInt()
            sum2 += player2.deck[i].value.toInt()
        }
        var winnerMessage: String
        if (sum1 > sum2)
            winnerMessage = player1.name
        else if (sum2 > sum1)
            winnerMessage = player2.name
        else
            winnerMessage = "Draw"
        onAllRefreshables { refreshAfterEndGame(winnerMessage) }
        return winnerMessage
    }
}