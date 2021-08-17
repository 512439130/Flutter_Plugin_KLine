package com.zb.flutter.flutter_plugin_kline.view.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zb.module.kline_module.R;

import java.util.List;
import java.util.Objects;

/**
 * @author SoMustYY
 * @create 2021-04-01 14:20
 * @organize 卓世达科
 * @describe 展开小K线BaseView
 * @update
 */
public class ItemGridChartKView extends View {
    private static final String TAG = "ItemGridChartKView";

    // ////////////默认值////////////////
    /**
     * 字体颜色
     **/
    public int textColor = 0x78797A;
    /**
     * 边框颜色
     **/
    public int XYLineColor = 0x78797A;
    /**
     * 维度画线颜色
     **/
    public int xLineColor = 0x78797A;
    /**
     * 经度画线颜色
     **/
    public int yLineColor = 0x78797A;
    /**
     * 经度画线颜色
     **/
    public int topBottomLineColor = 0x78797A;


    /**
     * 默认字体大小
     **/
    public float DEFAULT_AXIS_TITLE_SIZE = 8;  //统一字体大小
    /**
     * 线效果
     */
    private PathEffect mDashEffect;

    /**
     * 横轴X刻度
     */
    private List<String> axisXTitles;
    /**
     * 纵轴Y刻度
     */
    private List<String> axisYTitles;
    //Y轴在图表内部
    protected boolean isInnerYAxis = false;

    /**
     * 默认Y轴刻度显示长度
     */
    private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 12;

    /**
     * 默认经线数
     */
    public int DEFAULT_LOGITUDE_NUM = 6;
    /**
     * 默认维线数
     */
    public int DEFAULT_LATITUDE_NUM = 3;
    /**
     * 上表的上间隙
     */
    public float UPER_CHART_MARGIN_TOP;
    /**
     * 上表的下间隙
     */
    public float UPER_CHART_MARGIN_BOTTOM;

    /**
     * 图表跟右边距离  x轴突出的位置
     */
    public float DEFAULT_AXIS_MARGIN_RIGHT_X = DEFAULT_AXIS_TITLE_SIZE;
    /**
     * 图表跟左边距离  x轴突出的位置
     */
    public float DEFAULT_AXIS_MARGIN_LEFT_X = DEFAULT_AXIS_TITLE_SIZE;
    /**
     * 图表跟右边距离
     */
    public float DEFAULT_AXIS_MARGIN_RIGHT = DEFAULT_AXIS_TITLE_SIZE * 3;
    /**
     * 图表跟左边距离
     */
    public float DEFAULT_AXIS_MARGIN_LEFT = 1;

    /**
     * 默认显示的Candle数(最大默认蜡烛数量)
     */
    public final static int DEFAULT_CANDLE_NUM = 76;

    // /////////////属性////////////////
    /**
     * 背景色
     */
    private int mBackGround;

    /**
     * 上表高度
     */
    public float mUperChartHeight;

    public void setLatLine(int num) {
        DEFAULT_LATITUDE_NUM = num;
    }

    public void setLogLine(int num) {
        DEFAULT_LOGITUDE_NUM = num;
    }

    //经线间隔度
    private float longitudeSpacing;
    //维线间隔度
    private float latitudeSpacing;
    private Context mContext;

    private boolean YisLeft = false;

    public void setYIsLeft(boolean isleft) {
        this.YisLeft = isleft;
    }

    private boolean LongitudeIsShow = true;
    private boolean LatitudeIsShow = true;

    private int dataSize;  //数据量

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dateSize) {
        this.dataSize = dateSize;
    }

    /**
     * 是否显示具体个数时间标识
     */
    private boolean isVisibleNumberTime = false;

    public boolean isVisibleNumberTime() {
        return isVisibleNumberTime;
    }

    public void setVisibleNumberTime(boolean visibleNumberTime) {
        isVisibleNumberTime = visibleNumberTime;
    }


    /**
     * 默认虚线效果
     */
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
            new float[]{15, 15, 15, 15}, 15);
    private boolean showYText = true;

    public void setShowYText(boolean show) {
        this.showYText = show;
    }

    private boolean showXText = true;

    public void setShowXText(boolean show) {
        this.showXText = show;
    }

    public void setLongitudeIsShow(boolean show) {
        this.LongitudeIsShow = show;
    }

    public void setLatitudeIsShow(boolean show) {
        this.LatitudeIsShow = show;
    }

    private boolean isLessData = false; //是否数据量不足

    public boolean isLessData() {
        return isLessData;
    }

    public void setLessData(boolean lessData) {
        isLessData = lessData;
    }

    public ItemGridChartKView(Context context) {
        super(context);
        init(context);
    }

    public ItemGridChartKView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ItemGridChartKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initColor(context);
        initSize();
    }


    private void initColor(Context context) {
        mBackGround = context.getResources().getColor(R.color.custom_item_kline_bg_light);
        textColor = context.getResources().getColor(R.color.zb_color_999999);
        xLineColor = context.getResources().getColor(R.color.custom_attr_input_box_50_bg_color_light);
        yLineColor = context.getResources().getColor(R.color.custom_attr_input_box_50_bg_color_light);
        topBottomLineColor = mContext.getResources().getColor(R.color.custom_attr_input_box_50_bg_color_light);
    }


    public void initSize() {
        DEFAULT_AXIS_TITLE_SIZE = sp2px(mContext, DEFAULT_AXIS_TITLE_SIZE);
        DEFAULT_AXIS_MARGIN_RIGHT = sp2px(mContext, 40);
        DEFAULT_AXIS_MARGIN_LEFT = sp2px(mContext, 0.35f);
        UPER_CHART_MARGIN_TOP = 4 * DEFAULT_AXIS_TITLE_SIZE;
        UPER_CHART_MARGIN_BOTTOM = DEFAULT_AXIS_TITLE_SIZE * 6;
        mDashEffect = DEFAULT_DASH_EFFECT;
    }

    public void setChartBottom(float size) {
        this.UPER_CHART_MARGIN_BOTTOM = size;
    }

    public void setChartTop(float size) {
        this.UPER_CHART_MARGIN_TOP = size;
    }


    /**
     * 重新绘制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    public boolean haveBorder = false;

    public void setHaveBorder(boolean border) {
        this.haveBorder = border;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(mBackGround);

        int viewHeight = getHeight();
        int viewWidth = getWidth();
        UPER_CHART_MARGIN_TOP = 2 * DEFAULT_AXIS_TITLE_SIZE;
        UPER_CHART_MARGIN_BOTTOM = 4 * DEFAULT_AXIS_TITLE_SIZE;
        mUperChartHeight = viewHeight - UPER_CHART_MARGIN_TOP - UPER_CHART_MARGIN_BOTTOM;
        //经度
        longitudeSpacing = (viewWidth - DEFAULT_AXIS_MARGIN_RIGHT - 2) / DEFAULT_LOGITUDE_NUM;
        //维度
        latitudeSpacing = (viewHeight - UPER_CHART_MARGIN_TOP - UPER_CHART_MARGIN_BOTTOM) / DEFAULT_LATITUDE_NUM;
        DEFAULT_AXIS_MARGIN_RIGHT_X = longitudeSpacing / 4;
        DEFAULT_AXIS_MARGIN_LEFT_X = longitudeSpacing * 3 / 4;

        // 绘制纬线（Y轴）
        drawLatitudes(canvas, viewWidth, latitudeSpacing);
        // 绘制经线（X轴）
        drawLongitudes(canvas, viewHeight);
    }


    /**
     * 绘制纬线 (Y轴)
     *
     * @param canvas
     * @param viewWidth
     */
    private void drawLatitudes(Canvas canvas, int viewWidth, float latitudeSpacing) {
        if (axisYTitles == null || axisYTitles.size() == 0)
            return;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(xLineColor);
        paint.setStrokeWidth(0.8f);
        paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        paint.setStyle(Paint.Style.STROKE);
        Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintzt.setColor(textColor);
        paintzt.setStrokeWidth(1);
        paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        for (int i = 0; i <= DEFAULT_LATITUDE_NUM && i < axisYTitles.size(); i++) {
            if (LatitudeIsShow) {
                canvas.drawLine(0, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i), viewWidth - DEFAULT_AXIS_MARGIN_RIGHT * 1.1f, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i), paint);
            }
            if (axisYTitles != null && showYText) {
                if (YisLeft) {
                    // 绘制Y刻度（左侧）
                    if (axisYTitles.size() - i - 1 != 0)
                        canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), sp2px(mContext, 5), UPER_CHART_MARGIN_TOP + latitudeSpacing * (i) + DEFAULT_AXIS_TITLE_SIZE / 3f, paintzt);
                } else {
                    // 绘制Y刻度（右侧）
                    if (axisYTitles.size() - i - 1 != 0) {
                        float w = paint.measureText(axisYTitles.get(axisYTitles.size() - i - 1));

                        if (w > DEFAULT_AXIS_MARGIN_RIGHT)//Y刻度超出右边的边距
                        {
                            canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), viewWidth - w, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i) - DEFAULT_AXIS_TITLE_SIZE / 2f, paintzt);
                        } else {
                            canvas.drawText(axisYTitles.get(axisYTitles.size() - i - 1), viewWidth - DEFAULT_AXIS_MARGIN_RIGHT, UPER_CHART_MARGIN_TOP + latitudeSpacing * (i) + DEFAULT_AXIS_TITLE_SIZE / 3f, paintzt);
                        }
                    }
                }
            }
        }
        //顶部和底部边界线
        paint.setColor(topBottomLineColor);
        canvas.drawLine(0, 2, viewWidth, 2, paint);
        canvas.drawLine(0, getHeight() - UPER_CHART_MARGIN_BOTTOM / 2, viewWidth, getHeight() - UPER_CHART_MARGIN_BOTTOM / 2, paint);
    }

    /**
     * 绘制经线（X轴）
     *
     * @param canvas
     */
    private void drawLongitudes(Canvas canvas, int viewHeight) {
        if (isLessData()) {
            if (axisXTitles == null || axisXTitles.size() == 0) return;
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(textColor);
            paintText.setStrokeWidth(1);
            paintText.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            float textY = getHeight() - 2;
            float textWidth; //时间文字的宽度

            if (axisXTitles != null && axisXTitles.size() > 0) {
                textWidth = calculateMaxMin(axisXTitles.get(0), paintText).width();
            } else {
                textWidth = 0;
            }
            //计算每个蜡烛所需宽度
            float mCandleWidth = (float) ((getWidth() - DEFAULT_AXIS_MARGIN_RIGHT - 2) / 10.0 * 10.0 / DEFAULT_CANDLE_NUM);
            //计算所有文字所需宽度
            float mAllTextWidth = textWidth * 2;
            //计算所在所需宽度
            float mAllCandleWidth = mCandleWidth * axisXTitles.size();
            //初始时间位置
            float textFirstX = DEFAULT_AXIS_MARGIN_LEFT;
            //最后时间位置
            float textEndX = textFirstX + (mCandleWidth * axisXTitles.size() - (textWidth/2));

            if (axisXTitles.size() > 0) {
                //start
                canvas.drawText(axisXTitles.get(axisXTitles.size() - 1), textFirstX, textY, paintText);  //默认数据不足一屏幕时，只绘制1个时间
                //end
                if(mAllTextWidth <= mAllCandleWidth){
                    canvas.drawText(axisXTitles.get(0), textEndX, textY, paintText);
                }
            }
        } else {
            if (axisXTitles == null || axisXTitles.size() == 0)
                return;

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(0.8f);
            paint.setColor(yLineColor);
            paint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            // paint.setPathEffect(mDashEffect);
            paint.setStyle(Paint.Style.STROKE);


            int mShowDataNum = getDataSize();
            int longitudeNum = DEFAULT_LOGITUDE_NUM;
            int step = (int) Math.floor(mShowDataNum / longitudeNum);
            //上新币，数据不足显示量时，默认step = 1
            if (step == 0) step = 1;

            float width; //K线绘制区域宽度
            float textWidth; //时间文字的宽度
            if (axisXTitles != null && axisXTitles.size() > 0) {
                width = getWidth() - DEFAULT_AXIS_MARGIN_RIGHT - 2;
                textWidth = calculateMaxMin(axisXTitles.get(0),paint).width();
            } else {
                width = 0;
                textWidth = 0;
            }
//            //计算每个蜡烛所需宽度
            float mCandleWidth = (float) ((getWidth() - DEFAULT_AXIS_MARGIN_RIGHT - 2) / 10.0 * 10.0 / DEFAULT_CANDLE_NUM);
            //计算所有文字所需宽度
            float mAllTextWidth = textWidth * 2;
            //计算所在所需宽度
            float mAllCandleWidth = mCandleWidth * axisXTitles.size();

            for (int i = 0; i < Objects.requireNonNull(axisXTitles).size(); i++) {
                float y = viewHeight - DEFAULT_AXIS_TITLE_SIZE * 0.5f;
                if (LongitudeIsShow) {
                    if (isVisibleNumberTime()) {
                        //数据不全不画线
//                        //初始时间位置
//                        float firstLineX = DEFAULT_AXIS_MARGIN_LEFT + (mCandleWidth / 2);
//                        //最后时间位置
//                        float endLineX = firstLineX + (mCandleWidth * axisXTitles.size() - (mCandleWidth / 2));
//                        // 绘制刻度
//                        if (axisXTitles.size() > 0) {
//                            //start
//                            canvas.drawLine(firstLineX, 0, firstLineX, viewHeight - UPER_CHART_MARGIN_BOTTOM / 2, paint);
//                            //end
//                            if(mAllTextWidth <= mAllCandleWidth){
//                                canvas.drawLine(endLineX, 0, endLineX, viewHeight - UPER_CHART_MARGIN_BOTTOM / 2, paint);
//                            }
//                        }
                    } else {
                        float lineX = width - ((mCandleWidth * (i)) * step) - (mCandleWidth / 2);
                        canvas.drawLine(lineX, 0, lineX, viewHeight - UPER_CHART_MARGIN_BOTTOM / 2, paint);
                    }

                } else {
                    y = y - DEFAULT_AXIS_TITLE_SIZE;
                }
                Paint paintzt = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintzt.setColor(textColor);
                paintzt.setStrokeWidth(1);
                paintzt.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
                if (axisXTitles != null && showXText) {
                    if (isVisibleNumberTime()) {
                        //初始时间位置
                        float firstTextX = DEFAULT_AXIS_MARGIN_LEFT;
                        //最后时间位置
                        float endTextX = firstTextX + (mCandleWidth * axisXTitles.size() - (textWidth / 2));
                        // 绘制刻度
                        if (axisXTitles.size() > 0) {
                            //start
                            canvas.drawText(axisXTitles.get(axisXTitles.size() - 1), firstTextX, y, paintzt);  //默认数据不足一屏幕时，只绘制1个时间
                            //end
                            if(mAllTextWidth <= mAllCandleWidth){
                                canvas.drawText(axisXTitles.get(0), endTextX, y, paintzt);
                            }
                        }
                    } else {
                        float textX = width - ((mCandleWidth * (i)) * step) - (mCandleWidth / 2) - (textWidth / 2);
                        // 绘制刻度
                        canvas.drawText(axisXTitles.get(i), textX, y, paintzt);
                    }
                }
            }
        }
    }

    public float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    //获取字体大小
    public float adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        float rate = ((float) screenWidth / 320);
        return rate; //字体太小也不好看的
    }

    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getxLineColor() {
        return xLineColor;
    }

    public void setxLineColor(int xLineColor) {
        this.xLineColor = xLineColor;
    }

    public int getyLineColor() {
        return yLineColor;
    }

    public void setyLineColor(int yLineColor) {
        this.yLineColor = yLineColor;
    }

    public int getTopBottomLineColor() {
        return topBottomLineColor;
    }

    public void setTopBottomLineColor(int topBottomLineColor) {
        this.topBottomLineColor = topBottomLineColor;
    }

    public float getUperChartHeight() {
        return mUperChartHeight;
    }

    public void setUperChartHeight(float UperChartHeight) {
        this.mUperChartHeight = UperChartHeight;
    }

    public int getAxisYMaxTitleLength() {
        return DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;
    }

    public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
        this.DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = axisYMaxTitleLength;
    }

    public float getLongitudeSpacing() {
        return longitudeSpacing;
    }

    public void setLongitudeSpacing(float longitudeSpacing) {
        this.longitudeSpacing = longitudeSpacing;
    }

    public float getLatitudeSpacing() {
        return latitudeSpacing;
    }

    public void setLatitudeSpacing(float latitudeSpacing) {
        this.latitudeSpacing = latitudeSpacing;
    }

    public List<String> getAxisXTitles() {
        return axisXTitles;
    }

    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }

    public List<String> getAxisYTitles() {
        return axisYTitles;
    }

    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }


    /**
     * 获取X轴刻度的百分比最大1
     *
     * @param value
     * @return
     */
    public String getAxisXGraduate(Object value) {

        float length = super.getWidth() - DEFAULT_AXIS_MARGIN_RIGHT;
        float valueLength = ((Float) value).floatValue();

        return String.valueOf(valueLength / length);
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

}
