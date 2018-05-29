package com.a91power.a91pos.common

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshContent

/**
 * @author:wq
 * @date:2018/4/26
 * @email:wangqi7676@163.com
 * @desc: 拓展字段
 */

/**
 * 标准的数字watcher
 */
val EditText.digitalStandardsTextWatcher: TextWatcher
    get() = object : TextWatcher {
        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            if (text.trim().isNotEmpty()) {
                if (text.trim().substring(0, 1) == ".") {
                    setText("0${text.trim()}")
                    setSelection(text.length)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(str: CharSequence, start: Int, before: Int, count: Int) {
            val maxLen = 9
            var s: String = str.toString()
            // 限制最多能输入maxLen位整数
            if (s.contains(".")) {
                if (s.indexOf(".") > maxLen) {
                    s = "${s.subSequence(0, maxLen)}${s.substring(s.indexOf("."))}"
                    setText(s)
                    setSelection(maxLen)
                }
            } else {
                if (s.length > maxLen) {
                    s = s.subSequence(0, maxLen).toString()
                    setText(s)
                    setSelection(maxLen)
                }
            }
            // 判断小数点后只能输入两位
            if (s.contains(".")) {
                if (s.length - 1 - s.indexOf(".") > 2) {
                    s = s.subSequence(0, s.indexOf(".") + 3).toString()
                    setText(s)
                    setSelection(s.length)
                }
            }
            //如果第一个数字为0，第二个不为点，就不允许输入
            if (s.startsWith("0") && s.trim().length > 1) {
                if (s.substring(1, 2) != ".") {
                    setText(s.subSequence(0, 1))
                    setSelection(1)
                    return
                }
            }
        }
    }

val EditText.editPriceTextWatcher: TextWatcher
    get() = object : TextWatcher {
        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            if (text.trim().isNotEmpty()) {
                if (text.trim().substring(0, 1) == ".") {
                    setText("0${text.trim()}")
                    setSelection(text.length)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(str: CharSequence, start: Int, before: Int, count: Int) {
            val maxLen = 6
            var s: String = str.toString()
            // 限制最多能输入maxLen位整数
            if (s.contains(".")) {
                if (s.indexOf(".") > maxLen) {
                    s = "${s.subSequence(0, maxLen)}${s.substring(s.indexOf("."))}"
                    setText(s)
                    setSelection(maxLen)
                }
            } else {
                if (s.length > maxLen) {
                    s = s.subSequence(0, maxLen).toString()
                    setText(s)
                    setSelection(maxLen)
                }
            }
            // 判断小数点后只能输入两位
            if (s.contains(".")) {
                if (s.length - 1 - s.indexOf(".") > 2) {
                    s = s.subSequence(0, s.indexOf(".") + 3).toString()
                    setText(s)
                    setSelection(s.length)
                }
            }
            //如果第一个数字为0，第二个不为点，就不允许输入
            if (s.startsWith("0") && s.trim().length > 1) {
                if (s.substring(1, 2) != ".") {
                    setText(s.subSequence(0, 1))
                    setSelection(1)
                    return
                }
            }
        }
    }