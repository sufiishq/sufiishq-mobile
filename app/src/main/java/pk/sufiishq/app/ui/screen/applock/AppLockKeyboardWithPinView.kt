package pk.sufiishq.app.ui.screen.applock

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import io.github.esentsov.PackagePrivate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pk.sufiishq.app.R
import pk.sufiishq.app.utils.rem
import pk.sufiishq.aurora.components.SIHeightSpace
import pk.sufiishq.aurora.components.SIImage
import pk.sufiishq.aurora.components.SIText
import pk.sufiishq.aurora.components.SIWidthSpace
import pk.sufiishq.aurora.components.TextSize
import pk.sufiishq.aurora.layout.SIBox
import pk.sufiishq.aurora.layout.SIColumn
import pk.sufiishq.aurora.layout.SIRow
import pk.sufiishq.aurora.theme.AuroraColor

@Composable
fun AppLockKeyboardWithPinView(
    modifier: Modifier = Modifier,
    onPinGenerated: suspend CoroutineScope.(pin: String) -> Unit,
    validPin: String? = null
) {

    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val offsetX = remember { Animatable(0f) }
    val pin = rem("")

    val indicatorColors = MutableList(4) { AuroraColor.Disabled }

    for (i in 1..pin.value.length) {
        indicatorColors[i - 1] = AuroraColor.Secondary
    }

    if (pin.value.length == 4) {
        if (validPin != null && pin.value != validPin) {
            for (i in 0..3) {
                indicatorColors[i] = AuroraColor.OnError
            }
            LaunchedEffect(Unit) {
                animateView(offsetX, coroutineScope, view)
                delay(400)
                pin.value = ""
            }
        } else {
            LaunchedEffect(Unit) {
                onPinGenerated(this, pin.value)
            }
        }
    }


    SIColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SIRow(
            modifier = Modifier
                .fillMaxWidth()
                .offset(offsetX.value.dp, 0.dp),
            horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally)
        ) {
            PinIndicatorView(bgColor = indicatorColors[0])
            SIWidthSpace(value = 8)
            PinIndicatorView(bgColor = indicatorColors[1])
            SIWidthSpace(value = 8)
            PinIndicatorView(bgColor = indicatorColors[2])
            SIWidthSpace(value = 8)
            PinIndicatorView(bgColor = indicatorColors[3])
        }
        SIHeightSpace(value = 40)
        SIRow(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyboardButton(label = "1", pin = pin)
            KeyboardButton(label = "2", pin = pin)
            KeyboardButton(label = "3", pin = pin)
        }
        SIHeightSpace(value = 16)
        SIRow(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyboardButton(label = "4", pin = pin)
            KeyboardButton(label = "5", pin = pin)
            KeyboardButton(label = "6", pin = pin)
        }
        SIHeightSpace(value = 16)
        SIRow(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyboardButton(label = "7", pin = pin)
            KeyboardButton(label = "8", pin = pin)
            KeyboardButton(label = "9", pin = pin)
        }
        SIHeightSpace(value = 16)
        SIRow(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyboardButton(label = "", pin = pin, bgColor = AuroraColor.Transparent)
            KeyboardButton(label = "0", pin = pin)
            KeyboardIconButton(resId = R.drawable.round_backspace_24, onClick = {
                if (pin.value.isNotEmpty()) {
                    pin.value = pin.value.substring(0, pin.value.length - 1)
                }
            })
        }
    }
}


@Composable
private fun PinIndicatorView(
    bgColor: AuroraColor
) {
    SIBox(
        modifier = Modifier
            .size(14.dp)
            .clip(RoundedCornerShape(30))
            .background(bgColor.color()),
        content = {}
    )
}

@Composable
private fun KeyboardButton(
    label: String,
    pin: MutableState<String>,
    bgColor: AuroraColor = AuroraColor.Background
) {

    var clickableModifier: Modifier = Modifier

    if (label.isNotEmpty()) {
        clickableModifier = Modifier.clickable {
            if (pin.value.length < 4) {
                pin.value = "${pin.value}$label"
            }
        }
    }

    SIBox(
        bgColor = bgColor,
        modifier = Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(30))
            .then(clickableModifier)
    ) {
        SIText(
            text = label,
            textColor = it,
            textSize = TextSize.Large
        )
    }
}

@Composable
private fun KeyboardIconButton(
    @DrawableRes resId: Int,
    onClick: () -> Unit
) {

    SIBox(
        modifier = Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(30))
            .clickable {
                onClick()
            }
    ) {
        SIImage(
            resId = resId,
            tintColor = it,
        )
    }
}

private val shakeKeyframes: AnimationSpec<Float> = keyframes {
    durationMillis = 500
    val easing = FastOutLinearInEasing

    // generate 8 keyframes
    for (i in 1..8) {
        val x = when (i % 3) {
            0 -> 4f
            1 -> -4f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}

private fun animateView(
    offset: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope,
    view: View? = null,
) {
    coroutineScope.launch {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = shakeKeyframes,
        )
    }
    view?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }
}