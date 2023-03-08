/*
 * Copyright 2022-2023 SufiIshq
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pk.sufiishq.app.ui.screen.videoplay

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay
import pk.sufiishq.app.R
import pk.sufiishq.app.feature.app.model.Media
import pk.sufiishq.app.feature.media.controller.VideoPlayController
import pk.sufiishq.app.feature.media.controller.VideoPlayViewModel
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.isNetworkAvailable
import pk.sufiishq.app.utils.extention.toLandscape
import pk.sufiishq.app.utils.extention.toPortrait
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.quickToast
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.layout.SIBox

@Composable
fun VideoPlayScreen(
    navController: NavController,
    media: Media,
    videoPlayController: VideoPlayController = hiltViewModel<VideoPlayViewModel>(),
) {
    val context = LocalContext.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val pauseVideo = videoPlayController.pauseVideo().observeAsState()
    val showLoadingIndicator = rem(true)
    val styledPlayerView = rem<StyledPlayerView?>(null)

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(media.src))

            addListener(
                object : Player.Listener {

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == Player.STATE_READY) {
                            showLoadingIndicator.value = false
                            styledPlayerView.value?.visibility = View.VISIBLE
                        }

                        styledPlayerView.value?.keepScreenOn =
                            !(playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED || !playWhenReady)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        videoPlayController.onIsPlayingChanged(isPlaying)
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
                        quickToast(
                            error.message ?: error.localizedMessage
                                ?: getString(TextRes.label_unknown_error),
                        )
                        showLoadingIndicator.value = false
                    }
                },
            )

            prepare()
            playWhenReady = true
        }
    }

    SIBox(modifier = Modifier.fillMaxSize()) {
        DisposableEffect(key1 = Unit) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    exoPlayer.pause()
                }
            }

            val lifecycle = lifecycleOwner.value.lifecycle
            lifecycle.addObserver(observer)

            onDispose {
                exoPlayer.pause()
                exoPlayer.seekToDefaultPosition()
                exoPlayer.clearMediaItems()
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = {
                styledPlayerView.value = StyledPlayerView(context).apply {
                    visibility = View.INVISIBLE
                    player = exoPlayer
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )

                    setFullscreenButtonClickListener { isFullscreen ->
                        if (isFullscreen) it.toLandscape() else it.toPortrait()
                    }

                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                    findViewById<View>(R.id.exo_settings).isVisible = false
                    setShowSubtitleButton(false)
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                    setShutterBackgroundColor(Color.TRANSPARENT)
                }
                styledPlayerView.value!!
            },
        )

        if (showLoadingIndicator.value) {
            SICircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
                strokeWidth = 4,
            )
        }
    }

    if (pauseVideo.value == true) {
        exoPlayer.playWhenReady = false
    }

    LaunchedEffect(key1 = Unit) {
        if (!context.isNetworkAvailable()) {
            quickToast(getString(TextRes.msg_no_network_connection))
            delay(1000)
            navController.popBackStack()
        }
    }

    BackHandler {
        context.toPortrait()
        navController.popBackStack()
    }
}
