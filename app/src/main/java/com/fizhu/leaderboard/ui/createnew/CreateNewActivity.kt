package com.fizhu.leaderboard.ui.createnew

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fizhu.leaderboard.R
import com.fizhu.leaderboard.databinding.ActivityCreateNewBinding
import com.fizhu.leaderboard.ui.addplayer.AddPlayerActivity
import com.fizhu.leaderboard.viewmodels.CreateNewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNewActivity : AppCompatActivity() {

    private val viewModel by viewModel<CreateNewViewModel>()
    private lateinit var binding: ActivityCreateNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new)
        binding.viewModel = this.viewModel
        binding.lifecycleOwner = this
        onInit()
    }

    private fun onInit() {
        with(binding) {
            toolbar.setNavigationOnClickListener { finish() }
            btnNext.setOnClickListener {
                if (this@CreateNewActivity.viewModel.getName() != "") {
                    tilName.isErrorEnabled = false
                    val i = Intent(this@CreateNewActivity, AddPlayerActivity::class.java)
                    i.putExtra("data", this@CreateNewActivity.viewModel.getName())
                    startActivity(i)
                } else {
                    tilName.isErrorEnabled = true
                    tilName.error = "Enter game name"
                }
            }
        }
    }
}