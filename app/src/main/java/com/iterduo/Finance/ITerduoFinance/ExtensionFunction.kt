package com.a91power.a91pos.common

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
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
fun SmartRefreshLayout.setNoMore(noMore: Boolean, @LayoutRes layoutId: Int = R.layout.no_more_list) {
    isEnableLoadmore = !noMore
    val declaredField = javaClass.getDeclaredField("mRefreshContent")
    declaredField.isAccessible = true
    val refreshContent: RefreshContent = declaredField.get(this) as RefreshContent
    val contentView = refreshContent.view

    when (contentView) {
        is RecyclerView -> {
            contentView.setNoMore(noMore, layoutId)
        }
        is ViewGroup -> {
            val recyclerView = contentView.find(RecyclerView::class.java)
            recyclerView?.setNoMore(noMore, layoutId)
        }
    }
}

fun RecyclerView.setNoMore(noMore: Boolean, @LayoutRes layoutId: Int = R.layout.no_more_list) {

    if (adapter is BaseQuickAdapter<*, *>) {
        (adapter as BaseQuickAdapter<*, *>).setNoMore(context, noMore, layoutId)
    }
}

fun <T, H : BaseViewHolder> BaseQuickAdapter<T, H>.setNoMore(context: Context, noMore: Boolean, @LayoutRes layoutId: Int = R.layout.no_more_list) {
    if (noMore) {
        val view = View.inflate(context, layoutId, null)
        setFooterView(view)
    } else {
        removeAllFooterView()
    }
}

///**
// * 在setNewData之后调用
// */
//fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, @LayoutRes layoutId: Int) {
//    showEmptyView(context, layoutId, null)
//}
//
///**
// * 在setNewData之后调用
// */
//fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, @LayoutRes layoutId: Int, desc: CharSequence?) {
//    showEmptyView(context, layoutId, desc, null)
//}
//
///**
// * 在setNewData之后调用
// */
//fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, @LayoutRes layoutId: Int, desc: CharSequence?, @DrawableRes iconId: Int?) {
//    showEmptyView(context, layoutId, desc, iconId, data == null || data.isEmpty())
//}
//
///**
// * @param desc 描述 不设置置为null         xml ID 必须为empty_tv_desc
// * @param iconId 图片id 不设置置为null     xml ID 必须为empty_iv_icon
// */
//fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.showEmptyView(context: Context?, @LayoutRes layoutId: Int, desc: CharSequence?, @DrawableRes iconId: Int?, show: Boolean) {
//    if (show) {
//        if (context == null) return
//        val view = View.inflate(context, layoutId, null)
//        emptyView = view?.apply {
//            if (iconId != null) {
//                view.findViewById<ImageView>(R.id.empty_iv_icon)?.setImageResource(iconId)
//            }
//            if (desc != null) {
//                view.findViewById<TextView>(R.id.empty_tv_desc)?.text = desc
//            }
//        }
//    } else {
//        dismissEmptyView()
//    }
//}
//
//fun <T, K : BaseViewHolder?> BaseQuickAdapter<T, K>.dismissEmptyView() {
//    if (emptyView != null) {
//        (emptyView as ViewGroup).removeAllViews()
//    }
//}


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

fun View.attachToScrollView() {
    if (parent is ScrollView) return
    viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (parent == null) return
            viewTreeObserver?.removeOnGlobalLayoutListener(this)
            if (parent is ScrollView) return
            val parentGroup = parent as ViewGroup

            val newLayoutParams = ViewGroup.MarginLayoutParams(layoutParams.width, layoutParams.height)
            val scrollView = ScrollView(context)
            val indexOfChild = parentGroup.indexOfChild(this@attachToScrollView)
            parentGroup.removeViewAt(indexOfChild)
            parentGroup.addView(scrollView, indexOfChild, layoutParams)
            scrollView.addView(this@attachToScrollView, newLayoutParams)
        }
    })
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
