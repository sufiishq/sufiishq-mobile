package pk.sufiishq.app.core.player.listener

import pk.sufiishq.app.core.player.state.MediaState

interface PlayerStateListener {
    fun onStateChange(mediaState: MediaState)
}