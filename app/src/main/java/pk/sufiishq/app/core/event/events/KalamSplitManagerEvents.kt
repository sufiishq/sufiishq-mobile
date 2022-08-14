package pk.sufiishq.app.core.event.events

import pk.sufiishq.app.helpers.KalamSplitManager
import pk.sufiishq.app.helpers.SplitStatus
import pk.sufiishq.app.models.Kalam

abstract class KalamSplitManagerEvents : Event(KalamSplitManager::class) {

    class SetKalam(val kalam: Kalam) : KalamSplitManagerEvents()
    object StartPreview : KalamSplitManagerEvents()
    object PlayPreview : KalamSplitManagerEvents()
    class SetSplitStart(val position: Int) : KalamSplitManagerEvents()
    class SetSplitEnd(val position: Int) : KalamSplitManagerEvents()
    class SetSplitStatus(val status: SplitStatus) : KalamSplitManagerEvents()
    class UpdateSeekbar(val value: Float) : KalamSplitManagerEvents()

}