package utils

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection


// copy to clipboard
fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
    val strSel = StringSelection(text)
    clipboard.setContents(strSel, null)
}


// get clipboard contents
fun getClipboardContents(): String {
    val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard()
    val contents = clipboard.getContents(null)
    var clipboardText = ""
    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        clipboardText = contents.getTransferData(DataFlavor.stringFlavor) as String
    }
    return clipboardText
}