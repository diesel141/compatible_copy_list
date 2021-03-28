package jp.co.compatible_copy_list.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import jp.co.compatible_copy_list.R
import jp.co.compatible_copy_list.extention.dp
import jp.co.compatible_copy_list.schema.CopyTextConstants.PREFIX_DIRECTORY
import jp.co.compatible_copy_list.schema.CopyTextConstants.PREFIX_EMPHASIS_CONTAINS
import jp.co.compatible_copy_list.schema.CopyTextConstants.PREFIX_RED_CONTAINS
import jp.co.compatible_copy_list.util.CopyTextUtil
import jp.co.compatible_copy_list.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_copy_list.*


class CopyListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy_list)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        toConvertCopyText()
        to_text.setOnClickListener {
            startActivity(Intent(this, CopyTextActivity::class.java))
        }
    }

    private fun toConvertCopyText() {
        copyText = SharedPreferencesUtil(applicationContext).loadCopyContains()
        if (copyText.isNotEmpty() && copyText.isNotBlank()) {
            var indent = 0
            var directoryIndex = 0
            var oldRootChildView: LinearLayout? = null
            var directoryMap = hashMapOf<String, LinearLayout>()
            for (record in copyText.split("\n")) {

                if (record.isEmpty() || record.isBlank()) return
                if (record.startsWith(PREFIX_DIRECTORY)) {
                    indent = 0
                    directoryIndex = 0
                    val directoryArray = record.substring(1, record.length).split(PREFIX_DIRECTORY)
                    var directoryStr = ""
                    for (directory in directoryArray) {
                        directoryStr += directory
                        var rootDirectory: LinearLayout =
                            layoutInflater.inflate(R.layout.layout_directory, null) as LinearLayout

                        val directoryName =
                            rootDirectory.findViewById<TextView>(R.id.directory_name)
                        directoryName.text = directory

                        rootDirectory.findViewById<View>(R.id.btn_close_open).setOnClickListener {
                            val childView =
                                rootDirectory.findViewById<LinearLayout>(R.id.directory_child)

                            if (childView.visibility == View.GONE) {
                                childView.visibility = View.VISIBLE
                                rootDirectory.findViewById<TextView>(R.id.icon_close_open)
                                    .setBackgroundResource(R.drawable.icon_close)
                            } else {
                                childView.visibility = View.GONE
                                rootDirectory.findViewById<TextView>(R.id.icon_close_open)
                                    .setBackgroundResource(R.drawable.icon_open)
                            }
                        }
                        if (directoryIndex == 0) {
//                            if (directoryMap.containsKey(directoryStr)) { TODO
//                                directoryMap.get(directoryStr)?.addView(
//                                    rootDirectory
//                                )
//                            } else {
                                findViewById<LinearLayout>(R.id.copy_contains_list).addView(
                                    rootDirectory
                                )
//                            }
                        } else {
                            indent += 10
                            val mlp = directoryName.layoutParams as ViewGroup.MarginLayoutParams
                            mlp.setMargins(
                                indent.dp,
                                mlp.topMargin,
                                mlp.rightMargin,
                                mlp.bottomMargin
                            )
                            directoryName.layoutParams = mlp
                            oldRootChildView?.findViewById<LinearLayout>(R.id.directory_child)
                                ?.apply {
                                    visibility = View.GONE
                                    addView(
                                        rootDirectory
                                    )
                                }
                        }
                        directoryMap[directoryStr] = rootDirectory
                        oldRootChildView = rootDirectory
                        directoryIndex++
                    }
                } else {
                    layoutContains(
                        directoryIndex,
                        oldRootChildView ?: layoutInflater.inflate(
                            R.layout.layout_directory,
                            null
                        ) as LinearLayout,
                        record
                    )
                }
            }
        }
    }

    private fun layoutContains(
        directoryIndex: Int,
        rootDirectory: LinearLayout,
        record: String
    ) {
        if (record.isEmpty() || record.isBlank()) return
        val containsView: View =
            layoutInflater.inflate(R.layout.layout_containts, null)

        val recordView = containsView.findViewById<TextView>(R.id.contains_text)

        if (record.startsWith(PREFIX_EMPHASIS_CONTAINS)) {
            recordView.typeface = Typeface.DEFAULT_BOLD
            recordView.text = record.substring(1, record.length)

        } else if (record.startsWith(PREFIX_RED_CONTAINS)) {
            recordView.setTextColor(ContextCompat.getColor(this, R.color.red))
            recordView.text = record.substring(1, record.length)
        } else {
            recordView.text = record
        }
        containsView.findViewById<View>(R.id.to_clip).setOnClickListener {
            CopyTextUtil().copyToClipboard(this, recordView.text.toString())
            Toast.makeText(this, getString(R.string.text_copied), Toast.LENGTH_SHORT).show();
        }
        if (directoryIndex == 0) {
            findViewById<LinearLayout>(R.id.copy_contains_list).addView(
                containsView
            )
        } else {
            rootDirectory.findViewById<LinearLayout>(R.id.directory_child).apply {
                addView(containsView)
                visibility = View.GONE
            }
        }
    }
}
