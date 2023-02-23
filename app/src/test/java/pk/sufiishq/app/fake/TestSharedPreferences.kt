/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.fake

import android.content.Context
import android.content.SharedPreferences
import pk.sufiishq.app.feature.storage.KeyValueStorage
import javax.inject.Inject

class TestSharedPreferences @Inject constructor(context: Context) : KeyValueStorage {
    private val sharedPreferences = context.getSharedPreferences("test_sufiishq_shared_prefs", Context.MODE_PRIVATE)

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
        }
            as T
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

    override fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}
