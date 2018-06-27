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
    const val P5 = "yyyy-MM-dd HH:mm:ss"


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


    fun getNewsTime(newsTime: Int?): String? {
        if (newsTime == null) return null
        val newsDate = newsTime * 1000L
        val sb = StringBuffer()
        // xinwen
        val news = Calendar.getInstance()
        news.timeInMillis = newsDate
        //当前时间
        val now = Calendar.getInstance()

        if (news.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            val disDay = now.get(Calendar.DAY_OF_YEAR) - news.get(Calendar.DAY_OF_YEAR)
            when (disDay) {
                0 -> {
                    val disHour = now.get(Calendar.DAY_OF_YEAR) - news.get(Calendar.DAY_OF_YEAR)
                    if (disHour > 0) {
                        //大于1小时
                        sb.append(disHour.toString() + "小时前")
                    } else {
                        //小于1小时
                        val disMinute = now.get(Calendar.MINUTE) - news.get(Calendar.MINUTE)
                        if (disMinute > 0) {
                            //大于1分钟
                            sb.append(disMinute.toString() + "分钟前")
                        } else {
                            //小于1分钟
                            sb.append("刚刚")
                        }
                    }
                }
                1 -> sb.append("昨天")
                2 -> sb.append("前天")
                else -> sb.append(getPatternTime(newsDate, P4))
            }

        } else {
            sb.append(getPatternTime(newsDate, P3))
        }

        return sb.toString()
    }
}

