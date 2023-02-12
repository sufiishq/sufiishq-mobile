package pk.sufiishq.aurora.components

import androidx.annotation.DrawableRes
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pk.sufiishq.aurora.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight
import kotlin.math.max

enum class TextSize(val value: Int) {
    ExtraSmall(12), Small(14), Regular(18), Large(22), ExtraLarge(26), Banner(36);
}

@Composable
fun SIText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: AuroraColor,
    textSize: TextSize = TextSize.Regular,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    @DrawableRes leadingIcon: Int? = null,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        textDirection = TextDirection.Content,
    ),
    onClick: (() -> Unit)? = null
) {

    onClick?.apply {
        TextButton(
            onClick = onClick,
        ) {

            if(leadingIcon != null) {
                SIIcon(
                    resId = leadingIcon,
                    tint = textColor
                )
                SIWidthSpace(value = 12)
            }

            Text(
                text = text,
                color = textColor.color(),
                fontWeight = fontWeight,
                fontStyle = fontStyle,
                fontSize = textSize.value.sp,
                textAlign = textAlign,
                maxLines = maxLines,
                modifier = modifier,
                style = textStyle
            )
        }
    } ?: run {
        Text(
            text = text,
            color = textColor.color(),
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontSize = textSize.value.sp,
            textAlign = textAlign,
            maxLines = maxLines,
            modifier = modifier,
            overflow = TextOverflow.Ellipsis,
            style = textStyle
        )
    }
}

@Composable
fun SIText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    textColor: AuroraColor,
    textSize: TextSize = TextSize.Regular,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start
) {

    Text(
        text = text,
        color = textColor.color(),
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontSize = textSize.value.sp,
        textAlign = textAlign,
        modifier = modifier
    )
}

//****************************************//
// LIGHT PREVIEW
//****************************************//

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Extra Small Text",
    group = "Light Theme"
)
@Composable
fun SITextLightPreviewExtraSmall() {
    AuroraLight {
        SIText(
            text = "Extra Small Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.ExtraSmall
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Small Text",
    group = "Light Theme"
)
@Composable
fun SITextLightPreviewSmall() {
    AuroraLight {
        SIText(
            text = "Small Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Small
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Regular Text",
    group = "Light Theme"
)
@Composable
fun SITextLightPreviewRegular() {
    AuroraLight {
        SIText(
            text = "Regular Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Regular
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Large Text",
    group = "Light Theme"
)
@Composable
fun SITextLightPreviewLarge() {
    AuroraLight {
        SIText(
            text = "Large Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Large
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Extra Large Text",
    group = "Light Theme"
)
@Composable
fun SITextLightPreviewExtraLarge() {
    AuroraLight {
        SIText(
            text = "Extra Large Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.ExtraLarge
        )
    }
}

//****************************************//
// DARK PREVIEW
//****************************************//

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Extra Small Text",
    group = "Dark Theme"
)
@Composable
fun SITextDarkPreviewExtraSmall() {
    AuroraDark {
        SIText(
            text = "Extra Small Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.ExtraSmall
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Small Text",
    group = "Dark Theme"
)
@Composable
fun SITextDarkPreviewSmall() {
    AuroraDark {
        SIText(
            text = "Small Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Small
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Regular Text",
    group = "Dark Theme"
)
@Composable
fun SITextDarkPreviewRegular() {
    AuroraDark {
        SIText(
            text = "Regular Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Regular
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Large Text",
    group = "Dark Theme"
)
@Composable
fun SITextDarkPreviewLarge() {
    AuroraDark {
        SIText(
            text = "Large Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.Large
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Extra Large Text",
    group = "Dark Theme"
)
@Composable
fun SITextDarkPreviewExtraLarge() {
    AuroraDark {
        SIText(
            text = "Extra Large Text",
            textColor = AuroraColor.Background,
            textSize = TextSize.ExtraLarge
        )
    }
}


