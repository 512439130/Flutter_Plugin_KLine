package com.zb.module.kline_module.view.klinechart.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.zb.module.kline_module.view.klinechart.utils.SharedPreKlineUtils;

import androidx.core.view.GestureDetectorCompat;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 可以滑动和放大的view
 * @update
 */
public abstract class ScrollAndScaleView extends RelativeLayout implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = "ScrollAndScaleView";
    protected int mScrollX = 0;
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;

    public boolean isLongPress = false;

    private OverScroller mScroller;

    protected boolean touch = false;

    protected float mScaleX = 1f; //缩放比例

    protected float mScaleFocusX = 0; //按压X
    protected float mScaleFocusY = 0; //按压Y

    protected float mScaleXMax = 5f; //缩放最大值

    protected float mScaleXMin = 1f; //缩放最小值

    private boolean mMultipleTouch = false;

    private boolean mScrollEnable = true;

    private boolean mScaleEnable = true;

    private boolean mScrollOnFling = true;  //是否开启顺滑滑动

    private boolean isInnerLayerSlidingConflict = false;  // 是否内层处理滑动冲突

    public void setLongPress(boolean isLongPress) {
        this.isLongPress = isLongPress;
    }
    /**
     * 是否长按
     */
    public boolean isLongPress() {
        return isLongPress;
    }

    public boolean isInnerLayerSlidingConflict() {
        return isInnerLayerSlidingConflict;
    }

    public void setInnerLayerSlidingConflict(boolean innerLayerSlidingConflict) {
        isInnerLayerSlidingConflict = innerLayerSlidingConflict;
    }

    private PointF mTouchPoint;  //保存用户点击的坐标

    public PointF getTouchPoint() {
        return mTouchPoint;
    }

    public void setTouchPoint(PointF mTouchPoint) {
        Log.d(TAG,"getTouchPoint:"+mTouchPoint);
        this.mTouchPoint = mTouchPoint;
    }

    public ScrollAndScaleView(Context context) {
        super(context);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     * 单击视为双击
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        onLongPress(motionEvent);
        //两次点击距离远时消失按压素有绘制的View（还有问题，待修改）
//        if (touchX != 0 && touchY != 0) {
//            if (Math.abs(motionEvent.getX() - touchX) < 300 && Math.abs(motionEvent.getY() - touchY) < 300) {
//                onLongPress(motionEvent);
//            } else {
//                if(isLongPress()){
//                    setLongPress(false);
//                    touchX = 0;
//                    touchY = 0;
//                    invalidate();
//                }
//            }
//        }else{
//            onLongPress(motionEvent);
//        }
//
//        touchX = motionEvent.getX();
//        touchY = motionEvent.getY();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.d("test","开始滚动-onScroll："+"-distanceX:"+distanceX+"-distanceY:"+distanceY);
        onScrollStart();
        //加速滑动时，隐藏按压辅助线（X,Y）
        if (isLongPress()) {
            setLongPress(false);
            Log.d(TAG,"setLongPress-onScroll");
            invalidate();
        }
        if (!isLongPress() && !isMultipleTouch()) {
            scrollBy(Math.round(distanceX), 0);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(TAG,"onLongPress");
        setLongPress(true);
    }

    //滑动加速器(调整滑动速度)
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        System.out.println("onFling" + "-velocityX:" + velocityX + "-velocityY:" + velocityY);
        if (!isTouch() && isScrollEnable()) {
//            mScroller.fling(mScrollX, 0, Math.round(velocityX / mScaleX * 1.2f), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            mScroller.fling(mScrollX, 0, Math.round(velocityX / mScaleX * 1.6f), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
        return true;
    }

    @Override
    public void computeScroll() {
//        Log.d("test", "滚动结束-computeScroll");
        onScrollEnd();
        if (isScrollOnFling()) {
            if (mScroller.computeScrollOffset()) {
                if (!isTouch()) {
                    scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                } else {
                    mScroller.forceFinished(true);
                }
            }
        }
    }

    public boolean isScrollOnFling() {
        return mScrollOnFling;
    }

    public void setScrollOnFling(boolean mScrollOnFling) {
        this.mScrollOnFling = mScrollOnFling;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG,"mScaleX：" + mScaleX);
        if (!isLongPress()) {  //解决快速滑动，按压会引起的多点触发问题

            if (!isScaleEnable()) {
                return false;
            }
            float oldScale = mScaleX;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                detector.setQuickScaleEnabled(true);
            }

            mScaleX = mScaleX * detector.getScaleFactor();

            mScaleFocusX = detector.getFocusX();
            mScaleFocusY = detector.getFocusY();
//            Log.d(TAG,"mScaleFocusX：" + mScaleFocusX);
//            Log.d(TAG,"mScaleFocusY：" + mScaleFocusY);

            if (mScaleX < mScaleXMin) {
                mScaleX = mScaleXMin;
            } else if (mScaleX > mScaleXMax) {
                mScaleX = mScaleXMax;
            } else {
                onScaleChanged(mScaleX, oldScale);
            }
        } else {
//            System.out.println("mScaleX。。。");
        }
        return true;
    }


    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    protected void onScaleChanged(float scale, float oldScale) {
        updateScaleData();
//        invalidate();
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    @Override
    public void scrollBy(int x, int y) {
        scrollTo(mScrollX - Math.round(x / mScaleX), 0);
    }

    /**
     * 滑动View触发
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
//        Log.d("scrollTo-test", "-x:" + x + "-y:" + y);
//        Log.d("scrollTo-test", "-getMinScrollX:" + getMinScrollX());
//        Log.d("scrollTo-test", "-getMaxScrollX:" + getMaxScrollX());
        mScrollX = x;
        if (!isScrollEnable()) {
            mScroller.forceFinished(true);
            return;
        }
        onRightSpacingShowOrHide();
        if (mScrollX < getMinScrollX()) {
            mScrollX = getMinScrollX();
            onRightSide();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            onLeftSide();
            mScroller.forceFinished(true);
        }
        onScrollChanged(mScrollX, 0, mScrollX, 0);
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 按压手指超过1个
        if (event.getPointerCount() > 1) {
            setLongPress(false);
//            Log.d(TAG,"setLongPress-event.getPointerCount() > 1");
        }
        //长按，点击，长按取消，滑动取消，长按滑动等事件区分 down move up
        //缩放 双指：ACTION_POINTER_UP
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"onTouchEvent-ACTION_DOWN");
                touch = true;
                setTouchPoint(new PointF(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"onTouchEvent-ACTION_MOVE");
                if (isLongPress()) {
                    onLongPress(event);
                    vibration(false);
                    setTouchPoint(new PointF(event.getX(), event.getY()));
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG,"onTouchEvent-ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:  //多个触摸点存在的情况下，其中一个触摸点消失了
                Log.d(TAG,"onTouchEvent-ACTION_POINTER_UP");
                if (!isLongPress()) {
                    //放大缩小时触发
//                    invalidate();
                    //每次手指离开时，保存最新的缩放比例
                    SharedPreKlineUtils.getInstance().putString("KLINE_SCALE_X_COUNT", String.valueOf(mScaleX), getContext());
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"onTouchEvent-ACTION_UP");
                touch = false;
                invalidate();

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"onTouchEvent-ACTION_CANCEL");
                setLongPress(false);
                touch = false;
                invalidate();
                break;
            default:
                break;
        }
        mMultipleTouch = event.getPointerCount() > 1;
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }


    public float positionX;
    public float positionY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isInnerLayerSlidingConflict()){
            float x = ev.getX();
            float y = ev.getY();
            int action = ev.getAction();

            if (isTouchPointInView(this, (int) ev.getRawX(), (int) ev.getRawY())) {
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.d(TAG, "ACTION_DOWN");
                        positionX = x;
                        positionY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = Math.abs(x - positionX);
                        float deltaY = Math.abs(y - positionY);

//                        Log.d(TAG, "ACTION_MOVE:" + "deltaX:" + deltaX + "deltaY:" + deltaY);
                        if (!isLongPress()) {
                            if (deltaX > deltaY) { // 水平滑动时，滑动事件子View自己处理
                                if (deltaX > 10) {
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                }
                            } else{ //上下滑动时，滑动事件父View处理。
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }else{
                            // 长按滑动时，滑动事件子View自己处理
                            getParent().requestDisallowInterceptTouchEvent(true);
                            if (deltaX > deltaY) { //上下滑动时
                                if (deltaX > 10) { //水平滑动
//                                    Log.d(TAG, "长按水平滑动");
                                } else{
//                                    Log.d(TAG, "长按水平轻滑");
                                }
                            } else{
                                  //有问题，会影响上下长按
//                                if(isLongPress()){
//                                    Log.d(TAG, "上下滑动清空长按view");
//                                    setLongPress(false);
//                                    invalidate();
//                                }
                                if (deltaY > 10) { //水平滑动
//                                    Log.d(TAG, "长按上下滑动");
                                } else{
//                                    Log.d(TAG, "长按上下轻滑");
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.d(TAG, "ACTION_UP");
                        break;
                    case MotionEvent.ACTION_CANCEL:
//                        Log.d(TAG, "ACTION_CANCEL");
                        break;
                }
            }
        }else{
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        return y >= top && y <= bottom && x >= left && x <= right;
    }

    /**
     * 震动
     */
    abstract public void vibration(boolean isFirst);


    /**
     * 滑到了最左边
     */
    abstract public void onLeftSide();

    /**
     * 滑到了最右边
     */
    abstract public void onRightSide();


    /**
     * 滑到右边留出的空隙处
     */
    abstract public void onRightSpacingShowOrHide();

    /**
     * 正在滑动
     */
    abstract public void onScrollStart();

    /**
     * 滚动结束
     */
    abstract public void onScrollEnd();


    /**
     * 是否在触摸中
     *
     * @return
     */
    public boolean isTouch() {
        return touch;
    }

    /**
     * 获取位移的最小值
     *
     * @return
     */
    public abstract int getMinScrollX();


    /**
     * 获取位移的最大值
     *
     * @return
     */
    public abstract int getMaxScrollX();


    /**
     * 设置ScrollX
     *
     * @param scrollX
     */
    public void setScrollX(int scrollX) {
        this.mScrollX = scrollX;
        scrollTo(scrollX, 0);
    }

    /**
     * 是否是多指触控
     *
     * @return
     */
    public boolean isMultipleTouch() {
        return mMultipleTouch;
    }

    protected void checkAndFixScrollX() {
        if (mScrollX < getMinScrollX()) {
            mScrollX = getMinScrollX();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            mScroller.forceFinished(true);
        }
    }

    public float getScaleXMax() {
        return mScaleXMax;
    }

    public float getScaleXMin() {
        return mScaleXMin;
    }

    public boolean isScrollEnable() {
        return mScrollEnable;
    }

    public boolean isScaleEnable() {
        return mScaleEnable;
    }

    /**
     * 设置缩放的最大值
     */
    public void setScaleXMax(float scaleXMax) {
        mScaleXMax = scaleXMax;
    }

    /**
     * 设置缩放的最小值
     */
    public void setScaleXMin(float scaleXMin) {
        mScaleXMin = scaleXMin;
    }

    /**
     * 设置是否可以滑动
     */
    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }


    /**
     * 设置是否可以缩放
     */
    public void setScaleEnable(boolean scaleEnable) {
        Log.d(TAG,"setScaleEnable:"+scaleEnable);
        mScaleEnable = scaleEnable;
    }

    @Override
    public float getScaleX() {
        return mScaleX;
    }


    public void setScaleX(float mScaleX) {
        this.mScaleX = mScaleX;
        invalidate();
    }

    abstract void updateScaleData();
}
