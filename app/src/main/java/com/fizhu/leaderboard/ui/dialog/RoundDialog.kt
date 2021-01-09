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
        }
    }

    override fun onBackPressed() {
        this.dismiss()
    }
}