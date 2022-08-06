package pk.sufiishq.app.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kalam")
data class Kalam(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val title: String,
    val code: Int,

    @ColumnInfo(name = "recorded_date")
    val recordeDate: String,

    val location: String,

    @ColumnInfo(name = "online_src")
    val onlineSource: String,

    @ColumnInfo(name = "offline_src")
    var offlineSource: String = "",

    @ColumnInfo(name = "favorite")
    var isFavorite: Int = 0,

    @ColumnInfo(name = "playlist_id")
    var playlistId: Int = 0

)
