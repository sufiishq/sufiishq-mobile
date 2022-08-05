package pk.sufiishq.app.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

@Suppress("IMPLICIT_CAST_TO_ANY")
class SecureSharedPreferencesStorage @Inject constructor(context: Context) : KeyValueStorage {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = EncryptedSharedPreferences.create(
            "sufiishq_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun <T> get(key: String, default: T): T {
        return when (default) {
            is String -> sharedPreferences.getString(key, default)
            is Int -> sharedPreferences.getInt(key, default)
            is Float -> sharedPreferences.getFloat(key, default)
            is Long -> sharedPreferences.getLong(key, default)
            is Boolean -> sharedPreferences.getBoolean(key, default)
            else -> default
        } as T
    }

    override fun <T> put(key: String, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Boolean -> editor.putBoolean(key, value)
        }
        editor.apply()
    }
}