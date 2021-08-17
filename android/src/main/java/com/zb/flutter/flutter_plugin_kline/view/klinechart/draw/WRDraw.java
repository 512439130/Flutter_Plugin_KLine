package com.zb.flutter.flutter_plugin_kline.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IChartDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.IWR;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter.BigValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe WR实现类
 * @update
 */
public class WRDraw implements IChartDraw<IWR> {

    private Paint mRPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mDecimal = DEFAULT_DECIMAL;

    public WRDraw(BaseKLineChartView view, int decimal) {
        mDecimal = decimal;
        smoothPaint();
    }
    private void smoothPaint() {
        mRPaint.setStrokeJoin(Paint.Join.ROUND);
        mRPaint.setAntiAlias(true);//抗锯齿
        //线条结束处绘制一个半圆
        mRPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    public void drawZoomTranslatedLine(@Nullable IWR lastPoint, @NonNull IWR curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IWR lastPoint, @NonNull IWR curPoint,@NonNull IWR nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (lastPoint.getR() != -10) {
            view.drawChildLine(canvas, mRPaint, lastX, lastPoint.getR(), curX, curPoint.getR());
        }
    }


    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
//        if (view.isLongPress()) {
            IWR point = (IWR) view.getItem(position);
            if (point != null) {
                if (point.getR() != -10) {
                    String text = "WR(14): ";
                    Paint paint = view.getTextPaint();
//                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                    canvas.drawText(text, x+ view.dp2px(4), y, paint);
                    x += view.getTextPaint().measureText(text);
                    text = view.formatPriceValue(point.getR());
                    canvas.drawText(text, x+ view.dp2px(4), y, mRPaint);
                }
            }

//        }
    }

    @Override
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {

    }

    @Override
    public float getMaxValue(IWR point) {
        if(point != null){
            return point.getR();
        }else{
            return 0;
        }

    }

    @Override
    public float getMinValue(IWR point) {
        if(point != null){
            return point.getR();
        }else{
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }

    /**
     * 设置%R颜色
     */
    public void setRColor(int color) {
        mRPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mRPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {

    }

    /**
     * 设置index字体大小
     */
    public void setIndexTextSize(float textSize) {
        mRPaint.setTextSize(textSize);
        //字体加粗
//        mRPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
