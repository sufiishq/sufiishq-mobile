/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
