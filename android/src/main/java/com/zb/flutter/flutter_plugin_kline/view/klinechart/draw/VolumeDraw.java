package com.zb.flutter.flutter_plugin_kline.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IChartDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.IVolume;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter.BigValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe MainDraw当前子视图
 * @update
 */
public class VolumeDraw implements IChartDraw<IVolume> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mVolumeWidth;

    private Context mContext;
    private int mDecimal = DEFAULT_DECIMAL;

    private float mPointWidth = 0;
    private float mCandleGapWidth = 0;
    private BaseKLineChartView mView;

    public VolumeDraw(BaseKLineChartView view, int decimal) {
        Context context = view.getContext();
        mDecimal = decimal;
        mContext = context;
        this.mView = view;
        smoothPaint();
    }
    public BaseKLineChartView getView() {
        return mView;
    }

    private void smoothPaint() {
        mRedPaint.setStrokeJoin(Paint.Join.ROUND);
        mGreenPaint.setStrokeJoin(Paint.Join.ROUND);
        ma5Paint.setStrokeJoin(Paint.Join.ROUND);
        ma10Paint.setStrokeJoin(Paint.Join.ROUND);

        ma5Paint.setAntiAlias(true);//抗锯齿
        ma5Paint.setAntiAlias(true);//抗锯齿

        ma5Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        ma5Paint.setStrokeCap(Paint.Cap.ROUND); //线条结束处绘制一个半圆
    }

    public void setRed(int red) {
        mRedPaint.setColor(red);
    }

    public void setGreen(int green) {
        mGreenPaint.setColor(green);
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable IVolume lastPoint, @NonNull IVolume curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IVolume lastPoint, @NonNull IVolume curPoint  , @NonNull IVolume nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        drawVolume(canvas, curPoint, lastPoint, curX, view, position);
        if (lastPoint.getMA5Volume() != 0f) {
            view.drawVolLine(canvas, ma5Paint, lastX, lastPoint.getMA5Volume(), curX, curPoint.getMA5Volume());
        }
        if (lastPoint.getMA10Volume() != 0f) {
            view.drawVolLine(canvas, ma10Paint, lastX, lastPoint.getMA10Volume(), curX, curPoint.getMA10Volume());
        }
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
     * 绘制成交量
     *
     * @param canvas
     * @param curPoint
     * @param lastPoint
     * @param curX
     * @param view
     * @param position
     */
    private void drawVolume(Canvas canvas, IVolume curPoint, IVolume lastPoint, float curX, BaseKLineChartView view, int position) {
        float r = (mPointWidth* view.getScaleX() - (2 * (mCandleGapWidth))) / 2;

        float top = (float) view.getVolY(curPoint.getVol());
        int bottom = view.getVolRect().bottom;

        if(bottom - top < 2f){  //防止成交量过少，成交量无画线
            top = top - 2f;
        }

        if (curPoint.getClosePrice() >= curPoint.getOpenPrice()) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
//        if (view.isLongPress()) {
            IVolume point = (IVolume) view.getItem(position);
            if (point != null) {
                String textVol = "VOL:" + view.formatAmountValue(Float.parseFloat(Double.toString(point.getVol())));
                String textMA5 = "MA5:" + view.formatAmountValue(point.getMA5Volume());
                String textMA10 = "MA10:" + view.formatAmountValue(point.getMA10Volume());
                float volX = x;
                float MA5X = volX +  view.getTextPaint().measureText(textVol);
                float MA10X = MA5X +  view.getTextPaint().measureText(textMA5);

                Paint paint = view.getTextPaint();
//                paint.setTypeface(Typeface.DEFAULT_BOLD);
                canvas.drawText(textVol, volX+ view.dp2px(4), y, paint);
                canvas.drawText(textMA5, MA5X+ view.dp2px(18), y, ma5Paint);

                if(!view.isFull()){
                    if(MA10X> ((Double.parseDouble(String.valueOf(view.getMeasuredWidth()))/2) - view.dp2px(10)) && calculateWidth(textMA10)>view.dp2px(14)){
                        canvas.drawText(textMA10, volX+ view.dp2px(4), y+getFontHeight(ma5Paint)+view.dp2px(2), ma10Paint);
                    }else{
                        canvas.drawText(textMA10, MA10X+ view.dp2px(32), y, ma10Paint);
                    }
                }else {
                    canvas.drawText(textMA10, MA10X+ view.dp2px(32), y, ma10Paint);
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
    public float getMaxValue(IVolume point) {
        if(point != null){
            return (float) Math.max(point.getVol(), Math.max(point.getMA5Volume(), point.getMA10Volume()));
        }else{
            return 0;
        }
    }

    @Override
    public float getMinValue(IVolume point) {
        if(point != null){
            return (float) Math.min(point.getVol(), Math.min(point.getMA5Volume(), point.getMA10Volume()));
        }else{
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }


    /**
     * 设置Volume线的宽度
     *
     * @param width
     */
    public void setVolumeWidth(float width) {
        mVolumeWidth = width;
    }

    /**
     * 设置 MA5 线的颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置 MA10 线的颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
//字体加粗
//        ma5Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        ma10Paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    /**
     * 设置index字体大小
     */
    public void setIndexTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
//字体加粗
//        ma5Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        ma10Paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

}
