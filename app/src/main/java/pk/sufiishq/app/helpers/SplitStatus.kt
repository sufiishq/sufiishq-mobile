package pk.sufiishq.app.helpers

import pk.sufiishq.app.utils.SPLIT_CANCEL

sealed interface SplitStatus

object SplitInProgress : SplitStatus
data class SplitCompleted(val returnCode: Int = SPLIT_CANCEL) : SplitStatus
object SplitDone : SplitStatus