package com.codersworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codersworld.rklib.progressDialog.Constant
import com.codersworld.rklib.progressDialog.RKProgress

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mRKProgress =RKProgress(this@MainActivity);
        mRKProgress.setIconSide(Constant.CENTER)
        mRKProgress.isIconShow(true)
        mRKProgress.isCancelable(true)
         mRKProgress.showProgressDialog();
    }
}