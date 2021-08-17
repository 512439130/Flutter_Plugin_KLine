package com.zb.module.kline_module.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;
import com.zb.module.kline_module.view.klinechart.base.IChartDraw;
import com.zb.module.kline_module.view.klinechart.base.IValueFormatter;
import com.zb.module.kline_module.view.klinechart.entity.IMACD;
import com.zb.module.kline_module.view.klinechart.formatter.BigValueFormatter;
import com.zb.module.kline_module.view.klinechart.utils.KlineDeviceUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe macd实现类
 * @update
 */
public class MACDDraw implements IChartDraw<IMACD> {

    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mPointWidth = 0;
    private float mCandleGapWidth = 0;
    private float mCandleLineWidth = 0;

    private boolean candleHollowSwitch = false;  //空心开关

    private Context mContext;
    /**
     * macd 中柱子的宽度
     */
    private float mMACDWidth = 0;

    private int mDecimal = DEFAULT_DECIMAL;
    private BaseKLineChartView mView;

    public MACDDraw(BaseKLineChartView view, int decimal, Context context) {
        this.mView = view;
        mDecimal = decimal;
        mContext = context;
        smoothPaint();
    }

    public BaseKLineChartView getView() {
        return mView;
    }

    private void smoothPaint() {
        mDIFPaint.setStrokeJoin(Paint.Join.ROUND);
        mDEAPaint.setStrokeJoin(Paint.Join.ROUND);
        mDIFPaint.setAntiAlias(true);//抗锯齿
        mDEAPaint.setAntiAlias(true);//抗锯齿
        mDIFPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mDEAPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
    }

    public void setRed(int red) {
        mRedPaint.setColor(red);
        mRedPaint.setStrokeWidth(mView.dp2px(0.25f));
        mRedPaint.setAntiAlias(true);//抗锯齿
    }

    public void setGreen(int green) {
        mGreenPaint.setColor(green);
        mGreenPaint.setStrokeWidth(mView.dp2px(0.25f));
        mGreenPaint.setAntiAlias(true);//抗锯齿
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable IMACD lastPoint, @NonNull IMACD curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IMACD lastPoint, @NonNull IMACD curPoint, @Nullable IMACD nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (lastPoint != null && nextPoint != null) {
            drawMACD(canvas, view, curX, curPoint.getMacd(), nextPoint.getMacd());
            view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
            view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
        }
    }


    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
//        if (view.isLongPress()) {
        IMACD point = (IMACD) view.getItem(position);
        if (point != null) {

            Paint paint = view.getTextPaint();
//            paint.setTypeface(Typeface.DEFAULT_BOLD);
            String textStart = "MACD(12,26,9) ";
            String textMACD = "MACD:" + view.formatPriceValue(point.getMacd());
            String textDIF = "DIF:" + view.formatPriceValue(point.getDif());
            String textDEA = "DEA:" + view.formatPriceValue(point.getDea());
            float START = x;
            float MACD = START + view.getTextPaint().measureText(textStart);
            float DIFX = MACD + view.getTextPaint().measureText(textMACD);
            float DEAX = DIFX + view.getTextPaint().measureText(textDIF);

            canvas.drawText(textStart, START + view.dp2px(4), y, paint);
            if (point.getMacd() != 0) {
                canvas.drawText(textMACD, MACD + view.dp2px(10), y, mMACDPaint);
            }

            if (!view.isFull()) {
                float densityWidth = KlineDeviceUtil.getScreenWidth(mContext);
                float densityHeight = KlineDeviceUtil.getScreenHeight(mContext);
                if (densityWidth <= 600 && densityHeight <= 1000) {  //480*800（旧款手机）
                    if (view.getPriceDecimal() > 6) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else if (densityWidth <= 800 && densityHeight <= 1400) {  //758*1280（旧款手机）
                    if (view.getPriceDecimal() > 6) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else if (densityWidth <= 1080 && densityHeight <= 2100) {  //标准高清（基础）（小米6）
                    if (view.getPriceDecimal() > 6) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else if (densityWidth <= 1080 && densityHeight <= 2400) {   //高清（高度高）（华为 P10）
                    if (view.getPriceDecimal() > 8) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else if (densityWidth <= 1200 && densityHeight <= 2200) {  //1200特殊
                    if (view.getPriceDecimal() > 8) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else if (densityWidth <= 1600 && densityHeight <= 3200) {   //2k（华为 例：mate 10）
                    if (view.getPriceDecimal() > 6) {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDIFPaint);
                        }
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + (getFontHeight(mDEAPaint) + view.dp2px(2)) * 2, mDEAPaint);
                        }
                    } else {
                        if (point.getDif() != 0) {
                            canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                        }
                        if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(13)) {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                            }
                        } else {
                            if (point.getDea() != 0) {
                                canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                            }
                        }
                    }
                } else {
                    if (point.getDif() != 0) {
                        canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                    }
                    if (DEAX > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textDEA) > view.dp2px(14)) {
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, MACD + view.dp2px(10), y + getFontHeight(mDEAPaint) + view.dp2px(2), mDEAPaint);
                        }
                    } else {
                        if (point.getDea() != 0) {
                            canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                        }
                    }
                }
            } else {
                if (point.getDif() != 0) {
                    canvas.drawText(textDIF, DIFX + view.dp2px(20), y, mDIFPaint);
                }
                if (point.getDea() != 0) {
                    canvas.drawText(textDEA, DEAX + view.dp2px(32), y, mDEAPaint);
                }
            }

        }

//        }

    }

    @Override
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {

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
    public float getMaxValue(IMACD point) {
        if (point != null) {
            return Math.max(point.getMacd(), Math.max(point.getDea(), point.getDif()));
        } else {
            return 0;
        }
    }

    @Override
    public float getMinValue(IMACD point) {
        if (point != null) {
            return Math.min(point.getMacd(), Math.min(point.getDea(), point.getDif()));
        } else {
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }

    /**
     * 画MACD
     *
     * @param canvas
     * @param x
     * @param currentMACD
     */
    private void drawMACD(Canvas canvas, BaseKLineChartView view, float x, float currentMACD, float nextMACD) {
        float r = (mPointWidth * view.getScaleX() - (2 * (mCandleGapWidth))) / 2;
        //如果当前macd<下一个点macd，则为空心，否则为实心
        float zeroy = (float) view.getChildY(0);
        float maxHeight = (view.getMeasuredHeight() - view.mVolTextPadding - view.mChildTextPadding - view.mChildBottomPadding);
        if(zeroy > maxHeight){  //柱子超出边界情况
            zeroy = maxHeight;
        }
        float mCurrentMACDy = (float) view.getChildY(currentMACD);
        float mNextMACDy = (float) view.getChildY(nextMACD);

        if(isCandleHollowSwitch()){
            if (mCurrentMACDy > mNextMACDy) { //空心
                mRedPaint.setStyle(Paint.Style.STROKE);
                mGreenPaint.setStyle(Paint.Style.STROKE);
            } else { //实心
                mRedPaint.setStyle(Paint.Style.FILL);
                mGreenPaint.setStyle(Paint.Style.FILL);
            }
        }else{
            mRedPaint.setStyle(Paint.Style.FILL);
            mGreenPaint.setStyle(Paint.Style.FILL);
        }
        if (currentMACD > 0) {
            if (currentMACD < 1f) {
                mCurrentMACDy = mCurrentMACDy - 1f;
            }
            canvas.drawRect(x - r, mCurrentMACDy, x + r, zeroy, mRedPaint);
        } else {
            if (currentMACD > -1f) {
                mCurrentMACDy = mCurrentMACDy + 1f;
            }
            canvas.drawRect(x - r, zeroy, x + r, mCurrentMACDy, mGreenPaint);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.mDEAPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.mMACDPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDWidth = MACDWidth;
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
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
        //字体加粗
//        mDEAPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mDIFPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mMACDPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }


    /**
     * 设置index字体大小
     */
    public void setIndexTextSize(float textSize) {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
        //字体加粗
//        mDEAPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mDIFPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mMACDPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public boolean isCandleHollowSwitch() {
        return candleHollowSwitch;
    }

    public void setCandleHollowSwitch(boolean candleHollowSwitch) {
        this.candleHollowSwitch = candleHollowSwitch;
    }
}
