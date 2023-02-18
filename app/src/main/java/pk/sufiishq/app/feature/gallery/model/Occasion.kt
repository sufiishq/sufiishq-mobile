package pk.sufiishq.app.feature.gallery.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pk.sufiishq.app.feature.gallery.OccasionType

@Entity(tableName = "occasion")
data class Occasion(

    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val description: String?,
    val cover: String,
    @ColumnInfo(name = "start_timestamp") val startTimestamp: Long,
    @ColumnInfo(name = "end_timestamp") val endTimestamp: Long,
    @ColumnInfo(name = "hijri_date") val hijriDate: String,
    val type: OccasionType,
    val address: String,
)