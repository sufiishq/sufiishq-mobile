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

package pk.sufiishq.app.feature.personalize.data.repository

import androidx.lifecycle.LiveData
import pk.sufiishq.app.feature.personalize.data.dao.PersonalizeDao
import pk.sufiishq.app.feature.personalize.model.Personalize
import javax.inject.Inject

class PersonalizeRepository @Inject constructor(
    private val personalizeDao: PersonalizeDao,
) {

    fun getPersonalize(): LiveData<Personalize?> {
        return personalizeDao.get()
    }

    suspend fun setPersonalize(personalize: Personalize) {
        personalizeDao.insert(personalize)
    }

    suspend fun clearPersonalize() {
        personalizeDao.clear()
    }
}
