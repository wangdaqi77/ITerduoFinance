package com.iterduo.Finance.ITerduoFinance.mvp.presenter

import com.iterduo.Finance.ITerduoFinance.base.BasePresenter
import com.iterduo.Finance.ITerduoFinance.mvp.contract.HomeContract
import com.iterduo.Finance.ITerduoFinance.mvp.contract.HomeOldContract
import com.iterduo.Finance.ITerduoFinance.mvp.model.HomeModel
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.BannerItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBanner
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeDataBean
import com.iterduo.Finance.ITerduoFinance.net.exception.ExceptionHandle

/**
 * Created by xuhao on 2017/11/8.
 * 首页精选的 Presenter
 * (数据是 Banner 数据和一页数据组合而成的 HomeBean,查看接口然后在分析就明白了)
 */

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private var bannerList: ArrayList<BannerItem>? = null

    private var page = 1
    private var pageSize = 15

    private val homeModel: HomeModel by lazy {

        HomeModel()
    }

    /**
     * 获取首页精选数据 banner 加 一页数据
     */
    override fun requestHomeData() {
        // 检测是否绑定 View
        checkViewAttached()
        bannerList = null
        mRootView?.showLoading()
        page = 1
        val disposable = homeModel.requestHomeBannerData()
                .flatMap({ bannerBean ->

                    bannerList = bannerBean.data

                    //根据 nextPageUrl 请求下一页数据
                    homeModel.loadMoreData(page, pageSize)
                })

                .subscribe({ homeData ->
                    page++
                    mRootView?.apply {
                        dismissLoading()
                        homeData.bannerData = HomeBanner(this@HomePresenter.bannerList
                                ?: ArrayList())
                        setHomeData(homeData!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
//                        if (bannerList!=null) {
//                            HomeDataBean("",ArrayList(),bannerList, -1)
//                        }
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
                homeModel.loadMoreData(page, pageSize)
                        .subscribe({ homeBean ->

                            page++
                            mRootView?.apply {
                                val noMore = homeBean.data.total_count <= homeBean.data.page * pageSize
                                setMoreData(homeBean.data.news_list, noMore)
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