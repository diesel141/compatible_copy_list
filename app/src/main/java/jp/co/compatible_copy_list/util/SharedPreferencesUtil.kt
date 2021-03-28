package jp.co.compatible_copy_list.util

import android.content.Context
import jp.co.compatible_copy_list.extention.string

class SharedPreferencesUtil (private val context: Context) {

    private val preferences = context.getSharedPreferences("copy_data", Context.MODE_PRIVATE)

    // コピーコンテンツ
    private var copyContains: String by preferences.string()

    fun saveCopyContains(contains: String) {
        copyContains = contains
    }
    fun loadCopyContains() = copyContains
}
