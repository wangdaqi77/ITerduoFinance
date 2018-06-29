package com.iterduo.Finance.ITerduoFinance.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewTreeObserver
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.base.BaseActivity
import com.iterduo.Finance.ITerduoFinance.common.Config
import com.iterduo.Finance.ITerduoFinance.common.ShareType
import com.iterduo.Finance.ITerduoFinance.showToast
import com.iterduo.Finance.ITerduoFinance.utils.FileManager
import com.iterduo.Finance.ITerduoFinance.utils.QrCodeUtil
import com.iterduo.Finance.ITerduoFinance.utils.StatusBarUtil
import com.iterduo.Finance.ITerduoFinance.view.ShareButtonsLayout
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.share_layout.*
import java.io.File
import java.io.FileOutputStream
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import java.util.*


/**
 * Created by WongKi on 2018/6/10.
 */
class ShareNewsDetailActivity : BaseActivity(), ShareButtonsLayout.ShareButtonOnClickListener {
    override fun onClick(view: View, type: ShareType) {
        when (type) {
            ShareType.WEIXIN -> {
                shareWeiXin()
            }
            ShareType.WEIXIN_CIRCLE -> {
                shareWeiXinCircle()
            }
            ShareType.QQ -> {
                shareQQ()
            }
            ShareType.DOWNLOAD -> {
                saveBitmap2File(bitmap)
            }
            ShareType.CLOSE -> {
                finish()
            }
        }
    }


    /*创建分享图片*/
    private fun createUMImage(): UMImage {
        return UMImage(this, bitmap)
    }

    /*压缩分享图片*/
    private fun compressImage(image: UMImage): UMImage =
            image.apply { compressStyle = UMImage.CompressStyle.SCALE }

    var mCallBack = object : UMShareListener {
        override fun onResult(p0: SHARE_MEDIA?) {
            toast("分享成功")
            finish()
        }

        override fun onCancel(p0: SHARE_MEDIA?) {
            toast("取消")
        }

        override fun onError(p0: SHARE_MEDIA?, t: Throwable?) {
            toast("失败：${t?.message}")
        }

        override fun onStart(p0: SHARE_MEDIA?) {
        }

    }

    /*分享微信*/
    private fun shareWeiXin() {
        val umImage = compressImage(createUMImage())
        ShareAction(this)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                //.withText("hello")
                .withMedia(umImage)
                .setCallback(mCallBack)
                .share()
    }

    /*分享到朋友圈*/
    private fun shareWeiXinCircle() {
        val umImage = compressImage(createUMImage())
        ShareAction(this)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                //.withText("hello")
                .withMedia(umImage)
                .setCallback(mCallBack)
                .share()
    }

    /*分享到QQ*/
    private fun shareQQ() {
        val umImage = compressImage(createUMImage())
        ShareAction(this)
                .setPlatform(SHARE_MEDIA.QQ)
                //.withText("hello")
                .withMedia(umImage)
                .setCallback(mCallBack)
                .share()
    }


    private fun saveBitmap2File(bitmap: Bitmap) {
        try {
            // 将截图保存在SD卡根目录的test.png图像文件中
            val fos = FileOutputStream(File(FileManager.getRoot(Config.FILE_SHARE_IMAGES), "iterduo_${Calendar.getInstance().timeInMillis}.png"))
            // 将Bitmap对象中的图像数据压缩成png格式的图像数据，并将这些数据保存在test.png文件中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            // 关闭文件输出流
            fos.close()
            showToast("保存成功")
        } catch (e: Exception) {
            showToast("保存失败")
        }
    }

    override fun layoutId(): Int = R.layout.share_layout

    private var mContent: String? = null
    override fun getExtData() {
        mContent = intent.getStringExtra(SHARE_CONTENT)
    }

    override fun initView() {
        StatusBarUtil.darkMode(this, Color.parseColor("#ffffff"), 0F)
        share_tv_content.text = mContent
        showDownloadAPPQR(intent.getStringExtra(DOWNLOAD_URL))
        createBitmap()
        share_buttons.shareButtonOnClickListener = this@ShareNewsDetailActivity
    }

    private fun showDownloadAPPQR(downloadUrl: String) {
        var createQRImage = QrCodeUtil.createQRImage(this, downloadUrl)
        if (createQRImage != null) {
            createQRImage = QrCodeUtil.addLogo(createQRImage, BitmapFactory.decodeResource(resources, R.drawable.ic_logo_qrcode))
            iv_share_er_wei_ma.setImageBitmap(createQRImage)
        }
    }

    lateinit var bitmap: Bitmap
    private fun createBitmap() {
        share_content_root.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    if (height<nsv.height) {
//                        share_tv_content.minHeight = nsv.height - height + share_tv_content.height
//                    }
                    //打开图像缓存
                    isDrawingCacheEnabled = true
                    // 必须要调用measure和layout方法才能成功保存可视组件的截图到png图像文件
                    // 测量View的大小

                    // 发送位置和尺寸到View及其所有的子View
                    //layout(0, 0, measuredWidth, measuredHeight)
//                share_content_root.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    // 获取可视组件的截图
                    bitmap = loadBitmapFromView(this@run)
                }
            })
        }
    }

    private fun loadBitmapFromView(v: View): Bitmap {
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)


        val c = Canvas(bmp)

        /** 如果不设置canvas画布为白色，则生成透明  */
        //        c.drawColor(Color.WHITE);
        v.layout(0, 0, w, h)
        share_content_root.run {
            if (height < nsv.height) {
                share_tv_content.minHeight = nsv.height - height + share_tv_content.height
            }
        }
        v.draw(c)

        return bmp
    }

    override fun loadData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    companion object {
        private const val SHARE_CONTENT = "share_content"
        private const val DOWNLOAD_URL = "download_url"
        fun start(activity: Activity, content: String, downloadUrl: String) {
            val intent = Intent(activity, ShareNewsDetailActivity::class.java)
            intent.putExtra(SHARE_CONTENT, content)
            intent.putExtra(DOWNLOAD_URL, downloadUrl)
            activity.startActivity(intent)
        }
    }
}


