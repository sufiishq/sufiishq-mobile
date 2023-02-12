package pk.sufiishq.app.core.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.data.repository.AdminSettingsRepository
import pk.sufiishq.app.di.qualifier.IoDispatcher
import pk.sufiishq.app.models.Highlight
import pk.sufiishq.app.utils.getString
import kotlin.coroutines.CoroutineContext

class HighlightManager @Inject constructor(
    private val adminSettingsRepository: AdminSettingsRepository,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    private val highlightAvailable = MutableLiveData<Highlight?>(null)

    private val highlightStatus = MutableLiveData<HighlightStatus?>(null)
    private val startDate = MutableLiveData(Calendar.getInstance())
    private val endDate = MutableLiveData(Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 1)
    })
    private val minEndDate = MutableLiveData(Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 1)
    })
    private val startTime = MutableLiveData(Calendar.getInstance())
    private val endTime = MutableLiveData(Calendar.getInstance())
    private val title = MutableLiveData<String?>(null)
    private val detail = MutableLiveData<String?>(null)
    private val contacts =
        MutableLiveData<MutableList<Pair<String, String>?>>(mutableListOf(null, null, null))

    init {
        checkHighlightAvailable()
    }

    private fun checkHighlightAvailable() {

        callAsync {
            fetch()
                .takeIf { it is FirebaseDatabaseStatus.ReadHighlight }
                ?.let {
                    it as FirebaseDatabaseStatus.ReadHighlight
                    it.highlight }
                ?.let(highlightAvailable::postValue)

        }
    }

    fun getHighlightAvailable(): LiveData<Highlight?> {
        return highlightAvailable
    }

    fun highlightStatus(): LiveData<HighlightStatus?> {
        return highlightStatus
    }

    fun startDate(): LiveData<Calendar> {
        return startDate
    }

    fun endDate(): LiveData<Calendar> {
        return endDate
    }

    fun minEndDate(): LiveData<Calendar> {
        return minEndDate
    }

    fun startDateChanged(calendar: Calendar) {
        startDate.value = calendar
        minEndDate.value = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, startDate.value!!.get(Calendar.DAY_OF_MONTH))
            set(Calendar.MONTH, startDate.value!!.get(Calendar.MONTH))
            set(Calendar.YEAR, startDate.value!!.get(Calendar.YEAR))
        }.apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        if (endDate.value!!.timeInMillis <= startDate.value!!.timeInMillis) {
            endDate.value = minEndDate.value
        }
    }

    fun endDateChanged(calendar: Calendar) {
        endDate.value = calendar
    }

    fun startTimeChanged(calendar: Calendar) {
        startTime.value = calendar
    }

    fun endTimeChanged(calendar: Calendar) {
        endTime.value = calendar
    }

    fun startTime(): LiveData<Calendar> {
        return startTime
    }

    fun endTime(): LiveData<Calendar> {
        return endTime
    }

    fun getTitle(): LiveData<String?> {
        return title
    }

    fun getDetail(): LiveData<String?> {
        return detail
    }

    fun getContacts(): LiveData<MutableList<Pair<String, String>?>> {
        return contacts
    }

    fun setTitle(newTitle: String) {
        title.value = newTitle
    }

    fun setDetail(newDetail: String) {
        detail.value = newDetail
    }

    fun load(highlight: Highlight) {

        val start = Calendar.getInstance().apply {
            timeInMillis = highlight.startDateTime
        }
        val end = Calendar.getInstance().apply {
            timeInMillis = highlight.endDateTime
        }

        // check if highlight is ongoing or expired
        highlightStatus.postValue(
            if (end.timeInMillis < Calendar.getInstance().timeInMillis) HighlightStatus.Expired()
            else HighlightStatus.OnGoing()
        )

        startDate.value = start
        startTime.value = start
        endDate.value = end
        endTime.value = end
        setTitle(highlight.title)
        setDetail(highlight.detail)

        val defaultContacts = mutableListOf<Pair<String, String>?>(null, null, null)
        highlight.contacts
            ?.map { it.value.toList().map { data -> data.second } }
            ?.flatten()
            ?.let {
                val size = it.size
                val cut = (size + 1) / 2
                val first = it.subList(0, cut)
                val second = it.subList(cut, size)
                first.zip(second)
            }?.forEachIndexed { index, pair ->
                defaultContacts[index] = pair
            }
        contacts.value = defaultContacts
    }

    suspend fun fetch(): FirebaseDatabaseStatus {
        return adminSettingsRepository.readHighlight()
    }

    suspend fun addOrUpdate(): FirebaseDatabaseStatus {

        val highlight = Highlight(
            startDateTime = buildDateTime(startDate.value!!, startTime.value!!),
            endDateTime = buildDateTime(endDate.value!!, endTime.value!!),
            title = title.value!!,
            detail = detail.value!!,
            contacts = resolveContacts()
        )

        // highlight is expired need to fixed the end time
        if (highlight.endDateTime < Calendar.getInstance().timeInMillis) {
            return FirebaseDatabaseStatus.Failed(RuntimeException(getString(R.string.msg_highlight_invalid_end_time)))
        }

        return adminSettingsRepository.addOrUpdateHighlight(highlight)
    }

    suspend fun delete(): FirebaseDatabaseStatus {
        return adminSettingsRepository.deleteHighlight()
    }

    fun clear() {
        highlightStatus.value = null
        startDate.value = Calendar.getInstance()
        endDate.value = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        minEndDate.value = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        startTime.value = Calendar.getInstance()
        endTime.value = Calendar.getInstance()
        title.value = null
        detail.value = null
        contacts.value = mutableListOf(null, null, null)
    }

    private fun resolveContacts(): Map<String, Map<String, String>>? {
        return contacts.value
            ?.filterNotNull()
            ?.filter { it.first.trim().isNotEmpty() && it.second.trim().isNotEmpty() }
            ?.unzip()
            ?.let {
                Pair(
                    it.first.mapIndexed { index, name -> index.toString() to name.trim() }.toMap(),
                    it.second.mapIndexed { index, name -> index.toString() to name.trim() }.toMap(),
                )
            }?.let {
                mapOf(
                    Highlight.NAME to it.first,
                    Highlight.NUMBER to it.second
                )
            }
    }

    private fun buildDateTime(dateCal: Calendar, timeCal: Calendar): Long {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, dateCal.get(Calendar.YEAR))
            set(Calendar.MONTH, dateCal.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
        }.timeInMillis
    }

    private fun callAsync(block: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(dispatcher).launch {
            block()
        }
    }
}