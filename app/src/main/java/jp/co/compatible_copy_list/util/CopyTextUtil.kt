package jp.co.compatible_copy_list.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * テキストコピー機能ユーティリティ
 */
class CopyTextUtil {

    /**
     * 引数の文言をクリップボードにコピーします
     * @param context Context
     * @param text 対象文言
     * @return result
     */
    fun copyToClipboard(
        context: Context,
        text: String?
    ) {
        val clipboardManager: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", text))
    }
}
