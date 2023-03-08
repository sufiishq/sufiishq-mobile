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

package pk.sufiishq.app.feature.app.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import pk.sufiishq.app.feature.app.MediaType

@Entity(tableName = "media")
data class Media(

    @PrimaryKey val id: Long,
    val title: String? = null,
    val thumbnail: String? = null,
    val src: String,
    val type: MediaType,
    val referenceId: String,
) {

    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}
