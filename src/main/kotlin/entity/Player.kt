package entity

data class Player(val name: String? = "Player", val knocked: Boolean, val viewedCards: Boolean) {
    val hand : Card? = null
    val ownSelected : Card? = null
    val otherSelected : Card? = null
    val deck = mutableListOf<Card>()
}
