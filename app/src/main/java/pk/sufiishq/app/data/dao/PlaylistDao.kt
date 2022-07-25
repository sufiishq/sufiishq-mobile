package pk.sufiishq.app.data.dao

import androidx.room.*
import pk.sufiishq.app.models.Playlist

@Dao
interface PlaylistDao {

    @Insert
    fun add(playlist: Playlist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(playlist: Playlist)

    @Delete
    fun delete(playlist: Playlist)

    @Query("SELECT * FROM playlist WHERE id = :id")
    fun get(id: Int): Playlist

    @Query(
        "SELECT playlist.*, COUNT(kalam.playlist_id) AS totalKalam FROM playlist " +
                "LEFT JOIN kalam ON kalam.playlist_id = playlist.id " +
                "GROUP by playlist.id " +
                "ORDER BY playlist.id DESC"
    )
    fun getAll(): List<Playlist>

    @Query("SELECT COUNT(*) FROM playlist")
    fun countAll(): Int
}