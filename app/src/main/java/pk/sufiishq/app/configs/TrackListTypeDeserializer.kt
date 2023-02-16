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

package pk.sufiishq.app.configs

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.helpers.createTrackListType
import java.lang.reflect.Type

class TrackListTypeDeserializer : JsonDeserializer<TrackListType> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): TrackListType {
        return json?.asJsonObject?.let {
            val type = it.get("type").asString
            val title = it.get("title").asString

            if (type == ScreenType.Tracks.PLAYLIST) {
                type.createTrackListType(title, it.get("playlistId").asInt)
            } else {
                type.createTrackListType(type)
            }
        }
            ?: TrackListType.All()
    }
}
