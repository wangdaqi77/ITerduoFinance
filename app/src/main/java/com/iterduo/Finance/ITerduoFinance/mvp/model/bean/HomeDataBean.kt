package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IMultiItem
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter.CommonMultiItemAdapter

/**
 * Created by WongKi on 2018/6/7.
 */

data class HomeDataBean(
        val msg: String,
        val data: ArrayList<News>,
        var bannerData: HomeBanner,
        val error: Int
)

data class News(
        val title: String,
        val subtitle: String,
        val desc: String,
        val read_num: Long,
        val pub_time: Int,
        val author: String,
        val small_url: String,
        val news_id: Int,
        val jump_url: String
) : IMultiItem {
    override fun getItemType(): Int = IMultiItem.ITEM_TYPE_CONTENT
}