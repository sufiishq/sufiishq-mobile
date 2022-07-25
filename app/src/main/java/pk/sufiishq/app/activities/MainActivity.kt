package pk.sufiishq.app.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import pk.sufiishq.app.data.repository.KalamRepository
import pk.sufiishq.app.db.SufiIshqDatabase
import pk.sufiishq.app.models.Kalam
import pk.sufiishq.app.services.AudioPlayerService
import pk.sufiishq.app.ui.screen.MainView
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.viewmodels.PlayerViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var kalamRepository: KalamRepository

    @Inject
    lateinit var db: SufiIshqDatabase

    private val playerViewModel: PlayerViewModel by viewModels()
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init {

            setContent {
                SufiIshqTheme {
                    MainView(playerViewModel)
                }
            }

            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.disposeDownload()
        unbindService(serviceConnection)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            val binder = service as AudioPlayerService.AudioPlayerBinder
            val playerController = binder.getService()
            if (playerController.getActiveTrack() == null) {
                playerController.setActiveTrack(kalamRepository.getDefaultKalam())
            }
            playerViewModel.setPlayerService(playerController)
            playerController.setPlayerListener(playerViewModel)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            playerViewModel.setPlayerService(null)
        }
    }

    private fun init(init: () -> Unit) {
        if (kalamRepository.countAll() <= 0) {
            prePopulateKalams(db, this)
        }
        init()
    }

    private fun prePopulateKalams(db: SufiIshqDatabase, context: Context) {
        val allKalams = getAllKalams(context)
        db.kalamDao().insertAll(allKalams)
    }

    private fun getAllKalams(context: Context): List<Kalam> {
        val list = mutableListOf<Kalam>()

        val fileContent = context.assets.open("kalam.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(fileContent)
        (0 until jsonArray.length()).forEach {
            val jsonObject = jsonArray.getJSONObject(it)
            list.add(parseKalam(jsonObject))
        }
        return list
    }

    @SuppressLint("Range")
    private fun parseKalam(jsonObject: JSONObject): Kalam {
        return Kalam(
            id = jsonObject.getInt(KalamTableInfo.COLUMN_ID),
            title = jsonObject.getString(KalamTableInfo.COLUMN_TITLE),
            code = jsonObject.getInt(KalamTableInfo.COLUMN_CODE),
            year = jsonObject.getString(KalamTableInfo.COLUMN_YEAR),
            location = jsonObject.getString(KalamTableInfo.COLUMN_LOCATION),
            onlineSource = jsonObject.getString(KalamTableInfo.ONLINE_SRC),
            offlineSource = jsonObject.getString(KalamTableInfo.OFFLINE_SRC),
            isFavorite = jsonObject.getInt(KalamTableInfo.FAVORITE),
            playlistId = jsonObject.getInt(KalamTableInfo.PLAYLIST_ID)
        )
    }

    private class KalamTableInfo {
        companion object {
            const val COLUMN_ID = "id"
            const val COLUMN_TITLE = "title"
            const val COLUMN_CODE = "code"
            const val COLUMN_YEAR = "year"
            const val COLUMN_LOCATION = "location"
            const val ONLINE_SRC = "online_src"
            const val OFFLINE_SRC = "offline_src"
            const val FAVORITE = "favorite"
            const val PLAYLIST_ID = "playlist_id"
        }
    }
}