package com.iterduo.Finance.ITerduoFinance.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.common.ShareType
import kotlinx.android.synthetic.main.share_layout.*

/**
 * Created by WongKi on 2018/6/10.
 */
class ShareButtonsLayout : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context).inflate(R.layout.share_buttons, this)
        initView()
    }

    private fun initView() {
        findViewById<View>(R.id.share_action_wechat).setOnClickListener { v ->
            shareButtonOnClickListener?.run {
                onClick(v, ShareType.WECHAT)
            }
        }
        findViewById<View>(R.id.share_action_friend).setOnClickListener { v ->
            shareButtonOnClickListener?.run {
                onClick(v, ShareType.FRIENDS)
            }
        }
        findViewById<View>(R.id.share_action_qq).setOnClickListener { v ->
            shareButtonOnClickListener?.run {
                onClick(v, ShareType.QQ)
            }
        }
        findViewById<View>(R.id.share_action_download).setOnClickListener { v ->
            shareButtonOnClickListener?.run {
                onClick(v, ShareType.DOWNLOAD)
            }
        }
        findViewById<View>(R.id.share_action_close).setOnClickListener { v ->
            shareButtonOnClickListener?.run {
                onClick(v, ShareType.CLOSE)
            }
        }
    }

    var shareButtonOnClickListener: ShareButtonOnClickListener? = null

    interface ShareButtonOnClickListener {
        fun onClick(view: View, type: ShareType)
    }
}