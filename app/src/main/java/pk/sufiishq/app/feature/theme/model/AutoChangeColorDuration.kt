package pk.sufiishq.app.feature.theme.model

import java.util.concurrent.TimeUnit
import pk.sufiishq.aurora.models.DataMenuItem
import pk.sufiishq.aurora.theme.AuroraColor

data class AutoChangeColorDuration(
    override val label: String,
    override val resId: Int? = null,
    override val iconTint: AuroraColor? = null,
    val id: Int,
    val repeatInterval: Long,
    val repeatIntervalTimeUnit: TimeUnit,
) : DataMenuItem {

    companion object {
        fun every1Hour() = AutoChangeColorDuration(
            id = 1,
            label = "Every 1 Hour",
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )

        fun every2Hour() = AutoChangeColorDuration(
            id = 2,
            label = "Every 2 Hour",
            repeatInterval = 2,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )

        fun every6Hour() = AutoChangeColorDuration(
            id = 3,
            label = "Every 6 Hour",
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )

        fun every12Hour() = AutoChangeColorDuration(
            id = 4,
            label = "Every 12 Hour",
            repeatInterval = 12,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )

        fun daily() = AutoChangeColorDuration(
            id = 5,
            label = "Daily",
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )

        fun weekly() = AutoChangeColorDuration(
            id = 6,
            label = "Weekly",
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )

        fun getList(): List<AutoChangeColorDuration> {
            return listOf(
                every1Hour(),
                every2Hour(),
                every6Hour(),
                every12Hour(),
                daily(),
                weekly(),
            )
        }
    }
}
