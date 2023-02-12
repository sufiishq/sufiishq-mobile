package pk.sufiishq.app.core.storage

import com.google.gson.Gson
import javax.inject.Inject
import pk.sufiishq.app.di.qualifier.SecureSharedPreferences
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.KalamInfo
import pk.sufiishq.app.utils.LAST_PLAY_KALAM

class LastKalamPlayLiveData @Inject constructor(
    private val gson: Gson,
    @SecureSharedPreferences private val keyValueStorage: KeyValueStorage
) : SPLiveData<KalamInfo?>(keyValueStorage.getSharedPreferences(), LAST_PLAY_KALAM, null) {

    override fun getValueFromPreferences(key: String, defValue: KalamInfo?): KalamInfo? {

        return keyValueStorage.get(key, "")
            .takeIf { it.isNotEmpty() }
            ?.let {
                gson.fromJson(it, KalamInfo::class.java)
            }
    }
}