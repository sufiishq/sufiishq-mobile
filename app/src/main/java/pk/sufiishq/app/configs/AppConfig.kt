package pk.sufiishq.app.configs

import pk.sufiishq.app.utils.IS_SHUFFLE_ON
import pk.sufiishq.app.utils.getFromStorage

class AppConfig {

    fun isShuffle(): Boolean {
        return IS_SHUFFLE_ON.getFromStorage(false)
    }
}