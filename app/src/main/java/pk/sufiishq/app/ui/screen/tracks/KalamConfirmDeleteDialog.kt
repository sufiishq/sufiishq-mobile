package pk.sufiishq.app.ui.screen.tracks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import io.github.esentsov.PackagePrivate
import pk.sufiishq.app.R
import pk.sufiishq.app.data.controller.KalamController
import pk.sufiishq.app.ui.components.dialogs.ConfirmDialogParam
import pk.sufiishq.app.ui.components.dialogs.ConfirmationDialog
import pk.sufiishq.app.utils.optString
import pk.sufiishq.app.utils.rem

@PackagePrivate
@Composable
fun KalamConfirmDeleteDialog(
    kalamController: KalamController,
) {

    kalamController
        .showKalamConfirmDeleteDialog()
        .observeAsState()
        .value
        ?.apply {
            val param = rem<ConfirmDialogParam?>(null)
            param.value = ConfirmDialogParam(
                message = optString(R.string.dynamic_confirm_delete_kalam, kalam.title),
                onConfirmed = {
                    kalamController.delete(this@apply)
                },
                onDismissed = {
                    kalamController.showKalamConfirmDeleteDialog(null)
                }
            )
            ConfirmationDialog(state = param)
        }
}