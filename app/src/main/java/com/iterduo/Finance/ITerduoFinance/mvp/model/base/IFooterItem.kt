package com.iterduo.Finance.ITerduoFinance.mvp.model.base

/**
 * Created by WongKi on 2018/6/8.
 */
interface IFooterItem : IMultiItem {
    override fun getItemType(): Int = IMultiItem.ITEM_TYPE_FOOTER
}