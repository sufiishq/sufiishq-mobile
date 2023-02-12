package pk.sufiishq.app.configs

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.helpers.createTrackListType

class TrackListTypeDeserializer : JsonDeserializer<TrackListType> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TrackListType {
        return json
            ?.asJsonObject
            ?.let {

                val type = it.get("type").asString
                val title = it.get("title").asString

                if (type == ScreenType.Tracks.PLAYLIST) {
                    type.createTrackListType(title, it.get("playlistId").asInt)
                } else {
                    type.createTrackListType(type)
                }
            } ?: TrackListType.All()
    }
}