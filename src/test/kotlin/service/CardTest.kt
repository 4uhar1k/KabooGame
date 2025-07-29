package service
import entity.Card
import entity.CardSuit
import entity.CardValue
import kotlin.test.*

class CardTest {
    private val aceOfSpades = Card(CardSuit.SPADES, CardValue.ACE)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val kingOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val queenOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.JACK)

    // unicode characters for the suits, as those should be used by [WarCard.toString]
    private val heartsChar = '\u2665' // ♥
    private val diamondsChar = '\u2666' // ♦
    private val spadesChar = '\u2660' // ♠
    private val clubsChar = '\u2663' // ♣

    @Test //tests, how will the card's value be converted to string
    fun testToString() {
        assertEquals(spadesChar + "A", aceOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "K", kingOfHearts.toString())
        assertEquals(diamondsChar + "Q", queenOfDiamonds.toString())
    }

    @Test //tests the length of converted string a card's value
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
}