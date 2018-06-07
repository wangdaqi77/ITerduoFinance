package com.iterduo.Finance.ITerduoFinance.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.bingoogolapple.bgabanner.BGABanner
import com.a91power.a91pos.common.toReadedStr
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.iterduo.Finance.ITerduoFinance.Constants
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.glide.GlideApp
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBean
import com.iterduo.Finance.ITerduoFinance.ui.activity.VideoDetailActivity
import com.iterduo.Finance.ITerduoFinance.utils.DateUtils
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter.CommonAdapter
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/23.
 * desc: 首页精选的 Adapter
 */

class ExpressNewsAdapter(context: Context, data: ArrayList<HomeBean.Issue.Item>)
    : CommonAdapter<HomeBean.Issue.Item>(context, data, -1) {


    // banner 作为 RecyclerView 的第一项
    var bannerItemSize = 0

    companion object {
        private val ITEM_TYPE_BANNER = 1   //textHeader
        private val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        private val ITEM_TYPE_CONTENT = 3    //item
    }

    /**
     * 设置 Banner 大小
     */
    fun setBannerSize(count: Int) {
        bannerItemSize = count
    }

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }


    /**
     * 得到 Item 的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 ->
                ITEM_TYPE_BANNER
            mData[position + bannerItemSize - 1].type == "textHeader" ->
                ITEM_TYPE_TEXT_HEADER
            else ->
                ITEM_TYPE_CONTENT
        }
    }


    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return when {
            mData.size > bannerItemSize -> mData.size - bannerItemSize + 1
            mData.isEmpty() -> 0
            else -> 1
        }
    }


    /**
     * 绑定布局
     */
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when (getItemViewType(position)) {
        //TextHeader
            ITEM_TYPE_TEXT_HEADER -> {
                holder.setText(R.id.tvHeader, mData[position + bannerItemSize - 1].data?.text ?: "")
            }

        //content
            ITEM_TYPE_CONTENT -> {
                setVideoItem(holder, mData[position + bannerItemSize - 1])
            }


        }

    }

    /**
     *  创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TYPE_BANNER ->
                ViewHolder(inflaterView(R.layout.item_home_banner, parent))
            ITEM_TYPE_TEXT_HEADER ->
                ViewHolder(inflaterView(R.layout.item_express_news_header, parent))
            else ->
                ViewHolder(inflaterView(R.layout.item_express_news_content, parent))
        }
    }


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
    private fun setVideoItem(holder: ViewHolder, item: HomeBean.Issue.Item) {
        val itemData = item.data
        val defPlaceHolder = R.drawable.placeholder_banner
        val title = itemData?.title ?: ""
        val content =
                "据全球审计巨头德勤（Deloitte）的报告《区块链技术演进：从GitHub平台得出的见解》显示，2017年，仅前6个月，在GitHub上新创建的区块链项目就达到近25000个，与2016年全年的新建区块链项目数量几乎持平。与此同时，现阶段能主网上线的区块链项目却寥若晨星。也正因为能够看到具体落地的区块链应用还太少，才导致现阶段社会上对区块链的负能量过多。"
        val author = itemData?.author?.name
        val time = DateUtils.getNewsTime(5254543)


        holder.setText(R.id.tv_title, itemData?.title ?: "")
        holder.setText(R.id.tv_content, content)

        holder.setText(R.id.tv_time, time ?: "")

        holder.setText(R.id.tv_author_name, author ?: "")

        holder.setOnItemClickListener(listener = View.OnClickListener {
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