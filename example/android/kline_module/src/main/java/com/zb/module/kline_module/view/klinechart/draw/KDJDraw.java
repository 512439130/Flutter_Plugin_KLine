package com.zb.module.kline_module.view.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.BaseKLineChartView;
import com.zb.module.kline_module.view.klinechart.base.IChartDraw;
import com.zb.module.kline_module.view.klinechart.base.IValueFormatter;
import com.zb.module.kline_module.view.klinechart.entity.IKDJ;
import com.zb.module.kline_module.view.klinechart.formatter.BigValueFormatter;
import com.zb.module.kline_module.view.klinechart.utils.KlineDeviceUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe KDJ实现类
 * @update
 */
public class KDJDraw implements IChartDraw<IKDJ> {

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mDecimal = DEFAULT_DECIMAL;
    private Context mContext;

    public KDJDraw(BaseKLineChartView view, int decimal, Context context) {
        mDecimal = decimal;
        mContext = context;
        smoothPaint();
    }

    private void smoothPaint() {
        mKPaint.setStrokeJoin(Paint.Join.ROUND);
        mDPaint.setStrokeJoin(Paint.Join.ROUND);
        mJPaint.setStrokeJoin(Paint.Join.ROUND);

        mKPaint.setAntiAlias(true);//抗锯齿
        mDPaint.setAntiAlias(true);//抗锯齿
        mJPaint.setAntiAlias(true);//抗锯齿
        mKPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mDPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
        mJPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
    }

    @Override
    public void drawZoomTranslatedLine(@Nullable IKDJ lastPoint, @NonNull IKDJ curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

    }

    @Override
    public void drawNoZoomTranslatedLine(@Nullable IKDJ lastPoint, @NonNull IKDJ curPoint , @NonNull IKDJ nextPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (lastPoint.getK() != 0) {
            view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        }
        if (lastPoint.getD() != 0) {
            view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        }
        if (lastPoint.getJ() != 0) {
            view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
        }
    }


    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IKDJ point = (IKDJ) view.getItem(position);
        if (point != null) {
            if (point.getK() != 0) {
                Paint paint = view.getTextPaint();
//                paint.setTypeface(Typeface.DEFAULT_BOLD);
                String textStart = "KDJ(14,1,3) ";
                String textK = "K:" + view.formatPriceValue(point.getK());
                String textD = "D:" + view.formatPriceValue(point.getD());
                String textJ = "J:" + view.formatPriceValue(point.getJ());
                float START = x;
                float K = START + view.getTextPaint().measureText(textStart);
                float D = K + view.getTextPaint().measureText(textD);
                float J = D + view.getTextPaint().measureText(textJ);

                canvas.drawText(textStart, START + view.dp2px(4), y, paint);
                if (point.getK() != 0) {
                    canvas.drawText(textK, K + view.dp2px(10), y, mKPaint);
                }
                if (!view.isFull()) {
                    float densityWidth = KlineDeviceUtil.getScreenWidth( mContext);
                    float densityHeight = KlineDeviceUtil.getScreenHeight( mContext);
                    if (densityWidth <= 600 && densityHeight <= 1000) {  //480*800（旧款手机）
                        if (view.getPriceDecimal() > 6) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else if (densityWidth <= 800 && densityHeight <= 1400) {  //758*1280（旧款手机）
                        if (view.getPriceDecimal() > 6) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else if (densityWidth <= 1080 && densityHeight <= 2100) {  //标准高清（基础）（小米6）
                        if (view.getPriceDecimal() > 6) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else if (densityWidth <= 1080 && densityHeight <= 2400) {   //高清（高度高）（华为 P10）
                        if (view.getPriceDecimal() > 8) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else if (densityWidth <= 1200 && densityHeight <= 2200) {  //1200特殊
                        if (view.getPriceDecimal() > 8) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else if (densityWidth <= 1600 && densityHeight <= 3200) {   //2k（华为 例：mate 10）
                        if (view.getPriceDecimal() > 6) {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mDPaint);
                            }
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + (getFontHeight(paint) + view.dp2px(2)) * 2, mJPaint);
                            }
                        } else {
                            if (point.getD() != 0) {
                                canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                            }
                            if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                                }
                            } else {
                                if (point.getJ() != 0) {
                                    canvas.drawText(textJ, J + view.dp2px(32), y, mJPaint);
                                }
                            }
                        }
                    } else {
                        if (point.getD() != 0) {
                            canvas.drawText(textD, D + view.dp2px(20), y, mDPaint);
                        }
                        if (J > ((Double.parseDouble(String.valueOf(view.getMeasuredWidth())) / 5 * 3) - view.dp2px(10)) && calculateWidth(textJ) > view.dp2px(13)) {
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, K + view.dp2px(10), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                            }
                        } else {
                            if (point.getJ() != 0) {
                                canvas.drawText(textJ, J + view.dp2px(52), y, mJPaint);
                            }
                        }
                    }
                } else {
                    if (point.getJ() != 0) {
                        canvas.drawText(textJ, K + view.dp2px(36), y + getFontHeight(paint) + view.dp2px(2), mJPaint);
                    }
                    if (point.getJ() != 0) {
                        canvas.drawText(textJ, J + view.dp2px(52), y, mJPaint);
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
    public void drawSelectorBox(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {

    }

    @Override
    public float getMaxValue(IKDJ point) {
        if (point != null) {
            return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
        } else {
            return 0;
        }
    }

    @Override
    public float getMinValue(IKDJ point) {
        if (point != null) {
            return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
        } else {
            return 0;
        }

    }

    @Override
    public IValueFormatter getValueFormatter(Context context) {
        return new BigValueFormatter(context);
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
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
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);

        //字体加粗
//        mKPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mDPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mJPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }
}
