package com.zb.flutter.flutter_plugin_kline.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IChartDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.IBOLL;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter.BigValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe BOLL实现类
 * @update
 */
public class BOLLDraw implements IChartDraw<IBOLL> {

    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mDecimal = DEFAULT_DECIMAL;

    public BOLLDraw(BaseKLineChartView view, int decimal) {
        mDecimal = decimal;
        smoothPaint();
    }

    private void smoothPaint() {
        mUpPaint.setStrokeJoin(Paint.Join.ROUND);
        mMbPaint.setStrokeJoin(Paint.Join.ROUND);
        mDnPaint.setStrokeJoin(Paint.Join.ROUND);

        mUpPaint.setAntiAlias(true);//抗锯齿
        mMbPaint.setAntiAlias(true);//抗锯齿
        mDnPaint.setAntiAlias(true);//抗锯齿
        mUpPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mMbPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mDnPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable IBOLL lastPoint, @NonNull IBOLL curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IBOLL lastPoint, @NonNull IBOLL curPoint , @NonNull IBOLL nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        view.drawChildLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
        view.drawChildLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
        view.drawChildLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
    }


    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
//        if (view.isLongPress()) {
            String text = "";
            IBOLL point = (IBOLL) view.getItem(position);
            if (point != null) {
                text = "UP:" + view.formatPriceValue(point.getUp());
                canvas.drawText(text, x + view.dp2px(4), y, mUpPaint);
                x += mUpPaint.measureText(text);
                text = "MB:" + view.formatPriceValue(point.getMb());
                canvas.drawText(text, x + view.dp2px(20), y, mMbPaint);
                x += mMbPaint.measureText(text);
                text = "DN:" + view.formatPriceValue(point.getDn());
                canvas.drawText(text, x + view.dp2px(36), y, mDnPaint);
            }

//        }
    }

    @Override
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {

    }

    @Override
    public float getMaxValue(IBOLL point) {
        if(point != null){
            if (Float.isNaN(point.getUp())) {
                return point.getMb();
            }
            return point.getUp();
        }else{
            return 0;
        }
    }

    @Override
    public float getMinValue(IBOLL point) {
        if(point != null){
            if (Float.isNaN(point.getDn())) {
                return point.getMb();
            }
            return point.getDn();
        }else{
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }


    /**
     * 设置up颜色
     */
    public void setUpColor(int color) {
        mUpPaint.setColor(color);
    }

    /**
     * 设置mb颜色
     * @param color
     */
    public void setMbColor(int color) {
        mMbPaint.setColor(color);
    }

    /**
     * 设置dn颜色
     */
    public void setDnColor(int color) {
        mDnPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width)
    {
        mUpPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize)
    {

    }

    /**
     * 设置index字体大小
     */
    public void setIndexTextSize(float textSize) {
        mUpPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
        mDnPaint.setTextSize(textSize);
        //字体加粗
//        mUpPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mMbPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mDnPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
