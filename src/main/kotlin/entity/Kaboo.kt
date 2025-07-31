package entity
import tools.aqua.bgw.util.Stack

/**
 * Class Kaboo is an entity of the game
 * @property currentPlayer The player who is on the turn
 * @property players The list of players who are in the game
 * @property newStack The stack of new cards, players can draw a card from it
 * @property usedStack The stack of used cards, players can draw a card from it too
 */
class Kaboo () {
    var currentPlayer: Player? = null
    var players : MutableList<Player> = mutableListOf()
    var newStack : Stack<Card> = Stack()
    var usedStack : Stack<Card> = Stack()
}
