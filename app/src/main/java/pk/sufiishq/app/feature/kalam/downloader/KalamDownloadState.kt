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

package pk.sufiishq.app.feature.kalam.downloader

import pk.sufiishq.app.feature.kalam.model.Kalam

sealed class KalamDownloadState(val silent: Boolean) {

    class Idle(silent: Boolean) : KalamDownloadState(silent)
    class Started(val kalam: Kalam, silent: Boolean) : KalamDownloadState(silent)
    class InProgress(val fileInfo: FileInfo, val kalam: Kalam, silent: Boolean) : KalamDownloadState(silent)
    class Error(val error: String, val kalam: Kalam, silent: Boolean) : KalamDownloadState(silent)
    class Completed(val kalam: Kalam, silent: Boolean) : KalamDownloadState(silent)
}
