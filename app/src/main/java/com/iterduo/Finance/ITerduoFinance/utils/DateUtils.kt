package com.iterduo.Finance.ITerduoFinance.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author:wq
 * @date:2018/5/30
 * @email:wangqi7676@163.com
 * @desc:
 */
object DateUtils {
    const val P1 = "MM-dd HH:mm"
    const val P2 = "yyyy-MM-dd HH:mm"
    const val P3 = "yyyy-MM-dd"
    const val P4 = "MM-dd"


    /**
     * 得到一定模式的提示时间
     *
     * @param time
     * @param pattern
     * @return
     */
    fun getPatternTime(time: Long, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(Date(time))
    }


    fun getNewsTime(time: Int?): String? {
        if (time == null) return null
        val date = time * 1000L
        val sb = StringBuffer()
        val minMill = (60 * 1000).toLong()//1min
        val hourMill = 60 * minMill//1hour
        val dayMill = 24 * hourMill//1day
        //当前时间
        val now = Calendar.getInstance().timeInMillis
        // 相减之后时间差
        val deltime = now - date
        val day = deltime / dayMill
        if (day > 0) {
            //一个月内
            if (day <= 30) {
                when (day) {
                    1L -> sb.append("昨天")
                    2L -> sb.append("前天")
                    else -> sb.append(getPatternTime(date, P4))
                }
            } else {
                sb.append(getPatternTime(date, P3))
            }
        } else {
            //小于1天

            val hour = deltime / hourMill
            if (hour > 0) {
                //大于1小时
                sb.append(hour.toString() + "小时前")
            } else {
                //小于1小时
                val minute = deltime / minMill
                if (minute > 0) {
                    //大于1分钟
                    sb.append(minute.toString() + "分钟前")
                } else {
                    //小于1分钟
                    sb.append("刚刚")
                }
            }
        }
        return sb.toString()
    }
}

