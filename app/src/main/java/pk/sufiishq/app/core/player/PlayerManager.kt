package pk.sufiishq.app.core.player

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import pk.sufiishq.app.helpers.TrackListType
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.LAST_PLAY_KALAM
import pk.sufiishq.app.utils.canPlay
import pk.sufiishq.app.utils.putInStorage

class PlayerManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val gson: Gson
) {

    fun changeTrack(kalam: Kalam, trackListType: TrackListType) {
        if (kalam.canPlay(appContext)) {
            putKalamInStorage(kalam, trackListType)
        }
    }

    private fun putKalamInStorage(kalam: Kalam, trackListType: TrackListType) {
        LAST_PLAY_KALAM.putInStorage(
            gson.toJson(
                KalamInfo(
                    playerState = PlayerState.IDLE,
                    kalam = kalam,
                    currentProgress = 0,
                    totalDuration = 0,
                    false,
                    trackListType,
                )
            )
        )
    }
}