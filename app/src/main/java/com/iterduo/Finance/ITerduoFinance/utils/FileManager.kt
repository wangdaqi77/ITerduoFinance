package com.iterduo.Finance.ITerduoFinance.utils

import android.util.Log
import com.iterduo.Finance.ITerduoFinance.MyApplication
import com.iterduo.Finance.ITerduoFinance.common.Config
import java.io.*
import java.util.LinkedHashMap

/**
 * Created by WongKi on 2018/6/10.
 */

class FileManager private constructor() {


    /**
     * 写入文件中
     * key  时间戳  String具体 详情信息
     *
     * @param map
     * @param fileRootName 存放文件的文件夹的名字
     */
    @Synchronized
    fun writerMapToFile(map: LinkedHashMap<Long, String>?, fileRootName: String) {
        var buffer: StringBuffer? = null
        try {
            buffer = StringBuffer()

            if (map != null && map.size > 0) {

                for ((key, value) in map) {

                    val timer = DateUtils.getPatternTime(System.currentTimeMillis(), DateUtils.P5)
                    buffer.append(timer + "    ：" + value + "\n")
                }
            }

            writerToFile(buffer.toString(), fileRootName)

        } catch (e: Exception) {

        }

    }

    /**
     * @param s
     * @param fileRootName
     */
    private fun writerToFile(s: String, fileRootName: String) {

        try {
            /**
             * 创建日志文件名称
             */

            val timer = DateUtils.getPatternTime(System.currentTimeMillis(), DateUtils.P5)

            val fileName = timer + ".log"
            //Logger.d(this, "[fileName]  == " + fileName)
            /**
             * 创建文件夹
             */
            val folder = File(fileRoot + File.separator + fileRootName)

            if (!folder.exists()) {
                folder.mkdirs()
            }

            /**
             * 创建日志文件
             */
            val file = File(folder, fileName)


            if (!file.exists()) {
                //Logger.d(this, "file" + "不存在，创建文件前")
                file.createNewFile()
                //Logger.d(this, "file" + "不存在，创建文件后")
            } else {
                //Logger.d(this, "file" + "已经存在")
                return
            }

            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)

            bufferedWriter.write(s)
            bufferedWriter.flush()
            bufferedWriter.close()
            //sendLogToServer(folder, file);

        } catch (e: Exception) {
            Log.e(TAG, "writerToFile - " + e.message)
        }


    }

    /**
     * 发送日志文件到服务器
     *
     * @param folder 文件路径
     * @param file   文件
     */
    fun sendLogToServer(folder: File, file: File) {
        //TODO WQ 发送日志文件到服务器
    }

    companion object {
        private val TAG = "FileManager"
        private var fileManager: FileManager? = null

        val instance: FileManager
            get() {
                if (fileManager == null) {
                    synchronized(FileManager::class.java) {
                        if (fileManager == null) {
                            fileManager = FileManager()
                        }
                    }
                }
                return fileManager!!
            }

        /**
         * 本应用的文件路径
         *
         * @return 路径
         */
        // 外置卡
        val fileRoot: String
            get() {
                var rootPath: String? = null
                if (FileUtil.extSDCardPath != null && FileUtil.extSDCardPath.size !== 0) {
                    //Logger.d(TAG, "[SD卡状态] 外置卡")
                    rootPath = FileUtil.extSDCardPath.get(0) + File.separator + Config.APP_FILE_ROOT_NAME
                } else {
                    //Logger.d(TAG, "[SD卡状态] 内置卡")
                    rootPath = FileUtil.innerSDCardPath + File.separator + Config.APP_FILE_ROOT_NAME
                }
                return rootPath
            }

         fun getRoot(subRootName: String): String {
            val file = File(fileRoot + File.separator + subRootName)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

        /*获取急救文件夹根目录路径*/
        val publishRoot: String
            get() = getRoot(Config.PUBLISH_FILE_ROOT_NAME)

        /*获取temp文件夹根目录路径*/
        val tempRoot: String
            get() = getRoot(Config.TEMP_FILE_ROOT_NAME)

        /*获取环信聊天cache文件夹根目录路径*/
        val cacheMsgRoot: String
            get() = getRoot(Config.CACHE_MSG_NAME)

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


        /**
         * 如果写入成功 得到文件路径
         *
         * @param resName      资源名
         * @param fileRootPath filerootpath 存储的文件夹
         * @param fileName     fileName  文件名
         * @return
         */
        fun savaAccessResToFile(resName: String, fileRootPath: String, fileName: String): String? {

            val inputStream = loadAssetsData(resName) ?: throw NullPointerException("AccessRes inputStream is null")
            var path: String? = null

            val fileRoot = File(fileRootPath)
            if (!fileRoot.exists()) {
                fileRoot.mkdirs()
            }

            val file = File(fileRoot, fileName)

            try {
                if (!file.exists()) {
                    file.createNewFile()
                }

                val fos = FileOutputStream(file, false)

                val bytes = ByteArray(1024)
                var len = -1

                while (inputStream.read(bytes).let {
                    len = it
                    len != -1
                }) {
                    fos.write(bytes, 0, len)
                }

                path = file.absolutePath

                fos.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()

            }

            return path
        }

        /**
         * @return 如果成功或者存在返回路径
         */
        fun saveImTempVoiceToFile(): String? {
            val file = File(tempRoot, "temp.amr")
            return if (file.exists()) {
                file.absolutePath
            } else savaAccessResToFile("temp.amr", tempRoot, "temp.amr")
        }

        /**
         * @return 如果成功或者存在返回路径
         */
        fun saveVideoWarningToneToFile(): String? {
            val file = File(tempRoot, Config.VIDEO_WARNING_TONE_FILE_NAME)
            return if (file.exists()) {
                file.absolutePath
            } else savaAccessResToFile(Config.VIDEO_WARNING_TONE_FILE_NAME, tempRoot, Config.VIDEO_WARNING_TONE_FILE_NAME)
        }
    }

}
