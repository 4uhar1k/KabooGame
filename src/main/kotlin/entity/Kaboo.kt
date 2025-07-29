package entity

class Kaboo {
    val currentPlayer: Player? = null
    val players = mutableListOf<Player>()
    val drawNewStack = mutableListOf<Card>()
    val drawUsedStack = mutableListOf<Card>()
}
