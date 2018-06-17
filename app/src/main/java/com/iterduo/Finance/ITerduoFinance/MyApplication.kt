package com.iterduo.Finance.ITerduoFinance

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.iterduo.Finance.ITerduoFinance.ui.activity.MainActivity
import com.iterduo.Finance.ITerduoFinance.utils.DisplayManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import kotlin.properties.Delegates


/**
 * Created by xuhao on 2017/11/16.
 *
 */

class MyApplication : Application() {

    private var refWatcher: RefWatcher? = null

    companion object {

        private val TAG = "MyApplication"

        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }


        @SuppressLint("WrongConstant")
        fun reStartApp() {
            val intent = Intent()
            intent.setClassName(context.getPackageName(), MainActivity::class.java.name)
            val restartIntent = PendingIntent.getActivity(context, 0,
                    intent, Intent.FLAG_ACTIVITY_NEW_TASK)
            val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300,
                    restartIntent)
            // MyApp.stackInstance().exitAll()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        refWatcher = setupLeakCanary()
        initConfig()
        initUMengShare()
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)


    }

    private fun initUMengShare() {

        UMConfigure.init(this, "5b0e5252f29d984e710000c6"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0")
        //豆瓣RENREN平台目前只能在服务器端配置
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com")
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf")
        PlatformConfig.setQQZone("1106975702", "KEYPOyo4uC5ZbEK7Pjz")
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO")
//        PlatformConfig.setAlipay("2015111700822536")
//        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e")
//        PlatformConfig.setPinterest("1439206")
//        PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f")
//        PlatformConfig.setDing("dingoalmlnohc0wggfedpk")
//        PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J")
//        PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a")
//        PlatformConfig.setYnote("9c82bf470cba7bd2f1819b0ee26f86c6ce670e9b")
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }


    /**
     * 初始化配置
     */
    private fun initConfig() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Finance")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }


    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }
    }


}
