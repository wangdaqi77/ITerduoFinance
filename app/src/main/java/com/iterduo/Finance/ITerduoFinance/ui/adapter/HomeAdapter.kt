package com.iterduo.Finance.ITerduoFinance.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.a91power.a91pos.common.toReadedStr
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.glide.GlideApp
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IFooterItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IMultiItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.BannerItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBanner
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.News
import com.iterduo.Finance.ITerduoFinance.utils.DateUtils
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.ViewHolder
import com.iterduo.Finance.ITerduoFinance.view.recyclerview.adapter.CommonMultiItemAdapter
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/23.
 * desc: 首页精选的 Adapter
 */

class HomeAdapter(context: Context, val bannerList: ArrayList<HomeBanner>, data: ArrayList<News>)
    : CommonMultiItemAdapter<HomeBanner, News, IFooterItem>(context, bannerList, data, null, -1) {

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: List<News>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getMultiItemLayout(viewType: Int): Int {
        return when (viewType) {
            IMultiItem.ITEM_TYPE_CONTENT -> R.layout.item_home_content
            else -> R.layout.item_home_content
        }
    }

    override fun getHeaderItemLayout(): Int = R.layout.item_home_banner
    override fun getFooterItemLayout(): Int = -1
    override fun bindFooterData(holder: ViewHolder, f: IFooterItem, headerPosition: Int) {}

    /**
     * 绑定布局
     */
    override fun bindData(holder: ViewHolder, data: News, position: Int) {
        setNewsItem(holder, data)
    }

    override fun bindHeaderData(holder: ViewHolder, header: HomeBanner, headerPosition: Int) {
        val bannerList = header.bannerList
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        //取出banner 显示的 img 和 Title
        Observable.fromIterable(bannerList)
                .subscribe({ list ->
                    bannerFeedList.add(list.img_url)
                    bannerTitleList.add(list.jump_url)
                })

        //设置 banner
        with(holder) {
            getView<BGABanner>(R.id.banner).run {
                setAutoPlayAble(bannerFeedList.size > 1)
                setData(bannerFeedList, bannerTitleList)
                setAdapter(object : BGABanner.Adapter<ImageView, String> {
                    override fun fillBannerItem(bgaBanner: BGABanner?, imageView: ImageView?, feedImageUrl: String?, position: Int) {
                        GlideApp.with(mContext)
                                .load(feedImageUrl)
                                .transition(DrawableTransitionOptions().crossFade())
                                .placeholder(R.drawable.placeholder_banner)
                                .into(imageView)

                    }
                })
            }
        }
        //没有使用到的参数在 kotlin 中用"_"代替
        holder.getView<BGABanner>(R.id.banner).setDelegate { _, imageView, _, i ->
            goToNewsDetail(mContext as Activity, imageView, bannerList[i].jump_url)
        }
    }


    /**
     * 加载 content item
     */
    private fun setNewsItem(holder: ViewHolder, itemData: News) {
        val defPlaceHolder = R.drawable.placeholder_banner
        val title = itemData.title ?: ""
        val author = itemData.author ?: ""
        val coverFoodUrl = itemData.small_url ?: ""
        val time = DateUtils.getNewsTime(itemData.pub_time)
        val readedStr = itemData.read_num.toReadedStr()



        if (coverFoodUrl.isNullOrEmpty()) {
            GlideApp.with(mContext)
                    .load(defPlaceHolder)
                    .placeholder(defPlaceHolder).circleCrop()
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(holder.getView(R.id.iv_cover_feed))

        } else {
            // 加载封页图
            GlideApp.with(mContext)
                    .load(coverFoodUrl)
                    .placeholder(R.drawable.placeholder_banner)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(holder.getView(R.id.iv_cover_feed))
        }
        holder.setText(R.id.tv_title, title ?: "")

        holder.setText(R.id.tv_time, time ?: "")

        holder.setText(R.id.tv_author_name, author ?: "")

        holder.setText(R.id.tv_read, "${readedStr}人阅读")

        holder.setOnItemClickListener(listener = View.OnClickListener {
            goToNewsDetail(mContext as Activity, holder.getView(R.id.iv_cover_feed), itemData.jump_url)
        })


    }

    /**
     * 跳转到视频详情页面播放
     *
     * @param activity
     * @param view
     */
    private fun goToNewsDetail(activity: Activity, view: View, url: String) {
//        val intent = Intent(activity, VideoDetailActivity::class.java)
//        intent.putExtra(Constants.BUNDLE_VIDEO_DATA, itemData)
//        intent.putExtra(VideoDetailActivity.Companion.TRANSITION, true)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            val pair = Pair<View, String>(view, VideoDetailActivity.Companion.IMG_TRANSITION)
//            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity, pair)
//            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
//        } else {
//            activity.startActivity(intent)
//            activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
//        }
    }


}

