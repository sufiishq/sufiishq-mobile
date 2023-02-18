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

package pk.sufiishq.app.feature.kalam.splitter

import pk.sufiishq.app.feature.kalam.model.Kalam

data class SplitKalamInfo(
    val kalam: Kalam,
    val splitStart: Int = 0,
    val splitEnd: Int = 0,
    val kalamLength: Int = 0,
    val previewKalamLength: Int = 0,
    val previewKalamProgress: Int = 0,
    val previewPlayStart: Boolean = false,
    val splitStatus: SplitStatus = SplitStatus.Start,
)
