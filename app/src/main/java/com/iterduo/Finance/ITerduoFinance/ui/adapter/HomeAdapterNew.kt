package com.iterduo.Finance.ITerduoFinance.ui.adapter

import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.a91power.a91pos.common.toReadedStr
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.iterduo.Finance.ITerduoFinance.R
import com.iterduo.Finance.ITerduoFinance.glide.GlideApp
import com.iterduo.Finance.ITerduoFinance.glide.GlideRoundTransform
import com.iterduo.Finance.ITerduoFinance.mvp.model.base.IMultiItem
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.HomeBanner
import com.iterduo.Finance.ITerduoFinance.mvp.model.bean.NewsItem
import com.iterduo.Finance.ITerduoFinance.ui.activity.NewsDetailActivity
import com.iterduo.Finance.ITerduoFinance.utils.DateUtils
import com.iterduo.Finance.ITerduoFinance.utils.NewsItemStatusUtils
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/23.
 * desc: 首页精选的 Adapter
 */

class HomeAdapterNew(data: ArrayList<NewsItem>) : BaseMultiItemQuickAdapter<NewsItem, BaseViewHolder>(data) {
    init {
        addItemType(IMultiItem.ITEM_TYPE_HEADER, R.layout.item_home_banner)
        addItemType(IMultiItem.ITEM_TYPE_CONTENT, R.layout.item_home_content)
    }

    override fun convert(helper: BaseViewHolder, item: NewsItem) {
        val itemViewType = helper.itemViewType
        when (itemViewType) {
            IMultiItem.ITEM_TYPE_HEADER -> {
                convertForHeaderData(helper, item.bannerData!!)
            }
            IMultiItem.ITEM_TYPE_CONTENT -> {
                convertForNewsData(helper, item)
            }
        }
    }

    fun convertForHeaderData(helper: BaseViewHolder, header: HomeBanner) {
        val bannerList = header.bannerList
        bannerList.addAll(header.bannerList)
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        //取出banner 显示的 img 和 Title
        Observable.fromIterable(bannerList)
                .subscribe({ list ->
                    bannerFeedList.add(list.img_url)
                    bannerTitleList.add(list.title)
                })

        //设置 banner
        with(helper) {
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
        helper.getView<BGABanner>(R.id.banner).setDelegate { _, imageView, _, i ->
            if (!bannerList[i].jump_url.isNullOrEmpty()) {
                NewsDetailActivity.start(helper.itemView.context, bannerList[i].jump_url)
            }
        }
    }


    /**
     * 加载 content item
     */
    private fun convertForNewsData(helper: BaseViewHolder, itemData: NewsItem) {
        val title = itemData.title
        val author = itemData.author
        val coverFoodUrl = itemData.small_url
        val time = DateUtils.getNewsTime(itemData.pub_time)
        val readedStr = itemData.read_num.toReadedStr()

        if (coverFoodUrl.isNullOrEmpty()) {
            GlideApp.with(mContext)
                    .load(R.drawable.placeholder_image)
                    .optionalTransform(GlideRoundTransform())
                    .placeholder(R.drawable.placeholder_image)
//                    .transition(DrawableTransitionOptions().crossFade())
                    .into(helper.getView(R.id.iv_cover_feed))

        } else {
            // 加载封页图
            GlideApp.with(mContext)
                    .load(coverFoodUrl)
                    .optionalTransform(GlideRoundTransform())
                    .placeholder(R.drawable.placeholder_image)
//                    .transition(DrawableTransitionOptions().crossFade())
                    .into(helper.getView(R.id.iv_cover_feed))
        }
        helper.setText(R.id.tv_title, title)

        helper.setText(R.id.tv_time, time ?: "")

        helper.setText(R.id.tv_author_name, author)

        helper.setText(R.id.tv_read, "${readedStr}人阅读")

        helper.getView<View>(R.id.tv_title).apply {
            val select = NewsItemStatusUtils[context, itemData.url, false] as Boolean
            isSelected = select
        }

    }
}
