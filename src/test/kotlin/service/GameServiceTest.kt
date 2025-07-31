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
        rootService.gameService.startGame()
    }

    /**
     * Tests, if startGame() method works correctly
     */
    @Test
    fun testStartGame(){
        assertNotNull(rootService.currentGame)
        assertEquals(2, rootService.currentGame?.players?.size)
        assertEquals(Player(viewedCards = true), rootService.currentGame?.currentPlayer)
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

    @Test
    fun testGameMove(){

    }

    @Test
    fun testEndTurn(){

    }

    @Test
    fun testEndGame(){

    }
}