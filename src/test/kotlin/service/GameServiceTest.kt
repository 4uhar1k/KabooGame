package service
import kotlin.test.*
import entity.*
import tools.aqua.bgw.util.Stack

/**
 * This class is implemented for testing [GameService]
 */
class GameServiceTest {
    private lateinit var rootService: RootService

    /**
     * Little setup before tests: we create the game
     */
    @BeforeTest
    fun setUp(){
        rootService = RootService()
        rootService.gameService.addPlayers("Vladimir", "Player2")
    }

    /**
     * Tests, if startGame() method works correctly
     */
    @Test
    fun testStartGame(){
        assertNotNull(rootService.currentGame)
        assertEquals(2, rootService.currentGame?.players?.size)
        assertEquals(Player(name="Vladimir", viewedCards = true), rootService.currentGame?.currentPlayer)
        assertNotNull( rootService.currentGame?.newStack)
        assertNotNull(rootService.currentGame?.usedStack)
        assertEquals(44, rootService.currentGame?.newStack?.size)
        assertEquals(0, rootService.currentGame?.usedStack?.size)
        val players = rootService.currentGame!!.players
        assertEquals(4, players[0].deck.size)
        assertEquals(4, players[1].deck.size)
    }

    /**
     * Tests, if cards in players' decks are all different
     */
    @Test
    fun testCardsInDecksAreDifferent(){
        val deck1 = rootService.currentGame!!.players[0].deck
        val deck2 = rootService.currentGame!!.players[1].deck
        for (i in 1..3) {
            assertFalse{deck1[i].equalsTo(deck1[i-1])}
            assertFalse{deck2[i].equalsTo(deck2[i-1])}
        }
    }

    /**
     * Tests, if cards in game stack are all different too
     */
    @Test
    fun testCardsInStackAreDifferent(){
        val testStack = rootService.currentGame!!.newStack
        val testList : MutableList<Card> = mutableListOf()
        for (i in 0..43) {
            testList.add(testStack.pop())
        }
        for (i in 1..43){
            assertFalse{testList[i].equalsTo(testList[i-1])}
        }
    }

    /**
     * tests, if players are correctly added to the game
     */
    @Test
    fun testAddPlayers(){
        assertEquals("Vladimir", rootService.currentGame!!.players[0].name)
        assertEquals("Player2", rootService.currentGame!!.players[1].name)
    }
    @Test
    fun testGameMove(){

    }

    /**
     * tests, if currentPlayer will be changed correctly by the end of the turn
     */
    @Test
    fun testEndTurn(){
        val player1 = rootService.currentGame!!.players[0]
        val player2 = rootService.currentGame!!.players[1]
        rootService.gameService.endTurn()
        assertEquals(player2, rootService.currentGame!!.currentPlayer)
        rootService.gameService.endTurn()
        assertEquals(player1, rootService.currentGame!!.currentPlayer)
    }

    /**
     * Tests, if endGame() chooses the winner correctly and if it ends the game correctly
     */
    @Test
    fun testEndGame(){
        val card1 : Card = Card(CardSuit.DIAMONDS, CardValue.QUEEN)
        val card2 : Card = Card(CardSuit.DIAMONDS, CardValue.JACK)
        val card3 : Card = Card(CardSuit.DIAMONDS, CardValue.TEN)
        val card4 : Card = Card(CardSuit.DIAMONDS, CardValue.NINE)
        val card5 : Card = Card(CardSuit.DIAMONDS, CardValue.EIGHT)
        val card6 : Card = Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        val card7 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card8 : Card = Card(CardSuit.DIAMONDS, CardValue.KING)
        rootService.currentGame!!.players[0].deck = mutableListOf(card1, card2, card3, card4)
        rootService.currentGame!!.players[1].deck = mutableListOf(card5, card6, card7, card8)
        assertEquals(rootService.currentGame!!.players[1].name, rootService.gameService.endGame())

        assertFails{
            rootService.currentGame!!.players[1].deck = mutableListOf(card1, card2, card3, card4)
            rootService.currentGame!!.players[0].deck = mutableListOf(card5, card6, card7, card8)
            assertEquals(rootService.currentGame!!.players[0].name, rootService.gameService.endGame())
        }

        rootService.gameService.addPlayers("Vladimir", "Player2")
        rootService.currentGame!!.players[1].deck = mutableListOf(card1, card2, card3, card4)
        rootService.currentGame!!.players[0].deck = mutableListOf(card5, card6, card7, card8)
        assertEquals(rootService.currentGame!!.players[0].name, rootService.gameService.endGame())

        rootService.gameService.addPlayers("Vladimir", "Player2")
        val card11 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card12 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card13 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card14 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card15 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card16 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card17 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        val card18 : Card = Card(CardSuit.DIAMONDS, CardValue.ACE)
        rootService.currentGame!!.players[0].deck = mutableListOf(card11, card12, card13, card14)
        rootService.currentGame!!.players[1].deck = mutableListOf(card15, card16, card17, card18)
        assertEquals("Draw", rootService.gameService.endGame())
    }
}