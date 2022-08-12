package pk.sufiishq.app.configs

import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.utils.IS_SHUFFLE_ON
import pk.sufiishq.app.utils.getFromStorage
import javax.inject.Inject

class AppConfig @Inject constructor() {

    var trackListType: TrackListType = TrackListType.All()

    fun isShuffle(): Boolean {
        return IS_SHUFFLE_ON.getFromStorage(false)
    }
}