package pk.sufiishq.app.feature.gallery.model

import androidx.annotation.DrawableRes

data class Section(
    val title: String,
    @DrawableRes val leadingIcon: Int,
    val detail: String,
    val route: String
)
