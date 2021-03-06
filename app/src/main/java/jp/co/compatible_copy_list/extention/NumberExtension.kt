package jp.co.compatible_copy_list.extention

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.applyDimension

val Float.dp
    get() = applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
val Int.dp get() = toFloat().dp.toInt()