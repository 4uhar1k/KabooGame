package entity

data class Player(val name: String? = "Player", var knocked: Boolean? = false, var viewedCards: Boolean? = false) {
    val hand : Card? = null
    val ownSelected : Card? = null
    val otherSelected : Card? = null
    val deck : MutableList<Card> = mutableListOf()
}
