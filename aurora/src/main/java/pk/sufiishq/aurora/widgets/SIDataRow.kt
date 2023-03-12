package pk.sufiishq.aurora.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.getForegroundColor

@Composable
fun SIDataRow(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Background,
    fgColor: AuroraColor = bgColor.getForegroundColor(bgColor.color()),
    @DrawableRes leadingIcon: Int? = null,
    leadingIconColor: AuroraColor? = null,
    leadingIconScope: @Composable BoxScope.() -> Unit = {},
    onLeadingIconClick: (() -> Unit)? = null,
    @DrawableRes trailingIcon: Int? = null,
    trailingIconColor: AuroraColor? = null,
    trailingIconScope: @Composable BoxScope.() -> Unit = {},
    onTrailingIconClick: (() -> Unit)? = null,
    onClick: () -> Unit = {},
    rowHeight: Int = 60,
    title: String,
    subTitle: String? = null
) {
    SIDataRow(
        modifier = modifier,
        bgColor = bgColor,
        fgColor = fgColor,
        leadingIcon = leadingIcon,
        leadingIconColor = leadingIconColor,
        leadingIconScope = leadingIconScope,
        onLeadingIconClick = onLeadingIconClick,
        trailingIcon = trailingIcon,
        trailingIconColor = trailingIconColor,
        trailingIconScope = trailingIconScope,
        onTrailingIconClick = onTrailingIconClick,
        onClick = onClick,
        rowHeight = rowHeight
    ) { textColor ->
        SIColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            SIText(
                text = title,
                textColor = textColor,
                textSize = TextSize.Regular,
                maxLines = 1
            )

            subTitle?.run {
                SIText(
                    text = subTitle,
                    textColor = textColor,
                    textSize = TextSize.Small
                )
            }
        }
    }
}

@Composable
fun SIDataRow(
    modifier: Modifier = Modifier,
    bgColor: AuroraColor = AuroraColor.Background,
    fgColor: AuroraColor = bgColor.getForegroundColor(bgColor.color()),
    @DrawableRes leadingIcon: Int? = null,
    leadingIconColor: AuroraColor? = null,
    leadingIconScope: @Composable BoxScope.() -> Unit = {},
    onLeadingIconClick: (() -> Unit)? = null,
    @DrawableRes trailingIcon: Int? = null,
    trailingIconColor: AuroraColor? = null,
    trailingIconScope: @Composable BoxScope.() -> Unit = {},
    onTrailingIconClick: (() -> Unit)? = null,
    onClick: () -> Unit = {},
    rowHeight: Int = 60,
    content: @Composable RowScope.(fgColor: AuroraColor) -> Unit
) {
    SIConstraintLayout(
        modifier = modifier
            .height(rowHeight.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable {
                onClick.invoke()
            },
        bgColor = AuroraColor.Background
    ) {

        val (leadingRefs, contentRefs, trailingRefs) = createRefs()

        SIBox(
            modifier = Modifier.constrainAs(leadingRefs) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            padding = 12
        ) {

            if (leadingIcon != null) {
                SIIcon(
                    modifier = Modifier.size(24.dp),
                    resId = leadingIcon,
                    tint = leadingIconColor,
                    onClick = onLeadingIconClick
                )

                leadingIconScope(this@SIBox)
            }
        }

        SIRow(
            modifier = Modifier
                .constrainAs(contentRefs) {
                    start.linkTo(leadingRefs.end)
                    top.linkTo(parent.top)
                    end.linkTo(trailingRefs.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            content(fgColor)
        }

        SIBox(
            modifier = Modifier.constrainAs(trailingRefs) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            padding = 8
        ) {

            if (trailingIcon != null) {
                SIIcon(
                    resId = trailingIcon,
                    tint = trailingIconColor,
                    onClick = onTrailingIconClick
                )

                trailingIconScope(this@SIBox)
            }
        }
    }
}