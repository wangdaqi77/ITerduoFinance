package com.iterduo.Finance.ITerduoFinance.mvp.model.base

/**
 * Created by WongKi on 2018/6/8.
 */
interface IMultiItem {
    fun getItemType(): Int
    companion object {
        val ITEM_TYPE_HEADER = 1    //头部 类型
        val ITEM_TYPE_FOOTER = 999    //尾部 类型
        val ITEM_TYPE_CONTENT = 3    //item
    }
}