package com.iterduo.Finance.ITerduoFinance.mvp.contract

import com.iterduo.Finance.ITerduoFinance.base.IBaseView
import com.iterduo.Finance.ITerduoFinance.base.IPresenter
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/25.
 * desc: 视频详情契约类
 */
interface VideoDetailContract {

    interface View : IBaseView {

        /**
         * 设置视频播放源
         */
        fun setVideo(url: String)

        /**
         * 设置视频信息
         */
        fun setVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 设置背景
         */
        fun setBackground(url: String)

        /**
         * 设置最新相关视频
         */
        fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * 设置错误信息
         */
        fun setErrorMsg(errorMsg: String)


    }

    interface Presenter : IPresenter<View> {

        /**
         * 加载视频信息
         */
        fun loadVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * 请求相关的视频数据
         */
        fun requestRelatedVideo(id: Long)

    }


}