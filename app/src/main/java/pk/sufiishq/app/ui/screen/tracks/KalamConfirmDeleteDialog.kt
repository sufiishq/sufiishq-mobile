package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.providers.KalamDataProvider
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem

@PackagePrivate
@Composable
fun KalamConfirmDeleteDialog(
    kalamDataProvider: KalamDataProvider,
) {

    kalamDataProvider
        .showKalamConfirmDeleteDialog()
        .observeAsState()
        .value
        ?.apply {
            val param = rem<ConfirmDialogParam?>(null)
            param.value = ConfirmDialogParam(
                message = optString(R.string.dynamic_confirm_delete_kalam, kalam.title),
                onConfirmed = {
                    kalamDataProvider.delete(this@apply)
                },
                onDismissed = {
                    kalamDataProvider.showKalamConfirmDeleteDialog(null)
                }
            )
            ConfirmationDialog(state = param)
        }
}