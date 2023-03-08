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

package pk.sufiishq.app.ui.screen.occasionlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import pk.sufiishq.app.feature.occasions.OccasionType
import pk.sufiishq.app.feature.occasions.controller.OccasionController
import pk.sufiishq.app.feature.occasions.controller.OccasionViewModel
import pk.sufiishq.app.feature.occasions.model.Occasion
import pk.sufiishq.app.helpers.ScreenType
import pk.sufiishq.app.ui.components.OccasionCard
import pk.sufiishq.app.ui.components.OutlinedTextField
import pk.sufiishq.app.utils.ImageRes
import pk.sufiishq.app.utils.TextRes
import pk.sufiishq.app.utils.extention.optString
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.layout.SIParallaxLazyColumn

@Composable
fun OccasionListScreen(
    navController: NavController,
    occasionType: OccasionType,
    occasionController: OccasionController = hiltViewModel<OccasionViewModel>(),
) {
    val searchText = rem("")
    val lazyOccasionItems: LazyPagingItems<Occasion> =
        occasionController.getOccasions(occasionType).collectAsLazyPagingItems()

    SIParallaxLazyColumn(
        leadingIcon = getLeadingIcon(occasionType),
        title = optString(getTitle(occasionType)),
        noItemText = optString(TextRes.msg_no_occasions, optString(getTitle(occasionType))),
        data = lazyOccasionItems,
        bottomView = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = optString(
                    TextRes.dynamic_search_occasion,
                    optString(getTitle(occasionType)),
                ),
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                    occasionController.searchOccasion(it)
                    lazyOccasionItems.refresh()
                },
            )
        },
    ) { _, item ->

        OccasionCard(
            occasion = item,
            onCardClick = {
                navController.navigate(ScreenType.Occasion.buildRoute(it))
            },
        )
    }
}

private fun getLeadingIcon(occasionType: OccasionType): Int {
    return when (occasionType) {
        OccasionType.Niaz -> ImageRes.niaz_full
        OccasionType.Urs -> ImageRes.urs_full
    }
}

private fun getTitle(occasionType: OccasionType): Int {
    return when (occasionType) {
        OccasionType.Niaz -> TextRes.title_niaz
        OccasionType.Urs -> TextRes.title_urs
    }
}
