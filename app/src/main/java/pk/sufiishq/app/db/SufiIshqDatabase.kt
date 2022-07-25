package pk.sufiishq.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pk.sufiishq.app.data.dao.KalamDao
import pk.sufiishq.app.data.dao.PlaylistDao
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.models.Playlist

@Database(entities = [Kalam::class, Playlist::class], version = 1)
abstract class SufiIshqDatabase : RoomDatabase() {

    abstract fun kalamDao(): KalamDao
    abstract fun playlistDao(): PlaylistDao

    companion object {

        private var INSTANCE: SufiIshqDatabase? = null

        fun getInstance(context: Context): SufiIshqDatabase {

            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room
                        .databaseBuilder(context, SufiIshqDatabase::class.java, "sufiishq")
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE!!
        }

    }
}