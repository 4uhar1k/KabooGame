package entity

/**
 * Enum to distinguish between player's deck positions
 * Each player's got 4 card on his deck (2x2)
 */
enum class DeckPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    ;

    /**
     * provide an integer to represent position
     */
    fun toInt() = when(this) {
        TOP_LEFT -> 1
        TOP_RIGHT -> 2
        BOTTOM_LEFT -> 3
        BOTTOM_RIGHT -> 4
    }
}

