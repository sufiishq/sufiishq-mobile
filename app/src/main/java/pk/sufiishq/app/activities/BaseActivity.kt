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
import pk.sufiishq.app.feature.admin.auth.AuthManager
import pk.sufiishq.app.feature.admin.maintenance.MaintenanceManager
import pk.sufiishq.app.feature.app.AppManager
import pk.sufiishq.app.feature.app.PermissionManager
import pk.sufiishq.app.feature.app.SyncManager
import pk.sufiishq.app.feature.app.controller.AssetKalamLoaderViewModel
import pk.sufiishq.app.feature.app.controller.MainViewModel
import pk.sufiishq.app.feature.applock.AppLockManager
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.feature.player.di.qualifier.AndroidMediaPlayer
import pk.sufiishq.app.feature.player.service.AudioPlayerService
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : FragmentActivity() {

    @Inject @AndroidMediaPlayer
    lateinit var player: AudioPlayer

    @Inject lateinit var appManager: AppManager

    @Inject lateinit var authManager: AuthManager

    @Inject lateinit var appLockManager: AppLockManager

    @Inject lateinit var maintenanceManager: MaintenanceManager

    @Inject lateinit var permissionManager: PermissionManager

    @Inject lateinit var syncManager: SyncManager

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

        permissionManager.validateNotificationPermission(this)

        syncManager.sync()
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
