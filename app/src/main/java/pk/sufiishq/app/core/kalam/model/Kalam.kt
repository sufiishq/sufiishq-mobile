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

package pk.sufiishq.app.core.kalam.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kalam")
data class Kalam(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    val code: Int,
    @ColumnInfo(name = "recorded_date") val recordeDate: String,
    val location: String,
    @ColumnInfo(name = "online_src") val onlineSource: String,
    @ColumnInfo(name = "offline_src") var offlineSource: String = "",
    @ColumnInfo(name = "favorite") var isFavorite: Int = 0,
    @ColumnInfo(name = "playlist_id") var playlistId: Int = 0,
)
