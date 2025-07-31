package entity
import kotlin.test.*

/**
 * [DeckPositionTest] is created for testing [DeckPosition]
 */
class DeckPositionTest {

    /**
     * tests, if the deck position will get correct int value
     */
    @Test
    fun testToInt(){
        assertEquals(0, DeckPosition.TOP_LEFT.toInt())
        assertEquals(1, DeckPosition.TOP_RIGHT.toInt())
        assertEquals(2, DeckPosition.BOTTOM_LEFT.toInt())
        assertEquals(3, DeckPosition.BOTTOM_RIGHT.toInt())
    }
}