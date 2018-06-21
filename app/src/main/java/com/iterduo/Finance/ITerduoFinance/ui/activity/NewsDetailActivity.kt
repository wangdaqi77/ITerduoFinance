package com.iterduo.Finance.ITerduoFinance.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.webkit.WebView
import android.webkit.WebViewClient
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseActivity
import com.iterduo.Finance.ITerduoFinance.utils.CleanLeakUtils
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import kotlinx.android.synthetic.main.activity_news_detail.*
import java.util.*

/**
 * Created by xuhao on 2018/6/9.
 * desc: 个人主页
 */

class NewsDetailActivity : BaseActivity() {

    companion object {
        private const val EXT_URL = "ext_url"
        fun start(context: Context?, url: String?) {
            if (context == null) return
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(EXT_URL, url)
            if (context is Activity) {
                context.startActivity(intent)
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.applicationContext.startActivity(intent)
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_news_detail

    private var mUrl: String? = null
    override fun getExtData() {
        mUrl = intent.getStringExtra(EXT_URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

        //返回
        toolbar.setNavigationOnClickListener { finish() }


        refreshLayout.setOnRefreshListener { mWebView.loadUrl(mUrl) }
        refreshLayout.autoRefresh()

        mWebView.settings.javaScriptEnabled = true
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                refreshLayout.finishRefresh()
                view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", DensityUtil.px2dp(mWebView.paddingTop.toFloat())))
            }
        }

    }

    override fun loadData() {

    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
    }
}
