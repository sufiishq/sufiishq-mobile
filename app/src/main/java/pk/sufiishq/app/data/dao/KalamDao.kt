package pk.sufiishq.app.data.dao

import androidx.room.*
import pk.sufiishq.app.models.Kalam

@Dao
interface KalamDao {

    @Query("SELECT * FROM kalam ORDER BY id LIMIT 1 offset 0")
    fun getFirstKalam(): Kalam

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword)" +
                "ORDER BY id DESC " +
                "LIMIT 10 OFFSET :offset"
    )
    fun getAllKalam(searchKeyword: String, offset: Int): List<Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND offline_src != ''" +
                "ORDER BY id DESC " +
                "LIMIT 10 OFFSET :offset"
    )
    fun getDownloadsKalam(searchKeyword: String, offset: Int): List<Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND is_favorite = 1 " +
                "ORDER BY id DESC " +
                "LIMIT 10 OFFSET :offset"
    )
    fun getFavoritesKalam(searchKeyword: String, offset: Int): List<Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND playlist_id = :playlistId " +
                "ORDER BY id DESC " +
                "LIMIT 10 OFFSET :offset"
    )
    fun getPlaylistKalam(playlistId: Int, searchKeyword: String, offset: Int): List<Kalam>

    @Query("SELECT * FROM kalam WHERE playlist_id = :playlistId")
    fun getAllPlaylistKalam(playlistId: Int): List<Kalam>

    @Insert
    fun insert(kalam: Kalam)

    @Insert
    fun insertAll(allKalams: List<Kalam>)

    @Query("SELECT COUNT(*) FROM kalam")
    fun countAll(): Int

    @Query("SELECT COUNT(*) FROM kalam WHERE offline_src != ''")
    fun countDownloads(): Int

    @Query("SELECT COUNT(*) FROM kalam WHERE is_favorite = 1")
    fun countFavorites(): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(kalam: Kalam)

    @Delete
    fun delete(kalam: Kalam)
}