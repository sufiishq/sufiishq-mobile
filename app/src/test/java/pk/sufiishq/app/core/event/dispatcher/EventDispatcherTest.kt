package pk.sufiishq.app.core.event.dispatcher

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pk.sufiishq.app.SufiIshqTest
import pk.sufiishq.app.core.event.events.Event
import pk.sufiishq.app.core.event.events.KalamEvents
import pk.sufiishq.app.core.event.events.PlayerEvents
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.viewmodels.PlayerViewModel

class EventDispatcherTest : SufiIshqTest() {

    private lateinit var eventDispatcher: EventDispatcher

    @Before
    fun setUp() {
        eventDispatcher = EventDispatcher.getInstance()
    }

    @Test
    fun testDispatch_shouldDispatchEvent_toRespectiveResolver() {

        var isPlayPauseEventDispatched = false

        // register player event resolver
        val playerEventResolver = mockk<PlayerViewModel> {
            every { onEvent(any()) } answers {
                if (firstArg<Event>() is PlayerEvents.PlayPauseEvent) {
                    isPlayPauseEventDispatched = true
                }
            }
        }

        eventDispatcher.registerEventHandler(playerEventResolver)
        eventDispatcher.dispatch(PlayerEvents.PlayPauseEvent)

        assertTrue(isPlayPauseEventDispatched)
    }

    @Test(expected = NullPointerException::class)
    fun testDispatch_shouldThrow_NullPointerException() {
        eventDispatcher.dispatch(KalamEvents.SearchKalam("", TrackListType.All()))
    }
}