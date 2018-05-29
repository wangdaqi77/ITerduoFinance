package com.iterduo.Finance.ITerduoFinance.mvp.contract

import com.iterduo.Finance.ITerduoFinance.base.IBaseView
import com.iterduo.Finance.ITerduoFinance.base.IPresenter
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: 契约类
 */
interface FollowContract {

    interface View : IBaseView {
        /**
         * 设置关注信息数据
         */
        fun setFollowInfo(issue: HomeBean.Issue)

        fun showError(errorMsg: String, errorCode: Int)
    }


    interface Presenter : IPresenter<View> {
        /**
         * 获取List
         */
        fun requestFollowList()

        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}