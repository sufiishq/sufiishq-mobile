package pk.sufiishq.app.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
import pk.sufiishq.app.core.help.HelpData
import pk.sufiishq.app.models.HelpContent

@Composable
fun HelpExpandableCard(
    expandedIndex: MutableState<Int>,
    index: Int,
    bgColor: Color,
    fgColor: Color,
    item: HelpContent,
    matColors: Colors
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                if (expandedIndex.value == index) RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp)
                else RoundedCornerShape(6.dp, 6.dp, 6.dp, 6.dp)
            )
            .background(matColors.primaryVariant)
            .clickable {
                expandedIndex.value = index
            }
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp, 16.dp, 0.dp, 16.dp)
                .size(14.dp),
            painter = painterResource(id = R.drawable.ic_baseline_circle_24),
            tint = matColors.secondaryVariant,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(12.dp, 16.dp, 16.dp, 16.dp),
            text = item.title,
            color = fgColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 400, // Animation Speed
                    easing = LinearOutSlowInEasing // Animation Type
                )
            )
    ) {
        if (expandedIndex.value == index) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp, 0.dp, 6.dp, 6.dp))
                    .background(bgColor)
                    .padding(12.dp, 0.dp, 12.dp, 12.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearOutSlowInEasing
                        )
                    )
            ) {
                item.content.onEach {
                    when (it) {
                        is HelpData.Paragraph -> Text(
                            modifier = Modifier
                                .padding(top = 12.dp),
                            text = it.text,
                            color = fgColor,
                            style = MaterialTheme.typography.body1
                        )
                        is HelpData.Photo -> AssetImage(
                            path = it.path,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        )
                        is HelpData.Divider -> Divider(
                            thickness = it.height.dp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        is HelpData.Spacer -> Spacer(modifier = Modifier.height(it.height.dp))
                    }
                }
            }
        }
    }
}