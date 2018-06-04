package com.iterduo.Finance.ITerduoFinance.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseFragment
import com.iterduo.Finance.ITerduoFinance.mvp.contract.ExpressNewsContract
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.presenter.ExpressNewsPresenter
import com.iterduo.Finance.ITerduoFinance.net.exception.ErrorStatus
import com.iterduo.Finance.ITerduoFinance.showToast
import com.iterduo.Finance.ITerduoFinance.ui.adapter.ExpressNewsAdapter
import com.iterduo.Finance.ITerduoFinance.ui.adapter.HomeAdapter
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import kotlinx.android.synthetic.main.fragment_express_news.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wq on 2018/6.4.
 * desc: 快讯
 */
class ExpressNewsFragment : BaseFragment() , ExpressNewsContract.View {
    private val mPresenter by lazy { ExpressNewsPresenter() }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.ENGLISH)
    }

    private var mTitle: String? = null


    private var num: Int = 1

    private var mExpressNewsAdapter: ExpressNewsAdapter? = null

    private var loadingMore = false

    private var isRefresh = false
    private var mMaterialHeader: MaterialHeader? = null
    companion object {
        fun getInstance(title: String): ExpressNewsFragment {
            val fragment = ExpressNewsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_express_news

    override fun initView() {
        mPresenter.attachView(this)

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)

        tv_header_title.text = mTitle

        mLayoutStatusView = multipleStatusView

        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mPresenter.requestHomeData(num)
        }
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = mRecyclerView.childCount
                    val itemCount = mRecyclerView.layoutManager.itemCount
                    val firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }
            }

            //RecyclerView滚动的时候调用
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })


    }


    override fun lazyLoad() {
        mPresenter.requestHomeData(num)
    }


    /**
     * 显示 Loading （下拉刷新的时候不需要显示 Loading）
     */
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }

    /**
     * 隐藏 Loading
     */
    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }

    /**
     * 设置首页数据
     */
    override fun setHomeData(homeBean: HomeBean) {
        mLayoutStatusView?.showContent()
        Logger.d(homeBean)

        // Adapter
        mExpressNewsAdapter = ExpressNewsAdapter(activity, homeBean.issueList[0].itemList)
        //设置 banner 大小
        mExpressNewsAdapter?.setBannerSize(homeBean.issueList[0].count)

        mRecyclerView.adapter = mExpressNewsAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()

    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mExpressNewsAdapter?.addItemData(itemList)
    }


    /**
     * 显示错误信息
     */
    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    fun getColor(colorId: Int): Int {
        return resources.getColor(colorId)
    }


}