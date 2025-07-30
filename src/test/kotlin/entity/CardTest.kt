package entity

import kotlin.test.Test
import kotlin.test.assertEquals

class CardTest {
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val kingOfHearts = Card(CardSuit.HEARTS, CardValue.KING)
    private val queenOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.QUEEN)

    // unicode characters for the suits, as those should be used by [WarCard.toString]
    private val heartsChar = '\u2665' // ♥
    private val diamondsChar = '\u2666' // ♦
    private val spadesChar = '\u2660' // ♠
    private val clubsChar = '\u2663' // ♣

    /**
     * tests, how will the card's value be converted to string
     */
    @Test
    fun testToString() {
        assertEquals(spadesChar + "A", aceOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "K", kingOfHearts.toString())
        assertEquals(diamondsChar + "Q", queenOfDiamonds.toString())
    }

    /**
     * tests the length of converted string a card's value
     */
    @Test
    fun testToStringLength(){
        CardSuit.entries.forEach { suit ->
            CardValue.entries.forEach { value ->
                if (value == CardValue.TEN)
                    assertEquals(3, Card(suit, value).toString().length)
                else
                    assertEquals(2, Card(suit, value).toString().length)
            }
        }
    }

    /**
     * tests, how will the card's value be converted to int
     */
    @Test
    fun testToInt(){
        assertEquals(2, CardValue.TWO.toInt())
        assertEquals(3, CardValue.THREE.toInt())
        assertEquals(4, CardValue.FOUR.toInt())
        assertEquals(5, CardValue.FIVE.toInt())
        assertEquals(6, CardValue.SIX.toInt())
        assertEquals(7, CardValue.SEVEN.toInt())
        assertEquals(8, CardValue.EIGHT.toInt())
        assertEquals(9, CardValue.NINE.toInt())
        assertEquals(10, CardValue.TEN.toInt())
        assertEquals(10, CardValue.JACK.toInt())
        assertEquals(10, CardValue.QUEEN.toInt())
        assertEquals(-1, CardValue.KING.toInt())
        assertEquals(1, CardValue.ACE.toInt())
    }
}