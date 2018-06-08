package com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IFooterItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IHeaderItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IMultiItem
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder

/**
 * Created by wq on 2018/06/07.
 * desc: 通用的 Adapter
 */

abstract class CommonMultiItemAdapter<H : IHeaderItem, T : IMultiItem, F : IFooterItem>(var context: Context, var headerData: ArrayList<H>?, var data: ArrayList<T>, var footerData: ArrayList<F>?, //条目布局
                                                                                        mLayoutId: Int) : CommonAdapter<T>(context, data, mLayoutId) {

    /**
     * 得到 Item 的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in 0..(getHeaderCount() - 1) -> {
                if (getHeaderCount() == 0) {
                    val itemType = mData[position - getHeaderCount()].getItemType()
                    if (itemType == IMultiItem.ITEM_TYPE_HEADER || itemType == IMultiItem.ITEM_TYPE_FOOTER) {
                        throw IllegalArgumentException("itemType 不能是1和999")
                    }
                    itemType
                } else {

                    IMultiItem.ITEM_TYPE_HEADER
                }
            }
            in getHeaderCount() + mData.size..(itemCount - position - 1) ->
                IMultiItem.ITEM_TYPE_FOOTER
            else -> {
                val itemType = mData[position - getHeaderCount()].getItemType()
                if (itemType == IMultiItem.ITEM_TYPE_HEADER || itemType == IMultiItem.ITEM_TYPE_FOOTER) {
                    throw IllegalArgumentException("itemType 不能是1和999")
                }
                itemType
            }
        }
    }


    /**
     *  创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            IMultiItem.ITEM_TYPE_HEADER ->
                ViewHolder(inflaterView(getHeaderItemLayout(), parent))
            IMultiItem.ITEM_TYPE_FOOTER ->
                ViewHolder(inflaterView(getFooterItemLayout(), parent))
            else ->
                ViewHolder(inflaterView(getMultiItemLayout(viewType), parent))
        }
    }

    /**
     * 加载布局
     */
    private fun inflaterView(layoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(layoutId, parent, false)
        return view!!
    }


    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            IMultiItem.ITEM_TYPE_HEADER -> {
                bindHeaderData(holder, headerData!![position], position)
            }
            IMultiItem.ITEM_TYPE_FOOTER -> {
                val footerPosition = getHeaderCount() - itemCount - position - 1
                bindFooterData(holder, footerData!![footerPosition], footerPosition)
            }
            else -> super.onBindViewHolder(holder, position - getHeaderCount())
        }

    }


    final override fun getItemCount(): Int {
        return super.getItemCount() + getHeaderCount() + getFooterCount()
    }

    abstract fun bindHeaderData(holder: ViewHolder, header: H, headerPosition: Int)
    abstract fun bindFooterData(holder: ViewHolder, footer: F, headerPosition: Int)

    @LayoutRes
    abstract fun getFooterItemLayout(): Int

    @LayoutRes
    abstract fun getHeaderItemLayout(): Int

    @LayoutRes
    abstract fun getMultiItemLayout(viewType: Int): Int

    private fun getHeaderCount(): Int = headerData?.size ?: 0
    private fun getFooterCount(): Int = footerData?.size ?: 0
}
