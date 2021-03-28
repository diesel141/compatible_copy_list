package jp.co.compatible_copy_list.extention

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * String Read Write Delegate
 */
fun SharedPreferences.string(
    defaultValue: String = "",
    key: String? = null
): ReadWriteProperty<Any, String> =
    delegate(defaultValue, key, SharedPreferences::getString, SharedPreferences.Editor::putString)

private inline fun <T : Any> SharedPreferences.delegate(
    defaultValue: T, key: String?,
    crossinline getter: SharedPreferences.(key: String, defaultValue: T) -> T?,
    crossinline setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor
) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>) =
        getter(key ?: property.name, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
        edit().setter(key ?: property.name, value).apply()
}
