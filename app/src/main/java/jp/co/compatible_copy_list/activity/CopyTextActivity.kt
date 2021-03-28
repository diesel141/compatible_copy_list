package jp.co.compatible_copy_list.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import jp.co.compatible_copy_list.R
import jp.co.compatible_copy_list.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_copy_text.*

class CopyTextActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy_text)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        copyText = SharedPreferencesUtil(applicationContext).loadCopyContains()
        if (copyText.isNotEmpty() && copyText.isNotBlank()) {
            input_text.setText(copyText)
        }

        to_copy_list.setOnClickListener {
            transitionCopyList()
        }
        apply_text.setOnClickListener {
            transitionCopyList()
        }
    }

    override fun onPause() {
        super.onPause()
        SharedPreferencesUtil(applicationContext).saveCopyContains(input_text.text.toString())
    }

    private fun transitionCopyList() {
        startActivity(
            Intent(
                this,
                CopyListActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
