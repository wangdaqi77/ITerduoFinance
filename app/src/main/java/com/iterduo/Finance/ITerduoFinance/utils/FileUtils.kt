package com.iterduo.Finance.ITerduoFinance.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.iterduo.Finance.ITerduoFinance.MyApplication
import java.io.*
import java.util.*

/**
 * Created by WongKi on 2018/6/10.
 */
object FileUtil {
    private val TAG = "FileUtil"

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    val innerSDCardPath: String
        get() = Environment.getExternalStorageDirectory().path

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    val extSDCardPath: List<String>
        get() {
            val lResult = ArrayList<String>()
            try {
                val rt = Runtime.getRuntime()
                val proc = rt.exec("mount")
                val `is` = proc.inputStream
                val isr = InputStreamReader(`is`)
                val br = BufferedReader(isr)
                var line: String?=null
                    while (br.readLine().let {
                        line = it
                        line != null
                    }) {
                    if (line!!.contains("extSdCard")) {
                        val arr = line!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val path = arr[1]
                        val file = File(path)
                        if (file.isDirectory) {
                            lResult.add(path)
                        }
                    }
                }
                isr.close()
            } catch (e: Exception) {
            }

            //Logger.d(TAG, "lResult 的size" + lResult.size)
            return lResult
        }


    /**
     * 将图片保存到缓存目录
     *
     * @param context
     * @param bitmap
     * @return
     */
    fun saveBmpToCacheDir(context: Context, bitmap: Bitmap): String? {
        var isSuccess = false
        val file = File(context.cacheDir, randomFileName(".jpg"))
        try {
            file.delete()
            file.createNewFile()

            val fos = FileOutputStream(file)
            isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (isSuccess) file.absolutePath else null
    }

    /**
     * 随机生成文件名字
     *
     * @param sufix 文件后缀名
     * @return
     */
    fun randomFileName(sufix: String): String {
        return UUID.randomUUID().toString() + sufix
    }


    fun loadAssetsData(assetsName: String): InputStream? {
        //获取AssetManager
        val assets = MyApplication.context.getResources().getAssets()
        var open: InputStream? = null
        try {
            //获取assets目录下的所有文件及目录名
            open = assets.open(assetsName)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return open
    }


    fun getStringFromInputStream(a_is: InputStream): String {
        var br: BufferedReader? = null
        val sb = StringBuilder()
        var line: String?=null
        try {
            br = BufferedReader(InputStreamReader(a_is))
            while (br.readLine().let {
                line = it
                line != null
            }) {
                sb.append(line)
            }
        } catch (e: IOException) {
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                }

            }
        }
        //Logger.e(TAG, sb.toString())
        return sb.toString()
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    /**
     * 删除文件
     *
     * @param srcFilePath 文件路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(srcFilePath: String): Boolean {
        return deleteFile(getFileByPath(srcFilePath).toString())
    }
}
