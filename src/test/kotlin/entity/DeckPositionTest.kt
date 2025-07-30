package entity
import kotlin.test.*
class DeckPositionTest {

    /**
     * tests, if the deck position will get correct int value
     */
    @Test
    fun testToInt(){
        assertEquals(1, DeckPosition.TOP_LEFT.toInt())
        assertEquals(2, DeckPosition.TOP_RIGHT.toInt())
        assertEquals(3, DeckPosition.BOTTOM_LEFT.toInt())
        assertEquals(4, DeckPosition.BOTTOM_RIGHT.toInt())
    }
}