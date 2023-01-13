package pk.sufiishq.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import pk.sufiishq.app.R
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.utils.optValue

@Composable
fun HelpView(
    helpDataProvider: HelpDataProvider
) {

    val helpContent = helpDataProvider.getHelpContent().observeAsState().optValue(listOf())
    val matColors = MaterialTheme.colors

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(matColors.secondaryVariant)
    ) {

        itemsIndexed(helpContent) { _, item ->
            Text(
                text = item.title,
                style = MaterialTheme.typography.h4
            )

            Column {
                item.content.onEach {
                    when (it) {
                        is HelpData.Paragraph -> Text(text = it.text)
                        is HelpData.Photo -> Image(
                            painter = painterResource(id = R.drawable.ic_round_favorite_24),
                            contentDescription = null
                        )
                        is HelpData.Divider -> Divider()
                        is HelpData.Spacer -> Spacer(modifier = Modifier)
                    }
                }
            }
        }
    }

}