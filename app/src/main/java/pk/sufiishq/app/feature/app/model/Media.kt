package pk.sufiishq.app.feature.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pk.sufiishq.app.feature.app.MediaKind
import pk.sufiishq.app.feature.app.MediaType

@Entity(tableName = "media")
data class Media(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String?,
    val thumbnail: String?,
    val src: String,
    val type: MediaType,
    val kind: MediaKind
)
