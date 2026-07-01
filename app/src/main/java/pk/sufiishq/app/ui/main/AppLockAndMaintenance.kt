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

package pk.sufiishq.app.ui.main

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.applock.AppLockManager
import pk.sufiishq.app.feature.applock.model.AppLockStatus
import pk.sufiishq.app.feature.player.controller.AudioPlayer
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.ui.screen.applock.AppLockKeyboardWithPinView
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.extention.toastShort
import pk.sufiishq.app.utils.getString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIMarqueeText
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@PackagePrivate
@Composable
fun AppLockAndMaintenance(
    appLockManager: AppLockManager,
    audioPlayer: AudioPlayer,
) {
    val appLockStatus = appLockManager.getAppLockStatus().observeAsState().value
    if (appLockStatus != null) {
        AppLockView(
            appLockStatus = appLockStatus,
            appLockManager = appLockManager,
            audioPlayer = audioPlayer,
        )
    }
}

@Composable
private fun AppLockView(
    appLockStatus: AppLockStatus,
    appLockManager: AppLockManager,
    audioPlayer: AudioPlayer,
) {
    val context = LocalContext.current
    val showSecurityQuestion = rem(false)

    SIBox(
        modifier = Modifier.fillMaxSize(),
        bgColor = AuroraColor.Background,
    ) {
        SIBox(
            modifier = Modifier.fillMaxSize(),
            padding = 12,
        ) {
            if (showSecurityQuestion.value) {
                SecurityQuestionView(
                    scope = this,
                    showSecurityQuestion = showSecurityQuestion,
                    appLockStatus = appLockStatus,
                    appLockManager = appLockManager,
                )
            } else {
                AppLockDetailView(
                    scope = this,
                    showSecurityQuestion = showSecurityQuestion,
                    appLockStatus = appLockStatus,
                    appLockManager = appLockManager,
                    audioPlayer = audioPlayer,
                )
            }
        }
    }

    if (appLockStatus.isBiometricEnable) {
        LaunchedEffect(Unit) {
            appLockManager.promptBiometricForVerification(context as FragmentActivity)
        }
    }
}

@Composable
private fun SecurityQuestionView(
    scope: BoxScope,
    showSecurityQuestion: MutableState<Boolean>,
    appLockStatus: AppLockStatus,
    appLockManager: AppLockManager,
) {
    val context = LocalContext.current
    with(scope) {
        SIColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
        ) {
            val coroutineScope = rememberCoroutineScope()
            val answer = rem("")

            SIRow(
                modifier = Modifier.fillMaxWidth(),
                bgColor = AuroraColor.Background,
                radius = 4,
                padding = 12,
            ) {
                SIText(
                    text =
                        optString(
                            TextRes.dynamic_ask_security_question,
                            appLockStatus.securityQuestion.question,
                        ),
                    textColor = it,
                )
            }
            SIHeightSpace(value = 12)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = optString(TextRes.label_answer),
                value = answer.value,
                onValueChange = { answer.value = it },
                maxLength = 30,
                emptyFieldError = optString(TextRes.msg_ans_required),
            )

            SIBox(modifier = Modifier.fillMaxWidth()) {
                SIRow(modifier = Modifier.align(Alignment.CenterEnd)) {
                    SIButton(
                        text = optString(TextRes.label_cancel),
                        onClick = { showSecurityQuestion.value = false },
                    )
                    SIWidthSpace(value = 8)
                    SIButton(
                        text = optString(TextRes.label_reset),
                        onClick = {
                            coroutineScope.launch {
                                if (answer.value.trim().isEmpty()) {
                                    context.toastShort(getString(TextRes.msg_ans_not_empty))
                                } else if (answer.value.trim().lowercase() !=
                                    appLockStatus.securityQuestion.answer.trim().lowercase()
                                ) {
                                    context.toastShort(getString(TextRes.msg_ans_not_matched))
                                } else {
                                    appLockManager.removeAppLock()
                                    appLockManager.setAppLockStatus(null)
                                    context.toastShort(getString(TextRes.msg_app_lock_removed))
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AppLockDetailView(
    scope: BoxScope,
    showSecurityQuestion: MutableState<Boolean>,
    appLockStatus: AppLockStatus,
    appLockManager: AppLockManager,
    audioPlayer: AudioPlayer,
) {
    with(scope) {
        SIConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        ) {
            val (playControlRef, headerRef, forgotPinRef, pinKeyboardRef) = createRefs()

            val showPauseButton = rem(audioPlayer.isPlaying())

            if (showPauseButton.value) {
                SIColumn(
                    modifier =
                        Modifier.constrainAs(playControlRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(headerRef.top)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val length = audioPlayer.getActiveTrack().title.length
                    val textLayoutWidth: Int =
                        when {
                            length <= 5 -> 45
                            length <= 7 -> 60
                            length <= 10 -> 80
                            length <= 13 -> 100
                            length <= 16 -> 120
                            length <= 19 -> 130
                            else -> 150
                        }
                    SIIcon(
                        modifier = Modifier.size(25.dp),
                        resId = ImageRes.ic_pause,
                        tint = it,
                        onClick = {
                            showPauseButton.value = false
                            audioPlayer.doPlayOrPause()
                        },
                    )
                    SIMarqueeText(
                        modifier = Modifier.width(textLayoutWidth.dp),
                        text = audioPlayer.getActiveTrack().title,
                        textColor = it,
                        backgroundColor = AuroraColor.Background,
                    )
                }
            }

            SIImage(
                modifier =
                    Modifier
                        .padding(top = 20.dp)
                        .constrainAs(headerRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(pinKeyboardRef.top)
                        },
                resId = ImageRes.caligraphi,
            )

            SIRow(
                modifier =
                    Modifier.constrainAs(forgotPinRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
            ) {
                SIText(
                    text = optString(TextRes.label_forgot_pin),
                    textColor = it,
                    textSize = TextSize.Small,
                    textStyle =
                        LocalTextStyle.current.copy(
                            textDecoration = TextDecoration.Underline,
                        ),
                    onClick = { showSecurityQuestion.value = true },
                )
            }

            SIColumn(
                modifier =
                    Modifier
                        .padding(bottom = 24.dp)
                        .constrainAs(pinKeyboardRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SIText(
                    text = optString(TextRes.label_enter_pin),
                    textColor = it,
                    textSize = TextSize.Large,
                )
                SIHeightSpace(value = 48)
                AppLockKeyboardWithPinView(
                    onPinGenerated = { appLockManager.setAppLockStatus(null) },
                    validPin = appLockStatus.savedPin,
                )
            }
        }
    }
}