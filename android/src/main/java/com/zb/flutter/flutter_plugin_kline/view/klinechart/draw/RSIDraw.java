package com.zb.flutter.flutter_plugin_kline.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IChartDraw;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.IRSI;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter.BigValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe RSI实现类
 * @update
 */
public class RSIDraw implements IChartDraw<IRSI> {

    private Paint mRSI1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mDecimal = DEFAULT_DECIMAL;

    public RSIDraw(BaseKLineChartView view, int decimal) {
        mDecimal = decimal;
        smoothPaint();
    }
    private void smoothPaint() {
        mRSI1Paint.setStrokeJoin(Paint.Join.ROUND);
        mRSI2Paint.setStrokeJoin(Paint.Join.ROUND);
        mRSI3Paint.setStrokeJoin(Paint.Join.ROUND);

        mRSI1Paint.setAntiAlias(true);//抗锯齿
        mRSI2Paint.setAntiAlias(true);//抗锯齿
        mRSI3Paint.setAntiAlias(true);//抗锯齿
        mRSI1Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mRSI2Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mRSI3Paint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable IRSI lastPoint, @NonNull IRSI curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IRSI lastPoint, @NonNull IRSI curPoint , @NonNull IRSI nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (lastPoint.getRsi() != 0) {
            view.drawChildLine(canvas, mRSI1Paint, lastX, lastPoint.getRsi(), curX, curPoint.getRsi());
        }
    }


    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
//        if (view.isLongPress()) {
            IRSI point = (IRSI) view.getItem(position);
            if (point != null) {
                if (point.getRsi() != 0) {
                    String text = "RSI(14) ";
                    Paint paint = view.getTextPaint();
//                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                    canvas.drawText(text, x+ view.dp2px(4), y, paint);
                    x += view.getTextPaint().measureText(text);
                    text = view.formatPriceValue(point.getRsi());
                    canvas.drawText(text, x+ view.dp2px(20), y, mRSI1Paint);
                }
            }

//        }

    }

    @Override
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {

    }

    @Override
    public float getMaxValue(IRSI point) {
        if(point != null){
            return point.getRsi();
        }else{
            return 0;
        }
    }

    @Override
    public float getMinValue(IRSI point) {
        if(point != null){
            return point.getRsi();
        }else{
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }

    public void setRSI1Color(int color) {
        mRSI1Paint.setColor(color);
    }

    public void setRSI2Color(int color) {
        mRSI2Paint.setColor(color);
    }

    public void setRSI3Color(int color) {
        mRSI3Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mRSI1Paint.setStrokeWidth(width);
        mRSI2Paint.setStrokeWidth(width);
        mRSI3Paint.setStrokeWidth(width);
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
        mRSI1Paint.setTextSize(textSize);
        mRSI2Paint.setTextSize(textSize);
        mRSI3Paint.setTextSize(textSize);

        //字体加粗
//        mRSI1Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        mRSI2Paint.setTypeface(Typeface.DEFAULT_BOLD);
//        mRSI3Paint.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
