package pk.sufiishq.aurora.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pk.sufiishq.aurora.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraLight

enum class BadgeStyle(val elevation: Dp) {
    Flat(0.dp), Elevated(1.dp)
}

enum class BadgeShape(val size: Dp) {
    Round(6.dp), Square(0.dp)
}

enum class BadgeType(val bgColor: Color) {
    Primary(Color(0xFF252525)),
    Info(Color(0xFF34A2C2)),
    Success(Color(0xFF8CC34D)),
    Warning(Color(0xFFF0AE50)),
    Danger(Color(0xFFB10823))
}

@Composable
fun SIBadge(
    text: String,
    textSize: TextSize = TextSize.Small,
    badgeShape: BadgeShape = BadgeShape.Round,
    badgeStyle: BadgeStyle = BadgeStyle.Flat,
    badgeType: BadgeType = BadgeType.Info,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = badgeType.bgColor,
        shape = RoundedCornerShape(badgeShape.size),
        elevation = badgeStyle.elevation
    ) {
        SIText(
            text = text,
            textColor = AuroraColor.White,
            textSize = textSize,
            modifier = Modifier.padding(8.dp, 2.dp)
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun PrimaryBadgeWithFlatAndRound() {
    AuroraLight {
        SIBadge(
            text = "PRIMARY",
            badgeType = BadgeType.Primary,
            badgeShape = BadgeShape.Round,
            badgeStyle = BadgeStyle.Flat
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun InfoBadgeWithFlatAndRound() {
    AuroraLight {
        SIBadge(
            text = "INFO",
            badgeType = BadgeType.Info,
            badgeShape = BadgeShape.Round,
            badgeStyle = BadgeStyle.Flat
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun SuccessBadgeWithFlatAndRound() {
    AuroraLight {
        SIBadge(
            text = "SUCCESS",
            badgeType = BadgeType.Success,
            badgeShape = BadgeShape.Round,
            badgeStyle = BadgeStyle.Flat
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun WarningBadgeWithFlatAndRound() {
    AuroraLight {
        SIBadge(
            text = "WARNING",
            badgeType = BadgeType.Warning,
            badgeShape = BadgeShape.Round,
            badgeStyle = BadgeStyle.Flat
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
fun DangerBadgeWithFlatAndRound() {
    AuroraLight {
        SIBadge(
            text = "DANGER",
            badgeType = BadgeType.Danger,
            badgeShape = BadgeShape.Round,
            badgeStyle = BadgeStyle.Flat
        )
    }
}