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

package pk.sufiishq.app.core.help

import androidx.compose.ui.text.AnnotatedString

sealed interface HelpDataType {
    class Photo(val path: String) : HelpDataType
    class Divider(val height: Int) : HelpDataType
    class Spacer(val height: Int) : HelpDataType
    class Paragraph(val text: AnnotatedString) : HelpDataType
}
