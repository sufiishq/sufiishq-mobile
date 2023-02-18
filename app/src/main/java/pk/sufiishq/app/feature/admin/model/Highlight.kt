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

package pk.sufiishq.app.feature.admin.model

data class Highlight(
    val startDateTime: Long,
    val endDateTime: Long,
    val title: String?,
    val detail: String,
    val contacts: Map<String, Map<String, String>>? = null,
) {
    companion object {
        const val NAME = "name"
        const val NUMBER = "number"
    }
}
