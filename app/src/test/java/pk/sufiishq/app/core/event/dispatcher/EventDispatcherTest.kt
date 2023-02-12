package pk.sufiishq.app.core.event.dispatcher

import org.junit.Ignore
import pk.sufiishq.app.SufiIshqTest

@Ignore("this file will be deleted later")
class EventDispatcherTest : SufiIshqTest() {
/*
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

        playerEventResolver.registerEventHandler()
        PlayerEvents.PlayPauseEvent.dispatch()

        assertTrue(isPlayPauseEventDispatched)
    }

    @Test(expected = NullPointerException::class)
    fun testDispatch_shouldThrow_NullPointerException() {
        KalamEvents.SearchKalam("", TrackListType.All()).dispatch()
    }*/
}