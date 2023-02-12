package pk.sufiishq.app.core.kalam.splitter

import pk.sufiishq.app.models.Kalam

data class SplitKalamInfo(
    val kalam: Kalam,
    val splitStart: Int = 0,
    val splitEnd: Int = 0,
    val kalamLength: Int = 0,
    val previewKalamLength: Int = 0,
    val previewKalamProgress: Int = 0,
    val previewPlayStart: Boolean = false,
    val splitStatus: SplitStatus = SplitStatus.Start
)
