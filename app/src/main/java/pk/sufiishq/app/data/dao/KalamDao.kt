package pk.sufiishq.app.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import pk.sufiishq.app.models.Kalam

@Dao
interface KalamDao {

    @Query("SELECT * FROM kalam ORDER BY id DESC LIMIT 1")
    fun getFirstKalam(): LiveData<Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword)" +
                "ORDER BY id DESC"
    )
    fun getAllKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND offline_src != ''" +
                "ORDER BY id DESC"
    )
    fun getDownloadsKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND favorite = 1 " +
                "ORDER BY id DESC"
    )
    fun getFavoritesKalam(searchKeyword: String): PagingSource<Int, Kalam>

    @Query(
        "SELECT * FROM kalam " +
                "WHERE (LOWER(title) LIKE :searchKeyword OR LOWER(location) LIKE :searchKeyword OR year LIKE :searchKeyword) AND playlist_id = :playlistId " +
                "ORDER BY id DESC"
    )
    fun getPlaylistKalam(playlistId: Int, searchKeyword: String): PagingSource<Int, Kalam>

    @Query("SELECT * FROM kalam WHERE playlist_id = :playlistId")
    fun getAllPlaylistKalam(playlistId: Int): LiveData<List<Kalam>>

    @RawQuery(observedEntities = [Kalam::class])
    fun getSingleKalam(query: SimpleSQLiteQuery): LiveData<Kalam?>

    @Insert
    suspend fun insert(kalam: Kalam)

    @Insert
    suspend fun insertAll(allKalams: List<Kalam>)

    @Query("SELECT COUNT(*) FROM kalam")
    fun countAll(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM kalam WHERE offline_src != ''")
    fun countDownloads(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM kalam WHERE favorite = 1")
    fun countFavorites(): LiveData<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(kalam: Kalam)

    @Delete
    suspend fun delete(kalam: Kalam)
}