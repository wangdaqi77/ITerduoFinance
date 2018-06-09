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
import android.widget.Toast
import com.iterduo.Finance.ITerduoFinance.Constants
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.ExpressNewsItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.ui.activity.NewsDetailActivity
import com.iterduo.Finance.ITerduoFinance.ui.activity.VideoDetailActivity
import com.iterduo.Finance.ITerduoFinance.utils.DateUtils
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter.CommonAdapter

/**
 * Created by xuhao on 2017/11/23.
 * desc: 首页精选的 Adapter
 */

class ExpressNewsAdapter(context: Context, data: ArrayList<ExpressNewsItem>)
    : CommonAdapter<ExpressNewsItem>(context, data, -1) {

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<ExpressNewsItem>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }


    /**
     * 绑定布局
     */
    override fun bindData(holder: ViewHolder, data: ExpressNewsItem, position: Int) {

        setExpressNewsItem(holder, data)

    }

    /**
     *  创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(inflaterView(R.layout.item_express_news_content, parent))


    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view!!
    }


    /**
     * 加载 content item
     */
    private fun setExpressNewsItem(holder: ViewHolder, itemData: ExpressNewsItem) {
        val defPlaceHolder = R.drawable.placeholder_banner
        val title = itemData.title
        val content = itemData.content
        val author = itemData.author
        val time = DateUtils.getNewsTime(itemData.pub_time)


        holder.setText(R.id.tv_title, title)
        holder.setText(R.id.tv_content, content)

        holder.setText(R.id.tv_time, time ?: "")

        holder.setText(R.id.tv_author_name, author)

        holder.setOnItemClickListener(listener = View.OnClickListener {
            NewsDetailActivity.start(holder.itemView.context, itemData.fast_id)
            //goToVideoPlayer(mContext as Activity, holder.getView(R.id.iv_cover_feed), item)
        })
        holder.getView<TextView>(R.id.tv_action_share).setOnClickListener {
            // 分享
            Toast.makeText(holder.itemView.context, "分享", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * 跳转到视频详情页面播放
     *
     * @param activity
     * @param view
     */
    private fun goToVideoPlayer(activity: Activity, view: View, itemData: HomeBean.Issue.Item) {
        val intent = Intent(activity, VideoDetailActivity::class.java)
        intent.putExtra(Constants.BUNDLE_VIDEO_DATA, itemData)
        intent.putExtra(VideoDetailActivity.TRANSITION, true)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val pair = Pair<View, String>(view, VideoDetailActivity.IMG_TRANSITION)
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair)
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
        } else {
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
        }
    }


}