package service
import entity.*
import tools.aqua.bgw.util.Stack
import java.util.Collections.addAll
import kotlin.IllegalStateException

class GameService (private val rootService: RootService){
    val kaboo = Kaboo()
    var player1 = Player()
    var player2 = Player()
    fun startGame(){
        kaboo.currentPlayer = player1
        kaboo.players.add(player1)
        kaboo.players.add(player2)
        kaboo.newStack = createDeck()
        giveStartCards()
        rootService.currentGame = kaboo
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
        val drawFromNewStack = false // should be updated by actions in gui

        if (drawFromNewStack){
            kaboo.currentPlayer?.hand = kaboo.newStack.peek()
            kaboo.newStack.pop()
        }
        else
        {
            kaboo.currentPlayer?.hand = kaboo.usedStack.peek()
            kaboo.usedStack.pop()
        }

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
        //end game menu
        if (sum1 > sum2)
            return player1.name
        else if (sum2 > sum1)
            return player2.name
        else
            return "Draw"

    }
}