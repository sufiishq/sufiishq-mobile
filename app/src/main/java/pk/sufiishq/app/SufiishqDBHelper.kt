package pk.sufiishq.app

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class SufiishqDBHelper(private val context: Context): SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    fun getKalams(page: Int, searchKeyword: String): List<Track> {
        val list = mutableListOf<Track>()

        val query = "SELECT * FROM ${Kalam.TABLE_NAME} " +
                "WHERE LOWER(${Kalam.COLUMN_TITLE}) LIKE '%${searchKeyword.lowercase()}%' OR LOWER(${Kalam.COLUMN_LOCATION}) LIKE '%${searchKeyword.lowercase()}%' OR ${Kalam.COLUMN_YEAR} LIKE '%${searchKeyword.lowercase()}%'" +
                "ORDER BY ${Kalam.COLUMN_ID} ASC " +
                "LIMIT ${page.minus(1) * 10}, 10"

        val cursor = readableDatabase.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                list.add(parseKalam(cursor))
            } while (cursor.moveToNext())
        }

        return list
    }

    fun getFirstKalam(): Track {
        val cursor = readableDatabase.rawQuery("SELECT * FROM ${Kalam.TABLE_NAME} ORDER BY ${Kalam.COLUMN_ID} ASC LIMIT 0, 1", null)
        cursor.moveToFirst()
        return parseKalam(cursor)
    }

    @SuppressLint("Range")
    private fun parseKalam(cursor: Cursor): Track {
        return Track(
            id = cursor.getInt(cursor.getColumnIndex(Kalam.COLUMN_ID)),
            title = cursor.getString(cursor.getColumnIndex(Kalam.COLUMN_TITLE)),
            code = cursor.getInt(cursor.getColumnIndex(Kalam.COLUMN_CODE)),
            year = cursor.getString(cursor.getColumnIndex(Kalam.COLUMN_YEAR)),
            location = cursor.getString(cursor.getColumnIndex(Kalam.COLUMN_LOCATION)),
            src = cursor.getString(cursor.getColumnIndex(Kalam.COLUMN_SRC)),
            startFrom = cursor.getInt(cursor.getColumnIndex(Kalam.COLUMN_START_FROM))
        )
    }

    companion object {
        private class Kalam {
            companion object {
                const val TABLE_NAME = "kalam"
                const val COLUMN_ID = "id"
                const val COLUMN_TITLE = "title"
                const val COLUMN_CODE = "code"
                const val COLUMN_YEAR = "year"
                const val COLUMN_LOCATION = "location"
                const val COLUMN_SRC = "src"
                const val COLUMN_START_FROM = "start_from"
            }
        }
    }
}