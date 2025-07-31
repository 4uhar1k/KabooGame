package entity
/**
 * @constructor Player is for players in the game
 * @param name Name of the player
 * @param knocked Shows us, if the player has already knocked on the table(if it's the last turn of game)
 * @param viewedCards Shows us, if the player has viewed his bottom cards
 * @property hand The hand card of the player
 * @property ownSelected The card the player has chosen for swapping from own deck
 * @property otherSelected The card the player has chosen for swapping from rival's deck
 * @property deck Player's card deck (4 cards, 2x2)
 */
data class Player(val name: String = "Player", var knocked: Boolean = false, var viewedCards: Boolean = false) {
    var hand : Card? = null
    var ownSelected : DeckPosition = DeckPosition.NOT_SELECTED
    var otherSelected : DeckPosition = DeckPosition.NOT_SELECTED
    var deck : MutableList<Card> = mutableListOf()
}
