package entity
import tools.aqua.bgw.util.Stack
import kotlin.test.*

/**
 * [KabooTest] is created for testing [Kaboo]
 */
class KabooTest {
    private val kaboo = Kaboo()

    /**
     * tests, if an object of [Kaboo] will be initialized correctly
     */
    @Test
    fun testInit(){
        assertEquals(null, kaboo.currentPlayer)
        assertEquals(mutableListOf(), kaboo.players)
        assertEquals(Stack(), kaboo.newStack)
        assertEquals(Stack(), kaboo.usedStack)
    }
}