package service
import entity.*
import tools.aqua.bgw.util.Stack
import java.util.Collections.addAll
import kotlin.IllegalStateException

/**
 * [GameService] is an entity, which provides all logic, performed by system during the game
 * @param rootService The [RootService] instance to access the other service methods and entity layer
 */
class GameService (private val rootService: RootService): AbstractRefreshingService() {
    var player1 = Player()
    var player2 = Player()
    var otherPlayer = Player()
    var playerWantsToKnock = false // should be updated by interactions in gui
    public var kaboo = Kaboo()
    /**
     * [startGame] is a method, which is needed to start the game
     * It creates new game, adds both player, defines the current player, shuffles new stack of cards
     * and give start cards for players
     */
    fun startGame(){
        kaboo = Kaboo()
        kaboo.currentPlayer = player2
        kaboo.players.add(player1)
        kaboo.players.add(player2)
        kaboo.newStack = createDeck()
        rootService.currentGame = kaboo
        giveStartCards()
        onAllRefreshables { refreshAfterStartGame() }
        rootService.playerService.peakCardsFirstRound()
    }

    /**
     * [addPlayers] is a method, which accepts names of players and adds them to the game
     * @param namePlayer1 is a name of first player
     * @param namePlayer2 is a name of other player
     */
    fun addPlayers(namePlayer1: String, namePlayer2: String){
        player1 = Player(namePlayer1)
        player2 = Player(namePlayer2)
        onAllRefreshables { refreshAfterAddPlayers() }
        startGame()
    }
    /**
     * [createDeck] is a method, which shuffles all our cards into the new stack
     * we use [index/13] for CardSuit because we got four type of suits (52/13=4)
     * we use [index%13] for CardValue because we got 12 values of cards
     */
    private fun createDeck() : Stack<Card> {
        val randomListOfCards = List(52){ index ->
            Card(
                CardSuit.entries[index / 13],
                CardValue.entries[index % 13]
            )
        }.shuffled().toMutableList()
        val deck = Stack<Card>()
        for (card in randomListOfCards) {
            deck.push(card)  // Use push instead of add/addAll
        }
        return deck
    }

    /**
     * [giveStartCards] is a method, which gives players their start cards
     * @exception IllegalStateException is thrown, if the game is not started (kaboo == null)
     */
    fun giveStartCards(){
        val kaboo = rootService.currentGame
        if (kaboo == null) {
            throw IllegalStateException("Game not started yet")
        }

        if (kaboo.newStack.size < 8) {
            throw IllegalStateException("Not enough cards in the stack to deal starting cards: ${kaboo.newStack.size}")
        }
        for (i in 0..3){
            player1.deck.add(kaboo.newStack.pop())
        }
        for (i in 0..3){
            player2.deck.add(kaboo.newStack.pop())
        }
    }

    /**
     * [gameMove] is a method, which is responsible for game logic
     * if current player has already knocked, and it's his turn or the stack of new cards is empty,
     * then we call [endGame] method. If nobody has knocked yet, current player has to choose
     * between 3 interactions: to knock, to pick card from new or from used stack.
     * @exception IllegalStateException is thrown, if the game is not started (kaboo == null)
     */
    fun gameMove(){
        val kaboo = rootService.currentGame

        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }

        if (kaboo.currentPlayer == player1)
            otherPlayer = player2
        else
            otherPlayer = player1

        if (kaboo.currentPlayer?.knocked == true || kaboo.newStack.size == 0){
            endGame()
        }
        if (!otherPlayer.knocked && playerWantsToKnock){
            rootService.playerService.knock()
        }
        else
        {
            val used = false // should be updated by interactions in gui
            rootService.playerService.drawCard(used)
            onAllRefreshables { refreshAfterGameMove(!playerWantsToKnock, !used) }
        }

    }

    /**
     * [endTurn] is a method, which ends the current player's turn and starts the next one for another player
     * @exception IllegalStateException is thrown, if the game is not started (kaboo == null)
     */
    fun endTurn(){
        val kaboo = rootService.currentGame
        if (kaboo == null){
            throw IllegalStateException("Game not started yet")
        }
        if (kaboo.currentPlayer == player1)
            kaboo.currentPlayer = player2
        else
            kaboo.currentPlayer = player1
        if (kaboo.currentPlayer?.viewedCards == false){
            rootService.playerService.peakCardsFirstRound()
        }
        if (kaboo.currentPlayer?.knocked == true){
            endGame()
        }
        onAllRefreshables { refreshAfterEachTurn() }
    }

    /**
     * [endGame] is a method, which ends the game
     * It calculates points of each player's deck and returns winner's name
     * @return The method returns the name of a player, who got fewer points.
     * If players got equal points, "Draw" is returned.
     * @exception IllegalStateException is thrown, if the game is not started (kaboo == null)
     */
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
        val winnerMessage: String = if (sum1 < sum2)
            player1.name
        else if (sum2 < sum1)
            player2.name
        else
            "Draw"
        onAllRefreshables { refreshAfterEndGame(winnerMessage) }
        rootService.currentGame = null
        return winnerMessage
    }
}