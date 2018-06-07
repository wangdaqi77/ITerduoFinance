package com.iterduo.Finance.ITerduoFinance.mvp.model

import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.BannerBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeDataBean
import com.iterduo.Finance.ITerduoFinance.net.RetrofitManager
import com.iterduo.Finance.ITerduoFinance.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/21.
 * desc: 首页精选 model
 */

class HomeModel{

    /**
     * 获取首页 Banner 数据
     */
    fun requestHomeData(num:Int):Observable<HomeBean>{
        return RetrofitManager.service.getFirstHomeData(num)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取Banner 数据
     */
    fun requestHomeBannerData():Observable<BannerBean>{
        return RetrofitManager.service.getBanner()
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多
     */
    fun loadMoreData(page:Int,pageSize:Int):Observable<HomeDataBean>{

        return RetrofitManager.service.getHomeNewsList(page,pageSize)
                .compose(SchedulerUtils.ioToMain())
    }
    /**
     * 加载更多
     */
    fun loadMoreData(url:String):Observable<HomeBean>{

        return RetrofitManager.service.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }



}
