package com.iterduo.Finance.ITerduoFinance.mvp.contract

import com.iterduo.Finance.ITerduoFinance.base.IBaseView
import com.iterduo.Finance.ITerduoFinance.base.IPresenter
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeDataBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.News

/**
 * Created by wq on 2018/6.
 * 契约类
 */

interface HomeContract {

    interface View : IBaseView {

        /**
         * 设置第一次请求的数据
         */
        fun setHomeData(homeBean: HomeDataBean)

        /**
         * 设置加载更多的数据
         */
        fun setMoreData(itemList:List<News>)

        /**
         * 显示错误信息
         */
        fun showError(msg: String,errorCode:Int)


    }

    interface Presenter : IPresenter<View> {

        /**
         * 获取首页精选数据
         */
        fun requestHomeData()

        /**
         * 加载更多数据
         */
        fun loadMoreData()


    }


}