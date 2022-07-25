package pk.sufiishq.app.helpers

import com.arthenica.mobileffmpeg.Config

sealed interface SplitStatus

object SplitInProgress : SplitStatus
data class SplitCompleted(val returnCode: Int = Config.RETURN_CODE_CANCEL) : SplitStatus
object SplitDone : SplitStatus