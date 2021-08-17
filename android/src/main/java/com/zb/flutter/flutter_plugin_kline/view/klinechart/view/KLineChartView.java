package com.zb.flutter.flutter_plugin_kline.view.klinechart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zb.flutter.flutter_plugin_kline.R;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.adapter.KLineChartAdapter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.KDJDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.MACDDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.MainDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.RSIDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.VolumeDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.WRDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.loading.LoadingFrameLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe k线图
 * @update
 */
public class KLineChartView extends BaseKLineChartView {
    private static final String TAG = "KLineChartView";

    private LoadingFrameLayout loadingFrameLayout;
    private boolean isRefreshing = false;
    private boolean isLoadMoreEnd = false;
    private boolean mLastScrollEnable;
    private boolean mLastScaleEnable;

    private KChartRefreshListener mRefreshListener;

    private MACDDraw mMACDDraw;

    private RSIDraw mMADraw;
    private RSIDraw mEMADraw;
    private RSIDraw mBOLLDraw;

    private RSIDraw mRSIDraw;
    private MainDraw mMainDraw;
    private KDJDraw mKDJDraw;
    private WRDraw mWRDraw;
    private VolumeDraw mVolumeDraw;

    private com.zb.flutter.flutter_plugin_kline.view.klinechart.adapter.KLineChartAdapter KLineChartAdapter;
    private boolean isViewScroll = false;

    public KLineChartView(Context context) {
        this(context, null);
    }

    public KLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(attrs);
    }


    private void initView() {
//        mProgressBar = new AVLoadingIndicatorView(getContext());
//        mProgressBar.setIndicator("LineScaleIndicator");
//        mProgressBar.setIndicatorColor(getColor(R.color.loading_color));
        loadingFrameLayout = new LoadingFrameLayout(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);  //loading 边距
        layoutParams.addRule(CENTER_IN_PARENT);
//        addView(mProgressBar, layoutParams);
//        mProgressBar.setVisibility(GONE);
        addView(loadingFrameLayout, layoutParams);
        loadingFrameLayout.setVisibility(GONE);

        mVolumeDraw = new VolumeDraw(this, mAmountDecimal);
        mMACDDraw = new MACDDraw(this, mPriceDecimal,getContext());
        mWRDraw = new WRDraw(this, mPriceDecimal);
        mKDJDraw = new KDJDraw(this, mPriceDecimal,getContext());
        mRSIDraw = new RSIDraw(this, mPriceDecimal);
        mMainDraw = new MainDraw(this, mPriceDecimal);


        addChildDraw(mMACDDraw);
        addChildDraw(mKDJDraw);
        addChildDraw(mRSIDraw);
        addChildDraw(mWRDraw);

        setMainDraw(mMainDraw);
        setVolDraw(mVolumeDraw);
        setMacdDraw(mMACDDraw);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KLineChartView);
        if (array != null) {
            try {
                //K线图背景
//                setCircleBackgroundColor(array.getColor(R.styleable.KLineChartView_kc_background_color, getColor(R.color.chart_background)));

                //-------------------------基础默认-------------------------
                setPointWidth(array.getDimension(R.styleable.KLineChartView_kc_point_width, getDimension(R.dimen.chart_point_width)));

                setCandleGapWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_gap_width, getDimension(R.dimen.chart_candle_gap_width)));
                setTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_text_size)));

                setRightTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.right_price_text_size)));
                setIndexTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_index_text_size)));

                setTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.kc_text_color)));
                setLineWidth(array.getDimension(R.styleable.KLineChartView_kc_line_width, getDimension(R.dimen.chart_line_width)));
                setTimeLineWidth(array.getDimension(R.styleable.KLineChartView_kc_line_width, getDimension(R.dimen.chart_time_line_width)));
                //-------------------------基础默认-------------------------

                //X轴时间
                setTimeTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_x_time_text_size)));
                setTimeTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.kc_text_color)));

                //K线蜡烛图
                setCandleWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_width, getDimension(R.dimen.chart_candle_width)));
                setCandleLineWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_line_width, getDimension(R.dimen.chart_candle_line_width)));

                //K线蜡烛图(是否实心)
                setCandleSolid(array.getBoolean(R.styleable.KLineChartView_kc_candle_solid, true));


                //最高价
                setMaxTextSize(array.getDimension(R.styleable.KLineChartView_max_min_text_size, getDimension(R.dimen.chart_max_min_text_size)));
                setRedColor(array.getColor(R.styleable.KLineChartView_kc_max_color, getColor(R.color.kc_max_color)));
                setMaxCircleStrokeWidth(array.getDimension(R.styleable.KLineChartView_max_min_circle_stroke_size, getDimension(R.dimen.chart_max_min_circle_stroke_size)));

                //最低价
                setMinTextSize(array.getDimension(R.styleable.KLineChartView_max_min_text_size, getDimension(R.dimen.chart_max_min_text_size)));
                setGreenColor(array.getColor(R.styleable.KLineChartView_kc_min_color, getColor(R.color.kc_min_color)));

                setNormalColor(array.getColor(R.styleable.KLineChartView_kc_normal_color, getColor(R.color.kc_normal_color)));
                setWhiteColor(array.getColor(R.styleable.KLineChartView_kc_white_color, getColor(R.color.kc_white_color)));

                setMinCircleStrokeWidth(array.getDimension(R.styleable.KLineChartView_max_min_circle_stroke_size, getDimension(R.dimen.chart_max_min_circle_stroke_size)));


                //按压后（点）
                setSelectPointColor(array.getColor(R.styleable.KLineChartView_kc_selected_point_color, getColor(R.color.chart_selected_point_color_night)));
                //按压后（点外Stroke）
                setSelectPointStrokeColor(array.getColor(R.styleable.KLineChartView_kc_selected_point_stroke_color, getColor(R.color.chart_selected_point_stroke_color_night)));

                //当前最新价
                setCurrentFrameStrokePaintColor(array.getColor(R.styleable.KLineChartView_kc_current_point_stroke_color, getColor(R.color.chart_selected_frame_stroke_color_night)));
                setCurrentFrameTextPaintColor(array.getColor(R.styleable.KLineChartView_kc_current_line_color, getColor(R.color.chart_selected_frame_stroke_color_night)));
                setCurrentFrameBgPaint(array.getColor(R.styleable.KLineChartView_kc_current_point_stroke_color, getColor(R.color.chart_selected_frame_bg_color_light)));
                setCurrentFrameTextPaintTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_right_current_text_size)));

                //XY轴动态值
                setSelectXYFrameStrokePaintColor(array.getColor(R.styleable.KLineChartView_kc_selected_point_stroke_color, getColor(R.color.chart_selected_frame_stroke_color_night)));
                setSelectXYFrameTextPaintColor(array.getColor(R.styleable.KLineChartView_kc_selected_point_stroke_color, getColor(R.color.chart_selected_frame_stroke_color_night)));
                setSelectXYFrameBgPaint(array.getColor(R.styleable.KLineChartView_kc_selected_point_stroke_color, getColor(R.color.chart_selected_frame_bg_color_light)));
                setSelectXYFrameTextPaintTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_selector_text_size)));
                //按压后（线）
                setSelectedXLineColor(array.getColor(R.styleable.KLineChartView_kc_selected_line_color, getColor(R.color.chart_selected_line_x_color_night)));
                setSelectedXLineWidth(array.getDimension(R.styleable.KLineChartView_kc_selected_line_width, getDimension(R.dimen.chart_selector_x_line_width)));
                setCurrentLineWidth(array.getDimension(R.styleable.KLineChartView_kc_selected_line_width, getDimension(R.dimen.chart_selector_x_line_width)));

                setSelectedYLineColor(array.getColor(R.styleable.KLineChartView_kc_selected_line_color, getColor(R.color.chart_selected_line_y_color_night)));
                setSelectedYLineWidth(array.getDimension(R.styleable.KLineChartView_kc_selected_line_width, getDimension(R.dimen.chart_selector_y_line_width)));

                //按压后的选择器 box
                setSelectorBackgroundColor(array.getColor(R.styleable.KLineChartView_kc_selector_background_color, getColor(R.color.chart_selector_box_background_night)));
                setSelectorStrokeColor(array.getColor(R.styleable.KLineChartView_kc_selector_stroke_color, getColor(R.color.chart_selector_box_stroke_night)));
                setSelectorTextSize(array.getDimension(R.styleable.KLineChartView_kc_selector_text_size, getDimension(R.dimen.chart_selector_box_text_size)));
                setSelectorTextColor(array.getColor(R.styleable.KLineChartView_kc_selector_text_color, getColor(R.color.kc_text_color)));

                //背景网格
                setGridLineWidth(array.getDimension(R.styleable.KLineChartView_kc_grid_line_width, getDimension(R.dimen.chart_grid_line_width)));
                setGridLineColor(array.getColor(R.styleable.KLineChartView_kc_grid_line_color, getColor(R.color.chart_grid_line_night)));


                //-------------------------指标-------------------------
                //vol
                setVolumeWidth(array.getDimension(R.styleable.KLineChartView_kc_macd_width, getDimension(R.dimen.chart_candle_width)));
                //macd
                setMACDWidth(array.getDimension(R.styleable.KLineChartView_kc_macd_width, getDimension(R.dimen.chart_candle_width)));
                setDIFColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setDEAColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setMACDColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //kdj
                setKColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setDColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setJColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //wr
                setRColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setRSI2Color(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setRSI3Color(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //main
                setMa5Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setMa10Color(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setMa30Color(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //-------------------------指标-------------------------

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
    }

    private float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    private int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    @Override
    public void onLeftSide() {
//        System.out.println("onLeftSide-已经滑到K线右边界");

    }

    @Override
    public void onRightSide() {
//        System.out.println("onRightSide");
    }

    @Override
    public void onRightSpacingShowOrHide() {
        float textWidth = getTextRightBoxWidth() / mScaleX;
        if ((mScrollX*1.5f) < -(textWidth)) {
            setShowFullCurrentPriceLine(true);
            isViewScroll = false;
        }else{
            setShowFullCurrentPriceLine(false);
        }
    }

    @Override
    public void onScrollStart() {
//        System.out.println("onScrollStart");
        isViewScroll = true;
    }

    @Override
    public void onScrollEnd() {
//        System.out.println("onScrollEnd");
        isViewScroll = false;
    }


    public boolean isViewScroll() {
        return isViewScroll;
    }


    public void justShowLoading() {
        if (!isRefreshing) {
            setLongPress(false);
            isRefreshing = true;
//            if (mProgressBar != null) {
//                mProgressBar.setVisibility(View.VISIBLE);
//            }
            if (loadingFrameLayout != null) {
                loadingFrameLayout.setVisibility(View.VISIBLE);
            }
            if (mRefreshListener != null) {
                mRefreshListener.onLoadMoreBegin(this, getKLineChartAdapter());
            }
            mLastScaleEnable = isScaleEnable();
            mLastScrollEnable = isScrollEnable();
            super.setScrollEnable(false);
            super.setScaleEnable(false);
        }
    }

    public void hideLoading() {
//        if (mProgressBar != null) {
//            mProgressBar.setVisibility(View.GONE);
//        }
        if (loadingFrameLayout != null) {
            loadingFrameLayout.setVisibility(View.GONE);
        }
        super.setScaleEnable(mLastScaleEnable);
        super.setScrollEnable(mLastScrollEnable);
    }

    /**
     * 隐藏选择器内容
     */
    public void hideSelectData() {
        setLongPress(false);
        invalidate();
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        isRefreshing = false;
        //首次加载完成后显示最右侧当前值
//        setScrollX((int) (0-((getOverScrollRange()/getScaleX()))));
        hideLoading();
    }

    /**
     * 刷新完成，没有数据
     */
    public void refreshEnd() {
        isLoadMoreEnd = true;
        isRefreshing = false;
        //首次加载完成后显示最右侧当前值
        hideLoading();
        postInvalidate();
        setScrollX((int) (0 - ((getOverScrollRange() / getScaleX()))));
    }

    /**
     * 重置加载更多
     */
    public void resetLoadMoreEnd() {
        isLoadMoreEnd = false;
    }

    public void setLoadMoreEnd() {
        isLoadMoreEnd = true;
    }


    public interface KChartRefreshListener {
        /**
         * 加载更多
         *
         * @param chart
         */
        void onLoadMoreBegin(KLineChartView chart, KLineChartAdapter adapter);
    }

    public KLineChartAdapter getKLineChartAdapter() {
        return KLineChartAdapter;
    }

    public void setKLineChartAdapter(KLineChartAdapter KLineChartAdapter) {
        this.KLineChartAdapter = KLineChartAdapter;
    }

    @Override
    public void setScaleEnable(boolean scaleEnable) {
        if (isRefreshing) {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScaleEnable(scaleEnable);

    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        if (isRefreshing) {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScrollEnable(scrollEnable);
    }

    /**
     * 设置Volume的宽度
     *
     * @param width
     */
    public void setVolumeWidth(float width) {
        mVolumeDraw.setVolumeWidth(width);
    }


    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        mMACDDraw.setDIFColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        mMACDDraw.setDEAColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        mMACDDraw.setMACDColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param width
     */
    public void setMACDWidth(float width) {
        mMACDDraw.setMACDWidth(width);
    }


    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKDJDraw.setKColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mKDJDraw.setDColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mKDJDraw.setJColor(color);
    }

    /**
     * 设置R颜色
     */
    public void setRColor(int color) {
        mWRDraw.setRColor(color);
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        mMainDraw.setMa5Color(color);
        mVolumeDraw.setMa5Color(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        mMainDraw.setMa10Color(color);
        mVolumeDraw.setMa10Color(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color
     */
    public void setMa30Color(int color) {
        mMainDraw.setMa30Color(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mMainDraw.setSelectorTextSize(textSize);
    }

    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mMainDraw.setSelectorTextColor(color);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mMainDraw.setSelectorBackgroundColor(color);
    }

    /**
     * 设置选择器边框
     *
     * @param color
     */
    public void setSelectorStrokeColor(int color) {
        mMainDraw.setSelectorStrokeColor(color);
    }

    /**
     * 设置蜡烛宽度
     *
     * @param width
     */
    public void setCandleWidth(float width) {
        mMainDraw.setCandleWidth(width);
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param width
     */
    public void setCandleLineWidth(float width) {
        super.mCandleLineWidth = width;
        mMainDraw.setCandleLineWidth(width);
    }


    public int getOneScreenMaxCandleSize() {
//        Log.d(TAG, "getOneScreenMaxCandleSize-getKLineWidth" + getKLineWidth());
//        Log.d(TAG, "getOneScreenMaxCandleSize-getOverScrollRange" + getOverScrollRange());
//        Log.d(TAG, "getOneScreenMaxCandleSize-getCandleWidth" + getCandleWidth());
        return (int) ((getKLineWidth() - getMinRightWidth())/ getCandleWidth());
    }
    /**
     * 设置蜡烛间隙
     */
    public void setCandleGapWidth(float width) {
        super.mCandleGap = width;
        mMainDraw.setCandleGapWidth(width);
        mVolumeDraw.setCandleGapWidth(width);
        mMACDDraw.setCandleGapWidth(width);
    }

    /**
     * 蜡烛是否空心
     */
    public void setCandleSolid(boolean candleSolid) {
        mMainDraw.setCandleSolid(candleSolid);
    }

    public void setRSI1Color(int color) {
        mRSIDraw.setRSI1Color(color);
    }

    public void setRSI2Color(int color) {
        mRSIDraw.setRSI2Color(color);
    }

    public void setRSI3Color(int color) {
        mRSIDraw.setRSI3Color(color);
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
    }


    /**
     * 设置指标字体大小
     * @param textSize
     */
    public void setIndexTextSize(float textSize) {
        super.setTextSize(textSize);
        mMainDraw.setIndexTextSize(textSize);
        mRSIDraw.setIndexTextSize(textSize);
        mMACDDraw.setIndexTextSize(textSize);
        mKDJDraw.setIndexTextSize(textSize);
        mWRDraw.setIndexTextSize(textSize);
        mVolumeDraw.setIndexTextSize(textSize);
    }

    /**
     * 设置右侧辅助价格字体大小
     * @param textSize
     */
    public void setRightTextSize(float textSize) {
        super.setRightTextSize(textSize);
    }

    @Override
    public void setLineWidth(float lineWidth) {
        super.setLineWidth(lineWidth);
        mMainDraw.setLineWidth(lineWidth);
        mRSIDraw.setLineWidth(lineWidth);
        mMACDDraw.setLineWidth(lineWidth);
        mKDJDraw.setLineWidth(lineWidth);
        mWRDraw.setLineWidth(lineWidth);
        mVolumeDraw.setLineWidth(lineWidth);
    }

    public void setTimeLineWidth(float lineWidth) {
        mMainDraw.setTimeLineWidth(lineWidth);
    }


    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    /**
     * 设置刷新监听
     */
    public void setRefreshListener(KChartRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void setMainDrawLine(boolean isLine) {
        mMainDraw.setLine(isLine);
        invalidate();
    }

    public boolean isLine() {
        return mMainDraw.isLine();
    }

    private int startX;
    private int startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                 return true;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void onLongPress(MotionEvent e) {
        if (!isRefreshing) {
            super.onLongPress(e);
        }
    }

    public void setScaleXMax(float scaleXMax) {
        super.setScaleXMax(scaleXMax);
    }

    public void setScaleXMin(float scaleXMax) {
        super.setScaleXMin(scaleXMax);
    }

    public void setScaleX(float scaleXMax) {
        super.setScaleX(scaleXMax);
    }


    /**
     * 设置红色文字颜色
     */
    public void setRedColor(int color) {
//        Log.d("COLOR-YY2:", String.valueOf(color));
        mMainDraw.setRed(color);
        mVolumeDraw.setRed(color);
        mMACDDraw.setRed(color);
        super.setMaxColor(color);
        super.setRed(color);
    }

    /**
     * 设置最小值文字颜色
     */
    public void setGreenColor(int color) {
        mMainDraw.setGreen(color);
        mVolumeDraw.setGreen(color);
        mMACDDraw.setGreen(color);
        super.setMinColor(color);
        super.setGreen(color);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setNormalColor(int color) {
        super.setNormal(color);

    }

    public void setWhiteColor(int color) {
        super.setWhite(color);

    }

    public void setTimerLineColor(int color) {
        super.setTimerLineColor(color);
    }

    public void setTimerLineCircleColor(int color) {
        super.setCurrentCircleColor(color);
    }

    public void setTimerLineCircleStrokeColor(int color) {
        super.setCurrentCircleStrokeColor(color);
    }

    public void setTimerStartColor(int color) {
        super.setTimerStartColor(color);
    }

    public void setTimerEndColor(int color) {
        super.setTimerEndColor(color);
    }

    /**
     * 设置是否空心MACD开关
     * @param candleHollowSwitch
     */
    public void setCandleHollowSwitch(boolean candleHollowSwitch) {
        mMACDDraw.setCandleHollowSwitch(candleHollowSwitch);
    }

    public void updateAnimationDuration(int duration) {
        super.updateAnimationDuration(duration);
    }
}
