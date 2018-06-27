package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

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
    var page: Int? = null
    var total_count: Int? = null
    val news_list = ArrayList<NewsItem>()
}

data class NewsItem(
        val title: String,
        val subtitle: String,
        val desc: String,
        val read_num: Long,
        val pub_time: Int,
        val author: String,
        val small_url: String?,
        val news_id: Int,
        val url: String
) : IMultiItem {
    override fun getItemType(): Int = IMultiItem.ITEM_TYPE_CONTENT
}