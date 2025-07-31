package service
import entity.*
import kotlin.test.*
/**
 * This class is implemented for testing [PlayerService]
 */
class PlayerServiceTest {
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
     * tests, if drawing cards from both new and used stacks works correctly
     */
    @Test
    fun testDrawCard(){
        rootService.playerService.drawCard(false)
        assertEquals(43, rootService.currentGame!!.newStack.size)
        assertNotEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
        assertFails{
            rootService.playerService.drawCard(true)
        }
        val kaboo = rootService.currentGame
        kaboo!!.usedStack.push(Card(CardSuit.DIAMONDS, CardValue.TEN))
        rootService.playerService.drawCard(true)
        assertEquals(0, rootService.currentGame!!.usedStack.size)
        assertNotEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
    }

    /**
     * tests, if discard works correctly
     */
    @Test
    fun testDiscard(){
        assertFails{
            rootService.playerService.discard()
        }
        rootService.playerService.drawCard(false)
        rootService.playerService.discard()
        assertEquals(1, rootService.currentGame!!.usedStack.size)
        assertEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
    }

    /**
     * tests, if by swapping own card, it goes to used stack, hand card goes to player's deck
     * and hand becomes null
     */
    @Test
    fun testSwapSelf(){
        assertFails{
            rootService.playerService.swapSelf(DeckPosition.TOP_LEFT)
        }
        rootService.playerService.drawCard(false)
        val handCard = rootService.currentGame!!.currentPlayer!!.hand
        val cardToChange = rootService.currentGame!!.currentPlayer!!.deck[DeckPosition.TOP_LEFT.toInt()]
        rootService.playerService.swapSelf(DeckPosition.TOP_LEFT)
        assertEquals(handCard, rootService.currentGame!!.currentPlayer!!.deck[DeckPosition.TOP_LEFT.toInt()])
        assertEquals(cardToChange, rootService.currentGame!!.usedStack.peek())
        assertEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
    }

    /**
     * tests, if swapping cards between players works correctly
     */
    @Test
    fun testSwapOther(){
        rootService.playerService.drawCard(false)
        val prevOwnCard = rootService.currentGame!!.players[0].deck[DeckPosition.TOP_LEFT.toInt()]
        val prevOtherCard = rootService.currentGame!!.players[1].deck[DeckPosition.TOP_RIGHT.toInt()]
        rootService.playerService.swapOther(DeckPosition.TOP_LEFT, DeckPosition.TOP_RIGHT)
        assertEquals(prevOwnCard, rootService.currentGame!!.players[1].deck[DeckPosition.TOP_RIGHT.toInt()])
        assertEquals(prevOtherCard, rootService.currentGame!!.players[0].deck[DeckPosition.TOP_LEFT.toInt()])
    }

}