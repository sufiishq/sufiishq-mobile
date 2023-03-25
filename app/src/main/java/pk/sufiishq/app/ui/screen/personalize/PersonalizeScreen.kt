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

package pk.sufiishq.app.ui.screen.personalize

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import pk.sufiishq.app.feature.personalize.controller.PersonalizeController
import pk.sufiishq.app.feature.personalize.controller.PersonalizeViewModel
import pk.sufiishq.app.feature.personalize.model.LogoPath
import pk.sufiishq.app.feature.personalize.model.Personalize
import pk.sufiishq.app.ui.components.PersonalizedLogo
import pk.sufiishq.app.utils.Constants.MEDIA_PATH
import pk.sufiishq.app.utils.Constants.SUFI_ISHQ_HOST
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.appendPath
import pk.sufiishq.app.utils.extention.offlineFileExists
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIButton
import pk.sufiishq.aurora.components.SICircularProgressIndicator
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SILazyVerticalGrid
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

private const val FRONT_FACE = "photo_front"
private const val BACK_FACE = "photo_back"

@Composable
fun PersonalizeScreen(
    personalizeController: PersonalizeController = hiltViewModel<PersonalizeViewModel>(),
) {
    val context = LocalContext.current
    val personalize = personalizeController.get().observeAsState()
    val customPhotoPath = remember {
        MutableTransitionState(false).apply {
            targetState = false
        }
    }
    customPhotoPath.targetState = personalize.value?.let { true } ?: false
    val availableLogos = rem(listOf<LogoPath>())
    val selectionFileName = rem("")
    val scope = rememberCoroutineScope()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            scope.launch {
                it?.let {
                    val ext = when (context.contentResolver.getType(it)?.lowercase()) {
                        "image/jpeg" -> ".jpg"
                        "image/png" -> ".png"
                        else -> ".jpg"
                    }
                    val fileName = "${selectionFileName.value}$ext"
                    val output = context.filesDir.appendPath(PersonalizeViewModel.PERSONALIZE_DIR)
                        .appendPath(fileName)

                    context.contentResolver.openInputStream(it)?.use { inputStream ->
                        output.outputStream().write(inputStream.readBytes())
                    }

                    personalize.value?.apply {
                        val frontFace = takeOrElse(
                            selectionFileName.value == FRONT_FACE,
                            "${PersonalizeViewModel.PERSONALIZE_DIR}/$fileName",
                            frontPath,
                        )
                        val backFace = takeOrElse(
                            selectionFileName.value == BACK_FACE,
                            "${PersonalizeViewModel.PERSONALIZE_DIR}/$fileName",
                            backPath,
                        )

                        personalizeController.update(
                            copy(
                                frontPath = frontFace,
                                backPath = backFace,
                            ),
                        )
                    }
                }
            }
        },
    )

    SIConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 18.dp),
    ) {
        val (headerRef, bodyRef) = createRefs()

        SIColumn(
            modifier = Modifier
                .constrainAs(headerRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PersonalizedLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                personalizeController = personalizeController,
            )
            SIHeightSpace(value = 12)
            AnimatedVisibility(customPhotoPath.targetState) {
                SIRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    SIButton(
                        modifier = Modifier.weight(1f),
                        text = optString(TextRes.label_front_photo),
                        onClick = {
                            selectionFileName.value = FRONT_FACE
                            galleryLauncher.launch("image/*")
                        },
                    )
                    SIWidthSpace(value = 8)
                    SIButton(
                        modifier = Modifier.weight(1f),
                        text = optString(TextRes.label_back_photo),
                        onClick = {
                            selectionFileName.value = BACK_FACE
                            galleryLauncher.launch("image/*")
                        },
                    )
                }
            }
        }

        SIRow(
            modifier = Modifier
                .constrainAs(bodyRef) {
                    start.linkTo(parent.start)
                    top.linkTo(headerRef.bottom, 12.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            bgColor = AuroraColor.Background,
            radius = 4,
        ) {
            SILazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(top = 12.dp),
            ) {
                item {
                    DefaultLogoViewItem(personalizeController)
                }

                items(availableLogos.value) {
                    PersonalLogoViewItem(
                        logoPath = it,
                        personalizeController = personalizeController,
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        availableLogos.value = (1..14).toList().map {
            LogoPath(
                offlinePath = "${PersonalizeViewModel.PERSONALIZE_DIR}/photo_$it.jpg",
                onlinePath = SUFI_ISHQ_HOST + MEDIA_PATH + "/${PersonalizeViewModel.PERSONALIZE_DIR}/photo_$it.jpg",
            )
        }
    }
}

@Composable
fun DefaultLogoViewItem(
    personalizeController: PersonalizeController,
) {
    SIBox(
        modifier = Modifier.fillMaxSize(),
        padding = 8,
    ) {
        SIBox(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            padding = 3,
            bgColor = AuroraColor.SecondaryVariant,
        ) {
            SIBox(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable {
                        personalizeController.reset()
                    },
                bgColor = AuroraColor.Background,
            ) {
                SIImage(modifier = Modifier.fillMaxSize(0.9f), resId = ImageRes.logo)
            }
        }
    }
}

@Composable
fun PersonalLogoViewItem(
    logoPath: LogoPath,
    personalizeController: PersonalizeController,
) {
    val context = LocalContext.current
    val resolvedLogoPath = rem<LogoPath?>(null)

    LaunchedEffect(key1 = Unit) {
        resolvedLogoPath.value = personalizeController.resolveLogo(logoPath)
    }

    SIBox(
        modifier = Modifier.fillMaxSize(),
        padding = 8,
    ) {
        SIBox(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            padding = 3,
            bgColor = AuroraColor.SecondaryVariant,
        ) {
            SIBox(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable {
                        if (logoPath.offlineFileExists(context)) {
                            personalizeController.update(
                                Personalize(1, logoPath.offlinePath, logoPath.offlinePath),
                            )
                        }
                    },
                bgColor = AuroraColor.Background,
            ) {
                resolvedLogoPath.value?.apply {
                    SIImage(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(context.filesDir.appendPath(logoPath.offlinePath)),
                        contentScale = ContentScale.Crop,
                    )
                } ?: run {
                    SICircularProgressIndicator(
                        modifier = Modifier.fillMaxSize(0.5f),
                        color = AuroraColor.SecondaryVariant,
                        strokeWidth = 2,
                    )
                }
            }
        }
    }
}

fun <T> takeOrElse(condition: Boolean, take: T, default: T): T {
    return if (condition) take else default
}
