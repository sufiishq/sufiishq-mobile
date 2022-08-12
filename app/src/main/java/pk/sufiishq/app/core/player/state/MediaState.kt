package pk.sufiishq.app.core.player.state

import pk.sufiishq.app.models.Kalam

sealed class MediaState(val kalam: Kalam) {

    class Idle(kalam: Kalam) : MediaState(kalam)
    class Loading(kalam: Kalam) : MediaState(kalam)
    class Prepared(kalam: Kalam) : MediaState(kalam)
    class Playing(kalam: Kalam, val currentProgress: Int, val totalDuration: Int) : MediaState(kalam)
    class Pause(kalam: Kalam, val currentProgress: Int, val totalDuration: Int) : MediaState(kalam)
    class Resume(kalam: Kalam, val currentProgress: Int, val totalDuration: Int) : MediaState(kalam)
    class Complete(kalam: Kalam) : MediaState(kalam)
    class Stop(kalam: Kalam) : MediaState(kalam)
    class Error(kalam: Kalam, val what: Int, val extra: Int, val message: String): MediaState(kalam)
}