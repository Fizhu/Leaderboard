package com.fizhu.leaderboard.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.fizhu.leaderboard.adapters.PointDialogAdapter
import com.fizhu.leaderboard.data.models.Point
import com.fizhu.leaderboard.databinding.DialogPointBinding

/**
 * Created by fizhu on 05,January,2021
 * https://github.com/Fizhu
 */

class PointDialog(
    private val activity: AppCompatActivity,
    private val callBack: (point: Point) -> Unit
) : Dialog(activity) {

    private lateinit var binding: DialogPointBinding
    private lateinit var pointDialogAdapter: PointDialogAdapter
    private val listPoint = mutableListOf<Point>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPointBinding.inflate(layoutInflater)
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
        pointDialogAdapter = PointDialogAdapter {
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
                adapter = pointDialogAdapter
            }
            pointDialogAdapter.setData(listPoint)
        }
    }

    fun setList(list: List<Point>) {
        listPoint.addAll(list)
    }


    override fun onBackPressed() {
        this.dismiss()
    }
}