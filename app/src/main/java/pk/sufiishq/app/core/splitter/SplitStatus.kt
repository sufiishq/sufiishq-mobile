package pk.sufiishq.app.core.splitter

sealed interface SplitStatus {
    object Start : SplitStatus
    object InProgress : SplitStatus
    object Done : SplitStatus
    object Completed : SplitStatus
    data class Error(val error: String) : SplitStatus
}

