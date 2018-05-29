package com.iterduo.Finance.ITerduoFinance.mvp.presenter

import com.iterduo.Finance.ITerduoFinance.base.BasePresenter
import com.iterduo.Finance.ITerduoFinance.mvp.contract.HotTabContract
import com.iterduo.Finance.ITerduoFinance.mvp.model.HotTabModel
import com.iterduo.Finance.ITerduoFinance.net.exception.ExceptionHandle

/**
 * Created by xuhao on 2017/11/30.
 * desc: 获取 TabInfo Presenter
 */
class HotTabPresenter:BasePresenter<HotTabContract.View>(),HotTabContract.Presenter {

    private val hotTabModel by lazy { HotTabModel() }


    override fun getTabInfo() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = hotTabModel.getTabInfo()
                .subscribe({
                    tabInfo->
                    mRootView?.setTabInfo(tabInfo)
                },{
                    throwable->
                    //处理异常
                    mRootView?.showError(ExceptionHandle.handleException(throwable), ExceptionHandle.errorCode)
                })
        addSubscription(disposable)
    }
}