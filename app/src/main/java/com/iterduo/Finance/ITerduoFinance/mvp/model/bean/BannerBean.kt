package com.iterduo.Finance.ITerduoFinance.mvp.model.bean

import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IHeaderItem

/**
 * Created by WongKi on 2018/6/7.
 */

data class BannerBean(
        val msg: String,
        val data: ArrayList<BannerItem>,
        val error: Int
)

data class BannerItem(
    val img_url: String,
    val jump_url: String,
    val order: Int,
    val banner_id: Int
):IHeaderItem