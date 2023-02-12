package pk.sufiishq.app.core.storage

import android.content.SharedPreferences


interface KeyValueStorage {

    fun getSharedPreferences(): SharedPreferences
    fun contains(key: String): Boolean
    fun <T> get(key: String, default: T): T
    fun <T> put(key: String, value: T)
}