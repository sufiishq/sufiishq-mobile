package pk.sufiishq.app.ui.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIClickableText
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIIcon
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIConstraintLayout
import pk.sufiishq.aurora.layout.SIDialog
import pk.sufiishq.aurora.layout.SISurface
import pk.sufiishq.aurora.theme.AuroraColor
import pk.sufiishq.aurora.theme.AuroraDark
import pk.sufiishq.aurora.theme.AuroraLight

@Composable
fun AboutIconButton(
    onColor: AuroraColor
) {

    val showDialog = rem(false)

    SIIcon(
        resId = R.drawable.ic_outline_info_24,
        tint = onColor,
        onClick = {
            showDialog.value = true
        }
    )

    AboutDialog(showDialog = showDialog)
}

@Composable
private fun AboutDialog(showDialog: MutableState<Boolean>) {
    if (showDialog.value) {

        SIDialog(
            bgColor = AuroraColor.Transparent,
            borderColor = AuroraColor.Transparent,
            onDismissRequest = {
                showDialog.value = false
            }
        ) {

            SIConstraintLayout {

                val (icon, content) = createRefs()

                SISurface(
                    color = AuroraColor.Background,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.constrainAs(content) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 50.dp)
                        end.linkTo(parent.end)
                    }
                ) {
                    SIColumn(
                        padding = 12
                    ) { textColor ->

                        SIHeightSpace(value = 50)

                        SIBox(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) { textColor ->
                            SIText(
                                text = stringResource(id = R.string.app_name),
                                textColor = textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        SIHeightSpace(value = 12)

                        val linkColor = AuroraColor.SecondaryVariant.color()
                        val annotatedString = buildAnnotatedString {

                            withStyle(style = SpanStyle(color = textColor.color())) {

                                // creating a string to display in the Text
                                val mStr = "Please use Sufi Ishq facebook page for any "

                                // word and span to be hyperlinked
                                var mStartIndex = mStr.indexOf("Sufi Ishq")
                                var mEndIndex = mStartIndex + 9

                                append(mStr)
                                addStyle(
                                    style = SpanStyle(
                                        color = linkColor,
                                        textDecoration = TextDecoration.Underline
                                    ), start = mStartIndex, end = mEndIndex
                                )

                                // attach a string annotation that
                                // stores a URL to the text "link"
                                addStringAnnotation(
                                    tag = "FACEBOOK_PAGE",
                                    annotation = "https://www.facebook.com/sufiishq.pk",
                                    start = mStartIndex,
                                    end = mEndIndex
                                )

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Issues, Problems ")
                                }
                                append("or ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Suggestion ")
                                }

                                append("\n\n")

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Are you a Developer? \n")
                                }

                                append("You can contribute by joining the organization check the repository on Github ")

                                mStartIndex = toAnnotatedString().indexOf("Github")
                                mEndIndex = mStartIndex + 6

                                addStyle(
                                    style = SpanStyle(
                                        color = linkColor,
                                        textDecoration = TextDecoration.Underline
                                    ), start = mStartIndex, end = mEndIndex
                                )

                                addStringAnnotation(
                                    tag = "GITHUB_REPO",
                                    annotation = "https://github.com/sufiishq/sufiishq-mobile",
                                    start = mStartIndex,
                                    end = mEndIndex
                                )

                                append("join the Sufi Ishq slack workspace for developer conversation")

                                mStartIndex = toAnnotatedString().lastIndexOf("Sufi Ishq")
                                mEndIndex = mStartIndex + 9

                                addStyle(
                                    style = SpanStyle(
                                        color = linkColor,
                                        textDecoration = TextDecoration.Underline
                                    ), start = mStartIndex, end = mEndIndex
                                )

                                addStringAnnotation(
                                    tag = "SLACK_WORKSPACE",
                                    annotation = "https://join.slack.com/t/sufiishq/shared_invite/zt-1eu7kr4ap-1q7BSwHLOlco62l3ZUgJ7g",
                                    start = mStartIndex,
                                    end = mEndIndex
                                )

                            }
                        }

                        val mUriHandler = LocalUriHandler.current

                        SIClickableText(
                            text = annotatedString,
                            onClick = {
                                annotatedString
                                    .getStringAnnotations("FACEBOOK_PAGE", it, it)
                                    .firstOrNull()?.let { stringAnnotation ->
                                        mUriHandler.openUri(stringAnnotation.item)
                                    }

                                annotatedString
                                    .getStringAnnotations("GITHUB_REPO", it, it)
                                    .firstOrNull()?.let { stringAnnotation ->
                                        mUriHandler.openUri(stringAnnotation.item)
                                    }

                                annotatedString
                                    .getStringAnnotations("SLACK_WORKSPACE", it, it)
                                    .firstOrNull()?.let { stringAnnotation ->
                                        mUriHandler.openUri(stringAnnotation.item)
                                    }
                            }
                        )
                    }
                }

                SIBox(
                    bgColor = AuroraColor.Background,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .padding(3.dp)
                        .constrainAs(icon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    SIBox(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        SIImage(
                            resId = R.drawable.sarkar,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun LightPreviewAboutDialog() {
    AuroraLight {
        AboutDialog(showDialog = rem(true))
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun DarkPreviewAboutDialog() {
    AuroraDark {
        AboutDialog(showDialog = rem(true))
    }
}