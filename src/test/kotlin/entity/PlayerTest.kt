package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * [PlayerTest] is created for testing [Player]
 */
class PlayerTest {
    private val player1 = Player("Vladimir")
    private val player2 = Player(knocked = false, viewedCards = false)

    /**
     * tests if the player's name will be default, if a player didn't write anything in his name's textbox
     */
    @Test
    fun testEmptyName(){
        assertEquals("Vladimir", player1.name)
        assertEquals("Player", player2.name)
    }

     /**
     * tests if the player will be initialized correctly
     */
    @Test
    fun testInitializing(){
        assertEquals("Vladimir", player1.name)
        assertEquals(false, player1.knocked)
        assertEquals(false, player1.viewedCards)
        assertEquals(null, player1.hand)
        assertEquals(DeckPosition.NOT_SELECTED, player1.ownSelected)
        assertEquals(DeckPosition.NOT_SELECTED, player1.otherSelected)
        assertEquals(mutableListOf(), player1.deck)
    }
}