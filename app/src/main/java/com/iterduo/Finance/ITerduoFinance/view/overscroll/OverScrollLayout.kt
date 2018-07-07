package com.iterduo.Finance.ITerduoFinance.view.overscroll

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.webkit.WebView
import android.widget.*

/**
 * Created by WongKi on 2018/7/7.
 */
class OverScrollLayout : RelativeLayout {
    private val TAG = "OverScrollLayout"
    private var configuration: ViewConfiguration? = null
    private var child: View? = null
    private var downY: Float = 0.toFloat()
    private var oldY: Float = 0.toFloat()
    private var dealtY: Int = 0
    private var mScroller: Scroller? = null

    private var downX: Float = 0.toFloat()
    private var oldX: Float = 0.toFloat()
    private var dealtX: Int = 0
    private var isVerticalMove: Boolean = false
    private var isHorizontallyMove: Boolean = false

    private var isOverScrollTop: Boolean = false
    private var isOverScrollBottom: Boolean = false
    private var isOverScrollLeft: Boolean = false
    private var isOverScrollRight: Boolean = false

    private var checkScrollDirectionFinish: Boolean = false
    private var canOverScrollHorizontally: Boolean = false
    private var canOverScrollVertical: Boolean = false
    private var baseOverScrollLength: Float = 0.toFloat()

    private var topOverScrollEnable = true
    private var bottomOverScrollEnable = true
    private var leftOverScrollEnable = true
    private var rightOverScrollEnable = true
    private var onOverScrollListener: OnOverScrollListener? = null
    private var checkListener: OverScrollCheckListener? = null

    var SCROLL_VERTICAL = LinearLayout.VERTICAL
    var SCROLL_HORIZONTAL = LinearLayout.HORIZONTAL

    private var fraction = 0.5f
    private var finishOverScroll: Boolean = false
    private var abortScroller: Boolean = false
    private var shouldSetScrollerStart: Boolean = false
    private var disallowIntercept: Boolean = false

    private var detector: GestureDetector? = null

    private var flingRunnable: FlingRunnable? = null
    private var flingScroller: OverScroller? = null
    private var overScrollRunnable: OverScrollRunnable? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {

        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init()
    }

    private fun init() {
        configuration = ViewConfiguration.get(getContext())
        mScroller = Scroller(getContext(), OvershootInterpolator(0.75f))
        flingRunnable = FlingRunnable()
        overScrollRunnable = OverScrollRunnable()
        flingScroller = OverScroller(getContext())
        detector = GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (isOverScrollTop || isOverScrollBottom || isOverScrollLeft || isOverScrollRight) {
                    return false
                }
                //
                flingRunnable!!.start(velocityX, velocityY)
                return false
            }
        })
    }

    protected override fun onFinishInflate() {
        val childCount = getChildCount()
        if (childCount > 1) {
            throw IllegalStateException("OverScrollLayout only can host 1 element")
        } else if (childCount == 1) {
            child = getChildAt(0)
            child!!.overScrollMode = View.OVER_SCROLL_NEVER
        }
        super.onFinishInflate()
    }

    fun setDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }

    fun isTopOverScrollEnable(): Boolean {
        return topOverScrollEnable
    }

    /**
     * @param topOverScrollEnable true can over scroll top false otherwise
     */
    fun setTopOverScrollEnable(topOverScrollEnable: Boolean) {
        this.topOverScrollEnable = topOverScrollEnable
    }

    fun isBottomOverScrollEnable(): Boolean {
        return bottomOverScrollEnable
    }

    /**
     * @param bottomOverScrollEnable true can over scroll bottom false otherwise
     */
    fun setBottomOverScrollEnable(bottomOverScrollEnable: Boolean) {
        this.bottomOverScrollEnable = bottomOverScrollEnable
    }

    fun isLeftOverScrollEnable(): Boolean {
        return leftOverScrollEnable
    }

    /**
     * @param leftOverScrollEnable true can over scroll left false otherwise
     */
    fun setLeftOverScrollEnable(leftOverScrollEnable: Boolean) {
        this.leftOverScrollEnable = leftOverScrollEnable
    }

    fun isRightOverScrollEnable(): Boolean {
        return rightOverScrollEnable
    }

    /**
     * @param rightOverScrollEnable true can over scroll right false otherwise
     */
    fun setRightOverScrollEnable(rightOverScrollEnable: Boolean) {
        this.rightOverScrollEnable = rightOverScrollEnable
    }

    fun getOnOverScrollListener(): OnOverScrollListener? {
        return onOverScrollListener
    }

    /**
     * @param onOverScrollListener
     */
    fun setOnOverScrollListener(onOverScrollListener: OnOverScrollListener) {
        this.onOverScrollListener = onOverScrollListener
    }

    fun getOverScrollCheckListener(): OverScrollCheckListener? {
        return checkListener
    }

    /**
     * @param checkListener for custom view check over scroll
     */
    fun setOverScrollCheckListener(checkListener: OverScrollCheckListener) {
        this.checkListener = checkListener
    }

    fun getFraction(): Float {
        return fraction
    }

    /**
     * @param fraction the fraction for over scroll.it is num[0f,1f],
     */
    fun setFraction(fraction: Float) {
        if (fraction < 0 || fraction > 1) {
            return
        }
        this.fraction = fraction
    }

    private fun checkCanOverScrollDirection() {
        if (checkScrollDirectionFinish) {
            return
        }
        if (checkListener != null) {
            val mOrientation = checkListener!!.contentViewScrollDirection
            canOverScrollHorizontally = RecyclerView.HORIZONTAL == mOrientation
            canOverScrollVertical = RecyclerView.VERTICAL == mOrientation
        } else if (child is AbsListView || child is ScrollView || child is WebView) {
            canOverScrollHorizontally = false
            canOverScrollVertical = true
        } else if (child is RecyclerView) {
            val recyclerView = child as RecyclerView?
            val layoutManager = recyclerView!!.layoutManager
            var mOrientation = -1
            if (layoutManager is StaggeredGridLayoutManager) {
                mOrientation = layoutManager.orientation
            } else if (layoutManager is LinearLayoutManager) {
                mOrientation = layoutManager.orientation
            }
            canOverScrollHorizontally = RecyclerView.HORIZONTAL == mOrientation
            canOverScrollVertical = RecyclerView.VERTICAL == mOrientation
        } else if (child is HorizontalScrollView) {
            canOverScrollHorizontally = true
            canOverScrollVertical = false
        } else if (child is ViewPager) {
            //forbid ViewPager  over scroll
            canOverScrollHorizontally = false
            canOverScrollVertical = false
        } else {
            canOverScrollHorizontally = false
            canOverScrollVertical = true
        }
        checkScrollDirectionFinish = true
        if (canOverScrollVertical) {
            baseOverScrollLength = getHeight().toFloat()
        } else {
            baseOverScrollLength = getWidth().toFloat()
        }
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            val scrollerY = mScroller!!.currY
            scrollTo(mScroller!!.currX, scrollerY)
            postInvalidate()
        } else {
            if (abortScroller) {
                abortScroller = false
                return
            }
            if (finishOverScroll) {
                isOverScrollTop = false
                isOverScrollBottom = false
                isOverScrollLeft = false
                isOverScrollRight = false
                finishOverScroll = false
            }
        }

    }

    protected fun mSmoothScrollTo(fx: Int, fy: Int) {
        val dx = fx - mScroller!!.finalX
        val dy = fy - mScroller!!.finalY
        mSmoothScrollBy(dx, dy)
    }


    protected fun mSmoothScrollBy(dx: Int, dy: Int) {
        mScroller!!.startScroll(mScroller!!.finalX, mScroller!!.finalY, dx, dy)
        invalidate()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (disallowIntercept) {
            return super.dispatchTouchEvent(ev)
        }

        detector!!.onTouchEvent(ev)

        val action = ev.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldY = 0f
                oldX = 0f
            }
            MotionEvent.ACTION_DOWN -> {
                flingRunnable!!.abort()
                downY = ev.y
                oldY = 0f
                dealtY = mScroller!!.currY
                if (dealtY == 0) {
                    isVerticalMove = false
                } else {
                    shouldSetScrollerStart = true
                    abortScroller = true
                    mScroller!!.abortAnimation()
                }

                downX = ev.x
                oldX = 0f
                dealtX = mScroller!!.currX
                if (dealtX == 0) {
                    isHorizontallyMove = false
                } else {
                    shouldSetScrollerStart = true
                    abortScroller = true
                    mScroller!!.abortAnimation()
                }
                if (isOverScrollTop || isOverScrollBottom || isOverScrollLeft || isOverScrollRight) {
                    return true
                }
                checkCanOverScrollDirection()
            }

            MotionEvent.ACTION_MOVE -> {

                if (!canOverScroll()) {
                    return super.dispatchTouchEvent(ev)
                }

                if (canOverScrollVertical) {
                    if (isOverScrollTop || isOverScrollBottom) {
                        if (onOverScrollListener != null) {
                            if (isOverScrollTop) {
                                onOverScrollListener!!.onTopOverScroll()
                            }
                            if (isOverScrollBottom) {
                                onOverScrollListener!!.onBottomOverScroll()
                            }
                        }
                        if (shouldSetScrollerStart) {
                            shouldSetScrollerStart = false
                            mScroller!!.startScroll(dealtX, dealtY, 0, 0)
                        }
                        if (oldY == 0f) {
                            oldY = ev.y
                            return true
                        }
                        dealtY += getDealt(oldY - ev.y, dealtY.toFloat()).toInt()
                        oldY = ev.y
                        if (isOverScrollTop && dealtY > 0) {
                            dealtY = 0
                        }
                        if (isOverScrollBottom && dealtY < 0) {
                            dealtY = 0
                        }
                        overScroll(dealtX, dealtY)
                        if (isOverScrollTop && dealtY == 0 && !isOverScrollBottom || isOverScrollBottom && dealtY == 0 && !isOverScrollTop) {
                            oldY = 0f
                            isOverScrollTop = false
                            isOverScrollBottom = false
                            return if (!isChildCanScrollVertical()) {
                                true
                            } else super.dispatchTouchEvent(resetVertical(ev))
                        }
                        return true
                    } else {
                        checkMoveDirection(ev.x, ev.y)
                        if (oldY == 0f) {
                            oldY = ev.y
                            return true
                        }
                        val tempOverScrollTop = isTopOverScroll(ev.y)
                        if (!isOverScrollTop && tempOverScrollTop) {
                            oldY = ev.y
                            isOverScrollTop = tempOverScrollTop
                            ev.action = MotionEvent.ACTION_CANCEL
                            super.dispatchTouchEvent(ev)
                            return true
                        }
                        isOverScrollTop = tempOverScrollTop
                        val tempOverScrollBottom = isBottomOverScroll(ev.y)
                        if (!isOverScrollBottom && tempOverScrollBottom) {
                            oldY = ev.y
                            isOverScrollBottom = tempOverScrollBottom
                            ev.action = MotionEvent.ACTION_CANCEL
                            super.dispatchTouchEvent(ev)
                            return true
                        }
                        isOverScrollBottom = tempOverScrollBottom
                        oldY = ev.y
                    }
                } else if (canOverScrollHorizontally) {
                    if (isOverScrollLeft || isOverScrollRight) {
                        if (onOverScrollListener != null) {
                            if (isOverScrollLeft) {
                                onOverScrollListener!!.onLeftOverScroll()
                            }
                            if (isOverScrollRight) {
                                onOverScrollListener!!.onRightOverScroll()
                            }
                        }
                        if (shouldSetScrollerStart) {
                            shouldSetScrollerStart = false
                            mScroller!!.startScroll(dealtX, dealtY, 0, 0)
                        }
                        if (oldX == 0f) {
                            oldX = ev.x
                            return true
                        }
                        dealtX += getDealt(oldX - ev.x, dealtX.toFloat()).toInt()
                        oldX = ev.x
                        if (isOverScrollLeft && dealtX > 0) {
                            dealtX = 0
                        }
                        if (isOverScrollRight && dealtX < 0) {
                            dealtX = 0
                        }
                        overScroll(dealtX, dealtY)
                        if (isOverScrollLeft && dealtX == 0 && !isOverScrollRight || isOverScrollRight && dealtX == 0 && !isOverScrollLeft) {
                            oldX = 0f
                            isOverScrollRight = false
                            isOverScrollLeft = false
                            return if (!isChildCanScrollHorizontally()) {
                                true
                            } else super.dispatchTouchEvent(resetHorizontally(ev))
                        }
                        return true
                    } else {
                        checkMoveDirection(ev.x, ev.y)
                        if (oldX == 0f) {
                            oldX = ev.x
                            return true
                        }
                        val tempOverScrollLeft = isLeftOverScroll(ev.x)
                        if (!isOverScrollLeft && tempOverScrollLeft) {
                            oldX = ev.x
                            isOverScrollLeft = tempOverScrollLeft
                            ev.action = MotionEvent.ACTION_CANCEL
                            super.dispatchTouchEvent(ev)
                            return true
                        }
                        isOverScrollLeft = tempOverScrollLeft
                        val tempOverScrollRight = isRightOverScroll(ev.x)
                        if (!isOverScrollRight && tempOverScrollRight) {
                            oldX = ev.x
                            isOverScrollRight = tempOverScrollRight
                            ev.action = MotionEvent.ACTION_CANCEL
                            super.dispatchTouchEvent(ev)
                            return true
                        }
                        isOverScrollRight = tempOverScrollRight
                        oldX = ev.x
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                oldY = 0f
                oldX = 0f
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                finishOverScroll = true
                mSmoothScrollTo(0, 0)
            }
        }
        return super.dispatchTouchEvent(ev)

    }

    private fun getDealt(dealt: Float, distance: Float): Float {
        if (dealt * distance < 0)
            return dealt
        //x 为0的时候 y 一直为0, 所以当x==0的时候,给一个0.1的最小值
        val x = Math.min(Math.max(Math.abs(distance).toDouble(), 0.1) / Math.abs(baseOverScrollLength), 1.0).toFloat()
        val y = Math.min(AccelerateInterpolator(0.15f).getInterpolation(x), 1f)
        return dealt * (1 - y)
    }

    private fun resetVertical(event: MotionEvent): MotionEvent {
        oldY = 0f
        dealtY = 0
        event.action = MotionEvent.ACTION_DOWN
        super.dispatchTouchEvent(event)
        event.action = MotionEvent.ACTION_MOVE
        return event
    }

    private fun resetHorizontally(event: MotionEvent): MotionEvent {
        oldX = 0f
        dealtX = 0
        event.action = MotionEvent.ACTION_DOWN
        super.dispatchTouchEvent(event)
        event.action = MotionEvent.ACTION_MOVE
        return event
    }

    private fun canOverScroll(): Boolean {
        return child != null
    }


    private fun overScroll(dealtX: Int, dealtY: Int) {
        mSmoothScrollTo(dealtX, dealtY)
    }

    private fun isTopOverScroll(currentY: Float): Boolean {
        if (isOverScrollTop) {
            return true
        }
        if (!topOverScrollEnable || !isVerticalMove) {
            return false
        }
        val dealtY = oldY - currentY
        return dealtY < 0 && !canChildScrollUp()
    }

    private fun isBottomOverScroll(currentY: Float): Boolean {
        if (isOverScrollBottom) {
            return true
        }
        if (!bottomOverScrollEnable || !isVerticalMove) {
            return false
        }
        val dealtY = oldY - currentY
        return dealtY > 0 && !canChildScrollDown()
    }

    private fun isLeftOverScroll(currentX: Float): Boolean {
        if (isOverScrollLeft) {
            return true
        }
        if (!leftOverScrollEnable || !isHorizontallyMove) {
            return false
        }
        val dealtX = oldX - currentX
        return dealtX < 0 && !canChildScrollLeft()
    }

    private fun isRightOverScroll(currentX: Float): Boolean {
        if (!rightOverScrollEnable || !isHorizontallyMove) {
            return false
        }
        val dealtX = oldX - currentX
        return dealtX > 0 && !canChildScrollRight()
    }

    private fun isChildCanScrollVertical(): Boolean {
        return canChildScrollDown() || canChildScrollUp()
    }

    private fun isChildCanScrollHorizontally(): Boolean {
        return canChildScrollLeft() || canChildScrollRight()
    }

    private fun checkMoveDirection(currentX: Float, currentY: Float) {
        if (isVerticalMove || isHorizontallyMove) {
            return
        }
        if (canOverScrollVertical) {
            val dealtY = currentY - downY
            isVerticalMove = Math.abs(dealtY) >= configuration!!.scaledTouchSlop
        } else if (canOverScrollHorizontally) {
            val dealtX = currentX - downX
            isHorizontallyMove = Math.abs(dealtX) >= configuration!!.scaledTouchSlop
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }

    /**
     * 是否能下拉
     *
     * @return
     */
    private fun canChildScrollUp(): Boolean {
        if (checkListener != null) {
            return checkListener!!.canScrollUp()
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (child is AbsListView) {
                val absListView = child as AbsListView?
                return absListView!!.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                        .top < absListView.paddingTop)
            }
        }
        return ViewCompat.canScrollVertically(child!!, -1)

    }


    /**
     * 是否能上拉
     *
     * @return
     */
    private fun canChildScrollDown(): Boolean {
        if (checkListener != null) {
            return checkListener!!.canScrollDown()
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (child is AbsListView) {
                val absListView = child as AbsListView?
                return absListView!!.childCount > 0 && (absListView.lastVisiblePosition < absListView.childCount - 1 || absListView.getChildAt(absListView.childCount - 1).bottom > absListView.height - absListView.paddingBottom)
            }
        }
        return ViewCompat.canScrollVertically(child!!, 1)

    }

    /**
     * 是否能左拉
     *
     * @return
     */
    private fun canChildScrollLeft(): Boolean {
        return if (checkListener != null) {
            checkListener!!.canScrollLeft()
        } else ViewCompat.canScrollHorizontally(child!!, -1)
    }

    /**
     * 是否能右拉
     *
     * @return
     */
    private fun canChildScrollRight(): Boolean {
        return if (checkListener != null) {
            checkListener!!.canScrollRight()
        } else ViewCompat.canScrollHorizontally(child!!, 1)
    }

    private fun startOverScrollAim(currVelocity: Float) {
        val speed = currVelocity / configuration!!.scaledMaximumFlingVelocity
        if (canOverScrollVertical) {
            if (!canChildScrollUp()) {
                overScrollRunnable!!.start(0f, -speed)
            } else {
                overScrollRunnable!!.start(0f, speed)
            }
        } else {
            if (canChildScrollRight()) {
                overScrollRunnable!!.start(-speed, 0f)
            } else {
                overScrollRunnable!!.start(speed, 0f)
            }
        }
    }

    private inner class OverScrollRunnable : Runnable {
        private val duration: Long = 160
        private var speedX: Float = 0.toFloat()
        private var speedY: Float = 0.toFloat()
        private var timePass: Long = 0
        private var startTime: Long = 0
        private var distanceX: Int = 0
        private var distanceY: Int = 0

        fun start(speedX: Float, speedY: Float) {
            this.speedX = speedX
            this.speedY = speedY
            startTime = System.currentTimeMillis()
            run()
        }

        override fun run() {
            timePass = System.currentTimeMillis() - startTime
            if (timePass < duration) {
                distanceY = (DELAY_TIME * speedY).toInt()
                distanceX = (DELAY_TIME * speedX).toInt()
                mSmoothScrollBy(distanceX, distanceY)
                postDelayed(this, DELAY_TIME)
            } else if (timePass > duration) {
                mSmoothScrollTo(0, 0)
            }
        }

        private val DELAY_TIME: Long = 20
    }

    private inner class FlingRunnable : Runnable {
        val DELAY_TIME: Long = 40
        private var abort: Boolean = false
        private val mMinimumFlingVelocity = configuration!!.scaledMinimumFlingVelocity

        fun start(velocityX: Float, velocityY: Float) {
            abort = false
            val velocity = if (canOverScrollVertical) velocityY else velocityX
            flingScroller!!.fling(0, 0, 0, velocity.toInt(), 0, 0,
                    Integer.MIN_VALUE, Integer.MAX_VALUE)
            postDelayed(this, 40)
        }

        override fun run() {
            if (!abort && flingScroller!!.computeScrollOffset()) {
                var scrollEnd = false
                if (canOverScrollVertical) {
                    scrollEnd = !canChildScrollDown() || !canChildScrollUp()
                } else {
                    scrollEnd = !canChildScrollLeft() || !canChildScrollRight()
                }

                val currVelocity = flingScroller!!.currVelocity
                if (scrollEnd) {
                    if (currVelocity > mMinimumFlingVelocity) {
                        startOverScrollAim(currVelocity)
                    }
                } else {
                    if (currVelocity > mMinimumFlingVelocity) {
                        postDelayed(this, DELAY_TIME)
                    }
                }

            }


        }

        fun abort() {
            abort = true
        }

    }
}