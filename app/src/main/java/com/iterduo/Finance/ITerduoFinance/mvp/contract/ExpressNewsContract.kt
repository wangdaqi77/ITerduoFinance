package com.iterduo.Finance.ITerduoFinance.mvp.contract

import com.iterduo.Finance.ITerduoFinance.base.IBaseView
import com.iterduo.Finance.ITerduoFinance.base.IPresenter
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.ExpressNews
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.ExpressNewsItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean

/**
 * Created by wq on 2018/6.
 * 契约类
 */

interface ExpressNewsContract {

    interface View : IBaseView {
        fun setData(itemList:ArrayList<ExpressNewsItem>)
        /**
         * 设置加载更多的数据
         */
        fun setMoreData(itemList:ArrayList<ExpressNewsItem>)

        /**
         * 显示错误信息
         */
        fun showError(msg: String,errorCode:Int)


    }

    interface Presenter : IPresenter<View> {

        /**
         * 获取首页精选数据
         */
        fun requestData()

        /**
         * 加载更多数据
         */
        fun loadMoreData()


    }


}