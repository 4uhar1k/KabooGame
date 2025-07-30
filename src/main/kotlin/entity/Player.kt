package entity

data class Player(val name: String? = "Player", val knocked: Boolean? = false, val viewedCards: Boolean? = false) {
    val hand : Card? = null
    val ownSelected : Card? = null
    val otherSelected : Card? = null
    val deck : MutableList<Card> = mutableListOf()
}
