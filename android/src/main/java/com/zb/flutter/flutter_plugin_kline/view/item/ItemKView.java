package com.zb.flutter.flutter_plugin_kline.view.item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.zb.flutter.flutter_plugin_kline.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SoMustYY
 * @create 2021-04-01 14:20
 * @organize 卓世达科
 * @describe 展开小K线View
 * @update
 */
public class ItemKView extends ItemGridChartKView {
    private static final String TAG = "ItemKView";
    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndex;

    /**
     * 显示的OHLC数据个数
     */
    private int mShowDataNum;

    /**
     * 当前数据的最大最小值
     */
    private double mMaxPrice;
    private double mMinPrice;
    /**
     * 记录最小值下标
     */
    private int minIndex;
    /**
     * 记录最大值下标
     */
    private int maxIndex;

    /**
     * 当前数据的最大最小值（分时）
     */
    private double mCloseMaxPrice;
    private double mCloseMinPrice;

    /**
     * 记录最小值下标（分时）
     */
    private int minCloseIndex;
    /**
     * 记录最大值下标（分时）
     */
    private int maxCloseIndex;

    /**
     * 显示纬线数（横轴）
     */
    private int latitudeNum = super.DEFAULT_LATITUDE_NUM;

    /**
     * 显示经线数(纵轴)
     */
    private int longitudeNum = super.DEFAULT_LOGITUDE_NUM;

    /**
     * 显示的最小Candle数
     */
    private final static int MIN_CANDLE_NUM = 10;
    /**
     * 显示的最大Candle数
     */
    private final static int MAX_CANDLE_NUM = 480;

    /**
     * 是否显示最大值最小值
     */
    private boolean showMaxMinTextShow = true;

    public void setShowMaxMinTextShow(boolean show) {
        this.showMaxMinTextShow = show;
    }

    /**
     * Candle宽度
     */
    private double mCandleWidth;
    /**
     * MA数据
     */
    private List<MALineEntity> MALineData;
    /**
     * OHLC数据
     */
    private List<MarketChartData> mOHLCData = new ArrayList<MarketChartData>();

    private int klineRed = 0xCD1A1E;
    private int klineGreen = 0x7AA376;
    /**
     * 默认分时均线颜色
     **/
    private int klineTimeColor = 0x535d66;

    /**
     * 默认五日均线颜色
     **/
    private int kline5dayLine = 0x535d66;
    /**
     * 默认十日均线颜色
     **/
    private int kline10dayLine = 0x535d66;
    /**
     * 默认30日均线颜色
     **/
    private int kline30dayLine = 0x535d66;
    /**
     * 分时线的渐变颜色(Start)
     **/
    private int timeStartColor = 0xCD1A1E;
    /**
     * 分时线的渐变颜色(End)
     **/
    private int timeEndColor = 0xCD1A1E;
    /**
     * K线类型（0:k线 1:分时）
     **/
    private int type = 1;

    public void setType(int t) {
        this.type = t;
    }

    public int getType() {
        return type;
    }

    private Context mContext;
    private Resources res;

    public ItemKView(Context context) {
        super(context);
        mContext = context;
        res = mContext.getResources();
        init();
    }

    public ItemKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        res = mContext.getResources();
        init();
    }

    public ItemKView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        res = mContext.getResources();
        init();
    }

    private void init() {
        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndex = 0;
        mMaxPrice = 0;
        mMinPrice = 0;
        initColor();
    }

    public void initColor() {
        setKlineRed(res.getColor(R.color.custom_item_kline_color_red_light));
        setKlineGreen(res.getColor(R.color.custom_item_kline_color_green_light));
        setKlineTimeColor(res.getColor(R.color.custom_item_kline_color_red_light));
        setKline5dayLine(res.getColor(R.color.custom_item_kline_5_day_line_light));
        setKline10dayLine(res.getColor(R.color.custom_item_kline_10_day_line_light));
        setKline30dayLine(res.getColor(R.color.custom_item_kline_30_day_line_light));
        setTimerStartColor(res.getColor(R.color.custom_color_kline_timer_start_text_color_light));
        setTimerEndColor(res.getColor(R.color.custom_color_kline_timer_end_text_color_light));
    }

    public void setKlineRed(int color) {
        klineRed = color;
    }

    public void setKlineGreen(int color) {
        klineGreen = color;
    }

    public void setKlineTimeColor(int klineTimeColor) {
        this.klineTimeColor = klineTimeColor;
    }

    public void setKline5dayLine(int color) {
        kline5dayLine = color;
    }

    public void setKline10dayLine(int color) {
        kline10dayLine = color;
    }

    public void setKline30dayLine(int color) {
        kline30dayLine = color;
    }

    public void setTimerStartColor(int color) {
        this.timeStartColor = color;
    }

    public void setTimerEndColor(int color) {
        this.timeEndColor = color;
    }

    public int getKlineRed() {
        return klineRed;
    }

    public int getKlineGreen() {
        return klineGreen;
    }

    public int getKlineTimeColor() {
        return klineTimeColor;
    }

    public int getKline5dayLine() {
        return kline5dayLine;
    }

    public int getKline10dayLine() {
        return kline10dayLine;
    }

    public int getKline30dayLine() {
        return kline30dayLine;
    }

    public int getTimeStartColor() {
        return timeStartColor;
    }

    public int getTimeEndColor() {
        return timeEndColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initAxisX();
        initAxisY();
        super.onDraw(canvas);
        if (type == 0) {
            drawUpperRegion(canvas);
            drawMA(canvas);
        } else {
//            drawTime(canvas);
            drawCloseTimeLine(canvas);
        }
    }


    /**
     * 初始化X轴
     */
    protected void initAxisX() {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> titleX = new ArrayList<>();

        if (isLessData()) {
            //不足1屏幕时，存储全部数据
            if (null != mOHLCData) {
                int step = 1;
                //上新币，数据不足显示量时，默认step = 1
                int limitSize = mOHLCData.size();
                setVisibleNumberTime(true);
                for (int i = 0; i < limitSize && mDataStartIndex + (i) * step < mOHLCData.size(); i++) {
                    String time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime2());
                    if (time.equals("00:00")) {
                        time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime4());
                    } else {
                        time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime2());
                    }
                    titleX.add(time);
                }

            }
        } else {
            if (null != mOHLCData) {
                int step = (int) Math.floor(mShowDataNum / longitudeNum);
                //上新币，数据不足显示量时，默认step = 1
                if (step == 0) step = 1;
//            LogUtils.d("ItemKView-mDataStartIndex", String.valueOf(mDataStartIndex));
//            LogUtils.d("ItemKView-mShowDataNum", String.valueOf(mShowDataNum));
//            LogUtils.d("ItemKView-longitudeNum", String.valueOf(longitudeNum));
//            LogUtils.d("ItemKView-step", String.valueOf(step));

                int limitSize;
                if (step == 1) {
//                LogUtils.d("ItemKView-type","ItemKView-step == 1-mOHLCData.size()："+mOHLCData.size());
                    limitSize = mOHLCData.size();
                    setVisibleNumberTime(true);
                } else {
//                LogUtils.d("ItemKView-type","ItemKView-step > 1-mOHLCData.size()："+mOHLCData.size());
                    limitSize = longitudeNum;
                    setVisibleNumberTime(false);
                }

//            for (int i = 0; i < mOHLCData.size(); i++) {
//                LogUtils.d("ItemKView-mOHLCData:",mOHLCData.get(i).getTime2());
//            }
                for (int i = 0; i < limitSize && mDataStartIndex + (i) * step < mOHLCData.size(); i++) {
                    String time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime2());
                    if (time.equals("00:00")) {
                        time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime4());
                    } else {
                        time = String.valueOf(mOHLCData.get((i) * step + mDataStartIndex).getTime2());
                    }
                    titleX.add(time);
                }

            }
        }
//        LogUtils.d("ItemKView", titleX.toString());
        super.setAxisXTitles(titleX);
    }

    /**
     * 初始化Y轴
     */
    protected void initAxisY() {
        //默认保留8位
        int format = 8;

        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        List<String> TitleY = new ArrayList<String>();
        float height = getUperChartHeight();
        if (height == 0 || height == 0.0f)
            return;
        double high = 0;
        double low = 0;
        float average = 1;
        String value = "";

        //1分时 0k线
        if (type == 1) {
            high = mOHLCData.get(maxCloseIndex).getClosePrice();
            low = mOHLCData.get(minCloseIndex).getClosePrice();
        } else {
            high = mOHLCData.get(maxIndex).getHighPrice();
            low = mOHLCData.get(minIndex).getLowPrice();
        }
        BigDecimal hBigDecimal = BigDecimal.valueOf(high);
        BigDecimal lBigDecimal = BigDecimal.valueOf(low);
        String maxp = deFormatNew(hBigDecimal.toString(), 8);
        String minp = deFormatNew(lBigDecimal.toString(), 8);

        int max = getPoint(maxp);
        int min = getPoint(minp);
        if (max > min) {
            format = max;
        } else {
            format = min;
        }
        if (format > 8) {
            format = 8;
        }
        //1分时 0k线
        if (type == 1) {
            average = (float) ((mCloseMaxPrice - mCloseMinPrice) / height) / 10 * 10;

            //处理最小值
            BigDecimal bigDecimal = BigDecimal.valueOf(mCloseMinPrice);
            value = deFormatNew(bigDecimal.toString(), format);
            TitleY.add(value);

            average = average * (getUperChartHeight() / latitudeNum);

            //处理所有Y刻度
            for (float i = 0; i < latitudeNum; i++) {
                BigDecimal mBigDecimal = BigDecimal.valueOf(mCloseMinPrice + i * average);
                value = mBigDecimal.toString();
                value = deFormatNew(value, format);
                TitleY.add(value);
            }
            //处理最大值
            BigDecimal maxDecimal = BigDecimal.valueOf(mCloseMaxPrice);
            value = deFormatNew(maxDecimal.toString(), format);
        } else {
            average = (float) ((mMaxPrice - mMinPrice) / height) / 10 * 10;

            //处理最小值
            BigDecimal bigDecimal = BigDecimal.valueOf(mMinPrice);
            value = deFormatNew(bigDecimal.toString(), format);
            TitleY.add(value);

            average = average * (getUperChartHeight() / latitudeNum);

            //处理所有Y刻度
            for (float i = 0; i < latitudeNum; i++) {
                BigDecimal mBigDecimal = BigDecimal.valueOf(mMinPrice + i * average);
                value = mBigDecimal.toString();
                value = deFormatNew(value, format);
                TitleY.add(value);
            }
            //处理最大值
            BigDecimal maxDecimal = BigDecimal.valueOf(mMaxPrice);
            value = deFormatNew(maxDecimal.toString(), format);
        }

        TitleY.add(value);
        super.setAxisYTitles(TitleY);
    }

    public int getPoint(String s) {
        int position = 0;
        int index = s.indexOf(".");
        if (index != -1) {
            position = s.length();
            index = s.indexOf(".");
            position = position - index - 1;
        } else {
            return 0;
        }
        return position;
    }

    public static String deFormatNew(String str, int type) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            String str_ws = "0.#";
            if (type == -1) {
                str_ws = "0.00";
            }
            for (int n = 1; type > 1 && n < type; n++) {
                str_ws = str_ws + "#";
            }
            DecimalFormat df_ls = new DecimalFormat(str_ws);
            str = df_ls.format(bigDecimal.setScale(type, BigDecimal.ROUND_FLOOR).doubleValue());
        } catch (Exception e) {
            str = str;
        }
        return str;
    }

    private void drawUpperRegion(Canvas canvas) {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        // 绘制蜡烛图
        Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(klineRed);
        redPaint.setStrokeWidth(2.5f); //蜡烛线宽
        Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setColor(klineGreen);
        greenPaint.setStrokeWidth(2.5f); //蜡烛线宽

        // 绘最大最小值
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        float gap = DEFAULT_AXIS_MARGIN_LEFT;
        float width = getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2;  //减去右标注边距

//        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;  //根据数据数量均分宽度
        mCandleWidth = width / 10.0 * 10.0 / DEFAULT_CANDLE_NUM; //蜡烛宽度为 最大宽度/最大数量（76）

        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mShowDataNum; i++) {
            MarketChartData entity;
            if (isLessData()) {
                entity = mOHLCData.get(mShowDataNum - (mDataStartIndex + i) - 1);
                rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
            } else {
                entity = mOHLCData.get(mDataStartIndex + i);
            }
            float open = (float) ((mMaxPrice - entity.getOpenPrice()) * rate + UPER_CHART_MARGIN_TOP);
            float close = (float) ((mMaxPrice - entity.getClosePrice()) * rate + UPER_CHART_MARGIN_TOP);
            float high = (float) ((mMaxPrice - entity.getHighPrice()) * rate + UPER_CHART_MARGIN_TOP);
            float low = (float) ((mMaxPrice - entity.getLowPrice()) * rate + UPER_CHART_MARGIN_TOP);

            float left;
            float right;
            float startX;
            if (isLessData()) {
                left = (float) ((mCandleWidth * i));
                right = (float) ((mCandleWidth * (i + 1)));
                startX = (float) ((mCandleWidth * i) + mCandleWidth / 2);
            } else {
                left = (float) (width - mCandleWidth * (i + 1));
                right = (float) (width - mCandleWidth * i);
                startX = (float) (width - mCandleWidth * i - mCandleWidth / 2);
            }
            if (isLessData()) {
                if (open < close) {
                    canvas.drawRect((float) (startX - (mCandleWidth / 2) + gap), open, (float) (startX + (mCandleWidth / 2) - gap), close, greenPaint);
                    canvas.drawLine(startX, high, startX, low, greenPaint);
                } else if (open == close) {
                    canvas.drawRect((float) (startX - (mCandleWidth / 2) + gap), close - 1, (float) (startX + (mCandleWidth / 2) - gap), open + 1, redPaint);
                    canvas.drawLine(startX, high, startX, low, redPaint);
                } else {
                    canvas.drawRect((float) (startX - (mCandleWidth / 2) + gap), close, (float) (startX + (mCandleWidth / 2) - gap), open, redPaint);
                    canvas.drawLine(startX, high, startX, close, redPaint);
                    canvas.drawLine(startX, open, startX, low, redPaint);
                }
            } else {
                if (open < close) {
                    canvas.drawRect(left + gap, open, right - gap, close, greenPaint);
                    canvas.drawLine(startX, high, startX, low, greenPaint);
                } else if (open == close) {
                    canvas.drawRect(left + gap, close - 1, right - gap, open + 1, redPaint);
                    canvas.drawLine(startX, high, startX, low, redPaint);
                } else {
                    canvas.drawRect(left + gap, close, right - gap, open, redPaint);
                    canvas.drawLine(startX, high, startX, close, redPaint);
                    canvas.drawLine(startX, open, startX, low, redPaint);
                }
            }
            Rect rect = new Rect();
            boolean isMaxIndex;
            boolean isMinIndex;
            if (isLessData()) {
                isMaxIndex = (mShowDataNum - mDataStartIndex - i - 1) == maxIndex;
                isMinIndex = (mShowDataNum - mDataStartIndex - i - 1) == minIndex;
            } else {
                isMaxIndex = mDataStartIndex + i == maxIndex;
                isMinIndex = mDataStartIndex + i == minIndex;
            }
            //最大值标识
            if (isMaxIndex) {
                String maxPrice = entity.getHighPrice() + "";
                paint.getTextBounds(maxPrice, 0, 1, rect);
                float w = paint.measureText(maxPrice);
                if (isLessData()) { //默认 右指左 箭头
                    canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 25, high - DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 10, high - DEFAULT_AXIS_TITLE_SIZE - 10, paint);
                    canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 10, high - DEFAULT_AXIS_TITLE_SIZE + 10, paint);
                    canvas.drawText(deFormatNew(maxPrice, 8), startX + 25, high - DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                } else {
                    if ((i * mCandleWidth + mCandleWidth / 2) > (w + 25)) {
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 25, high - DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 10, high - DEFAULT_AXIS_TITLE_SIZE - 10, paint);
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX + 10, high - DEFAULT_AXIS_TITLE_SIZE + 10, paint);
                        canvas.drawText(deFormatNew(maxPrice, 8), startX + 25, high - DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                    } else {
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX - 25, high - DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX - 10, high - DEFAULT_AXIS_TITLE_SIZE + 10, paint);
                        canvas.drawLine(startX, high - DEFAULT_AXIS_TITLE_SIZE, startX - 10, high - DEFAULT_AXIS_TITLE_SIZE - 10, paint);
                        canvas.drawText(deFormatNew(maxPrice, 8), startX - 25 - w, high - DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                    }
                }
            }
            //最小值标识
            if (isMinIndex) {
                String minPrice = entity.getLowPrice() + "";
                paint.getTextBounds(minPrice, 0, 1, rect);
                float w = paint.measureText(minPrice);
                if (isLessData()) { //默认 右指左 箭头
                    canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 25, low + DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 10, low + 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 10, low - 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                    canvas.drawText(deFormatNew(minPrice, 8), startX + 25, low + DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                } else {
                    if ((i * mCandleWidth + mCandleWidth / 2) > (w + 25)) {
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 25, low + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 10, low + 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX + 10, low - 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawText(deFormatNew(minPrice, 8), startX + 25, low + DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                    } else {
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX - 25, low + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX - 10, low - 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawLine(startX, low + DEFAULT_AXIS_TITLE_SIZE, startX - 10, low + 10 + DEFAULT_AXIS_TITLE_SIZE, paint);
                        canvas.drawText(deFormatNew(minPrice, 8), startX - 25 - w, low + DEFAULT_AXIS_TITLE_SIZE + rect.height() / 2, paint);
                    }
                }
            }
        }
    }

    private void setTimeMaxMinPrice() {
        if (mOHLCData == null || mOHLCData.size() <= 0 || mDataStartIndex < 0) {
            return;
        }
        mMinPrice = mOHLCData.get(mDataStartIndex).getLowPrice();
        mMaxPrice = mOHLCData.get(mDataStartIndex).getHighPrice();
        minIndex = mDataStartIndex;
        maxIndex = mDataStartIndex;
        mCloseMinPrice = mOHLCData.get(mDataStartIndex).getClosePrice();
        mCloseMaxPrice = mOHLCData.get(mDataStartIndex).getClosePrice();
        minCloseIndex = mDataStartIndex;
        maxCloseIndex = mDataStartIndex;

        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndex + i);
            if (mCloseMinPrice > entity.getClosePrice()) {
                mCloseMinPrice = entity.getClosePrice();
                minCloseIndex = mDataStartIndex + i;
            }
            if (mCloseMaxPrice < entity.getClosePrice()) {
                mCloseMaxPrice = entity.getClosePrice();
                maxCloseIndex = i + mDataStartIndex;
            }
            if (mMinPrice > entity.getLowPrice()) {
                mMinPrice = entity.getLowPrice();
                minIndex = mDataStartIndex + i;
            }
            if (mMaxPrice < entity.getHighPrice()) {
                mMaxPrice = entity.getHighPrice();
                maxIndex = i + mDataStartIndex;
            }
        }
    }

    private void drawCloseTimeLine(Canvas canvas) {
        if (mOHLCData == null) return;
        setTimeMaxMinPrice();
        float width = getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - 2;
        mCandleWidth = width / 10.0 * 10.0 / mShowDataNum;
        double rate = 1;
        if (mCloseMaxPrice != mCloseMinPrice) {
            rate = (getUperChartHeight()) / (mCloseMaxPrice - mCloseMinPrice);
        } else {
            rate = 1;
        }
        // 绘制上部曲线图及上部分MA值
        float startX = 0;
        float startY = 0;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //连接的外边缘以圆弧的方式相交
        paint.setStrokeJoin(Paint.Join.ROUND);
        //线条结束处绘制一个半圆
        paint.setStrokeCap(Paint.Cap.ROUND);
        //  paint.setPathEffect(new CornerPathEffect(5));
        paint.setColor(klineTimeColor);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mOHLCData.size(); i++) {
            //画分时线
            if (i != 0) {
                float wt = (startX + (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - mCandleWidth * i - mCandleWidth * 0.5f)) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startY;
                p3.x = wt;
                p4.y = (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndex + i).getClosePrice()) * rate) + UPER_CHART_MARGIN_TOP;
                p4.x = wt;

                Path path = new Path();
                path.moveTo(startX, startY);
                path.cubicTo(p3.x, p3.y, p4.x, p4.y, (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - mCandleWidth * i - mCandleWidth * 0.5f), (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndex + i).getClosePrice()) * rate) + UPER_CHART_MARGIN_TOP);
                canvas.drawPath(path, paint);
            }
            startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - mCandleWidth * i - mCandleWidth * 0.5f);
            startY = (float) ((mCloseMaxPrice - mOHLCData.get(mDataStartIndex + i).getClosePrice()) * rate) + UPER_CHART_MARGIN_TOP;

            //画最大和最小值
            Paint paintMinOrMax = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintMinOrMax.setStrokeWidth(4);
            paintMinOrMax.setAntiAlias(true);
            paintMinOrMax.setColor(klineTimeColor);
            paintMinOrMax.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            paintMinOrMax.setStyle(Paint.Style.FILL);
            if (showMaxMinTextShow) {
                Rect rect = new Rect();
                if (mDataStartIndex + i == maxCloseIndex) {
                    String price = mOHLCData.get(mDataStartIndex + i).getClosePrice() + "";
                    paintMinOrMax.getTextBounds(price, 0, 1, rect);
                    float w = paintMinOrMax.measureText(price);
                    if ((startX - w) > 0) {
                        canvas.drawCircle(startX, startY, 10, paintMinOrMax);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndex + i).getClosePrice() + "", 8), startX - w, startY - DEFAULT_AXIS_TITLE_SIZE + 5, paintMinOrMax);
                    } else {
                        canvas.drawCircle(startX, startY, 10, paintMinOrMax);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndex + i).getClosePrice() + "", 8), startX, startY - DEFAULT_AXIS_TITLE_SIZE + 5, paintMinOrMax);
                    }
                }

                if (mDataStartIndex + i == minCloseIndex) {
                    String price = mOHLCData.get(mDataStartIndex + i).getClosePrice() + "";
                    paintMinOrMax.getTextBounds(price, 0, 1, rect);
                    float w = paintMinOrMax.measureText(price);
                    if ((startX + w) > super.getWidth()) {
                        canvas.drawCircle(startX, startY, 10, paintMinOrMax);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndex + i).getClosePrice() + "", 8), startX - w, startY + 4 * DEFAULT_AXIS_TITLE_SIZE / 3, paintMinOrMax);
                    } else {
                        canvas.drawCircle(startX, startY, 10, paintMinOrMax);
                        canvas.drawText(deFormatNew(mOHLCData.get(mDataStartIndex + i).getClosePrice() + "", 8), startX, startY + 4 * DEFAULT_AXIS_TITLE_SIZE / 3, paintMinOrMax);
                    }
                }
            }
        }
    }

    private void drawMA(Canvas canvas) {
        if (MALineData == null) return;
        float gap = sp2px(mContext, 1);
        double rate = (getUperChartHeight()) / (mMaxPrice - mMinPrice);
        // 绘制上部曲线图及上部分MA值
        for (int j = 0; j < MALineData.size(); j++) {
            MALineEntity lineEntity = MALineData.get(j);
            float startX = 0;
            float startY = 0;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(lineEntity.getLineColor());
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mShowDataNum; i++) {
                if (isLessData()) {
                    if (i != 0) {
                        canvas.drawLine(
                                startX,
                                startY < UPER_CHART_MARGIN_TOP ? UPER_CHART_MARGIN_TOP : startY,
                                (float) (mCandleWidth * i + mCandleWidth * 0.5f),
                                (float) (((mMaxPrice - lineEntity.getLineData().get((mShowDataNum - i - 1))) * rate) + UPER_CHART_MARGIN_TOP) > super.getHeight() - UPER_CHART_MARGIN_BOTTOM ? super.getHeight() - UPER_CHART_MARGIN_BOTTOM : (float) ((mMaxPrice - lineEntity.getLineData().get((mShowDataNum - i - 1))) * rate) + UPER_CHART_MARGIN_TOP,
                                paint);
                    }
                    startX = (float) (mCandleWidth * i + mCandleWidth * 0.5f);
                    startY = (float) ((mMaxPrice - lineEntity.getLineData().get((mShowDataNum - i - 1))) * rate) + UPER_CHART_MARGIN_TOP;
                } else {
                    if (i != 0) {
                        canvas.drawLine(
                                startX,
                                startY < UPER_CHART_MARGIN_TOP ? UPER_CHART_MARGIN_TOP : startY,
                                (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - mCandleWidth * i - mCandleWidth * 0.5f),
                                (float) (((mMaxPrice - lineEntity.getLineData().get(i)) * rate) + UPER_CHART_MARGIN_TOP) > super.getHeight() - UPER_CHART_MARGIN_BOTTOM ? super.getHeight() - UPER_CHART_MARGIN_BOTTOM : (float) ((mMaxPrice - lineEntity.getLineData().get(i)) * rate) + UPER_CHART_MARGIN_TOP,
                                paint);
                    }
                    startX = (float) (super.getWidth() - super.DEFAULT_AXIS_MARGIN_RIGHT - mCandleWidth * i - mCandleWidth * 0.5f);
                    startY = (float) ((mMaxPrice - lineEntity.getLineData().get(i)) * rate) + UPER_CHART_MARGIN_TOP;
                }

            }
        }
    }

    private void initShowDataNum() {
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }
        if (MIN_CANDLE_NUM > mOHLCData.size()) {
            mShowDataNum = mOHLCData.size();
        }
    }

    private void setCurrentData() {
        initShowDataNum();
        setTimeMaxMinPrice();
        setMaxMinPrice();
    }


    private void setMaxMinPrice() {
        if (mOHLCData == null || mOHLCData.size() <= 0 || mDataStartIndex < 0) {
            return;
        }
        mMinPrice = mOHLCData.get(mDataStartIndex).getLowPrice();
        mMaxPrice = mOHLCData.get(mDataStartIndex).getHighPrice();

        minIndex = mDataStartIndex;
        maxIndex = mDataStartIndex;

        for (int i = 0; i < mShowDataNum && mDataStartIndex + i < mOHLCData.size(); i++) {
            MarketChartData entity = mOHLCData.get(mDataStartIndex + i);

            if (mMinPrice > entity.getLowPrice()) {
                mMinPrice = entity.getLowPrice();
                minIndex = mDataStartIndex + i;
            }
            if (mMaxPrice < entity.getHighPrice()) {
                mMaxPrice = entity.getHighPrice();
                maxIndex = i + mDataStartIndex;
            }
        }
    }

    public void setOHLCData(List<MarketChartData> OHLCData) {
        if (OHLCData.size() < DEFAULT_CANDLE_NUM) {
            setLessData(true);
        } else {
            setLessData(false);
        }
        //分时，小时切换，重置  mDataStartIndex
        mDataStartIndex = 0;
        mShowDataNum = DEFAULT_CANDLE_NUM;
        minCloseIndex = 0;
        maxCloseIndex = 0;
        minIndex = 0;
        maxIndex = 0;
        if (OHLCData.size() <= 0) {
            return;
        }
        super.setDataSize(OHLCData.size());
        if (null != mOHLCData) {
            mOHLCData.clear();
        }
        for (MarketChartData e : OHLCData) {
            addData(e);
        }
        initMALineData();
        setCurrentData();
        postInvalidate();
    }

    public void clearOHLCData() {
        if (null != mOHLCData) {
            mOHLCData.clear();
            postInvalidate();
        }
    }

    public void addData(MarketChartData entity) {
        if (null != entity) {
            if (null == mOHLCData || 0 == mOHLCData.size()) {
                mOHLCData = new ArrayList<>();
                this.mMinPrice = ((int) entity.getLowPrice()) / 10 * 10;
                this.mMaxPrice = ((int) entity.getHighPrice()) / 10 * 10;
            }
            this.mOHLCData.add(entity);
            if (this.mMinPrice > entity.getLowPrice()) {
                this.mMinPrice = ((int) entity.getLowPrice()) / 10 * 10;
            }
            if (this.mMaxPrice < entity.getHighPrice()) {
                this.mMaxPrice = 10 + ((int) entity.getHighPrice()) / 10 * 10;
            }
        }
    }

    /**
     * 初始化K线均线数据
     */
    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(kline30dayLine);
        MA5.setLineData(initMA(mOHLCData, 5));
        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(kline10dayLine);
        MA10.setLineData(initMA(mOHLCData, 10));
        MALineData = new ArrayList<>();
        MALineData.add(MA5);
        MALineData.add(MA10);
    }


    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private List<Double> initMA(List<MarketChartData> entityList, int days) {
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Double> MAValues = new ArrayList<>();
        Double sum = 0.0;
        double avg;
        for (int i = entityList.size() - 1; i >= 0; i--) {
            Double close = entityList.get(i).getClosePrice();
            if (entityList.size() - i < days) {
                sum = sum + close;
                int d = entityList.size() - i;
                avg = sum / d;
            } else {
                sum = 0.0;
                for (int j = 0; j < days; j++) {
                    sum = sum + entityList.get(i + j).getClosePrice();
                }
                avg = sum / days;
            }
            MAValues.add(avg);
        }
        List<Double> result = new ArrayList<Double>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }

    public void refreshData() {
        postInvalidate();
    }

}


