package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

/**
 * Created by WongKi on 2018/6/8.
 */

data class ExpressNews(
        val msg: String,
        val data: ExpressNewsList,
        val error: Int
)

class ExpressNewsList {
    var page: Int = 0
    var total_count: Int = 0
    val fnews_list = ArrayList<ExpressNewsItem>()
}

data class ExpressNewsItem(
        val title: String,
        val subtitle: String,
        val pub_time: Int,
        val author: String,
        val fast_id: String,
        val content: String,
        val desc: String,
        val url: String//app下载地址
)