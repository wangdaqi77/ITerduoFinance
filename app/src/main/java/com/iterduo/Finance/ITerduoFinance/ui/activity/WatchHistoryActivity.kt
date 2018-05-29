package com.iterduo.Finance.ITerduoFinance.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.iterduo.Finance.ITerduoFinance.Constants
import com.iterduo.Finance.ITerduoFinance.MyApplication
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseActivity
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.ui.adapter.WatchHistoryAdapter
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import com.iterduo.Finance.ITerduoFinance.utils.WatchHistoryUtils
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by xuhao on 2017/12/11.
 * desc: 观看记录
 */

class WatchHistoryActivity : BaseActivity() {

    private var itemListData = ArrayList<HomeBean.Issue.Item>()

    companion object {
        private val HISTORY_MAX = 20
    }

    override fun layoutId(): Int = R.layout.layout_watch_history

    override fun getExtData() {
        multipleStatusView.showLoading()
        itemListData = queryWatchHistory()

    }

    override fun initView() {
        //返回
        toolbar.setNavigationOnClickListener { finish() }

        val mAdapter = WatchHistoryAdapter(this, itemListData, R.layout.item_video_small_card)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        if(itemListData.size>1){
            multipleStatusView.showContent()
        }else{
            multipleStatusView.showEmpty()
        }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this,mRecyclerView)

    }

    override fun loadData() {

    }

    /**
     * 查询观看的历史记录
     */
    private fun queryWatchHistory():ArrayList<HomeBean.Issue.Item> {
        val watchList = ArrayList<HomeBean.Issue.Item>()
        val hisAll = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context) as Map<*, *>
        //将key排序升序
        val keys = hisAll.keys.toTypedArray()
        Arrays.sort(keys)
        val keyLength = keys.size
        //这里计算 如果历史记录条数是大于 可以显示的最大条数，则用最大条数做循环条件，防止历史记录条数-最大条数为负值，数组越界
        val hisLength = if (keyLength > HISTORY_MAX) HISTORY_MAX else keyLength
        // 反序列化和遍历 添加观看的历史记录
        (1..hisLength).mapTo(watchList) {
            WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context,
                    keys[keyLength - it] as String) as HomeBean.Issue.Item
        }

        return watchList
    }

}
