package pk.sufiishq.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pk.sufiishq.app.R
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

    if (showDialog.value) {
        SufiIshqDialog {
            Text(
                text = stringResource(id = R.string.about_title),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val annotatedString = buildAnnotatedString {

                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {

                    // creating a string to display in the Text
                    val mStr = "Please use SufiIshq facebook page for any "

                    // word and span to be hyperlinked
                    val mStartIndex = mStr.indexOf("SufiIshq")
                    val mEndIndex = mStartIndex + 8

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
                        tag = "URL",
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
                }
            }

            val mUriHandler = LocalUriHandler.current

            ClickableText(
                text = annotatedString,
                onClick = {
                    annotatedString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            mUriHandler.openUri(stringAnnotation.item)
                        }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    showDialog.value = false
                }) {
                    Text(text = "OK")
                }
            }
        }
    }
}