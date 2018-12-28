package khsu.org.coal

    import android.content.Context
    import android.support.v7.app.AlertDialog
    import android.widget.EditText
    import android.widget.TextView
    import kotlinx.android.synthetic.main.activity_main.*

fun createDialog(
    context: Context,
    options: Array<String>,
    title: String,
    selectedIndex: Int,
    onSelectCallback: Callback,
    onPositiveCallback: Callback
): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setSingleChoiceItems(options, selectedIndex, onSelectCallback)
    builder.setPositiveButton("Ок", onPositiveCallback)
    return builder.create()
}

fun createDialogText(
    context: Context,
    title: String,
    textView: TextView,
    placeholder: String,
    onPositiveCallback: Callback? = null
): AlertDialog {
    val editText = EditText(context)
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setView(editText)
    builder.setPositiveButton("Ок") { dialogInterface, int ->
        textView.text = placeholder + editText.text
        onPositiveCallback?.invoke(dialogInterface, int)
    }
    return builder.create()
}