package pk.sufiishq.aurora.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SIClickableText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    onClick: (Int) -> Unit
) {
    var textLayoutResult: TextLayoutResult? = remember {
        null
    }

    BasicText(
        text = text,
        modifier = modifier.pointerInput(text) {
            detectTapGestures { position ->

                textLayoutResult?.let { layoutResult ->

                    val offset = layoutResult.getOffsetForPosition(
                        position
                    )

                    onClick(offset)
                }
            }
        },
        style = style,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { layoutResult ->

            textLayoutResult = layoutResult
            onTextLayout(layoutResult)
        }
    )
}