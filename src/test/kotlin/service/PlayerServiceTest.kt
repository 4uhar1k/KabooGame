package service
import entity.*
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.*
/**
 * This class is implemented for testing [PlayerService]
 */
class PlayerServiceTest {
    private lateinit var rootService: RootService
    private lateinit var refreshableTest: RefreshableTest
    /**
     * Little setup before tests: we create the game
     */
    @BeforeTest
    fun setUp(){
        rootService = RootService()
        refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.addPlayers("Vladimir", "Player2")
    }

    /**
     * tests all the methods, if game has not started yet
     */
    @Test
    fun testIfNull(){
        rootService.currentGame = null
        assertFails{
            rootService.playerService.drawCard(false)
        }
        assertFails{
            rootService.playerService.discard()
        }
        assertFails{
            rootService.playerService.swapSelf(DeckPosition.NOT_SELECTED)
        }
        assertFails{
            rootService.playerService.swapOther(DeckPosition.NOT_SELECTED, DeckPosition.NOT_SELECTED)
        }
        assertFails{
            rootService.playerService.usePower()
        }
        assertFails{
            rootService.playerService.knock()
        }
        assertFails{
            rootService.playerService.peakCardsFirstRound()
        }
        assertFails{
            rootService.playerService.chooseCard(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!)
        }
    }
    /**
     * tests all the methods, if there is no current player
     */
    @Test
    fun testIfNoCurrentPlayer(){
        val kaboo = rootService.currentGame
        kaboo!!.currentPlayer = null
        rootService.currentGame = kaboo
        assertFails{
            rootService.playerService.drawCard(false)
        }
        assertFails{
            rootService.playerService.discard()
        }
        assertFails{
            rootService.playerService.swapSelf(DeckPosition.NOT_SELECTED)
        }
        assertFails{
            rootService.playerService.swapOther(DeckPosition.NOT_SELECTED, DeckPosition.NOT_SELECTED)
        }
        assertFails{
            rootService.playerService.usePower()
        }
        assertFails{
            rootService.playerService.knock()
        }
        assertFails{
            rootService.playerService.peakCardsFirstRound()
        }
        assertFails{
            rootService.playerService.chooseCard(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!)
        }
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
        assertFails{rootService.playerService.drawCard(true)}
        rootService.playerService.discard()
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
        assertTrue(refreshableTest.refreshAfterDiscardCalled)
        refreshableTest.reset()
        assertEquals(1, rootService.currentGame!!.usedStack.size)
        assertEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
        rootService.playerService.drawCard(false)
        rootService.currentGame!!.newStack.clear()
        rootService.playerService.discard()
        assertTrue(refreshableTest.refreshAfterEndGameCalled)
        refreshableTest.reset()
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
        assertTrue(refreshableTest.refreshAfterSwapSelfCalled)
        refreshableTest.reset()
        assertEquals(handCard, rootService.currentGame!!.currentPlayer!!.deck[DeckPosition.TOP_LEFT.toInt()])
        assertEquals(cardToChange, rootService.currentGame!!.usedStack.peek())
        assertEquals(null, rootService.currentGame!!.currentPlayer!!.hand)
    }

    /**
     * tests, if swapping cards between players works correctly
     */
    @Test
    fun testSwapOther(){
        assertFails{
            rootService.playerService.swapOther(DeckPosition.TOP_LEFT, DeckPosition.TOP_LEFT)
        }
        rootService.playerService.drawCard(false)
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]
        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.TEN)
        assertDoesNotThrow{rootService.playerService.swapOther(DeckPosition.TOP_LEFT, DeckPosition.TOP_RIGHT)}
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]
        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.TEN)
        val prevOwnCard = rootService.currentGame!!.players[0].deck[DeckPosition.TOP_LEFT.toInt()]
        val prevOtherCard = rootService.currentGame!!.players[1].deck[DeckPosition.TOP_RIGHT.toInt()]
        rootService.playerService.swapOther(DeckPosition.TOP_LEFT, DeckPosition.TOP_RIGHT)
        assertTrue(refreshableTest.refreshAfterSwapOtherCalled)
        refreshableTest.reset()
        assertEquals(prevOwnCard, rootService.currentGame!!.players[1].deck[DeckPosition.TOP_RIGHT.toInt()])
        assertEquals(prevOtherCard, rootService.currentGame!!.players[0].deck[DeckPosition.TOP_LEFT.toInt()])
    }

    /**
     * tests, if the game correctly chooses power type for each power card
     * and correctly implements it
     */
    @Test
    fun testUsePower(){
        assertFails { rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.TWO)
            rootService.playerService.usePower() }
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]
        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        rootService.playerService.usePower()
        assertTrue(refreshableTest.refreshAfterUsePowerTrueFalseCalled)
        refreshableTest.reset()

        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!.ownSelected)
        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!.otherSelected)
        //rootService.gameService.endGame()
        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.NINE)
        rootService.playerService.usePower()
        assertTrue(refreshableTest.refreshAfterUsePowerFalseTrueCalled)
        refreshableTest.reset()

        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.JACK)
        rootService.playerService.usePower()
        assertTrue(refreshableTest.refreshAfterUsePowerTrueFalseCalled)
        assertTrue(refreshableTest.refreshAfterUsePowerFalseTrueCalled)
        refreshableTest.reset()

        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.QUEEN)
        rootService.playerService.usePower()
        assertTrue(refreshableTest.refreshAfterUsePowerTrueFalseCalled)
        assertTrue(refreshableTest.refreshAfterUsePowerFalseTrueCalled)
        refreshableTest.reset()


    }

    /**
     * tests, if a player can knock, while the other one already has,
     * and if knock() works correctly
     */
    @Test
    fun testKnock(){
        var otherPlayer = Player()
        if (rootService.currentGame!!.currentPlayer!! == rootService.currentGame!!.players[0]){
            otherPlayer = rootService.currentGame!!.players[1]
        }
        else {
            otherPlayer = rootService.currentGame!!.players[0]
        }
        otherPlayer.knocked = true
        assertFails { rootService.playerService.knock() }
        otherPlayer.knocked = false
        rootService.playerService.knock()
        assertTrue(refreshableTest.refreshAfterKnockCalled)
        refreshableTest.reset()
        if (rootService.currentGame!!.currentPlayer!! == rootService.currentGame!!.players[0]){
            otherPlayer = rootService.currentGame!!.players[1]
        }
        else {
            otherPlayer = rootService.currentGame!!.players[0]
        }
        assertEquals(true, rootService.currentGame!!.currentPlayer!!.knocked)
        assertEquals(false, otherPlayer.knocked)



    }

    /**
     * tests, if refreshAfterPeakCardPlayer() is called in the method
     */
    @Test
    fun testPeakCardPlayer(){
        rootService.playerService.peakCardPlayer(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0])
        assertTrue(refreshableTest.refreshAfterPeakCardPlayerCalled)
        refreshableTest.reset()
    }

    /**
     * tests, if a player can view bottom cards multiple times,
     * and if the method itself works correctly
     */
    @Test
    fun testPeakCardsFirstRound(){
        rootService.currentGame!!.currentPlayer!!.viewedCards = true
        assertFails{ rootService.playerService.peakCardsFirstRound()}
        rootService.currentGame!!.currentPlayer!!.viewedCards = false
        rootService.playerService.peakCardsFirstRound()
        assertEquals(true, rootService.currentGame!!.currentPlayer!!.viewedCards)

    }

    /**
     * tests, if the game works correctly by choosing the cards
     */
    @Test
    fun testChooseCard(){
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]

        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!.ownSelected )
        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.currentPlayer!!.otherSelected)

        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, rootService.currentGame!!.currentPlayer!!)
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].ownSelected )
        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.players[0].otherSelected)
        rootService.gameService.endGame()

        rootService.gameService.addPlayers("Vladimir", "Player2")
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]
        var otherPlayer: Player = rootService.currentGame!!.players[1]
        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, otherPlayer)
        assertEquals(DeckPosition.NOT_SELECTED, rootService.currentGame!!.players[0].ownSelected )
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].otherSelected)
        rootService.gameService.endGame()

        rootService.gameService.addPlayers("Vladimir", "Player2")
        rootService.currentGame!!.currentPlayer = rootService.currentGame!!.players[0]
        otherPlayer = rootService.currentGame!!.players[1]
        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.QUEEN)
        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, rootService.currentGame!!.currentPlayer!!)
        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, otherPlayer)
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].ownSelected )
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].otherSelected)
        assertTrue(refreshableTest.refreshAfterChooseCardCalled)
        refreshableTest.reset()

        rootService.currentGame!!.currentPlayer!!.hand = Card(CardSuit.DIAMONDS, CardValue.JACK)
        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, rootService.currentGame!!.currentPlayer!!)
        rootService.playerService.chooseCard(DeckPosition.TOP_LEFT, otherPlayer)
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].ownSelected )
        assertEquals(DeckPosition.TOP_LEFT, rootService.currentGame!!.players[0].otherSelected)
        assertTrue(refreshableTest.refreshAfterChooseCardCalled)
        refreshableTest.reset()

    }
}