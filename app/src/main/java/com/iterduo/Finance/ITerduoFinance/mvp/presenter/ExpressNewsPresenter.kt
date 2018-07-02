package com.iterduo.Finance.ITerduoFinance.mvp.presenter

import com.iterduo.Finance.ITerduoFinance.base.BasePresenter
import com.iterduo.Finance.ITerduoFinance.mvp.contract.ExpressNewsContract
import com.iterduo.Finance.ITerduoFinance.mvp.model.HomeModel
import com.iterduo.Finance.ITerduoFinance.net.exception.ExceptionHandle

/**
 * Created by xuhao on 2017/11/8.
 * 首页精选的 Presenter
 * (数据是 Banner 数据和一页数据组合而成的 HomeBean,查看接口然后在分析就明白了)
 */

class ExpressNewsPresenter : BasePresenter<ExpressNewsContract.View>(), ExpressNewsContract.Presenter {

    private var page = 1
    private var pageSize = 15


    private val homeModel: HomeModel by lazy {

        HomeModel()
    }

    /**
     * 获取首页精选数据 banner 加 一页数据
     */
    override fun requestData() {
        // 检测是否绑定 View
        page = 1
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeModel.getExpressNewsList(page, pageSize)
                .subscribe({ expressNews ->
                    mRootView?.apply {
                        dismissLoading()
                        page++
                        setData(expressNews.data.fnews_list)

                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)

    }

    /**
     * 加载更多
     */

    override fun loadMoreData() {
        val disposable =
                homeModel.getExpressNewsList(page, pageSize)
                        .subscribe({ expressNews ->
                            mRootView?.apply {
                                page++
                                val noMore = expressNews.data.total_count <= expressNews.data.page * pageSize
                                setMoreData(expressNews.data.fnews_list, noMore)
                            }

                        }, { t ->
                            mRootView?.apply {
                                showError(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                            }
                        })


        if (disposable != null) {
            addSubscription(disposable)
        }
    }


}