package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.core.splitter.KalamSplitManager
import pk.sufiishq.app.core.splitter.SplitStatus
import pk.sufiishq.app.models.Kalam

abstract class KalamSplitManagerEvents : Event(KalamSplitManager::class) {

    class SetKalam(val kalam: Kalam) : KalamSplitManagerEvents()
    object StartSplitting : KalamSplitManagerEvents()
    object PlayPreview : KalamSplitManagerEvents()
    class SetSplitStart(val position: Int) : KalamSplitManagerEvents()
    class SetSplitEnd(val position: Int) : KalamSplitManagerEvents()
    class SetSplitStatus(val status: SplitStatus) : KalamSplitManagerEvents()
    class UpdateSeekbar(val value: Float) : KalamSplitManagerEvents()

}