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

package pk.sufiishq.app.feature.admin

import pk.sufiishq.app.R
import pk.sufiishq.app.feature.admin.model.Highlight
import pk.sufiishq.app.feature.admin.model.Maintenance
import pk.sufiishq.app.utils.getString

sealed class FirebaseDatabaseStatus(val message: String?) {
    class Write(message: String = getString(R.string.msg_record_updated)) :
        FirebaseDatabaseStatus(message)

    class ReadHighlight(val highlight: Highlight) : FirebaseDatabaseStatus(null)
    class ReadMaintenance(val maintenance: Maintenance) : FirebaseDatabaseStatus(null)

    class Delete(message: String = getString(R.string.msg_highlight_deleted)) :
        FirebaseDatabaseStatus(message)

    class Failed(val ex: Exception) : FirebaseDatabaseStatus(ex.message ?: ex.localizedMessage)
}
