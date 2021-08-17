package com.zb.module.kline_module.view.klinechart.draw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;
import com.zb.module.kline_module.R;
import com.zb.module.kline_module.view.klinechart.base.IChartDraw;
import com.zb.module.kline_module.view.klinechart.base.IValueFormatter;
import com.zb.module.kline_module.view.klinechart.entity.ICandle;
import com.zb.module.kline_module.view.klinechart.entity.IKLine;
import com.zb.module.kline_module.view.klinechart.formatter.BigValueFormatter;
import com.zb.module.kline_module.view.klinechart.utils.FormatUtil2;
import com.zb.module.kline_module.view.klinechart.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 主图的实现类
 * @update
 */
public class MainDraw implements IChartDraw<ICandle> {
    private static final String TAG = "MainDraw";
    private float mPointWidth = 0;
    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;
    private float mCandleGapWidth = 0;

    private Paint mCandleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTimeLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//分时线
    private Paint timeBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  //分时线下部阴影

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma30Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorsStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    private boolean mCandleSolid = true;
    // 是否分时
    private boolean isLine = false;
    private MainIndexStatus mainIndexStatus = MainIndexStatus.NONE;
    private BaseKLineChartView mView;
    private int mDecimal = DEFAULT_DECIMAL;

    private int red;
    private int green;

    public MainDraw(BaseKLineChartView view, int decimal) {
        Context context = view.getContext();
        mView = view;
        mContext = context;
        mDecimal = decimal;

        smoothPaint();

    }

    public BaseKLineChartView getView() {
        return mView;
    }


    private void smoothPaint() {
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mRedPaint.setStrokeJoin(Paint.Join.ROUND);
        mGreenPaint.setStrokeJoin(Paint.Join.ROUND);
        ma5Paint.setStrokeJoin(Paint.Join.ROUND);
        ma10Paint.setStrokeJoin(Paint.Join.ROUND);
        ma30Paint.setStrokeJoin(Paint.Join.ROUND);


        ma5Paint.setAntiAlias(true);//抗锯齿
        ma10Paint.setAntiAlias(true);//抗锯齿
        ma30Paint.setAntiAlias(true);//抗锯齿

        ma5Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        ma10Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        ma30Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
    }


    public void setRed(int color) {
        this.red = color;
        mRedPaint.setColor(red);
    }

    public void setGreen(int color) {
        this.green = color;
        mGreenPaint.setColor(green);
    }

    public void setMainIndexStatus(MainIndexStatus mainIndexStatus) {
        this.mainIndexStatus = mainIndexStatus;
    }

    public MainIndexStatus getMainIndexStatus() {
        return mainIndexStatus;
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (isLine) {
            view.drawTimerBgLine(canvas, timeBgPaint, lastX, (float) lastPoint.getClosePrice(), curX, (float) curPoint.getClosePrice());
        }

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable ICandle lastPoint, @NonNull ICandle curPoint , @NonNull ICandle nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (isLine) {
            view.drawTimerLine(canvas, mTimeLinePaint, lastX, (float) lastPoint.getClosePrice(), curX, (float) curPoint.getClosePrice());
        } else {
            drawCandle(view, canvas, curX, (float) curPoint.getHighPrice(), (float) curPoint.getLowPrice(), (float) curPoint.getOpenPrice(), (float) curPoint.getClosePrice());
            drawTargetLine(lastPoint, curPoint, lastX, curX, canvas, view, position);
        }
    }


    /**
     * 绘制指标数据线
     *
     * @param lastPoint
     * @param curPoint
     * @param lastX
     * @param curX
     * @param canvas
     * @param view
     * @param position
     */
    private void drawTargetLine(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (mainIndexStatus == MainIndexStatus.MA) {
            //画ma5
            if (lastPoint.getMA5Price() != 0) {
                view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
            }
            //画ma10
            if (lastPoint.getMA10Price() != 0) {
                view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
            }
            //画ma30
            if (lastPoint.getMA30Price() != 0) {
                view.drawMainLine(canvas, ma30Paint, lastX, lastPoint.getMA30Price(), curX, curPoint.getMA30Price());
            }
        } else if (mainIndexStatus == MainIndexStatus.BOLL) {
            //画boll
            if (lastPoint.getUp() != 0) {
                view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
            }
            if (lastPoint.getMb() != 0) {
                view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
            }
            if (lastPoint.getDn() != 0) {
                view.drawMainLine(canvas, ma30Paint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
            }
        } else if (mainIndexStatus == MainIndexStatus.EMA) {
            //画ema
            if (lastPoint.getEMA5Value() != 0) {
                view.drawMainLine(canvas, ma5Paint, lastX, (float) lastPoint.getEMA5Value(), curX, (float) curPoint.getEMA5Value());
            }
            if (lastPoint.getEMA10Value() != 0) {
                view.drawMainLine(canvas, ma10Paint, lastX, (float) lastPoint.getEMA10Value(), curX, (float) curPoint.getEMA10Value());
            }
            if (lastPoint.getEMA30Value() != 0) {
                view.drawMainLine(canvas, ma30Paint, lastX, (float) lastPoint.getEMA30Value(), curX, (float) curPoint.getEMA30Value());
            }
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        if (!isLine()) {
            drawTargetText(canvas, view, position, x, y);
        }
    }

    @Override
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        if (view.isLongPress()) {
            drawSelectorBox(view, canvas);
        }
    }


    /**
     * 绘制主副指标图 值
     *
     * @param canvas
     * @param view
     * @param position
     * @param x
     * @param y
     */
    private void drawTargetText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        ICandle point = (IKLine) view.getItem(position);
        if (point != null) {
            if (mainIndexStatus == MainIndexStatus.MA) {
                String textMA5 = "MA5:" + view.formatPriceValue(point.getMA5Price());
                String textMA10 = "MA10:" + view.formatPriceValue(point.getMA10Price());
                String textMA30 = "MA30:" + view.formatPriceValue(point.getMA30Price());
                float MA5X = x;
                float MA10X = MA5X + view.getTextPaint().measureText(textMA5);
                float MA30X = MA10X + view.getTextPaint().measureText(textMA10);

                if (point.getMA5Price() != 0) {
                    canvas.drawText(textMA5, MA5X + view.dp2px(4), y, ma5Paint);
                }
                if (point.getMA10Price() != 0) {
                    canvas.drawText(textMA10, MA10X + view.dp2px(20), y, ma10Paint);
                }
                if(!view.isFull()){
                    if (MA30X > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textMA30) > view.dp2px(14)) {
                        if (point.getMA20Price() != 0) {
                            canvas.drawText(textMA30, MA5X + view.dp2px(4), y + getFontHeight(ma5Paint) + view.dp2px(2), ma30Paint);
                        }
                    } else {
                        if (point.getMA20Price() != 0) {
                            canvas.drawText(textMA30, MA30X + view.dp2px(36), y, ma30Paint);
                        }
                    }
                }else{
                    if (point.getMA20Price() != 0) {
                        canvas.drawText(textMA30, MA30X + view.dp2px(36), y, ma30Paint);
                    }
                }

            } else if (mainIndexStatus == MainIndexStatus.BOLL) {
                if (point.getMb() != 0) {
                    String textBOLL = "BOLL:" + view.formatPriceValue(point.getMb());
                    String textUB = "UB:" + view.formatPriceValue(point.getUp());
                    String textLB = "LB:" + view.formatPriceValue(point.getDn());
                    float BOLLX = x;
                    float UBX = BOLLX + view.getTextPaint().measureText(textBOLL);
                    float LBX = UBX + view.getTextPaint().measureText(textUB);

                    canvas.drawText(textBOLL, BOLLX + view.dp2px(4), y, ma5Paint);
                    canvas.drawText(textUB, UBX + view.dp2px(20), y, ma10Paint);
                    if(!view.isFull()){
                        if (LBX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textLB) > view.dp2px(14)) {
                            canvas.drawText(textLB, BOLLX + view.dp2px(4), y + getFontHeight(ma5Paint) + view.dp2px(2), ma30Paint);
                        } else {
                            canvas.drawText(textLB, LBX + view.dp2px(36), y, ma30Paint);
                        }
                    }else{
                        canvas.drawText(textLB, LBX + view.dp2px(36), y, ma30Paint);
                    }

                }
            } else if (mainIndexStatus == MainIndexStatus.EMA) {
                String textEMA5 = "EMA5:" + view.formatPriceValue(point.getEMA5Value());
                String textEMA10 = "EMA10:" + view.formatPriceValue(point.getEMA10Value());
                String textEMA30 = "EMA30:" + view.formatPriceValue(point.getEMA30Value());
                float EMA5X = x;
                float EMA10X = EMA5X + view.getTextPaint().measureText(textEMA5);
                float EMA30X = EMA10X + view.getTextPaint().measureText(textEMA10);

                if (point.getEMA5Value() != 0) {
                    canvas.drawText(textEMA5, EMA5X + view.dp2px(4), y, ma5Paint);
                }
                if (point.getEMA10Value() != 0) {
                    canvas.drawText(textEMA10, EMA10X + view.dp2px(20), y, ma10Paint);
                }
                if(!view.isFull()){
                    if (EMA30X > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textEMA30) > view.dp2px(14)) {
                        if (point.getEMA30Value() != 0) {
                            canvas.drawText(textEMA30, EMA5X + view.dp2px(4), y + getFontHeight(ma5Paint) + view.dp2px(2), ma30Paint);
                        }
                    } else {
                        if (point.getEMA30Value() != 0) {
                            canvas.drawText(textEMA30, EMA30X + view.dp2px(36), y, ma30Paint);
                        }
                    }
                }else{
                    if (point.getEMA30Value() != 0) {
                        canvas.drawText(textEMA30, EMA30X + view.dp2px(36), y, ma30Paint);
                    }
                }

            }
        }
    }

    /**
     * 计算文本长度
     *
     * @return
     */
    private int calculateWidth(String text) {
        Rect rect = new Rect();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    @Override
    public float getMaxValue(ICandle point) {
        if (point != null) {
            float price = (float) (isLine() ? point.getClosePrice() : (point.getMA30Price() == 0f) ? point.getHighPrice() : Math.max(point.getMA30Price(), point.getHighPrice()));
            if (mainIndexStatus == MainIndexStatus.BOLL) {
                if (Float.isNaN(point.getUp())) {
                    if (point.getMb() == 0) {
                        return price;
                    } else {
                        //getMb>getHighPrice，BOLL线会超出边界
                        return Math.max(price, point.getMb());
                    }
                } else if (point.getUp() == 0) {
                    return price;
                } else {
                    //getUp>getHighPrice，BOLL线会超出边界
                    return Math.max(price, point.getUp());
                }
            } else {
                return price;
            }
        } else {
            return 0;
        }
    }

    @Override
    public float getMinValue(ICandle point) {
        if (point != null) {
            float price = (float) (isLine() ? point.getClosePrice() : (point.getMA30Price() == 0f) ? point.getLowPrice() : Math.min(point.getMA30Price(), point.getLowPrice()));
            if (mainIndexStatus == MainIndexStatus.BOLL) {
                if (point.getDn() == 0) {
                    return price;
                } else {
                    //getDn<getLowPrice时，BOLL线会超出边界
                    return Math.min(point.getDn(), price);
                }
            } else {
                return price;
            }
        } else {
            return 0;
        }
    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }

    /**
     * 画蜡烛图
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKLineChartView view, Canvas canvas, float x, float high, float low, float open, float close) {
//        Log.d(TAG, "drawCandle-" + "x:" + x);
        high = (float) view.getMainY(high);
        low = (float) view.getMainY(low);
        open = (float) view.getMainY(open);
        close = (float) view.getMainY(close);

        float r = (mPointWidth * view.getScaleX() - (2 * (mCandleGapWidth))) / 2;
//        Log.d(TAG, "drawCandle-" + "mPointWidth:" + mPointWidth);
//        Log.d(TAG, "drawCandle-" + "r:" + r);
//        Log.d(TAG, "drawCandle-" + "view.px2dp(mPointWidth):" + view.px2dp(mPointWidth));
//        Log.d(TAG, "drawCandle-" + "view.px2dp(r):" + view.px2dp(r));
        float lineR = mCandleLineWidth / 2;
        if (open > close) {
            if (mCandleSolid) {
                canvas.drawRect(x - r, close, x + r, open, mRedPaint); // 蜡烛
//                canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint); //线
                mCandleLinePaint.setColor(red);
                mCandleLinePaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, low, mCandleLinePaint);
            } else {
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, close, mRedPaint);
                canvas.drawLine(x, open, x, low, mRedPaint);
                canvas.drawLine(x - r + lineR, open, x - r + lineR, close, mRedPaint);
                canvas.drawLine(x + r - lineR, open, x + r - lineR, close, mRedPaint);
                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                canvas.drawLine(x - r, open, x + r, open, mRedPaint);
                canvas.drawLine(x - r, close, x + r, close, mRedPaint);
            }

        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, mGreenPaint); // 蜡烛
//            canvas.drawRect(x - lineR, high, x + lineR, low, mGreenPaint); //线
            mCandleLinePaint.setColor(green);
            mCandleLinePaint.setStrokeWidth(mCandleLineWidth);
            canvas.drawLine(x, high, x, low, mCandleLinePaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, mRedPaint); //蜡烛
//            canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint); //线
            mCandleLinePaint.setColor(red);
            mCandleLinePaint.setStrokeWidth(mCandleLineWidth);
            canvas.drawLine(x, high, x, low, mCandleLinePaint);
        }


    }

    /**
     * draw选择器(长按触摸后，绘制动态矩形，显示各项值的数据)
     *
     * @param view
     * @param canvas
     */
    private void drawSelectorBox(BaseKLineChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;
        Resources res = mContext.getResources();
        int itemCount = view.getItemCount();
        int index = view.getSelectedIndex();
        float leftPadding = ViewUtil.Dp2Px(mContext, 8);  //左右边距 8dp
        float textPadding = ViewUtil.Dp2Px(mContext, 4);  //文字上下间距 4dp

        float margin = ViewUtil.Dp2Px(mContext, 10);
        float width = 0;
        float left;
        float top = margin + view.getTopPadding();
        float bottom = margin + view.getPaddingBottom();
        float height = textPadding * 9 + textHeight * 8;

        ICandle point = (ICandle) view.getItem(index);
        if (point != null) {
            List<String> selectorTitles = new ArrayList<>();
            List<String> selectorValues = new ArrayList<>();
            List<String> stringResults = new ArrayList<>();
            String gainOrAmplitude = calculateGainOrAmplitude(view, itemCount, index);
            String gain = gainOrAmplitude.split(",")[0];
            String amplitude = gainOrAmplitude.split(",")[1];

            selectorTitles.add(res.getString(R.string.txt_kline_time));
            selectorTitles.add(res.getString(R.string.txt_kline_box_open));
            selectorTitles.add(res.getString(R.string.txt_kline_box_high));
            selectorTitles.add(res.getString(R.string.txt_kline_box_low));
            selectorTitles.add(res.getString(R.string.txt_kline_box_close));
            selectorTitles.add(res.getString(R.string.txt_kline_gain));
            selectorTitles.add(res.getString(R.string.txt_kline_amplitude));
            selectorTitles.add(res.getString(R.string.txt_kline_vol));

            selectorValues.add(view.getAdapter().getAllDate(index));
            selectorValues.add("" + view.formatPriceValue(point.getOpenPrice()));
            selectorValues.add("" + view.formatPriceValue(point.getHighPrice()));
            selectorValues.add("" + view.formatPriceValue(point.getLowPrice()));
            selectorValues.add("" + view.formatPriceValue(point.getClosePrice()));
            selectorValues.add("" + gain);
            selectorValues.add("" + amplitude);
//            selectorValues.add("" + view.formatAmountValue(point.getVol()));
            selectorValues.add(getValueFormatter(mContext).format(Float.parseFloat(Double.toString(point.getVol())),view.getAmountDecimal()));

            stringResults.add(res.getString(R.string.txt_kline_time) + "   " + view.getAdapter().getAllDate(index));
            stringResults.add(res.getString(R.string.txt_kline_box_open) + "   " + view.formatPriceValue(point.getOpenPrice()));
            stringResults.add(res.getString(R.string.txt_kline_box_high) + "   " + view.formatPriceValue(point.getHighPrice()));
            stringResults.add(res.getString(R.string.txt_kline_box_low) + "   " + view.formatPriceValue(point.getLowPrice()));
            stringResults.add(res.getString(R.string.txt_kline_box_close) + "   " + view.formatPriceValue(point.getClosePrice()));
            stringResults.add(res.getString(R.string.txt_kline_gain) + "   " + gain);
            stringResults.add(res.getString(R.string.txt_kline_amplitude) + "   " + amplitude);
            stringResults.add(res.getString(R.string.txt_kline_vol) + "   " + getValueFormatter(mContext).format(Float.parseFloat(Double.toString(point.getVol())),view.getAmountDecimal()));
//          stringResults.add(res.getString(R.string.txt_kline_vol) + "   " + view.formatAmountValue(point.getVol()));


            for (String s : stringResults) {
                width = Math.max(width, mSelectorTextPaint.measureText(s));
            }

            width += leftPadding * 2;
            float x = view.translateXtoX(view.getX(index));

            if (x > view.getChartWidth() / 2) {
                if (view.isFull()) {
                    left = margin + ViewUtil.Dp2Px(mContext, 20);
                } else {
                    left = margin;
                }

            } else {
                if (view.isFull()) {
                    left = view.getChartWidth() - width - margin - margin - ViewUtil.Dp2Px(mContext, 20);
                } else {
                    left = view.getChartWidth() - width - margin - margin;
                }
            }
            RectF rect = new RectF((int) left, (int) top, (int) (left + width), (int) (top + height + bottom));
            RectF r = new RectF(left, top, left + width, top + height + bottom);
            canvas.drawRoundRect(rect, view.dp2px(2), view.dp2px(2), mSelectorsStrokePaint);
            canvas.drawRoundRect(r, view.dp2px(2), view.dp2px(2), mSelectorBackgroundPaint);
            float y = top + ViewUtil.Dp2Px(mContext, 5) + textPadding + (textHeight - metrics.bottom - metrics.top) / 2;
            float right = left + width;

            for (int i = 0; i < selectorValues.size(); ++i) {
                canvas.drawText(selectorTitles.get(i), left + leftPadding, y, mSelectorTextPaint);
                exchangeGainOrAmplitudePaint(selectorTitles.get(i), selectorValues.get(i));
                canvas.drawText(selectorValues.get(i), right - leftPadding - mSelectorTextPaint.measureText(selectorValues.get(i)), y, mSelectorTextPaint);
                restoreGainOrAmplitudePaint();
                y += textHeight + textPadding;
            }
        }

    }

    //保存之前的画笔色彩，用于之后的颜色恢复
    private int mSelectorColor;

    public void exchangeGainOrAmplitudePaint(String filter, String value) {
        if (filter.equals(mContext.getResources().getString(R.string.txt_kline_gain))) {
            if (value.contains("-")) {
                mSelectorTextPaint.setColor(green);
            } else {
                mSelectorTextPaint.setColor(red);
            }
        } else {
            mSelectorColor = mSelectorTextPaint.getColor();
        }
    }

    public void restoreGainOrAmplitudePaint() {
        mSelectorTextPaint.setColor(mSelectorColor);
    }

    /**
     * 获取涨幅，振幅 （红涨绿跌）
     *
     * @return
     */
    public String calculateGainOrAmplitude(BaseKLineChartView view, int itemCount, int index) {
        String gain = null;
        String amplitude = null;
        if (itemCount == 1) {
            gain = "0.00%";
            amplitude = "0.00%";
        } else {
            if (index > 0 && index < itemCount) {
                ICandle lastPoint = (ICandle) view.getItem(index - 1);
                ICandle currPoint = (ICandle) view.getItem(index);
                if (lastPoint != null && currPoint != null) {
                    gain = String.valueOf(((currPoint.getClosePrice() - lastPoint.getClosePrice()) / lastPoint.getClosePrice()) * 100F);
                    amplitude = String.valueOf(((currPoint.getHighPrice() - currPoint.getLowPrice()) / lastPoint.getClosePrice()) * 100F);

                    gain = FormatUtil2.parseDoubleMax2(gain) + "%";
                    amplitude = FormatUtil2.parseDoubleMax2(amplitude) + "%";
                }
            } else {
                gain = "0.00%";
                amplitude = "0.00%";
            }
        }
        String value = gain + "," + amplitude;
        return value;
    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mCandleWidth = candleWidth;
    }


    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mCandleLineWidth = candleLineWidth;
        mCandleLinePaint.setStrokeWidth(mCandleLineWidth);
    }

    /**
     * 设置每个点的宽度
     */
    public void setPointWidth(float pointWidth) {
        mPointWidth = pointWidth;
    }

    /**
     * 设置蜡烛间隙
     */
    public void setCandleGapWidth(float width) {
        mCandleGapWidth = width;
    }


    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    /**
     * 设置ma30颜色
     *
     * @param color
     */
    public void setMa30Color(int color) {
        this.ma30Paint.setColor(color);
    }

    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mSelectorTextPaint.setColor(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mSelectorTextPaint.setTextSize(textSize);
        mSelectorTextPaint.setTypeface(Typeface.DEFAULT_BOLD);//加粗
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mSelectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorStrokeColor(int color) {
        mSelectorsStrokePaint.setColor(color);
        mSelectorsStrokePaint.setStrokeWidth(1);
        mSelectorsStrokePaint.setStyle(Paint.Style.STROKE);
    }


    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        ma30Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma5Paint.setStrokeWidth(width);
        mLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置曲线宽度
     */
    public void setTimeLineWidth(float width) {
        mTimeLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {

    }


    /**
     * 设置文字大小
     */
    public void setIndexTextSize(float textSize) {
        ma30Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma5Paint.setTextSize(textSize);
        //字体加粗
//        ma5Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        ma10Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        ma30Paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    /**
     * 蜡烛是否实心
     */
    public void setCandleSolid(boolean candleSolid) {
        mCandleSolid = candleSolid;
    }

    public void setLine(boolean line) {
        if (isLine != line) {
            isLine = line;
        }
    }

    public boolean isLine() {
        return isLine;
    }
}
