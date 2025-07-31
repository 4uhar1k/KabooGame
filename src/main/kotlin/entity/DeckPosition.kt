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
    NOT_SELECTED
    ;

    /**
     * provide an integer to represent position
     */
    fun toInt() = when(this) {
        TOP_LEFT -> 0
        TOP_RIGHT -> 1
        BOTTOM_LEFT -> 2
        BOTTOM_RIGHT -> 3
        NOT_SELECTED -> -1
    }
}

