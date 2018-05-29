package com.iterduo.Finance.ITerduoFinance.mvp.contract

import com.iterduo.Finance.ITerduoFinance.base.IBaseView
import com.iterduo.Finance.ITerduoFinance.base.IPresenter
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.TabInfoBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: 契约类
 */
interface RankContract {

    interface View:IBaseView{
        /**
         * 设置排行榜的数据
         */
        fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg:String,errorCode:Int)
    }


    interface Presenter:IPresenter<View>{
        /**
         * 获取 TabInfo
         */
        fun requestRankList(apiUrl:String)
    }
}