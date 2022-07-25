package pk.sufiishq.app.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class Playlist(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val title: String,
) {
    var totalKalam: Int = 0
}
