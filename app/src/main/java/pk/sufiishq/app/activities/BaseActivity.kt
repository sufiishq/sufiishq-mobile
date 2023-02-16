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

package pk.sufiishq.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import pk.sufiishq.app.core.app.AppManager
import pk.sufiishq.app.core.applock.AppLockManager
import pk.sufiishq.app.core.firebase.AuthManager
import pk.sufiishq.app.core.firebase.MaintenanceManager
import pk.sufiishq.app.core.player.AudioPlayer
import pk.sufiishq.app.core.player.service.AudioPlayerService
import pk.sufiishq.app.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.viewmodels.AssetKalamLoaderViewModel
import pk.sufiishq.app.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : FragmentActivity() {

    @Inject @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject lateinit var appManager: AppManager

    @Inject lateinit var authManager: AuthManager

    @Inject lateinit var appLockManager: AppLockManager

    @Inject lateinit var maintenanceManager: MaintenanceManager

    private val mainController: MainViewModel by viewModels()
    private val assetKalamLoaderViewModel: AssetKalamLoaderViewModel by viewModels()
    private val playerIntent by lazy { Intent(this, AudioPlayerService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assetKalamLoaderViewModel.loadAllKalam()

        startService(playerIntent)

        // check any incoming update from play-store
        mainController.checkUpdate(this@BaseActivity)

        authManager.registerActivityResultListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        runCatching { mainController.unregisterListener(this) }

        if (!player.isPlaying()) {
            player.release()
            stopService(playerIntent)
        }
    }
}
