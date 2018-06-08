package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

/**
 * Created by WongKi on 2018/6/8.
 */

data class ExpressNews(
        val msg: String,
        val data: ArrayList<ExpressNewsItem>,
        val error: Int
)

data class ExpressNewsItem(
    val title: String,
    val subtitle: String,
    val pub_time: Int,
    val author: String,
    val fast_id: Int,
    val content: String,
    val desc: String
)