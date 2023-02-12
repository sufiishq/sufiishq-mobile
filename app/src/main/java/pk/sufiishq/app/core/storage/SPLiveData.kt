package pk.sufiishq.app.core.storage

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

abstract class SPLiveData<T>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defValue: T
) :
    LiveData<T>() {

    private val preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (this@SPLiveData.key == key) {
                postValue(getValueFromPreferences(key, defValue))
            }
        }

    abstract fun getValueFromPreferences(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        postValue(getValueFromPreferences(key, defValue))
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}