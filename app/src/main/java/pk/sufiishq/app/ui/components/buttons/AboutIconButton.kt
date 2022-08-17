package pk.sufiishq.app.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import pk.sufiishq.app.R
import pk.sufiishq.app.annotations.ExcludeFromJacocoGeneratedReport
import pk.sufiishq.app.ui.theme.SufiIshqTheme
import pk.sufiishq.app.utils.rem

@Composable
fun AboutIconButton() {

    val showDialog = rem(false)

    IconButton(onClick = {
        showDialog.value = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_outline_info_24),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }

    AboutDialog(showDialog = showDialog)
}

@Composable
private fun AboutDialog(
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {

        val matColors = MaterialTheme.colors

        Dialog(onDismissRequest = { showDialog.value = false }) {
            ConstraintLayout {

                val (icon, content) = createRefs()

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = matColors.background,
                    modifier = Modifier.constrainAs(content) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 50.dp)
                        end.linkTo(parent.end)
                    }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Spacer(modifier = Modifier.height(50.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val annotatedString = buildAnnotatedString {

                            withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {

                                // creating a string to display in the Text
                                val mStr = "Please use SufiIshq facebook page for any "

                                // word and span to be hyperlinked
                                var mStartIndex = mStr.indexOf("SufiIshq")
                                var mEndIndex = mStartIndex + 8

                                append(mStr)
                                addStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.secondary,
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

                                append("You can contribute by joining the organization check the repository on Github")

                                mStartIndex = toAnnotatedString().indexOf("Github")
                                mEndIndex = mStartIndex + 6

                                addStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.secondary,
                                        textDecoration = TextDecoration.Underline
                                    ), start = mStartIndex, end = mEndIndex
                                )

                                addStringAnnotation(
                                    tag = "GITHUB_REPO",
                                    annotation = "https://github.com/sufiishq/sufiishq-mobile",
                                    start = mStartIndex,
                                    end = mEndIndex
                                )
                            }
                        }

                        val mUriHandler = LocalUriHandler.current

                        ClickableText(
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
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(matColors.background)
                        .padding(3.dp)
                        .constrainAs(icon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sarkar),
                            contentDescription = null,
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
    SufiIshqTheme {
        AboutDialog(showDialog = rem(true))
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(showBackground = true)
@Composable
fun DarkPreviewAboutDialog() {
    SufiIshqTheme(darkTheme = true) {
        AboutDialog(showDialog = rem(true))
    }
}