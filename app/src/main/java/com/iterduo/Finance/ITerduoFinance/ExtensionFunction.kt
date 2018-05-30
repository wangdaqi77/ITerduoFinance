package com.a91power.a91pos.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.iterduo.Finance.ITerduoFinance.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshContent

/**
 * @author:wq
 * @date:2018/4/26
 * @email:wangqi7676@163.com
 * @desc: 拓展函数
 */


/**
 * 找到某个View
 */
fun <T> ViewGroup.find(calzz: Class<T>): T? {
    val count = childCount
    for (i in 0..count) {
        val view = getChildAt(i)
        if (view.javaClass.name == calzz.name) return view as T
        if (view is ViewGroup) {
            view.find(calzz)
        }
    }
    return null
}

/**
 * 有无更多数据 UI控制
 */
fun SmartRefreshLayout.setNoMore(noMore: Boolean) {
    isEnableLoadmore = !noMore
    val declaredField = javaClass.getDeclaredField("mRefreshContent")
    declaredField.isAccessible = true
    val refreshContent: RefreshContent = declaredField.get(this) as RefreshContent
    val contentView = refreshContent.view

    when (contentView) {
        is RecyclerView -> {
            contentView.setNoMore(noMore)
        }
        is ViewGroup -> {
            val recyclerView = contentView.find(RecyclerView::class.java)
            recyclerView?.setNoMore(noMore)
        }
    }
}

fun RecyclerView.setNoMore(noMore: Boolean) {

    if (adapter is BaseQuickAdapter<*, *>) {
        if (noMore) {
            val view = View.inflate(context, R.layout.common_footer_no_more, null)
            (adapter as BaseQuickAdapter<*, *>).setFooterView(view)
        } else {
            (adapter as BaseQuickAdapter<*, *>).removeAllFooterView()
        }
    }
}


fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, desc: CharSequence, iconId: Int, show: Boolean) {
    if (show) {
        if (context == null) return
        var view: View
        if (iconId == -1) {
            // 不带图标
            view = View.inflate(context, R.layout.common_empty_view, null)
        } else {
            // 带图标
            view = View.inflate(context, R.layout.common_empty_view_icon, null)
            //view.findViewById<ImageView>(R.id.iv_icon).setImageResource(iconId)
        }
        view.findViewById<TextView>(R.id.tv_desc).text = desc
        emptyView = view
    } else {
        dismissEmptyView()
    }
}

fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.dismissEmptyView() {
    if (emptyView != null) {
        (emptyView as ViewGroup).removeAllViews()
    }
}

/**
 * 在setNewData之后调用
 */
fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, desc: CharSequence, iconId: Int) {
    showEmptyView(context, desc, iconId, data == null || data.isEmpty())
}

/**
 * 在setNewData之后调用
 */
fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, desc: CharSequence) {
    showEmptyView(context, desc, -1)
}


/**
 * 如果为空显示默认指定字符串
 */
fun TextView.setTextForDef(str: String?, defStr: String?) {
    var text = str
    if (text.isNullOrEmpty()) {
        text = defStr ?: "-"
    }
    this.text = text
}

fun TextView.setTextForDef(str: String?) {
    setTextForDef(str, null)
}

fun BaseViewHolder.setTextForDef(tvId: Int, str: String?, defStr: String?) {
    getView<TextView>(tvId)?.setTextForDef(str, defStr)
}

fun BaseViewHolder.setTextForDef(tvId: Int, str: String?) {
    setTextForDef(tvId, str, null)
}

/**
 * 阅读量转换
 */
fun Long.toReadedStr(): String? {
    return when {
        this in 0..10000 -> "$this"
        else -> String.format("%.1f万", this / 10000f)
    }

}
