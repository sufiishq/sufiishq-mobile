package pk.sufiishq.app.ui.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.github.esentsov.PackagePrivate
import java.util.*
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.shortMonthName
import pk.sufiishq.app.utils.timeAs12HoursFormat
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun HighlightDateTimeView(
    label: String,
    selectedDate: State<Calendar?>,
    selectedTime: State<Calendar?>,
    minDate: State<Calendar?>,
    maxDate: State<Calendar?>,
    onDateChanged: (calendar: Calendar) -> Unit,
    onTimeChanged: (calender: Calendar) -> Unit
) {
    val context = LocalContext.current as FragmentActivity

    val year = selectedDate.value!!.get(Calendar.YEAR)
    val month = selectedDate.value!!.get(Calendar.MONTH)
    val day = selectedDate.value!!.get(Calendar.DAY_OF_MONTH)
    val hour = selectedTime.value!!.get(Calendar.HOUR_OF_DAY)
    val minute = selectedTime.value!!.get(Calendar.MINUTE)

    val datePickerDialog = DatePickerDialog.newInstance(
        { _: DatePickerDialog, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, mYear)
                set(Calendar.MONTH, mMonth)
                set(Calendar.DAY_OF_MONTH, mDayOfMonth)
            }
            onDateChanged(calendar)
        }, year, month, day
    )
    datePickerDialog.accentColor = AuroraColor.SecondaryVariant.color().toArgb()
    datePickerDialog.minDate = minDate.value
    datePickerDialog.maxDate = maxDate.value

    val timePickerDialog = TimePickerDialog.newInstance(
        { _: TimePickerDialog, mHourOfDay: Int, mMinute: Int, _: Int ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, mHourOfDay)
                set(Calendar.MINUTE, mMinute)
            }
            onTimeChanged(calendar)
        }, hour, minute, false
    )
    timePickerDialog.accentColor = AuroraColor.SecondaryVariant.color().toArgb()

    SIHeightSpace(value = 8)
    SIRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SIText(
            text = label,
            textColor = it
        )
        SIRow {

            SIButton(
                text = optString(
                    R.string.dynamic_date,
                    day,
                    month.shortMonthName(),
                    year
                ),
                onClick = {
                    datePickerDialog.show(context.supportFragmentManager, null)
                },
            )

            SIWidthSpace(value = 6)

            SIButton(
                text = timeAs12HoursFormat(
                    hour,
                    minute,
                    optString(R.string.label_am),
                    optString(R.string.label_pm)
                ),
                onClick = {
                    timePickerDialog.show(context.supportFragmentManager, null)
                }
            )
        }
    }
}