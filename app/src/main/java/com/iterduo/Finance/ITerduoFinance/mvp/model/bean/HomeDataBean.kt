package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IMultiItem

/**
 * Created by WongKi on 2018/6/7.
 */

data class HomeDataBean(
        val msg: String,
        val data: NewsList,
        var bannerData: HomeBanner,
        val error: Int
)

class NewsList {
    var page: Int = 0
    var total_count: Int = 0
    val news_list = ArrayList<NewsItem>()
}

data class NewsItem(
        var bannerData: HomeBanner?
) : IMultiItem, MultiItemEntity {
    val title: String = ""
    val subtitle: String = ""
    val desc: String = ""
    val read_num: Long = 0
    val pub_time: Int = 0
    val author: String = ""
    val small_url: String = ""
    val news_id: Int = 0
    val url: String = ""
    override fun getItemType(): Int = if (bannerData == null) IMultiItem.ITEM_TYPE_CONTENT else IMultiItem.ITEM_TYPE_HEADER
}