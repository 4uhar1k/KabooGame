package service
import entity.Player
import kotlin.test.*

class PlayerTest {
    private val player1 = Player("Vladimir", false, false)
    private val player2 = Player(knocked = false, viewedCards = false)

    @Test //tests if the player's name will be default, if a player didn't write anything in his name's textbox
    fun testEmptyName(){
        assertEquals("Vladimir", player1.name)
        assertEquals("Player", player2.name)
    }
}