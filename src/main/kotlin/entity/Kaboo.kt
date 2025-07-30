package entity

import tools.aqua.bgw.util.Stack

class Kaboo {
    val currentPlayer: Player? = null
    val players : MutableList<Player> = mutableListOf()
    val drawNewStack : Stack<Card> = Stack()
    val drawUsedStack : Stack<Card> = Stack()
}
