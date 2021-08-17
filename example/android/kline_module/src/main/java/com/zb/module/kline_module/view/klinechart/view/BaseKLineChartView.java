package com.zb.module.kline_module.view.klinechart.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.zb.module.kline_module.R;
import com.zb.module.kline_module.view.klinechart.adapter.KLineChartAdapter;
import com.zb.module.kline_module.view.klinechart.base.IChartDraw;
import com.zb.module.kline_module.view.klinechart.base.IDateTimeFormatter;
import com.zb.module.kline_module.view.klinechart.base.IValueFormatter;
import com.zb.module.kline_module.view.klinechart.draw.MACDDraw;
import com.zb.module.kline_module.view.klinechart.draw.MainDraw;
import com.zb.module.kline_module.view.klinechart.draw.MainIndexStatus;
import com.zb.module.kline_module.view.klinechart.draw.VolumeDraw;
import com.zb.module.kline_module.view.klinechart.entity.IKLine;
import com.zb.module.kline_module.view.klinechart.formatter.TimeFormatter;
import com.zb.module.kline_module.view.klinechart.utils.FormatUtil;
import com.zb.module.kline_module.view.klinechart.utils.KlineEditTextUtil;
import com.zb.module.kline_module.view.klinechart.utils.ViewUtil;
import com.zb.module.kline_module.view.klinechart.view.ScrollAndScaleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.view.GestureDetectorCompat;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe k线图
 * @update
 */
public abstract class BaseKLineChartView extends ScrollAndScaleView {
    private static final String TAG = "BaseKLineChartView";
    protected boolean isFull = false;  //默认非全屏

    public boolean isFirstLoad = true;
    int circleRadius = dp2px(3);

    int currentCircleRadius = dp2px(3);
    int currentCircleStrokeRadius = dp2px(4.5f);

    private int mChildDrawPosition = -1;

    private float mTranslateX = Float.MIN_VALUE;

    private int mWidth = 0;

    //整体上下padding（控制显示 主图指标 底部时间）
    private int mTopPadding;
    private int mBottomPadding;

    private int mMainTopPadding;
    private int mMainBottomPadding;
    private int mVolTopPadding;
    private int mVolBottomPadding;
    private int mChildTopPadding;
    public int mChildBottomPadding;


    public int mVolTextPadding;

    public int mChildTextPadding;

    private double mMainScaleY = 1;

    private double mVolScaleY = 1;

    private double mChildScaleY = 1;

    private float mDataLen = 0;

    private Double mMainMaxValue = Double.MAX_VALUE;

    private Double mMainMinValue = Double.MIN_VALUE;

    private Double mMainHighMaxValue = 0D;

    private Double mMainLowMinValue = 0D;

    private int mMainMaxIndex = 0;

    private int mMainMinIndex = 0;

    private Double mMainHighTimerMaxValue = 0D;

    private Double mMainLowTimerMinValue = 0D;

    private int mMainTimerMaxIndex = 0;

    private int mMainTimerMinIndex = 0;

    private Double mVolMaxValue = Double.MAX_VALUE;

    private Double mVolMinValue = Double.MIN_VALUE;

    private Double mChildMaxValue = Double.MAX_VALUE;

    private Double mChildMinValue = Double.MIN_VALUE;

    private int mStartIndex = 0;

    private int mStopIndex = 0;

    protected float mPointWidth = 6;
    private int mGridRows = 4;
    private int mGridColumns = 4;

    protected float mCandleGap = 2;
    protected float mCandleLineWidth = 1;

    private Paint mGridPaint;

    private Paint mTextPaint;
    private Paint mRightTextPaint;

    private Paint mTimeTextPaint;

    private Paint mCurrentFrameStrokePaint;
    private Paint mCurrentFrameBgPaint;
    private Paint mCurrentFrameTextPaint;
    private Paint mCurrentLinePaint;

    private Paint mCurrentCirclePaint;
    private Paint mCurrentCircleStrokePaint;

    private Paint mMaxPaint;

    private Paint mMinPaint;

    private Paint mMaxCirclePaint;

    private Paint mMinCirclePaint;

    private Paint mTimerMaxMinPaint;
    private Paint mTimerMaxMinCirclePaint;
    private Paint mTimerMaxMinCircleBgPaint;


//    private Paint mBackgroundPaint;

    private Paint mSelectedXLinePaint;

    private Paint mSelectedYLinePaint;

    private Paint mSelectPointPaint;

    private Paint mSelectPointStrokePaint;

    private Paint mSelectorPointFramePaint;


    private Paint mSelectXYFrameStrokePaint;
    private Paint mSelectXYFrameBgPaint;
    private Paint mSelectXYFrameTextPaint;

    private Paint measurePaint;

    private int mSelectedIndex;
    private int mSelectOldIndex = -1;

    private IChartDraw mMainDraw;
    private IChartDraw mVolDraw;
    private IChartDraw mMacdDraw;

    private MainDraw mainDraw;

    private VolumeDraw volumeDraw;
    private MACDDraw macdDraw;
    private KLineChartAdapter mAdapter;

    private Boolean isWR = false;
    private Boolean isShowChild = false;

    protected int mPriceDecimal = 8;    //价格显示小数位数，，默认显示4为小数
    protected int mAmountDecimal = 8;    //数量显示小数位数，，默认显示4为小数


    //是否展示全屏的价格辅助线
    protected boolean isShowFullCurrentPriceLine = true;

    public boolean isShowFullCurrentPriceLine() { //滑到最右边时，价格辅助线全屏显示
        return isShowFullCurrentPriceLine;
    }

    public void setShowFullCurrentPriceLine(boolean showFullCurrentPriceLine) {
        isShowFullCurrentPriceLine = showFullCurrentPriceLine;
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };
    //当前点的个数
    private int mItemCount;
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();

    private IValueFormatter mPriceValueFormatter;
    private IValueFormatter mAmountValueFormatter;
    private IDateTimeFormatter mDateTimeFormatter;

    private ObjectAnimator mAnimator;

    public int mAnimationDuration = 400;  //绘制动画时间

    public float mRightWidth = 0;  //向右最小的宽度
    public float mRightMinWidth = 0; //向右的宽度(动态变化)
    private boolean  isLessData = false; //是否数据量不足1屏幕

    private OnSelectedChangedListener mOnSelectedChangedListener = null;

    private Rect mMainRect;

    private Rect mVolRect;

    private Rect mChildRect;

    private Rect mTimeRect;

    private float mLineWidth;

    public BaseKLineChartView(Context context) {
        super(context);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        initPaint();

        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = (int) getResources().getDimension(R.dimen.chart_top_padding);
        mBottomPadding = (int) getResources().getDimension(R.dimen.chart_bottom_padding);
        mMainTopPadding = (int) getResources().getDimension(R.dimen.chart_main_top_padding);
        mMainBottomPadding = (int) getResources().getDimension(R.dimen.chart_main_bottom_padding);
        mVolTopPadding = (int) getResources().getDimension(R.dimen.chart_vol_top_padding);
        mVolBottomPadding = (int) getResources().getDimension(R.dimen.chart_vol_bottom_padding);
        mChildTopPadding = (int) getResources().getDimension(R.dimen.chart_child_top_padding);
        mChildBottomPadding = (int) getResources().getDimension(R.dimen.chart_child_bottom_padding);
        mVolTextPadding = (int) getResources().getDimension(R.dimen.chart_vol_text_padding);
        mChildTextPadding = (int) getResources().getDimension(R.dimen.chart_child_text_padding);
        mSelectorPointFramePaint.setStrokeWidth(ViewUtil.Dp2Px(getContext(), 0.6f));
        mSelectorPointFramePaint.setStyle(Paint.Style.STROKE);
        mSelectorPointFramePaint.setColor(Color.WHITE);

        mAnimator = ObjectAnimator.ofFloat(this,"scaleY",0.35f,1.0f);  //透明度变化
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
                setScrollX((int) (0 - ((getOverScrollRange() / getScaleX()))));
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                System.out.println("mAnimator-onAnimationStart");

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                System.out.println("mAnimator-onAnimationEnd");
//                setScrollX((int) (0-((getOverScrollRange()/getScaleX()))));
                animatorCancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                System.out.println("mAnimator-onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
//                System.out.println("mAnimator-onAnimationRepeat");
            }
        });


    }

    public void updateAnimationDuration(int duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }



    private void initPaint() {
        mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //消除锯齿
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentFrameStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentFrameBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentFrameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentCircleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerMaxMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerMaxMinCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerMaxMinCircleBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaxCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedXLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedYLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPointStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectorPointFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectXYFrameStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectXYFrameBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectXYFrameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        measurePaint = new Paint();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        displayHeight = h - mTopPadding - mBottomPadding;
        initRect();
        setTranslateXFromScrollX(mScrollX);
    }

    int displayHeight = 0;  //总画线高度
    int mMainHeight = 0;  //主分区高度
    int mVolHeight = 0;   //成交量分区高度
    int mChildHeight = 0;   //指标分区高度

    private void initRect() {
        if (isShowChild) {
            //3:1:1
            mMainHeight = (int) (displayHeight * 0.60f);
            mVolHeight = (int) (displayHeight * 0.20f);
            mChildHeight = (int) (displayHeight * 0.20f);
            mMainRect = new Rect(0, mTopPadding + mMainTopPadding, mWidth, mTopPadding + mMainHeight - mMainTopPadding - mMainBottomPadding);
            mVolRect = new Rect(0, mMainRect.bottom + mVolTextPadding + mMainBottomPadding + mVolTopPadding, mWidth, mMainRect.bottom + mVolHeight + mMainTopPadding + mMainBottomPadding - mVolTopPadding - mVolBottomPadding);
            mChildRect = new Rect(0, mVolRect.bottom + mChildTextPadding + mVolBottomPadding + mChildTopPadding, mWidth, mVolRect.bottom + mChildHeight + mVolTopPadding + mVolBottomPadding - mChildTopPadding - mChildBottomPadding);
        } else {
            //3:1
            mMainHeight = (int) (displayHeight * 0.80f);
            mVolHeight = (int) (displayHeight * 0.20f);
            mMainRect = new Rect(0, mTopPadding + mMainTopPadding, mWidth, mTopPadding + mMainHeight - mMainTopPadding - mMainBottomPadding);
            mVolRect = new Rect(0, mMainRect.bottom + mVolTextPadding + mMainBottomPadding + mVolTopPadding, mWidth, mMainRect.bottom + mVolHeight + mMainTopPadding + mMainBottomPadding);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0) {
            return;
        }
        calculateValue();
        canvas.save();
        canvas.scale(1, 1);
        obtainRightBoxWidth();
        drawGird(canvas);
        drawK(canvas);
        drawMaxAndMinPoint(canvas);
        drawXYText(canvas);
        drawValue(canvas, isLongPress() ? mSelectedIndex : mAdapter.getCount() - 1);  //修改：显示当前价格对应的指标值
        drawRightCurrentPrice(canvas);
        drawSelectorXY(canvas);
        drawSelectorBox(canvas, isLongPress() ? mSelectedIndex : mStopIndex);
        canvas.restore();
    }

    private void obtainRightBoxWidth() {
        IKLine point = (IKLine) getItem(getItemCount() - 1);
        if (point != null) {
            String text = formatPriceValue(point.getClosePrice());
            textRightBoxWidth = measurePaint.measureText(text);
        }

    }

    /**
     * 画右侧当前值Box 和 指示线
     *
     * @param canvas
     */
    private void drawRightCurrentPrice(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        int currentIndex = mAdapter.getCount() - 1;
        // 画Y轴Y值（价格）
        IKLine point = (IKLine) getItem(currentIndex);
        if (point != null) {
            float textLeftOrRightPadding = dp2px(4);
            float textTopOrBottomPadding = dp2px(2);
            float r = textHeight / 2 + textTopOrBottomPadding;
            float y = (float) getMainY(point.getClosePrice());
            float x;
            String text = formatPriceValue(point.getClosePrice());

            //如果Y超出边界，则等于边界
            float maxY = (float) getMainY(mMainLowMinValue);
            float minY = (float) getMainY(mMainHighMaxValue);
            float halfCandleWidth = (mPointWidth * mScaleX / 2 - 0.5f); //??
            if (maxY < y) {  //底部
                y = mMainRect.bottom + dp2px(2);
            } else if (minY > y) { //顶部
                y = mMainRect.top - mMainTopPadding - dp2px(2);
            } else {
                y = (float) getMainY(point.getClosePrice());
            }
            //价格辅助线
            if (isShowFullCurrentPriceLine()) {
                Path path = new Path();
                path.moveTo(getDynamicRightPointX(), y);
                path.lineTo(getChartWidth() - getTextRightBoxWidth() - halfCandleWidth, y);
                canvas.drawPath(path, mCurrentLinePaint);
            } else {
                if(!isLessData()){
                    Path path = new Path();
                    path.moveTo(0, y);
                    path.lineTo(getMeasuredWidth() - getTextRightBoxWidth() - halfCandleWidth, y);
                    canvas.drawPath(path, mCurrentLinePaint);
                }
            }

            //根据涨跌绘制颜色
            float open = (float) point.getOpenPrice();
            float close = (float) point.getClosePrice();
            if (close >= open) { //涨
                //red
                mCurrentFrameStrokePaint.setColor(getRed());
                mCurrentFrameBgPaint.setColor(getRed());
                mCurrentLinePaint.setColor(getRed());
                mCurrentFrameTextPaint.setColor(getWhite());  //字体颜色
            }  else {  //跌
                //green
                mCurrentFrameStrokePaint.setColor(getGreen());
                mCurrentFrameBgPaint.setColor(getGreen());
                mCurrentLinePaint.setColor(getGreen());
                mCurrentFrameTextPaint.setColor(getWhite());  //字体颜色
            }
            if (isShowFullCurrentPriceLine()) {
                if (mainDraw.isLine()) {
                    //绘制最小值
                    float circleX = translateXtoX(getX(getItemCount() - 1));
                    float circleY = (float) getMainY(point.getClosePrice());
                    //画圆Stroke
                    canvas.drawCircle(circleX, circleY, currentCircleStrokeRadius, mCurrentCircleStrokePaint);
                    //画圆中心
                    canvas.drawCircle(circleX, circleY, currentCircleRadius, mCurrentCirclePaint);

                }
            }
            float textWidth = mCurrentFrameTextPaint.measureText(text);
            x = mWidth - textWidth - (textLeftOrRightPadding * 2);
            RectF rectF = new RectF(x, y - r, mWidth - 2, y + r);
            canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), mCurrentFrameStrokePaint);
            canvas.drawRoundRect(rectF, dp2px(2), dp2px(2), mCurrentFrameBgPaint);
            canvas.drawText(text, x + textLeftOrRightPadding, fixTextY1(y), mCurrentFrameTextPaint);
        }


    }

    public float getRightWidth(String text){
       return mWidth - calculateWidth(text);
    }

    /**
     * 根据值计算Y轴坐标
     * Y轴坐标 = (主图最大值 - 价格值) * mMainScaleY + mMainRect.top
     * @param value
     * @return
     */
    public double getMainY(double value) {
        if (mainDraw.isLine()) {
            return (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
        } else {
            return (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
        }
    }

    /**
     * 根据Y坐标值计算价格值
     * 价格值 = 主图最大值 - ((Y轴坐标 - mMainRect.top) / mMainScaleY)
     * @param coordinate
     * @return
     */
    public double getPriceOrCoordinate(double coordinate){
        if (mainDraw.isLine()) {
            return mMainMaxValue - ((coordinate - mMainRect.top) / mMainScaleY);
        } else {
            return mMainMaxValue - ((coordinate - mMainRect.top) / mMainScaleY);
        }
    }

    /**
     * 获取屏幕点击Y轴坐标
     *
     * @return
     */
    public float getTouchY() {
        PointF mTouchPoint = getTouchPoint();
        if(mTouchPoint == null) return 0;
        return mTouchPoint.y;
    }
    /**
     * 获取屏幕点击X轴坐标
     *
     * @return
     */
    public float getTouchX() {
        PointF mTouchPoint = getTouchPoint();
        if(mTouchPoint == null) return 0;
        return mTouchPoint.x;
    }

    public double getMainBottom() {
        return mMainRect.bottom;
    }

    public double getVolY(double value) {
        return (mVolMaxValue - value) * mVolScaleY + mVolRect.top;
    }

    public double getChildY(double value) {
        return (mChildMaxValue - value) * mChildScaleY + mChildRect.top;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return y + fontMetrics.descent - fontMetrics.ascent;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY1(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * 画背景表格
     *
     * @param canvas
     */
    private void drawGird(Canvas canvas) {
        //-----------------------主图（K线图）------------------------
        //横向的grid
        float rowSpace = mMainRect.height() / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i + mMainRect.top, mWidth, rowSpace * i + mMainRect.top, mGridPaint);
        }
        //-----------------------副图、指标图（成交量，指标）------------------------
        if (mChildDraw != null) {
            canvas.drawLine(0, mVolRect.bottom, mWidth, mVolRect.bottom, mGridPaint);
            canvas.drawLine(0, mChildRect.bottom, mWidth, mChildRect.bottom, mGridPaint);
        } else {
            canvas.drawLine(0, mVolRect.bottom, mWidth, mVolRect.bottom, mGridPaint);
        }
        //纵向的grid
        float columnSpace = mWidth / mGridColumns;
        for (int i = 1; i < mGridColumns; i++) {
            canvas.drawLine(columnSpace * i, 0, columnSpace * i, mMainRect.bottom, mGridPaint);
            canvas.drawLine(columnSpace * i, mMainRect.bottom, columnSpace * i, mVolRect.bottom, mGridPaint);
            if (mChildDraw != null) {
                canvas.drawLine(columnSpace * i, mVolRect.bottom, columnSpace * i, mChildRect.bottom, mGridPaint);
            }
        }
    }

    private boolean isTouchFollowPoint = false;  //按压时是否跟随蜡烛点

    public boolean isTouchFollowPoint() {
        return isTouchFollowPoint;
    }

    public void setTouchFollowPoint(boolean touchFollowPoint) {
        isTouchFollowPoint = touchFollowPoint;
    }


    /**
     * 绘制手指触摸点（点）
     *
     * @param canvas
     */
    @SuppressLint("MissingPermission")
    private void drawSelectorXY(Canvas canvas) {
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        if (isLongPress()) {
            //长按时，绘制手指触摸选择线（线）
            IKLine point = (IKLine) getItem(mSelectedIndex);
            double closePrice = 0;
            if (point != null) {
                closePrice = point.getClosePrice();
                float x = getX(mSelectedIndex);

                if (isTouchFollowPoint()) {
                    float y = (float) getMainY(closePrice);
                    // k线图横线(X轴)
                    canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedXLinePaint);

                    float width = getContext().getResources().getDimension(R.dimen.chart_selector_y_line_width);
                    mSelectedYLinePaint.setStrokeWidth(width / getScaleX());
                    if (mChildDraw != null) {
                        // 子线图竖线（Y轴）
                        canvas.drawLine(x, mMainRect.top - mTopPadding - mMainTopPadding, x, mChildRect.bottom, mSelectedYLinePaint);
                    } else {
                        // 柱状图竖线（Y轴）
                        canvas.drawLine(x, mMainRect.top - mTopPadding - mMainTopPadding, x, mVolRect.bottom, mSelectedYLinePaint);
                    }
                    //绘制手指触摸点（点）
                    // 设置个新的长方形，扫描测量
                    RectF ovalSelector = new RectF(x - 8 / mScaleX, y - 8, x + 8 / mScaleX, y + 8);
                    canvas.drawOval(ovalSelector, mSelectPointPaint);
                    ovalSelector.set(x - 30 / mScaleX, y - 30, x + 30 / mScaleX, y + 30);
                    canvas.drawOval(ovalSelector, mSelectPointStrokePaint);
                } else {
                    //y值=按压时的区域区间值的精准坐标
                    float y = (float) getTouchY();
                    // k线图横线(X轴)
                    canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedXLinePaint);
                    float width = getContext().getResources().getDimension(R.dimen.chart_selector_y_line_width);
                    mSelectedYLinePaint.setStrokeWidth(width / getScaleX());
                    if (mChildDraw != null) {
                        // 子线图竖线（Y轴）
                        canvas.drawLine(x, mMainRect.top - mTopPadding - mMainTopPadding, x, mChildRect.bottom, mSelectedYLinePaint);
                    } else {
                        // 柱状图竖线（Y轴）
                        canvas.drawLine(x, mMainRect.top - mTopPadding - mMainTopPadding, x, mVolRect.bottom, mSelectedYLinePaint);
                    }
                    //绘制手指触摸点（点）
                    // 设置个新的长方形，扫描测量
                    RectF ovalSelector = new RectF(x - 8 / mScaleX, y - 8, x + 8 / mScaleX, y + 8);
                    canvas.drawOval(ovalSelector, mSelectPointPaint);
                    ovalSelector.set(x - 30 / mScaleX, y - 30, x + 30 / mScaleX, y + 30);
                    canvas.drawOval(ovalSelector, mSelectPointStrokePaint);
                }
            }
        }
        canvas.restore();

        //--------------画X轴时间Text---------------------
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        float y;
        //长按时，画XY轴边界值
        if (isLongPress()) {
            // 画Y轴Y值（价格）
            IKLine point = (IKLine) getItem(mSelectedIndex);
            if (point != null) {
                float textLeftOrRightPadding = dp2px(4);
                float textTopOrBottomPadding = dp2px(2);
                float r = textHeight / 2 + textTopOrBottomPadding;

                String text;
                float x;

                boolean textIsVisible;
                if (isTouchFollowPoint()) {
                    y = (float) getMainY(point.getClosePrice());
                    text = formatPriceValue(point.getClosePrice());
                    textIsVisible = true;
                } else {
                    y = (float) getTouchY();
                    //3个区域不同的显示X轴的参考值
                    String region = obtainRegionValue();
                    if (region != null) {
                        textIsVisible = true;
                        text = region;
                    } else {
                        text = "false";
                        textIsVisible = false;
                    }
                }
                if (textIsVisible) {
                    float widthY = mSelectXYFrameTextPaint.measureText(text);
                    if (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2f) {
                        x = 2;
                        RectF rectF = new RectF(x, y - r, widthY + (textLeftOrRightPadding * 2), y + r);
                        canvas.drawRoundRect(rectF, dp2px(1), dp2px(1), mSelectXYFrameStrokePaint);
                        canvas.drawRoundRect(rectF, dp2px(1), dp2px(1), mSelectXYFrameBgPaint);
                        canvas.drawText(text, x + textLeftOrRightPadding, fixTextY1(y), mSelectXYFrameTextPaint);
                    } else {
                        x = mWidth - widthY - (textLeftOrRightPadding * 2);
                        RectF rectF = new RectF(x, y - r, mWidth - 2, y + r);
                        canvas.drawRoundRect(rectF, dp2px(1), dp2px(1), mSelectXYFrameStrokePaint);
                        canvas.drawRoundRect(rectF, dp2px(1), dp2px(1), mSelectXYFrameBgPaint);
                        canvas.drawText(text, x + textLeftOrRightPadding, fixTextY1(y), mSelectXYFrameTextPaint);
                    }
                }


                // 画X轴X值（时间）
                String date = mAdapter.getAllDate(mSelectedIndex);
                float widthX = mSelectXYFrameTextPaint.measureText(date);
                r = textHeight / 2;
                if (isShowChild) {
                    y = mChildRect.bottom + 7.5f;
                } else {
                    y = mVolRect.bottom + 7.5f;
                }
                x = translateXtoX(getX(mSelectedIndex));
                if (isShowFullCurrentPriceLine()) {
                    if (x < widthX + 2 * textLeftOrRightPadding) {
                        x = 1 + widthX / 2 + textLeftOrRightPadding;
                    } else if (mWidth - x < widthX + 2 * textLeftOrRightPadding) {
                        x = translateXtoX(getX(mSelectedIndex));
                    }
                } else {
                    if (x < widthX + 2 * textLeftOrRightPadding) {
                        x = 1 + widthX / 2 + textLeftOrRightPadding;
                    } else if (mWidth - x < widthX + 2 * textLeftOrRightPadding) {
                        x = mWidth - 1 - widthX / 2 - textLeftOrRightPadding;
                    }
                }

                canvas.drawRect(x - widthX / 2 - textLeftOrRightPadding, y, x + widthX / 2 + textLeftOrRightPadding, y + baseLine + r, mSelectXYFrameStrokePaint);
                canvas.drawRect(x - widthX / 2 - textLeftOrRightPadding, y, x + widthX / 2 + textLeftOrRightPadding, y + baseLine + r, mSelectXYFrameBgPaint);
                canvas.drawText(date, x - widthX / 2, y + baseLine + 5, mSelectXYFrameTextPaint);
            }

        }
    }

    /**
     * 根据按压Y坐标获取对应区域的值
     * @return
     */
    private String obtainRegionValue() {
        PointF pointF = getTouchPoint();
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;

        double touchX = pointF.x;

        double touchY;
        double mainHigh;
        double mainLow;
        double volHigh;
        double volLow;
        double childHigh;
        double childLow;
        if (mMainRect != null && mVolRect != null) {
            touchY = pointF.y - textHeight + baseLine;
            mainHigh = mMainRect.bottom - textHeight + baseLine;  //成交量最大值的位置
            mainLow = mMainRect.top - mTopPadding + (baseLine / 2);

            volHigh = mVolRect.bottom - mVolBottomPadding;
            volLow = mMainRect.bottom + mMainBottomPadding - mVolTopPadding + baseLine;

            try{
                if (isShowChild) {
                    childHigh = mChildRect.bottom - mChildBottomPadding;
                    childLow = mVolRect.bottom + baseLine;
                    if (mainDraw.isLine()) {
                        //3:1:1
                        if ((touchY - mTopPadding - mMainTopPadding) < (mainHigh - mMainBottomPadding)) {  //主图区域
                            double priceValue = getPriceOrCoordinate(pointF.y);
                            if(priceValue >= 0){
                                return formatPriceValue(priceValue);
                            }else {
                                return null;
                            }
                        } else if (touchY >= (volLow - mVolTopPadding - mVolBottomPadding) && touchY < volHigh) {  //成交量区域
                            double volValue = mVolMaxValue - mVolMinValue;
                            double proportionVol = (touchY - (volLow - mVolTopPadding - mVolBottomPadding)) / (volHigh - (volLow - mVolTopPadding - mVolBottomPadding));
                            return mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(formatPriceValue(mVolMaxValue - proportionVol * volValue)), mAmountDecimal);
                        } else if (touchY >= (childLow - mChildTopPadding - mChildBottomPadding) && touchY <= (childHigh)) {  //子图区域
                            double childValue = mChildMaxValue - mChildMinValue;
                            double proportionChild = (touchY - (childLow - mChildTopPadding - mChildBottomPadding)) / (childHigh - (childLow - mChildTopPadding - mChildBottomPadding));
                            return formatPriceValue(mChildMaxValue - proportionChild * childValue);
                        } else {
                            return null;
                        }
                    } else {
                        //3:1:1
                        if ((touchY - mTopPadding - mMainTopPadding) < mainHigh - mMainBottomPadding) {  //主图区域
                            double priceValue = getPriceOrCoordinate(pointF.y);
                            if(priceValue >= 0){
                                return formatPriceValue(priceValue);
                            }else {
                                return null;
                            }
                        } else if (touchY >= (volLow - mVolTopPadding - mVolBottomPadding) && touchY < volHigh) {  //成交量区域
                            double volValue = mVolMaxValue - mVolMinValue;
                            double proportionVol = (touchY - (volLow - mVolTopPadding - mVolBottomPadding)) / (volHigh - (volLow - mVolTopPadding - mVolBottomPadding));
                            return mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(formatPriceValue(mVolMaxValue - proportionVol * volValue)), mAmountDecimal);
                        } else if (touchY >= (childLow - mChildTopPadding - mChildBottomPadding) && touchY <= (childHigh)) {  //子图区域
                            double childValue = mChildMaxValue - mChildMinValue;
                            double proportionChild = (touchY - (childLow - mChildTopPadding - mChildBottomPadding)) / (childHigh - (childLow - mChildTopPadding - mChildBottomPadding));
                            return formatPriceValue(mChildMaxValue - proportionChild * childValue);
                        } else {
                            return null;
                        }
                    }

                } else {
                    if (mainDraw.isLine()) {
                        //3:1
                        if ((touchY - mTopPadding - mMainTopPadding) < mainHigh - mMainBottomPadding) {  //主图区域
                            double priceValue = getPriceOrCoordinate(pointF.y);
                            if(priceValue >= 0){
                                return formatPriceValue(priceValue);
                            }else {
                                return null;
                            }
                        } else if (touchY >= (volLow - mVolTopPadding - mVolBottomPadding) && touchY < volHigh) {  //成交量区域
                            double volValue = mVolMaxValue - mVolMinValue;
                            double proportionVol = (touchY - (volLow - mVolTopPadding - mVolBottomPadding)) / (volHigh - (volLow - mVolTopPadding - mVolBottomPadding));
                            return mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(formatPriceValue(mVolMaxValue - proportionVol * volValue)), mAmountDecimal);
                        } else {
                            return null;
                        }
                    } else {
                        //3:1
                        if ((touchY - mTopPadding - mMainTopPadding) < mainHigh - mMainBottomPadding) {  //主图区域
                            double priceValue = getPriceOrCoordinate(pointF.y);
                            if(priceValue >= 0){
                                return formatPriceValue(priceValue);
                            }else {
                                return null;
                            }
                        } else if (touchY >= (volLow - mVolTopPadding - mVolBottomPadding) && touchY < volHigh) {  //成交量区域
                            double volValue = mVolMaxValue - mVolMinValue;
                            double proportionVol = (touchY - (volLow - mVolTopPadding - mVolBottomPadding)) / (volHigh - (volLow - mVolTopPadding - mVolBottomPadding));
                            return mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(formatPriceValue(mVolMaxValue - proportionVol * volValue)), mAmountDecimal);
                        } else {
                            return null;
                        }
                    }

                }
            }catch (Exception e){
                return null;
            }
        } else {
            return null;
        }

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
     * 画可放大缩小的K线图
     *
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            double currentPointX = getX(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            double lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw != null) { //主图
                if (lastPoint != null && currentPoint != null) {
                    mMainDraw.drawZoomTranslatedLine(lastPoint, currentPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
            if (mVolDraw != null) { //副图
                if (lastPoint != null && currentPoint != null) {
                    mVolDraw.drawZoomTranslatedLine(lastPoint, currentPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
            if (mChildDraw != null) { //指标图
                if (lastPoint != null && currentPoint != null) {
                    mChildDraw.drawZoomTranslatedLine(lastPoint, currentPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
        }
        //还原 平移缩放
        canvas.restore();

        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(1, 1);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            double currentPointX = getXScale(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);

            Object nextPoint = i == mStopIndex?currentPoint:getItem(i+1);
            double lastX = i == 0 ? currentPointX : getXScale(i - 1);
            if (mMainDraw != null) { //主图
                if (lastPoint != null && currentPoint != null) {
                    mMainDraw.drawNoZoomTranslatedLine(lastPoint, currentPoint,nextPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
            if (mVolDraw != null) { //副图
                if (lastPoint != null && currentPoint != null) {
                    mVolDraw.drawNoZoomTranslatedLine(lastPoint, currentPoint,nextPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
            if (mChildDraw != null) { //指标图
                if (lastPoint != null && currentPoint != null) {
                    mChildDraw.drawNoZoomTranslatedLine(lastPoint, currentPoint,nextPoint, (float) lastX, (float) currentPointX, canvas, this, i);
                }
            }
        }
        //还原 平移缩放
        canvas.restore();
    }


    /**
     * 计算文本长度
     *
     * @return
     */
    private int calculateWidth(String text) {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width() + 5;
    }


    /**
     * 计算文本长度
     *
     * @return
     */
    private Rect calculateMaxMin(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }


    float textRightBoxWidth;//有边当前价格

    public float getTextRightBoxWidth() {
        return textRightBoxWidth;
    }

    /**
     * 画XY轴标注
     *
     * @param canvas
     */
    private void drawXYText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        Paint.FontMetrics fmTime = mTimeTextPaint.getFontMetrics();
        float textTimeHeight = fmTime.descent - fmTime.ascent;
        float baseTimeLine = (textTimeHeight - fmTime.bottom - fmTime.top) / 2;
        mTextPaint.setTypeface(Typeface.DEFAULT);//普通字体

        float rightTextHeight = measureHeight(mTextPaint);
        //--------------（右侧Y轴坐标）-------------
        if (mMainDraw != null) {
            canvas.drawText(formatPriceValue(mMainMaxValue), mWidth - calculateWidth(formatPriceValue(mMainMaxValue)) - dp2px(4), (float) getMainY(mMainMaxValue) + (rightTextHeight / 2) - dp2px(0.55f), mTextPaint);
            float rowValue = (float) ((mMainMaxValue - mMainMinValue) / mGridRows);
            for (int i = 1; i < mGridRows; i++) {
                String text = formatPriceValue(rowValue * (mGridRows - i) + mMainMinValue);
                canvas.drawText(text, mWidth - calculateWidth(text) - dp2px(4), (float) getMainY(Double.parseDouble(text)) + (rightTextHeight / 2) - dp2px(0.55f), mTextPaint);
            }
            canvas.drawText(formatPriceValue(mMainMinValue), mWidth - calculateWidth(formatPriceValue(mMainMinValue)) - dp2px(4), (float) getMainY(mMainMinValue) + (rightTextHeight / 2) - dp2px(0.55f), mTextPaint);
        }
        if (mVolDraw != null) {
            if(KlineEditTextUtil.isNumeric(Double.toString(mVolMaxValue))){
                canvas.drawText(mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(Double.toString(mVolMaxValue)), getAmountDecimal()), mWidth - calculateWidth(mVolDraw.getValueFormatter(getContext()).format(Float.parseFloat(Double.toString(mVolMaxValue)), getAmountDecimal())) - dp2px(4), mMainRect.bottom + mMainBottomPadding - mVolTopPadding + baseLine, mTextPaint);
            }
        }
        if (mChildDraw != null) {
            if(KlineEditTextUtil.isNumeric(Double.toString(mChildMaxValue))){
                canvas.drawText(mChildDraw.getValueFormatter(getContext()).format(Float.parseFloat(Double.toString(mChildMaxValue)), getPriceDecimal()), mWidth - calculateWidth(mChildDraw.getValueFormatter(getContext()).format(Float.parseFloat(formatPriceValue(mChildMaxValue)), getPriceDecimal())) - dp2px(4), mVolRect.bottom+ mVolTopPadding + baseLine, mTextPaint);
            }
        }
        //---------------（右侧Y轴坐标）-------------

        //--------------画底部X轴时间Text---------------------
        float y;
        if (isShowChild) {
            y = mChildRect.bottom + baseTimeLine + 15;
        } else {
            y = mVolRect.bottom + baseTimeLine + 15;
        }
        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;
        if(isLessData()) {
            if (isFull()) {
                //最左边的时间
                String startText = getAdapter().getDate(mStartIndex);
                //最右边的时间
                String stopText = getAdapter().getDate(mStopIndex);

                //时间文字宽度
                int startTextWidth = calculateMaxMin(startText, mTimeTextPaint).width();
                int stopTextWidth = calculateMaxMin(stopText, mTimeTextPaint).width();
                //left
                if ((startTextWidth + stopTextWidth) < mAdapter.getDatas().size() * getCandleWidth()) {
                    float leftX = getXScale(mStartIndex);
                    canvas.drawText(startText, leftX - (float) (startTextWidth) / 3 + mTranslateX * mScaleX, y, mTimeTextPaint);
                }
                //right
                float rightX = getXScale(mStopIndex);
                canvas.drawText(stopText, rightX - (float) (stopTextWidth) / 2 + mTranslateX * mScaleX, y, mTimeTextPaint);
            } else {
                String startText;
                if (mAdapter.getTimeType().equals("single")) {
                    startText = mAdapter.getSingleTime(mStartIndex);
                } else {
                    startText = mAdapter.getWeekTime(mStartIndex);
                }
                String stopText;
                if (mAdapter.getTimeType().equals("single")) {
                    stopText = mAdapter.getSingleTime(mStopIndex);
                } else {
                    stopText = mAdapter.getWeekTime(mStopIndex);
                }

                //时间文字宽度
                int startTextWidth = calculateMaxMin(startText, mTimeTextPaint).width();
                int stopTextWidth = calculateMaxMin(stopText, mTimeTextPaint).width();
                //left
                if ((startTextWidth + stopTextWidth) < mAdapter.getDatas().size() * getCandleWidth()) {
                    float leftX = getXScale(mStartIndex);
                    canvas.drawText(startText, leftX - (float) (startTextWidth) / 3 + mTranslateX * mScaleX, y, mTimeTextPaint);
                }
                //right
                float rightX = getXScale(mStopIndex);
                canvas.drawText(stopText, rightX - (float) (stopTextWidth) / 2 + mTranslateX * mScaleX, y, mTimeTextPaint);
            }
        } else {
            if (isFull()) {
                if (isShowFullCurrentPriceLine()) {
                    float columnSpace = (mWidth - getOverScrollRange()) / (mGridColumns - 1);
                    for (int i = 1; i < mGridColumns - 1; i++) {
                        float translateX = xToTranslateX(columnSpace * i);
                        if (translateX >= startX && translateX <= stopX) {
                            int index = indexOfTranslateX(translateX);

                            String text = mAdapter.getDate(index);
                            canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTimeTextPaint);
                        }
                    }
                    //最左边的时间
                    float translateX = xToTranslateX(0);
                    String startText = getAdapter().getDate(mStartIndex);
                    canvas.drawText(startText, 0, y, mTimeTextPaint);
                    //最右边的时间
                    String stopText = getAdapter().getDate(mStopIndex);
                    canvas.drawText(stopText, mWidth - getOverScrollRange() - mTimeTextPaint.measureText(stopText), y, mTimeTextPaint);
                } else {
                    float columnSpace = mWidth / mGridColumns;
                    for (int i = 1; i < mGridColumns; i++) {
                        float translateX = xToTranslateX(columnSpace * i);
                        if (translateX >= startX && translateX <= stopX) {
                            int index = indexOfTranslateX(translateX);
                            String text = mAdapter.getDate(index);
                            canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTimeTextPaint);
                        }
                    }
                    String startText = getAdapter().getDate(mStartIndex);
                    canvas.drawText(startText, 0, y, mTimeTextPaint);
                    String stopText = getAdapter().getDate(mStopIndex);
                    canvas.drawText(stopText, mWidth - mTimeTextPaint.measureText(stopText), y, mTimeTextPaint);
                }
            } else {
                if (isShowFullCurrentPriceLine()) { //滑到最右边时，时间显示
                    float columnSpace = (mWidth - getOverScrollRange()) / (mGridColumns - 1);
                    //left(数据不足时，不绘制最左边的时间）
                    float x = xToTranslateX(columnSpace * 1);
                    if (x >= startX && x <= stopX) {
                        String startText;
                        if (mAdapter.getTimeType().equals("single")) {
                            startText = mAdapter.getSingleTime(mStartIndex);
                        } else {
                            startText = mAdapter.getWeekTime(mStartIndex);
                        }
                        canvas.drawText(startText, 0, y, mTimeTextPaint);
                    }
                    //center
                    for (int i = 1; i < mGridColumns - 1; i++) {
                        float translateX = xToTranslateX(columnSpace * i);
                        if (translateX >= startX && translateX <= stopX) {
                            int index = indexOfTranslateX(translateX);
                            String text;
                            if (mAdapter.getTimeType().equals("single")) {
                                text = mAdapter.getSingleTime(index);
                            } else {
                                text = mAdapter.getWeekTime(index);
                            }
                            canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTimeTextPaint);
                        }
                    }
                    //right
                    String stopText;
                    if (mAdapter.getTimeType().equals("single")) {
                        stopText = mAdapter.getSingleTime(mStopIndex);
                    } else {
                        stopText = mAdapter.getWeekTime(mStopIndex);
                    }
                    canvas.drawText(stopText, mWidth - getOverScrollRange() - mTimeTextPaint.measureText(stopText), y, mTimeTextPaint);

                } else {
                    float columnSpace = mWidth / mGridColumns;
                    //left(数据不足时，不绘制最左边的时间）
                    float x = xToTranslateX(columnSpace * 1);
                    if (x >= startX && x <= stopX) {
                        String startText;
                        if (mAdapter.getTimeType().equals("single")) {
                            startText = mAdapter.getSingleTime(mStartIndex);
                        } else {
                            startText = mAdapter.getWeekTime(mStartIndex);
                        }
                        canvas.drawText(startText, 0, y, mTimeTextPaint);
                    }
                    //center
                    for (int i = 1; i < mGridColumns; i++) {
                        float translateX = xToTranslateX(columnSpace * i);
                        if (translateX >= startX && translateX <= stopX) {
                            int index = indexOfTranslateX(translateX);
                            String text;
                            if (mAdapter.getTimeType().equals("single")) {
                                text = mAdapter.getSingleTime(index);
                            } else {
                                text = mAdapter.getWeekTime(index);
                            }
                            canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTimeTextPaint);
                        }
                    }
                    //right
                    String stopText;
                    if (mAdapter.getTimeType().equals("single")) {
                        stopText = mAdapter.getSingleTime(mStopIndex);
                    } else {
                        stopText = mAdapter.getWeekTime(mStopIndex);
                    }
                    canvas.drawText(stopText, mWidth - mTimeTextPaint.measureText(stopText), y, mTimeTextPaint);

                }
            }
        }
    }

    /**
     * 画当前可视范围内的最高价最低价
     *
     * @param canvas
     */
    private void drawMaxAndMinPoint(Canvas canvas) {
        if (!mainDraw.isLine()) {
            //绘制最小值
            float x = translateXtoX(getX(mMainMinIndex));
            float y = (float) getMainY(mMainLowMinValue);
            String LowString = getResources().getString(R.string.txt_kline_low) + " " + FormatUtil.parseDoubleMaxFillingZero_X(mMainLowMinValue, mPriceDecimal);
            //计算显示位置
            //计算文本宽度
            int lowStringWidth = calculateMaxMin(LowString, mMinPaint).width();
            int lowStringHeight = calculateMaxMin(LowString, mMinPaint).height();
            //画圆
            canvas.drawCircle(x, y + circleRadius, circleRadius, mMinCirclePaint);
            if (x < (float) getWidth() / 2) {
                //画右边
                canvas.drawText(LowString, x + circleRadius * 3, y + lowStringHeight  + circleRadius, mMinPaint);
            } else {
                //画左边
                canvas.drawText(LowString, x - lowStringWidth - circleRadius * 3, y + lowStringHeight + circleRadius, mMinPaint);
            }

            //绘制最大值
            x = translateXtoX(getX(mMainMaxIndex));
            y = (float) getMainY(mMainHighMaxValue);
            //计算显示位置
            //计算文本宽度
            String highString = getResources().getString(R.string.txt_kline_high) + " " + FormatUtil.parseDoubleMaxFillingZero_X(mMainHighMaxValue, mPriceDecimal);
            int highStringWidth = calculateMaxMin(highString, mMaxPaint).width();
            int highStringHeight = calculateMaxMin(highString, mMaxPaint).height();
            //画圆
            canvas.drawCircle(x, y - circleRadius, circleRadius, mMaxCirclePaint);
            if (x < (float) getWidth() / 2) {
                //画右边
                canvas.drawText(highString, x + circleRadius * 3, y - ((float)highStringHeight / 2), mMaxPaint);
            } else {
                //画左边
                canvas.drawText(highString, x - highStringWidth - circleRadius * 3, y - ((float)highStringHeight / 2), mMaxPaint);
            }

        } else {  //分时
            //绘制最小值
            float x = translateXtoX(getX(mMainTimerMinIndex));
            float y = (float) getMainY(mMainLowTimerMinValue);
            String LowString = getResources().getString(R.string.txt_kline_low) + " " + FormatUtil.parseDoubleMaxFillingZero_X(mMainLowTimerMinValue, mPriceDecimal);
            //计算显示位置
            //计算文本宽度
            int lowStringWidth = calculateMaxMin(LowString, mTimerMaxMinPaint).width();
            int lowStringHeight = calculateMaxMin(LowString, mTimerMaxMinPaint).height();
            //画圆背景
            canvas.drawCircle(x, y, circleRadius, mTimerMaxMinCircleBgPaint);
            //画圆
            canvas.drawCircle(x, y, circleRadius, mTimerMaxMinCirclePaint);
            if (x < (float) getWidth() / 2) {
                //画右边
                canvas.drawText(LowString, x + circleRadius * 2, y + lowStringHeight  + circleRadius, mTimerMaxMinPaint);
            } else {
                //画左边
                canvas.drawText(LowString, x - lowStringWidth - circleRadius * 3, y + lowStringHeight + circleRadius, mTimerMaxMinPaint);
            }


            //绘制最大值
            x = translateXtoX(getX(mMainTimerMaxIndex));
            y = (float) getMainY(mMainHighTimerMaxValue);
            //计算显示位置
            //计算文本宽度
            String highString = getResources().getString(R.string.txt_kline_high) + " " + FormatUtil.parseDoubleMaxFillingZero_X(mMainHighTimerMaxValue, mPriceDecimal);
            int highStringWidth = calculateMaxMin(highString, mTimerMaxMinPaint).width();
            int highStringHeight = calculateMaxMin(highString, mTimerMaxMinPaint).height();
            //画圆背景
            canvas.drawCircle(x, y, circleRadius, mTimerMaxMinCircleBgPaint);
            //画圆
            canvas.drawCircle(x, y, circleRadius, mTimerMaxMinCirclePaint);
            if (x < (float)getWidth() / 2) {
                //画右边
                canvas.drawText(highString, x + circleRadius * 2, y - ((float)lowStringHeight / 2), mTimerMaxMinPaint);
            } else {
                //画左边
                canvas.drawText(highString, x - highStringWidth - circleRadius * 3, y - ((float)lowStringHeight / 2), mTimerMaxMinPaint);
            }
        }
    }

    /**
     * 画值
     *
     * @param canvas
     * @param position 显示某个点的值
     */
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight - mMainTopPadding;
                mMainDraw.drawText(canvas, this, position, 0, y);
            }
            if (mVolDraw != null) {
                float y = mMainRect.bottom + baseLine + mMainBottomPadding - mVolTopPadding;
                mVolDraw.drawText(canvas, this, position, 0, y);
            }
            if (mChildDraw != null) {
                float y = mVolRect.bottom + baseLine + mVolBottomPadding + mChildTopPadding;
                mChildDraw.drawText(canvas, this, position, 0, y);
            }
        }
    }

    /**
     * 画值
     *
     * @param canvas
     * @param position 显示某个点的值
     */
    private void drawSelectorBox(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight - mMainTopPadding;
                mMainDraw.drawSelectorBox(canvas, this, position, 0, y);
            }
        }
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int getPriceDecimal() {
        return mPriceDecimal;
    }

    public void setPriceDecimal(int mPriceDecimal) {
        this.mPriceDecimal = mPriceDecimal;
    }

    public int getAmountDecimal() {
        return mAmountDecimal;
    }

    public void setAmountDecimal(int mAmountDecimal) {
        this.mAmountDecimal = mAmountDecimal;
    }

    /**
     * 格式化值
     */
    public String formatPriceValue(double value) {
        return FormatUtil.parseDoubleMaxFillingZero_X(value, mPriceDecimal);
    }

    /**
     * 格式化值
     */
    public String formatAmountValue(double value) {
        return FormatUtil.parseDoubleMaxFillingZero_X(value, mAmountDecimal);
    }

    /**
     * 重新计算并刷新线条
     */
    public void notifyChanged() {
        if (isShowChild && mChildDrawPosition == -1) {
            mChildDraw = mChildDraws.get(0);
            mChildDrawPosition = 0;
        }
        if (mItemCount != 0) {
            mDataLen = (mItemCount - 1) * mPointWidth;
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        } else {
            //首次进入后显示最右侧当前值
            setScrollX((int) (0 - (mRightWidth / mScaleX)));
        }
        invalidate();
    }

    /**
     * MA/BOLL切换及隐藏
     *
     * @param mainIndexStatus MA/BOLL/NONE
     */
    public void changeMainDrawType(MainIndexStatus mainIndexStatus) {
        if (mainDraw != null && mainDraw.getMainIndexStatus() != mainIndexStatus) {
            mainDraw.setMainIndexStatus(mainIndexStatus);
            invalidate();
        }
    }

    private void calculateSelectedX(float x) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void vibration(boolean isFirst) {
        //暂时屏蔽震动
        //按下后 第一次震动一次，移动继续震动，不移动不震动
//        Vibrator mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
//        if (isFirst) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1, VibrationEffect.DEFAULT_AMPLITUDE);
//                if (vibrationEffect != null && mVibrator != null && mVibrator.hasVibrator()) { // 判断手机硬件是否有振动器
//                    mVibrator.cancel();
//                    mVibrator.vibrate(vibrationEffect);
//                }
//            } else {
//                if (mVibrator != null && mVibrator.hasVibrator()) { // 判断手机硬件是否有振动器
//                    mVibrator.cancel();
//                    mVibrator.vibrate(1);  // 设置手机振动
//                }
//            }
//
//        } else {
//            if (mSelectOldIndex != -1 && mSelectedIndex != mSelectOldIndex) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    VibrationEffect vibrationEffect = VibrationEffect.createOneShot(1, VibrationEffect.DEFAULT_AMPLITUDE);
//                    if (vibrationEffect != null && mVibrator != null && mVibrator.hasVibrator()) { // 判断手机硬件是否有振动器
//                        mVibrator.cancel();
//                        mVibrator.vibrate(vibrationEffect);
//                    }
//                } else {
//                    if (mVibrator != null && mVibrator.hasVibrator()) { // 判断手机硬件是否有振动器
//                        mVibrator.cancel();
//                        mVibrator.vibrate(1);  // 设置手机振动
//                    }
//                }
//            }
//            mSelectOldIndex = mSelectedIndex;
//        }
    }

    /**
     * 长按事件
     *
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }


    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mMainMaxValue = Double.MIN_VALUE;
        mMainMinValue = Double.MAX_VALUE;
        mVolMaxValue = Double.MIN_VALUE;
        mVolMinValue = Double.MAX_VALUE;
        mChildMaxValue = Double.MIN_VALUE;
        mChildMinValue = Double.MAX_VALUE;
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        mMainMaxIndex = mStartIndex;
        mMainMinIndex = mStartIndex;
        mMainHighMaxValue = Double.MIN_VALUE;
        mMainLowMinValue = Double.MAX_VALUE;

        mMainTimerMaxIndex = mStartIndex;
        mMainTimerMinIndex = mStartIndex;
        mMainHighTimerMaxValue = Double.MIN_VALUE;
        mMainLowTimerMinValue = Double.MAX_VALUE;

        //计算K线可视范围内的坐标点对应的最大值最小值范围
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IKLine point = (IKLine) getItem(i);
            if (point != null) {
                if (mMainDraw != null) {
                    mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getMaxValue(point));
                    mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinValue(point));
                    if (mMainHighMaxValue != Math.max(mMainHighMaxValue, point.getHighPrice())) {
                        mMainHighMaxValue = point.getHighPrice();
                        mMainMaxIndex = i;
                    }
                    if (mMainLowMinValue != Math.min(mMainLowMinValue, point.getLowPrice())) {
                        mMainLowMinValue = point.getLowPrice();
                        mMainMinIndex = i;
                    }
                    if (mMainHighTimerMaxValue != Math.max(mMainHighTimerMaxValue, point.getClosePrice())) {
                        mMainHighTimerMaxValue = point.getClosePrice();
                        mMainTimerMaxIndex = i;
                    }
                    if (mMainLowTimerMinValue != Math.min(mMainLowTimerMinValue, point.getClosePrice())) {
                        mMainLowTimerMinValue = point.getClosePrice();
                        mMainTimerMinIndex = i;
                    }
                }
                if (mVolDraw != null) {
                    mVolMaxValue = Math.max(mVolMaxValue, mVolDraw.getMaxValue(point));
                    mVolMinValue = Math.min(mVolMinValue, mVolDraw.getMinValue(point));
                }
                if (mChildDraw != null) {
                    mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                    mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
                }
            }

        }
        if (mMainMaxValue.equals(mMainMinValue)) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mMainMaxValue += Math.abs(mMainMaxValue * 0.05f);
            mMainMinValue -= Math.abs(mMainMinValue * 0.05f);
            if (mMainMaxValue == 0) {
                mMainMaxValue = 1d;
            }
        } else {
            //老逻辑，最大最小值加减一个小值
//            double padding = (mMainMaxValue - mMainMinValue) * 0.05f;
//            mMainMaxValue += padding;
//            mMainMinValue -= padding;
        }

        if (Math.abs(mVolMaxValue) < Math.pow(0.1, mAmountDecimal)) {
            mVolMaxValue = 15.00d;
        }

        if (Math.abs(mChildMaxValue) < Math.pow(0.1, mPriceDecimal) && Math.abs(mChildMinValue) < Math.pow(0.1, mPriceDecimal)) {
            mChildMaxValue = 1d;
        } else if (mChildMaxValue.equals(mChildMinValue)) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mChildMaxValue += Math.abs(mChildMaxValue * 0.05f);
            mChildMinValue -= Math.abs(mChildMinValue * 0.05f);
            if (mChildMaxValue == 0) {
                mChildMaxValue = 1d;
            }
        }

        if (isWR) {
            mChildMaxValue = 0d;
            if (Math.abs(mChildMinValue) < Math.pow(0.1, mPriceDecimal))
                mChildMinValue = -10.00d;
        }
        mMainScaleY = mMainRect.height() * 1d / (mMainMaxValue - mMainMinValue);
        mVolScaleY = mVolRect.height() * 1d / (mVolMaxValue - mVolMinValue);
        if (mChildRect != null)
            mChildScaleY = mChildRect.height() * 1d / (mChildMaxValue - mChildMinValue);
        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }

    }

    /**
     * 获取平移的最小值
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mRightWidth / mScaleX);
    }


    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }


    /**
     * 在主区域画线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopX     结束点的横坐标
     * @param stopValue 结束点的值
     */
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawLine(startX, (float) getMainY(startValue), stopX, (float) getMainY(stopValue), paint);
    }

    /**
     * 画分时线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopX     结束点的横坐标
     * @param stopValue 结束点的值
     */
    public void drawTimerLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        paint.setColor(timeLineColor);
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawLine(startX, (float) getMainY(startValue), stopX, (float) getMainY(stopValue), paint);
    }

    /**
     * 在主区域画分时线背景
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopX     结束点的横坐标
     * @param stopValue 结束点的值
     */
    public void drawTimerBgLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        Path path = new Path();
        path.moveTo(startX, mMainHeight);
        path.lineTo(startX, (float) getMainY(startValue));
        path.lineTo(stopX, (float) getMainY(stopValue));
        path.lineTo(stopX, mMainHeight);
        path.close();

        Shader mShaderSell = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                new int[]{timeStartColor, timeEndColor, timeEndColor},
                new float[]{0f, 0.68f, 1.0f}, Shader.TileMode.CLAMP);
        paint.setShader(mShaderSell);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawPath(path, paint);
    }

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawLine(startX, (float) getChildY(startValue), stopX, (float) getChildY(stopValue), paint);
    }

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    public void drawVolLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawLine(startX, (float) getVolY(startValue), stopX, (float) getVolY(stopValue), paint);
    }

    /**
     * 根据索引获取实体
     *
     * @param position 索引值
     * @return
     */
    public Object getItem(int position) {
        //修复数组越界问题
        if (mAdapter != null && position < mAdapter.getCount()) {
            return mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    /**
     * 根据索引索取x坐标
     *
     * @param position 索引值
     * @return
     */
    public float getX(int position) {
        return position * mPointWidth;
    }


    /**
     * 根据索引索取x坐标(需要缩放的线，动态获取缩放坐标)
     *
     * @param position 索引值
     * @return
     */
    public float getXScale(int position) {
        return position * mPointWidth * getScaleX();
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public KLineChartAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 设置当前子图
     *
     * @param position
     */
    public void setChildDraw(int position) {
        if (mChildDrawPosition != position) {
            if (!isShowChild) {
                isShowChild = true;
                initRect();
            }
            mChildDraw = mChildDraws.get(position);
            mChildDrawPosition = position;
            isWR = position == 5;
            invalidate();
        }
    }

    /**
     * 隐藏子图
     */
    public void hideChildDraw() {
        mChildDrawPosition = -1;
        isShowChild = false;
        mChildDraw = null;
        initRect();
        invalidate();
    }

    /**
     * 给子区域添加画图方法
     *
     * @param childDraw IChartDraw
     */
    public void addChildDraw(IChartDraw childDraw) {
        mChildDraws.add(childDraw);
    }

    /**
     * scrollX 转换为 TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }

    /**
     * 获取价格显示ValueFormatter
     *
     * @return
     */
    public IValueFormatter getPriceValueFormatter() {
        return mPriceValueFormatter;
    }

    /**
     * 设置价格显示ValueFormatter
     *
     * @param valueFormatter value格式化器
     */
    public void setPriceValueFormatter(IValueFormatter valueFormatter) {
        this.mPriceValueFormatter = valueFormatter;
    }

    /**
     * 获取数量显示ValueFormatter
     *
     * @return
     */
    public IValueFormatter getAmountValueFormatter() {
        return mAmountValueFormatter;
    }

    /**
     * 设置数量显示ValueFormatter
     *
     * @param valueFormatter value格式化器
     */
    public void setAmountValueFormatter(IValueFormatter valueFormatter) {
        this.mAmountValueFormatter = valueFormatter;
    }

    /**
     * 获取DatetimeFormatter
     *
     * @return 时间格式化器
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return mDateTimeFormatter;
    }

    /**
     * 设置dateTimeFormatter
     *
     * @param dateTimeFormatter 时间格式化器
     */
    public void setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        mDateTimeFormatter = dateTimeFormatter;
    }

    /**
     * 格式化时间
     *
     * @param date
     */
    public String formatDateTime(Date date) {
        if (getDateTimeFormatter() == null) {
            setDateTimeFormatter(new TimeFormatter());
        }
        return getDateTimeFormatter().format(date);
    }

    /**
     * 获取主区域的 IChartDraw
     *
     * @return IChartDraw
     */
    public IChartDraw getMainDraw() {
        return mMainDraw;
    }

    /**
     * 设置主区域的 IChartDraw
     *
     * @param mainDraw IChartDraw
     */
    public void setMainDraw(IChartDraw mainDraw) {
        mMainDraw = mainDraw;
        this.mainDraw = (MainDraw) mMainDraw;
    }

    public IChartDraw getVolDraw() {
        return mVolDraw;
    }

    public void setVolDraw(IChartDraw mVolDraw) {
        this.mVolDraw = mVolDraw;
        this.volumeDraw = (VolumeDraw) mVolDraw;
    }

    public void setMacdDraw(IChartDraw macdDraw) {
        this.mMacdDraw = macdDraw;
        this.macdDraw = (MACDDraw) macdDraw;
    }

    /**
     * 二分查找当前值的index
     *
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (start == 0 && end == -1) {
            return 0;
        } else {
            if (end == start) {
                return start;
            }
            if (end - start == 1) {
                float startValue = getX(start);
                float endValue = getX(end);
                return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
            }
            int mid = start + (end - start) / 2;
            float midValue = getX(mid);
            if (translateX < midValue) {
                return indexOfTranslateX(translateX, start, mid);
            } else if (translateX > midValue) {
                return indexOfTranslateX(translateX, mid, end);  //
            } else {
                return mid;
            }
        }

    }

    /**
     * 设置数据适配器
     */
    public void setAdapter(KLineChartAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    /**
     * 开始动画
     */
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }


    /**
     * 设置表格行数
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
    }

    /**
     * 设置表格列数
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
    }

    /**
     * view中的x转化为TranslateX
     *
     * @param x
     * @return
     */
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    /**
     * translateX转化为view中的x
     *
     * @param translateX
     * @return
     */
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    /**
     * 获取上方padding
     */
    public float getTopPadding() {
        return mTopPadding;
    }


    /**
     * 获取图的宽度
     *
     * @return
     */
    public int getChartWidth() {
        return mWidth;
    }

    /**
     * 是否全屏
     *
     * @return
     */
    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }


    /**
     * 获取选择索引
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    //获取所有项索引
    public int getItemCount() {
        return mItemCount;
    }

    public Rect getChildRect() {
        return mChildRect;
    }

    public Rect getVolRect() {
        return mVolRect;
    }

    /**
     * 设置选择监听
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    public void onSelectedChanged(BaseKLineChartView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * 数据是否充满屏幕
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    /**
     * 设置超出右方后可滑动的范围（右边多余可滑动区域）
     */
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mRightWidth = overScrollRange;
        //设置右边区域宽度后初始化为最右边位置
        Log.d(TAG,"isFirstLoad:"+isFirstLoad);
        if (isFirstLoad) {
            setScrollX((int) (0 - (mRightWidth / getScaleX())));
            isFirstLoad = false;
        }
    }

    /**
     * 动态设置右方可滑动的范围（右边多余可滑动区域）
     */
    public void setRightWidth(float width) {
        if (width < 0) {
            width = 0;
        }
        mRightMinWidth = width;
    }


    /**
     * 设置上方padding
     *
     * @param topPadding
     */
    public void setTopPadding(int topPadding) {
        mTopPadding = topPadding;
    }

    /**
     * 设置下方padding
     *
     * @param bottomPadding
     */
    public void setBottomPadding(int bottomPadding) {
        mBottomPadding = bottomPadding;
    }

    /**
     * 设置表格线宽度
     */
    public void setGridLineWidth(float width) {
        mGridPaint.setStrokeWidth(width);
    }

    /**
     * 设置表格线颜色
     */
    public void setGridLineColor(int color) {
        mGridPaint.setColor(color);
    }

    /**
     * 设置选择器横线宽度
     */
    public void setSelectedXLineWidth(float width) {
        mSelectedXLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置选择器横线颜色
     */
    public void setSelectedXLineColor(int color) {
        mSelectedXLinePaint.setColor(color);
    }


    /**
     * 设置选择器竖线宽度
     */
    public void setSelectedYLineWidth(float width) {
        mSelectedYLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置选择器竖线颜色
     */
    public void setSelectedYLineColor(int color) {
        mSelectedYLinePaint.setColor(color);
    }

    public Paint getSelectedXLinePaint() {
        return mSelectedXLinePaint;
    }

    public Paint getSelectedYLinePaint() {
        return mSelectedYLinePaint;
    }

    /**
     * 设置文字颜色(时间)
     */
    public void setTimeTextColor(int color) {
        mTimeTextPaint.setColor(color);
    }

    /**
     * 设置文字大小(时间)
     */
    public void setTimeTextSize(float textSize) {
        mTimeTextPaint.setTextSize(textSize);
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
    }

    public void setRightTextColor(int color) {
        mRightTextPaint.setColor(color);
    }

    public void setRightTextSize(float textSize) {
        mRightTextPaint.setTextSize(textSize);
    }


    /**
     * 设置最大值文字大小
     */
    public void setMaxTextSize(float textSize) {
        mMaxPaint.setTextSize(textSize);
    }


    /**
     * 设置最大值文字颜色
     */
    public void setMaxColor(int color) {
        mMaxPaint.setColor(color);
        mMaxCirclePaint.setColor(color);
    }

    /**
     * 设置最小值文字颜色
     */
    public void setMinColor(int color) {
        mMinPaint.setColor(color);
        mMinCirclePaint.setColor(color);
    }

    public void setCurrentCircleColor(int color) {
        mCurrentCirclePaint.setColor(color);
        mCurrentCirclePaint.setStyle(Paint.Style.FILL);
    }

    public void setCurrentCircleStrokeColor(int color) {
        mCurrentCircleStrokePaint.setColor(color);
        mCurrentCircleStrokePaint.setStyle(Paint.Style.FILL);
    }


    private int red;
    private int green;
    private int normal;
    private int white;

    public void setRed(int color) {
        this.red = color;
    }

    public void setGreen(int color) {
        this.green = color;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    /**
     * 设置最小值文字大小
     */
    public void setMinTextSize(float textSize) {
        mMinPaint.setTextSize(textSize);
        mTimerMaxMinPaint.setTextSize(textSize);
    }


    /**
     * 设置最大值 画笔粗细(圆)
     */
    public void setMaxCircleStrokeWidth(float size) {
        mMaxCirclePaint.setStrokeWidth(size);
        mMaxCirclePaint.setStyle(Paint.Style.STROKE);  //默认空心圆
        circleRadius = (int) (size * 3);  //圆半径是stroke的3倍

        mTimerMaxMinCirclePaint.setStrokeWidth(size * 1.5f);
        mTimerMaxMinCirclePaint.setStyle(Paint.Style.STROKE);  //默认空心圆

        mTimerMaxMinCircleBgPaint.setStrokeWidth(size * 1.5f);
        mTimerMaxMinCircleBgPaint.setStyle(Paint.Style.FILL);  //实心圆盖住
    }


    /**
     * 设置最小值 画笔粗细(圆)
     */
    public void setMinCircleStrokeWidth(float size) {
        mMinCirclePaint.setStrokeWidth(size);
        mMinCirclePaint.setStyle(Paint.Style.STROKE);  //默认空心圆
        circleRadius = (int) size * 3;  //圆半径是stroke的3倍
    }

    /**
     * 设置背景颜色
     */
    public void setCircleBackgroundColor(int color) {
        mTimerMaxMinCircleBgPaint.setColor(color);
    }


    /**
     * 设置选中point 值显示背景
     */
    public void setSelectPointColor(int color) {
        mSelectPointPaint.setColor(color);
    }

    /**
     * 设置选中point 值显示背景
     */
    public void setSelectPointStrokeColor(int color) {
        mSelectPointStrokePaint.setColor(color);
    }


    /**
     * 设置选中point XY Frame Stroke
     */
    public void setSelectXYFrameStrokePaintColor(int color) {
        mSelectXYFrameStrokePaint.setColor(color);
        mSelectXYFrameStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectXYFrameStrokePaint.setStrokeWidth(dp2px(1));
    }

    /**
     * 设置选中point XY Frame Bg
     */
    public void setSelectXYFrameBgPaint(int color) {
        mSelectXYFrameBgPaint.setColor(color);
        mSelectXYFrameBgPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置选中point XY Frame 字体颜色
     */
    public void setSelectXYFrameTextPaintColor(int color) {
        mSelectXYFrameTextPaint.setColor(color);
    }

    /**
     * 设置point XY Frame 字体大小
     */
    public void setSelectXYFrameTextPaintTextSize(float textSize) {
        mSelectXYFrameTextPaint.setTextSize(textSize);
        mSelectXYFrameTextPaint.setTypeface(Typeface.DEFAULT_BOLD);//加粗
    }


    /**
     * 设置当前最新价格 Frame Stroke
     */
    public void setCurrentFrameStrokePaintColor(int color) {
        mCurrentFrameStrokePaint.setColor(color);
        mCurrentFrameStrokePaint.setStyle(Paint.Style.STROKE);
        mCurrentFrameStrokePaint.setStrokeWidth(dp2px(1));
    }

    /**
     * 设置当前最新价格 Frame Bg
     */
    public void setCurrentFrameBgPaint(int color) {
        mCurrentFrameBgPaint.setColor(color);
        mCurrentFrameBgPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置当前最新价格 Frame 字体颜色
     */
    public void setCurrentFrameTextPaintColor(int color) {
        mCurrentFrameTextPaint.setColor(color);
    }

    /**
     * 设置当前最新价格 Frame 字体大小
     */
    public void setCurrentFrameTextPaintTextSize(float textSize) {
        mCurrentFrameTextPaint.setTextSize(textSize);
        mSelectXYFrameTextPaint.setTypeface(Typeface.DEFAULT_BOLD);//加粗
    }

    /**
     * 设置选择器横线宽度
     */
    public void setCurrentLineWidth(float width) {
        mCurrentLinePaint.setStrokeWidth(width);
        mCurrentLinePaint.setStyle(Paint.Style.STROKE);
        mCurrentLinePaint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 6));
    }

    /**
     * 设置选择器横线颜色
     */
    public void setCurrentLineColor(int color) {
        mCurrentLinePaint.setColor(color);
    }

    /**
     * 选中点变化时的监听
     */
    public interface OnSelectedChangedListener {
        /**
         * 当选点中变化时
         *
         * @param view  当前view
         * @param point 选中的点
         * @param index 选中点的索引
         */
        void onSelectedChanged(BaseKLineChartView view, Object point, int index);
    }

    /**
     * 获取文字大小
     */
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     * 获取曲线宽度
     */
    public float getLineWidth() {
        return mLineWidth;
    }

    /**
     * 设置曲线的宽度
     */
    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    /**
     * 设置每个点的宽度
     */
    public void setPointWidth(float width) {
        mPointWidth = width;
        mainDraw.setPointWidth(width);
        volumeDraw.setPointWidth(width);
        macdDraw.setPointWidth(width);
    }


    public float getCandleGap() {
        return mCandleGap;
    }

    public Paint getGridPaint() {
        return mGridPaint;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

//    public Paint getBackgroundPaint() {
//        return mBackgroundPaint;
//    }

    public int getDisplayHeight() {
        return displayHeight + mTopPadding + mBottomPadding;
    }


    //分时线的渐变颜色
    private int timeLineColor;
    private int timeStartColor;
    private int timeEndColor;

    public void setTimerLineColor(int color) {
        this.timeLineColor = color;
        mTimerMaxMinPaint.setColor(color);
        mTimerMaxMinCirclePaint.setColor(color);
    }

    public void setTimerStartColor(int color) {
        this.timeStartColor = color;
    }

    public void setTimerEndColor(int color) {
        this.timeEndColor = color;
    }


    public void resetView() {
        if (isLongPress()) {
            setLongPress(false);
        }
        invalidate();
    }

    public float getOverScrollRange() {
        return mRightWidth;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public float getMinRightWidth() {
        return mRightMinWidth;
    }

    public boolean isLessData() {
        return isLessData;
    }

    public void setLessData(boolean lessData) {
        isLessData = lessData;
    }

    public void animatorCancel() {
        mAnimator.cancel();
    }

    public float getCandleLineWidth() {
        return mCandleLineWidth;
    }

    public static int measureHeight(Paint paint) {
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        return ~fm.top - (~fm.top - ~fm.ascent) - (fm.bottom - fm.descent);
    }

    public int getKLineWidth() {
        return mWidth;
    }

    public int getOneScreenMaxCandleSize() {
        Log.d(TAG, "getOneScreenMaxCandleSize-getKLineWidth" + getKLineWidth());
        Log.d(TAG, "getOneScreenMaxCandleSize-getOverScrollRange" + getOverScrollRange());
        Log.d(TAG, "getOneScreenMaxCandleSize-getCandleWidth" + getCandleWidth());
        return (int) ((getKLineWidth() - getMinRightWidth())/ getCandleWidth());
    }

    public int getOneScreenMaxRightCandleSize() {
        Log.d(TAG, "getOneScreenMaxCandleSize-getKLineWidth" + getKLineWidth());
        Log.d(TAG, "getOneScreenMaxCandleSize-getOverScrollRange" + getOverScrollRange());
        Log.d(TAG, "getOneScreenMaxCandleSize-getCandleWidth" + getCandleWidth());
        return (int) (getKLineWidth() / getCandleWidth());
    }

    /**
     * 获取单个蜡烛宽度
     * @return
     */
    public float getCandleWidth(){
        return (mPointWidth * getScaleX());
    }

    @Override
    public void updateScaleData() {
        if(mAdapter != null && mAdapter.getDatas() != null){
            int kLineDataSize = mAdapter.getDatas().size();
            int oneScreenMaxCandleSize = getOneScreenMaxCandleSize();
            int differSize;
            float candleWidth = getCandleWidth();
            if(kLineDataSize < oneScreenMaxCandleSize){
//                Log.d(TAG,"K线数据量不足，顶到左边显示");
                differSize = oneScreenMaxCandleSize - kLineDataSize;
                float rightWidth = differSize * candleWidth + getMinRightWidth() - (getCircleRadius() / 2);
                isFirstLoad = true;
                setOverScrollRange(rightWidth);
                setLessData(true);
            }else{
//                Log.d(TAG,"K线数据量足，正常显示");
                setLessData(false);
                setOverScrollRange(mRightWidth);
            }
        }
        invalidate();
    }

    /**
     * 动态获取滑动的位置
     * @return
     */
    protected float getDynamicRightPointX(){
        return getXScale(mStopIndex) + (mTranslateX * mScaleX);
    }
}
