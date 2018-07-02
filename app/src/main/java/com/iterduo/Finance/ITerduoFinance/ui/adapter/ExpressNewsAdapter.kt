package com.iterduo.Finance.ITerduoFinance.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.iterduo.Finance.ITerduoFinance.Constants
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.ExpressNewsItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.ui.activity.ShareNewsDetailActivity
import com.iterduo.Finance.ITerduoFinance.ui.activity.VideoDetailActivity
import com.iterduo.Finance.ITerduoFinance.utils.DateUtils
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder

/**
 * Created by xuhao on 2017/11/23.
 * desc: 首页精选的 Adapter
 */

class ExpressNewsAdapter(data: ArrayList<ExpressNewsItem>) : BaseQuickAdapter<ExpressNewsItem, BaseViewHolder>(R.layout.item_express_news_content, data) {
    override fun convert(helper: BaseViewHolder, item: ExpressNewsItem) {
        setExpressNewsItem(helper, item)
    }

    /**
     * 加载 content item
     */
    private fun setExpressNewsItem(helper: BaseViewHolder, itemData: ExpressNewsItem) {
        val title = itemData.title
        val content = itemData.content
        val author = itemData.author
        val downloadUrl = itemData.url
        val time = DateUtils.getNewsTime(itemData.pub_time)


        helper.setText(R.id.tv_title, title)
        helper.setText(R.id.tv_content, content)

        helper.setText(R.id.tv_time, time ?: "")

        helper.setText(R.id.tv_author_name, author)

        helper.getView<TextView>(R.id.tv_action_share).setOnClickListener {
            // 分享
            //Toast.makeText(holder.itemView.context, "分享", Toast.LENGTH_LONG).show()
            ShareNewsDetailActivity.start(helper.itemView.context as Activity, content, downloadUrl)
        }

    }
}


///**
// * 跳转到视频详情页面播放
// *
// * @param activity
// * @param view
// */
//private fun goToVideoPlayer(activity: Activity, view: View, itemData: HomeBean.Issue.Item) {
//    val intent = Intent(activity, VideoDetailActivity::class.java)
//    intent.putExtra(Constants.BUNDLE_VIDEO_DATA, itemData)
//    intent.putExtra(VideoDetailActivity.TRANSITION, true)
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//        val pair = Pair<View, String>(view, VideoDetailActivity.IMG_TRANSITION)
//        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                activity, pair)
//        ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
//    } else {
//        activity.startActivity(intent)
//        activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
//    }
//}
