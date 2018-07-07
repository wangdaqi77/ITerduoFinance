package com.iterduo.Finance.ITerduoFinance.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.HashMap

/**
 * @author:wq
 * @date:2018/6/25
 * @email:wangqi7676@163.com
 * @desc:
 */
object QrCodeUtil {

    /**
     * 生成二维码Bitmap
     *
     * @param logoBm 二维码中心的Logo图标
     * （可以为null）
     * @return 合成后的bitmap
     */

    fun createQRImage(context: Context, data: String?): Bitmap? {
        try {
            if (data == null || "" == data) {
                return null
            }

            var widthPix = (context as Activity).windowManager.defaultDisplay.width
            widthPix = widthPix / 5 * 3
            val heightPix = widthPix

            // 配置参数
            val hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 0

            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    pixels[y * widthPix + x] = if (bitMatrix.get(x, y)) -0x1000000 else -0x1
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap: Bitmap? = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap!!.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)

            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 在二维码中间添加Logo图案
     */
     fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }

        if (logo == null) {
            return src
        }

        // 获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height

        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }

        // logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5f / logoWidth.toFloat()
        var bitmap: Bitmap? = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.RGB_565)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, (srcWidth / 2).toFloat(), (srcHeight / 2).toFloat())
            canvas.drawBitmap(logo, ((srcWidth - logoWidth) / 2).toFloat(), ((srcHeight - logoHeight) / 2).toFloat(), null)

            canvas.save(Canvas.ALL_SAVE_FLAG)
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }

        return bitmap
    }
}