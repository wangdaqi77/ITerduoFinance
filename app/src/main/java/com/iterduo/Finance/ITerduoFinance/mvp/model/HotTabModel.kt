package com.iterduo.Finance.ITerduoFinance.mvp.model

import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.TabInfoBean
import com.iterduo.Finance.ITerduoFinance.net.RetrofitManager
import com.iterduo.Finance.ITerduoFinance.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/30.
 * desc: 热门 Model
 */
class HotTabModel {

    /**
     * 获取 TabInfo
     */
    fun getTabInfo(): Observable<TabInfoBean> {

        return RetrofitManager.service.getRankList()
                .compose(SchedulerUtils.ioToMain())
    }

}
