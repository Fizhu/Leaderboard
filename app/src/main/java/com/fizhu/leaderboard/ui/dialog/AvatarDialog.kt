package com.fizhu.leaderboard.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fizhu.leaderboard.adapters.AvatarAdapter
import com.fizhu.leaderboard.databinding.DialogAvatarBinding

/**
 * Created by fizhu on 05,January,2021
 * https://github.com/Fizhu
 */

class AvatarDialog(
    private val activity: AppCompatActivity,
    private val callBack: (avatar: String) -> Unit
) : Dialog(activity) {

    private lateinit var binding: DialogAvatarBinding
    private lateinit var avatarAdapter: AvatarAdapter
    private val listAvatar = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAvatarBinding.inflate(layoutInflater)
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
        avatarAdapter = AvatarAdapter {
            callBack.invoke(it)
            this.dismiss()
        }
        with(binding) {
            rv.apply {
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(
                        activity,
                        3,
                        GridLayoutManager.VERTICAL,
                        false
                    )
                adapter = avatarAdapter
            }
            avatarAdapter.setData(listAvatar)
        }
    }

    fun setList(list: List<String>) {
        listAvatar.addAll(list)
    }


    override fun onBackPressed() {
        this.dismiss()
    }
}