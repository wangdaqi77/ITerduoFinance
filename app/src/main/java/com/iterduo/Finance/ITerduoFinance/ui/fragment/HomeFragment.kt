package com.iterduo.Finance.ITerduoFinance.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.a91power.a91pos.common.setNoMore
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseFragment
import com.iterduo.Finance.ITerduoFinance.mvp.contract.HomeContract
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeDataBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.NewsItem
import com.iterduo.Finance.ITerduoFinance.mvp.presenter.HomePresenter
import com.iterduo.Finance.ITerduoFinance.net.exception.ErrorStatus
import com.iterduo.Finance.ITerduoFinance.showToast
import com.iterduo.Finance.ITerduoFinance.ui.activity.NewsDetailActivity
import com.iterduo.Finance.ITerduoFinance.ui.activity.SearchActivity
import com.iterduo.Finance.ITerduoFinance.ui.adapter.HomeAdapter
import com.iterduo.Finance.ITerduoFinance.ui.adapter.HomeAdapterNew
import com.iterduo.Finance.ITerduoFinance.utils.NewsItemStatusUtils
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
/**
 * Created by wq
 * 首页
 */

class HomeFragment : BaseFragment(), HomeContract.View {
    private val mPresenter by lazy { HomePresenter() }
    private var mTitle: String? = null
    private var mHomeAdapter: HomeAdapterNew? = null
    private var isRefresh = false
    private var mMaterialHeader: MaterialHeader? = null
    private var mClassicsFooter: ClassicsFooter? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.ENGLISH)
    }


    override fun getLayoutId(): Int = R.layout.fragment_home


    /**
     * 初始化 ViewI
     */
    override fun initView() {
        mPresenter.attachView(this)
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.isEnableLoadmore = true
        mClassicsFooter?.visibility = View.GONE
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mRefreshLayout.isEnableLoadmore = true
            mClassicsFooter?.visibility = View.GONE
            mHomeAdapter?.setNoMore(context, false)
            mPresenter.requestHomeData()
        }
        mRefreshLayout.setOnLoadmoreListener {
            mPresenter.loadMoreData()
        }
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        mClassicsFooter = mRefreshLayout.refreshFooter as ClassicsFooter?//打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    val childCount = mRecyclerView.childCount
//                    val itemCount = mRecyclerView.layoutManager.itemCount
//                    val firstVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//                    if (firstVisibleItem + childCount == itemCount) {
//                        if (!noMore) {
//                            mPresenter.loadMoreData()
//                        }
//                    }
//                }
            }

            //RecyclerView滚动的时候调用
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if (currentVisibleItemPosition == 0) {
                    //背景设置为透明
                    toolbar.setBackgroundColor(getColor(R.color.color_translucent))
                    iv_search.setImageResource(R.mipmap.ic_action_search_white)
                    tv_header_title.text = ""
                } else {
                    if (mHomeAdapter?.data!!.size > 1) {
//                        toolbar.setBackgroundColor(getColor(R.color.color_title_bg))
//                        iv_search.setImageResource(R.mipmap.ic_action_search_black)
//                        val itemList = mHomeAdapter!!.mData
//                        val item = itemList[currentVisibleItemPosition + mHomeAdapter!!.bannerItemSize - 1]
//                        if (item.type == "textHeader") {
//                            tv_header_title.text = item.data?.text
//                        } else {
//                            tv_header_title.text = simpleDateFormat.format(item.data?.date)
//                        }
                    }
                }


            }
        })

        iv_search.setOnClickListener { openSearchActivity() }

        mLayoutStatusView = multipleStatusView

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)


    }

    private fun openSearchActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, iv_search, iv_search.transitionName)
            startActivity(Intent(activity, SearchActivity::class.java), options.toBundle())
        } else {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    override fun lazyLoad() {
        mPresenter.requestHomeData()
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
    override fun setHomeData(homeBean: HomeDataBean) {
        mLayoutStatusView?.showContent()
        Logger.d(homeBean)
        homeBean.data.news_list.add(0, NewsItem(homeBean.bannerData))
        // Adapter
        mHomeAdapter = HomeAdapterNew(homeBean.data.news_list)
        mHomeAdapter?.setOnItemClickListener { adapter, view, position ->
            val newsItem = adapter?.getItem(position) as NewsItem
            NewsDetailActivity.start(this@HomeFragment.activity, newsItem.url)

            NewsItemStatusUtils.put(context, newsItem.url, true)
            adapter.notifyItemChanged(position)
        }
        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()

    }

    override fun setMoreData(itemList: List<NewsItem>, noMore: Boolean) {
        mRefreshLayout.finishLoadmore()
        mRefreshLayout.isEnableLoadmore = !noMore
        mClassicsFooter?.visibility = if (noMore) View.VISIBLE else View.GONE
        mHomeAdapter?.addData(itemList)
        mHomeAdapter?.setNoMore(this@HomeFragment.activity, noMore)
    }


    /**
     * 显示错误信息
     */
    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if (mRefreshLayout.isLoading) {
            mRefreshLayout.finishLoadmore()
            return
        }
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
