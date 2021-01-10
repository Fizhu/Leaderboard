package com.fizhu.leaderboard.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.fizhu.leaderboard.databinding.DialogRoundBinding

/**
 * Created by fizhu on 05,January,2021
 * https://github.com/Fizhu
 */

class RoundDialog(
    private val activity: AppCompatActivity,
    private val callBack: () -> Unit
) : Dialog(activity) {

    private var title: String? = null
    private var desc: String? = null

    private lateinit var binding: DialogRoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        onInit()
    }

    private fun onInit() {
        with(binding) {
            btnCancel.setOnClickListener { this@RoundDialog.dismiss() }
            btnConfirm.setOnClickListener {
                callBack.invoke()
                this@RoundDialog.dismiss()
            }
            this@RoundDialog.title?.let {
                title.text = it
            }

            this@RoundDialog.desc?.let {
                tvDesc.text = it
            }
        }
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun setDesc(desc: String?) {
        this.desc = desc
    }

    override fun onBackPressed() {
        this.dismiss()
    }
}