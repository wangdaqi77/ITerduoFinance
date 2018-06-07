package com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.MultipleType
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder

/**
 * Created by wq on 2018/06/07.
 * desc: 通用的 Adapter
 */

abstract class CommonHeaderMultiItemAdapter<T : MultiItemEntity>(var context: Context, var data: ArrayList<T>, //条目布局
                                                                 mLayoutId: Int) : CommonAdapter<T>(context, data, mLayoutId) {
    companion object {
        val ITEM_TYPE_HEADER = 1    //Banner 类型
        val ITEM_TYPE_CONTENT = 2    //item
    }

    /**
     * 得到 Item 的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in 0..(getHeaderCount() - 1) ->
                ITEM_TYPE_HEADER
            else ->
                mData[position - getHeaderCount()].itemType
        }
    }

    /**
     *  创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER ->
                ViewHolder(inflaterView(getHeaderItemLayout(), parent))
            else ->
                ViewHolder(inflaterView(getMultiItemLayout(viewType), parent))
        }
    }

    abstract fun getHeaderItemLayout(): Int
    abstract fun getMultiItemLayout(viewType: Int): Int
    @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view!!
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE_HEADER -> bindHeaderData(holder, position)
            else -> super.onBindViewHolder(holder, position)
        }

    }


    override fun getItemCount(): Int {
        return super.getItemCount() + getHeaderCount()
    }

    abstract fun bindHeaderData(holder: ViewHolder, headerPosition: Int)
    abstract fun getHeaderCount(): Int
}