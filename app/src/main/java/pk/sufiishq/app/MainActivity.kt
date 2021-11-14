package pk.sufiishq.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import android.os.Build
import androidx.core.app.ServiceCompat


class MainActivity : ComponentActivity() {


    private val db: SufiishqDBHelper by lazy { SufiishqDBHelper(this) }
    private val kalamRepository by lazy { KalamRepository(db) }
    private val kalamViewModel by lazy { KalamViewModel.get(this, kalamRepository) }
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kalamViewModel.init()

        setContent {
            SufiIshqTheme {
                MainScreen(kalamViewModel)
            }
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playerIntent)
        }
        else {
            startService(playerIntent)
        }*/

        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {
        super.onStart()
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playerIntent)
        }
        else {
            startService(playerIntent)
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        //stopService(playerIntent)
        unbindService(serviceConnection)
    }

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            val binder = service as AudioPlayerService.AudioPlayerBinder
            val playerController = binder.getService()
            if(playerController.getActiveTrack() == null) {
                playerController.setActiveTrack(kalamRepository.getDefaultKalam())
            }
            kalamViewModel.setPlayerService(playerController)
            playerController.setPlayerListener(kalamViewModel)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            kalamViewModel.setPlayerService(null)
        }
    }
}

@Composable
fun MainScreen(kalamDataProvider: KalamDataProvider) {
    val matColors = MaterialTheme.colors

    Surface(color = matColors.background) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            val lazyKalamItems: LazyPagingItems<Track> = kalamDataProvider.getKalamDataFlow().collectAsLazyPagingItems()

            SearchTextField(kalamDataProvider, matColors, lazyKalamItems)

            val gradientColors = listOf(
                0xFFff0000, 0xFFff4800, 0xFFff8400, 0xFFffb700
                , 0xFFffdd00, 0xFFe6ff00, 0xFFb3ff00, 0xFF59ff00
                , 0xFF00ff2a, 0xFF00ff90, 0xFF00ffd9, 0xFF00f7ff
                , 0xFF00ccff, 0xFF0099ff, 0xFF0073ff, 0xFF0048ff
                , 0xFF000dff, 0xFF4400ff, 0xFF5900ff, 0xFF7700ff
                , 0xFF9500ff, 0xFFc800ff, 0xFFff00f2, 0xFFff0077)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp, 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lazyKalamItems) { track ->
                    KalamItem(matColors = matColors, track = track!!, gradientColors, kalamDataProvider)
                }
            }

            Player(matColors, kalamDataProvider)

        }
    }
}


@Composable
fun SearchTextField(kalamDataProvider: KalamDataProvider, matColors: Colors, lazyKalamItems: LazyPagingItems<Track>) {
    var searchText by remember { mutableStateOf("") }
    val roundedCornerShape = RoundedCornerShape(12.dp)
    Box(
        Modifier
            .background(matColors.secondary)
            .padding(12.dp)
    ) {
        TextField(
            shape = roundedCornerShape,
            value = searchText,
            onValueChange = {
                searchText = it
                kalamDataProvider.searchKalam(it)
                lazyKalamItems.refresh()
            },
            placeholder = {
                Text("Search", color = matColors.primary)
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = matColors.primary,
                backgroundColor = matColors.primaryVariant,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(roundedCornerShape)
        )
    }
}

@Composable
fun Player(matColors: Colors, kalamDataProvider: KalamDataProvider) {
    val sliderValue by kalamDataProvider.getSeekbarValue().observeAsState()

    Box( Modifier.background(matColors.primaryVariant)) {
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp),
            value = sliderValue!!,
            valueRange = 0f..100f,
            enabled = kalamDataProvider.getSeekbarAccess().observeAsState().value!!,
            onValueChange = { kalamDataProvider.updateSeekbarValue(it) },
            onValueChangeFinished = {
                kalamDataProvider.onSeekbarChanged(sliderValue!!)
            })

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val playerState = kalamDataProvider.getPlayerState().observeAsState().value

            if (playerState == PlayerState.LOADING) {
                CircularProgressIndicator()
            }
            else {
                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }) {
                            kalamDataProvider.doPlayOrPause()
                        },
                    painter = painterResource(id = if(playerState == PlayerState.PAUSE || playerState == PlayerState.IDLE) R.drawable.ic_play else R.drawable.ic_pause),
                    colorFilter = ColorFilter.tint(matColors.primary),
                    contentDescription = null
                )
            }

            val activeKalam by kalamDataProvider.getActiveKalam().observeAsState()
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = 12.dp)
            ) {

                // kalam title
                Text(
                    color = matColors.primary,
                    fontSize = 18.sp,
                    text = activeKalam!!.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                // kalam meta info
                Text(
                    color = matColors.primary,
                    fontSize = 14.sp,
                    text = "${activeKalam!!.location} ${activeKalam!!.year}"
                )
            }
        }
    }
}

@Composable
fun KalamItem(matColors: Colors, track: Track, gradientColors: List<Long>, kalamDataProvider: KalamDataProvider) {

    val startColor = Color(gradientColors[(Math.random() * gradientColors.size).toInt()])
    val endColor = Color(gradientColors[(Math.random() * gradientColors.size).toInt()])

    val backgroundShape = RoundedCornerShape(6.dp)
    Column(Modifier.clickable {
        kalamDataProvider.changeTrack(track)
    }) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .clip(backgroundShape)
                .background(matColors.primaryVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val boxShape = RoundedCornerShape(topStart = 6.dp, topEnd = 0.dp, bottomStart = 6.dp, bottomEnd = 0.dp)
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .clip(boxShape)
                .background(brush = Brush.verticalGradient(listOf(startColor, endColor))))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // kalam title
                Text(
                    color = matColors.primary,
                    fontSize = 18.sp,
                    text = track.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)

                // kalam meta info
                Text(
                    color = matColors.primary,
                    fontSize = 14.sp,
                    text = "${track.location} ${track.year}"
                )
            }
        }

        //Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LightPreview() {
    SufiIshqTheme(darkTheme = false) {
        MainScreen(dummyKalamDataProvider())
    }
}

@Preview(showBackground = true)
@Composable
fun DarkPreview() {
    SufiIshqTheme(darkTheme = true) {
        MainScreen(dummyKalamDataProvider())
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemLightPreview() {
    SufiIshqTheme(darkTheme = false) {
        KalamItem(matColors = MaterialTheme.colors, track = dummyTrack(), listOf(0xFFFF0000, 0xFF00FF00), dummyKalamDataProvider())
    }
}

@Preview(showBackground = true)
@Composable
fun KalamItemDarkPreview() {
    SufiIshqTheme(darkTheme = true) {
        KalamItem(matColors = MaterialTheme.colors, track = dummyTrack(), listOf(0xFFFF0000, 0xFF00FF00), dummyKalamDataProvider())
    }
}

fun dummyKalamDataProvider(): KalamDataProvider {
    return object:KalamDataProvider {

        override fun getKalamDataFlow(): Flow<PagingData<Track>> {
            return emptyFlow()
        }

        override fun searchKalam(keyword: String) {}

        override fun getSeekbarValue(): LiveData<Float> {
            return MutableLiveData(0f)
        }

        override fun updateSeekbarValue(value: Float) {}

        override fun getSeekbarAccess(): LiveData<Boolean> {
            return MutableLiveData(true)
        }

        override fun onSeekbarChanged(value: Float) {}

        override fun getPlayerState(): LiveData<PlayerState> {
            return MutableLiveData(PlayerState.IDLE)
        }

        override fun doPlayOrPause() {}

        override fun getActiveKalam(): LiveData<Track> {
            return MutableLiveData(Track(0, "Kalam Title", 1, "1991", "Karachi", "", 0))
        }

        override fun changeTrack(track: Track) {}
    }
}

fun dummyTrack() = Track(1, "Kalam Title", 2, "1993", "Karachi", "", 0)