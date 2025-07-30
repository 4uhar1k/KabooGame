package entity
import tools.aqua.bgw.util.Stack

/**
 * Class Kaboo is an entity of the game
 * @property currentPlayer The player who is on the turn
 * @property players The list of players who are in the game
 * @property newStack The stack of new cards, players can draw a card from it
 * @property usedStack The stack of used cards, players can draw a card from it too
 */
class Kaboo {
    val currentPlayer: Player? = null
    val players : MutableList<Player> = mutableListOf()
    val newStack : Stack<Card> = Stack()
    val usedStack : Stack<Card> = Stack()
}
