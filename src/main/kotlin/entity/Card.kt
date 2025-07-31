package entity

/**
 * Card is an entity for cards
 * Each card has two properties:
 * @param suit - Suit (Diamonds, Hearts, Clubs, Spades)
 * @param value - Value (2-10, Jack, Queen, King, Ace)
 *
 */
data class Card(val suit: CardSuit, val value: CardValue) {
    /**
     * The [toString] method represents a card in string format as a combination of its suit and value
     */
    override fun toString() = "$suit$value"

    fun equalsTo(other: Card): Boolean{
        return suit == other.suit && value == other.value
    }
}
