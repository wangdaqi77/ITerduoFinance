package com.iterduo.Finance.ITerduoFinance.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.iterduo.Finance.ITerduoFinance.MyApplication
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseActivity
import com.iterduo.Finance.ITerduoFinance.utils.AppUtils
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by xuhao on 2017/12/6.
 * desc: 关于
 */
class AboutActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_about

    override fun getExtData() {
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

        tv_version_name.text ="v${AppUtils.getVerName(MyApplication.context)}"
        //返回
        toolbar.setNavigationOnClickListener { finish() }
        //访问 GitHub
        relayout_gitHub.setOnClickListener {
            val uri = Uri.parse("https://github.com/git-xuhao/KotlinMvp")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun loadData() {

    }
}