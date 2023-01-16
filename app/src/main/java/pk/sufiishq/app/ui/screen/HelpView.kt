package pk.sufiishq.app.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.data.providers.HelpDataProvider
import pk.sufiishq.app.models.HelpContent
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.annotateParagraph
import pk.sufiishq.app.utils.dummyHelpDataProvider
import pk.sufiishq.app.utils.optValue
import pk.sufiishq.app.viewmodels.HelpViewModel

@Composable
fun HelpView(
    helpDataProvider: HelpDataProvider = hiltViewModel<HelpViewModel>()
) {
    val helpContent = helpDataProvider.getHelpContent().observeAsState().optValue(listOf())
    val matColors = MaterialTheme.colors
    Column(
        modifier = Modifier
            .background(matColors.secondaryVariant)
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            painter = painterResource(id = R.drawable.help),
            alignment = Alignment.Center,
            contentDescription = null
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(matColors.secondaryVariant),
            contentPadding = PaddingValues(8.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(helpContent) { _, item ->
                CardContent(item = item, matColors = matColors)
            }
        }
    }
}

@Composable
private fun CardContent(item: HelpContent, matColors: Colors) {
    var expanded by remember { mutableStateOf(false) }
    var stroke by remember { mutableStateOf(1) } // Stroke State
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                if (expanded) RoundedCornerShape(
                    6.dp,
                    6.dp,
                    0.dp,
                    0.dp
                ) else RoundedCornerShape(6.dp, 6.dp, 6.dp, 6.dp)
            )
            .background(matColors.primaryVariant)
    ) {
        IconButton(
            modifier = Modifier
                .weight(.1f),
            onClick = {
                expanded = !expanded
                stroke = if (expanded) 2 else 1
            }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                tint = matColors.primary,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                text = item.title,
                color = matColors.primary,
            )
        }
    }

    if (expanded) {
        Divider(thickness = 1.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp, 0.dp, 6.dp, 6.dp))
            .background(matColors.primaryVariant)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 400, // Animation Speed
                    easing = LinearOutSlowInEasing // Animation Type
                )
            )
    ) {
        if (expanded) {
            item.content.onEach {
                when (it) {
                    is HelpData.Paragraph -> Text(
                        modifier = Modifier
                            .padding(12.dp, 0.dp),
                        text = annotateParagraph(it.text),
                        color = matColors.primary,
                    )
                    is HelpData.Photo -> Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(top = 12.dp, bottom = 12.dp),
                        painter = painterResource(id = R.drawable.image_placeholder),
                        alignment = Alignment.Center,
                        contentDescription = null
                    )
                    is HelpData.Divider -> Divider(thickness = it.height.dp)
                    is HelpData.Spacer -> Spacer(modifier = Modifier.height(it.height.dp))
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewLightHelpView() {
    SufiIshqTheme(darkTheme = false) {
        HelpView(
            helpDataProvider = dummyHelpDataProvider()
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun PreviewDarkHelpView() {
    SufiIshqTheme(darkTheme = true) {
        HelpView(
            helpDataProvider = dummyHelpDataProvider()
        )
    }
}