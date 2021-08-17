package com.zb.flutter.flutter_plugin_kline.view.klinechart.depth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zb.flutter.flutter_plugin_kline.R;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.utils.FormatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author SoMustYY
 * @create 2019/5/28 9:59 AM
 * @organize 卓世达科
 * @describe 深度图
 * @update
 */
public class DepthView extends View {

    private int maxHeight;
    //是否使用match_parent(默认补全屏)
    private boolean isShowMatch = false;
    //是否显示详情
    private boolean isShowDetail = false;
    //是否长按
    private boolean isLongPress = false;
    //是否显示竖线
    private boolean isShowDetailLine = true;
    //手指单击松开后，数据是否继续显示
    private boolean isShowDetailSingleClick = true;
    //单击松开，数据延时消失，单位毫秒
    private final int DISAPPEAR_TIME = 300;
    //手指长按松开后，数据是否继续显示
    private boolean isShowDetailLongPress = true;
    private boolean isLongPressSwitch = true; //长按按钮显示开关
    private boolean isBisectionWidth = true; //是否均分买卖盘

    //长按触发时长，单位毫秒
    private final int LONG_PRESS_TIME_OUT = 300;
    //横坐标中间值
//    private double abscissaCenterPrice = -1;
    private boolean isHorizontalMove;
    private Depth clickDepth;
    private String detailPriceTitle;
    private String detailVolumeTitle;


    private Rect textRect;
    private Path linePath;
    private List<Depth> buyDataList, sellDataList;
    private double maxVolume, avgVolumeSpace, avgOrdinateSpace, depthImgHeight;
    private float leftStart, topStart, rightEnd, bottomEnd, longPressDownX, longPressDownY,
            singleClickDownX, singleClickDownY, detailLineWidth, dispatchDownX;
    private int ordinateTextCol, ordinateTextSize,
            abscissaTextCol, abscissaTextSize, ordinateNum,
            buyLineStrokeWidth, sellLineStrokeWidth, detailLineCol,
            moveLimitDistance;

    private Paint strokePaint, fillPaint;
    private Paint textPaint1;
    private Paint textPaint2;


    private int buyLineCol;
    private int buyBgStartCol;
    private int buyBgCenterCol;
    private int buyBgEndCol;

    private int sellLineCol;
    private int sellBgStartCol;
    private int sellBgCenterCol;
    private int sellBgEndCol;
    private int detailBgCol;
    private int detailTextCol;
    private int detailTextSize;

    private int detailCircleColor;  //点颜色
    private int detailCircleStrokeColor;  //点边界颜色

    private float detailCircleStrokeWidth;  //点边界宽度
    private float detailCircleRadius;  //点半径

    private Runnable longPressRunnable;
    private Runnable singleClickDisappearRunnable;
    private String buyLeftPrice; //买 低
    private String buyRightPrice; //买 高

    private String sellLeftPrice; //卖 高
    private String sellRightPrice; //卖 低


    private int priceDeep = 8;  //价格精度
    private int amountDeep = 8;  //数量精度

    private Context mContext;

    public int getPriceDeep() {
        return priceDeep;
    }

    public void setPriceDeep(int priceDeep) {
        this.priceDeep = priceDeep;
    }

    public int getAmountDeep() {
        return amountDeep;
    }

    public void setAmountDeep(int amountDeep) {
        this.amountDeep = amountDeep;
    }


    public DepthView(Context context) {
        this(context, null);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context, attrs);
    }

    /**
     * 设置购买数据
     */
    public void setBuyDataList(List<Depth> depthList) {
        buyDataList.clear();
        buyDataList.addAll(depthList);
        //如果数据是无序的，则按价格进行排序。如果是有序的，则注释掉
        Collections.sort(buyDataList);
        //计算累积交易量
        for (int i = buyDataList.size() - 1; i >= 0; i--) {
            if (i < buyDataList.size() - 1) {
                buyDataList.get(i).setVolume(buyDataList.get(i).getVolume() + buyDataList.get(i + 1).getVolume());
            }
        }
        requestLayout();
        invalidate();
    }

    /**
     * 设置出售数据
     */
    public void setSellDataList(List<Depth> depthList) {
        sellDataList.clear();
        sellDataList.addAll(depthList);
        //如果数据是无序的，则按价格进行排序。如果是有序的，则注释掉
        Collections.sort(sellDataList);
        //计算累积交易量
        for (int i = 0; i < sellDataList.size(); i++) {
            if (i > 0) {
                sellDataList.get(i).setVolume(sellDataList.get(i).getVolume() + sellDataList.get(i - 1).getVolume());
            }
        }
        requestLayout();
        invalidate();
    }

    /**
     * 重置深度数据
     */
    public void resetAllData(List<Depth> buyDataList, List<Depth> sellDataList) {
        setBuyDataList(buyDataList);
        setSellDataList(sellDataList);

        requestLayout();
        invalidate();
    }

    /**
     * 重置深度数据
     */
    public void clearAllData() {
        buyDataList.clear();
        sellDataList.clear();
        isShowDetail = false;
        isLongPress = false;
        requestLayout();
        invalidate();
    }


    /**
     * 重置按压
     */
    public void clearLongPress() {
        if(isShowDetail){
            isShowDetail = false;
            invalidate();
        }
    }

//    /**
//     * 设置横坐标中间值
//     */
//    public void setAbscissaCenterPrice(double centerPrice) {
//        this.abscissaCenterPrice = centerPrice;
//    }


    public boolean isShowMatch() {
        return isShowMatch;
    }

    public void setShowMatch(boolean showMatch) {
        isShowMatch = showMatch;
    }

    /**
     * 是否显示竖线
     */
    public void setShowDetailLine(boolean isShowLine) {
        this.isShowDetailLine = isShowLine;
    }

    /**
     * 手指单击松开后，数据是否继续显示
     */
    public void setShowDetailSingleClick(boolean isShowDetailSingleClick) {
        this.isShowDetailSingleClick = isShowDetailSingleClick;
    }

    /**
     * 手指长按松开后，数据是否继续显示
     */
    public void setShowDetailLongPress(boolean isShowDetailLongPress) {
        this.isShowDetailLongPress = isShowDetailLongPress;
    }


    /**
     * 手指长按业务逻辑是否使用
     *
     * @param longPressSwitch
     */
    public void setLongPressSwitch(boolean longPressSwitch) {
        isLongPressSwitch = longPressSwitch;
    }

    /**
     * 设置数据详情的价钱说明
     */
    public void setDetailPriceTitle(String priceTitle) {
        this.detailPriceTitle = priceTitle;
    }

    /**
     * 设置数据详情的数量说明
     */
    public void setDetailVolumeTitle(String volumeTitle) {
        this.detailVolumeTitle = volumeTitle;
    }

    /**
     * 移除runnable
     */
    public void cancelCallback() {
        removeCallbacks(longPressRunnable);
        removeCallbacks(singleClickDisappearRunnable);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DepthView);
            buyLineCol = typedArray.getColor(R.styleable.DepthView_dvBuyLineCol, 0xffA62121);
            buyLineStrokeWidth = typedArray.getInt(R.styleable.DepthView_dvBuyLineStrokeWidth, 1);
            sellLineCol = typedArray.getColor(R.styleable.DepthView_dvSellLineCol, 0xFF4FB900);
            sellLineStrokeWidth = typedArray.getInt(R.styleable.DepthView_dvSellLineStrokeWidth, 1);
            ordinateTextCol = typedArray.getColor(R.styleable.DepthView_dvOrdinateCol, 0xff808F9E);
            ordinateTextSize = typedArray.getInt(R.styleable.DepthView_dvOrdinateTextSize, 10);
            ordinateNum = typedArray.getInt(R.styleable.DepthView_dvOrdinateNum, 5);
            abscissaTextCol = typedArray.getColor(R.styleable.DepthView_dvAbscissaCol, ordinateTextCol);
            abscissaTextSize = typedArray.getInt(R.styleable.DepthView_dvAbscissaTextSize, ordinateTextSize);
            detailLineCol = typedArray.getColor(R.styleable.DepthView_dvDetailLineCol, 0xff828EA2);
            detailLineWidth = typedArray.getFloat(R.styleable.DepthView_dvDetailLineWidth, 0);
            detailPriceTitle = typedArray.getString(R.styleable.DepthView_dvDetailPriceTitle);
            detailVolumeTitle = typedArray.getString(R.styleable.DepthView_dvDetailVolumeTitle);


            buyBgStartCol = typedArray.getColor(R.styleable.DepthView_dvBuyBgStartCol, 0x48E91C41);
            buyBgEndCol = typedArray.getColor(R.styleable.DepthView_dvBuyBgEndCol, 0x195E2525);
            sellBgStartCol = typedArray.getColor(R.styleable.DepthView_dvSellBgStartCol, 0x6640D090);
            sellBgEndCol = typedArray.getColor(R.styleable.DepthView_dvSellBgEndCol, 0x1011433A);

//            detailBgCol = typedArray.getColor(R.styleable.DepthView_dvDetailBgCol, 0x99F3F4F6);
            detailTextCol = typedArray.getColor(R.styleable.DepthView_dvDetailTextCol, 0xff294058);
            detailTextSize = typedArray.getInt(R.styleable.DepthView_dvDetailTextSize, 10);
            detailCircleRadius = typedArray.getInt(R.styleable.DepthView_dvDetailPointRadius, 5);

            typedArray.recycle();
        }

        buyDataList = new ArrayList<>();
        sellDataList = new ArrayList<>();

        initPaint();

        textRect = new Rect();
        linePath = new Path();


        if (TextUtils.isEmpty(detailPriceTitle)) {
            detailPriceTitle = "--(--)：";
        }
        if (TextUtils.isEmpty(detailVolumeTitle)) {
            detailVolumeTitle = "--：";
        }

        moveLimitDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        if (isLongPressSwitch) {
            longPressRunnable = new Runnable() {
                @Override
                public void run() {
                    isLongPress = true;
                    isShowDetail = true;
                    getClickDepth(longPressDownX);
                    invalidate();
                }
            };
        }

        singleClickDisappearRunnable = new Runnable() {
            @Override
            public void run() {
                isShowDetail = false;
                invalidate();
            }
        };


    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private Paint buyCirclePaint, buyCircleStrokePaint, buyBoxBgPaint;
    private Paint sellCirclePaint, sellCircleStrokePaint,sellBoxBgPaint;


    private void initPaint() {
        strokePaint = new Paint();
//        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);

        textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint = new Paint();
//        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);


        buyCirclePaint = new Paint();
//        buyCirclePaint.setAntiAlias(true);
        buyCirclePaint.setStyle(Paint.Style.FILL);

        buyCircleStrokePaint = new Paint();
//        buyCircleStrokePaint.setAntiAlias(true);
        buyCircleStrokePaint.setStyle(Paint.Style.FILL);

        buyBoxBgPaint = new Paint();
//        buyBoxBgPaint.setAntiAlias(true);
        buyBoxBgPaint.setStyle(Paint.Style.FILL);

        sellCirclePaint = new Paint();
//        sellCirclePaint.setAntiAlias(true);
        sellCirclePaint.setStyle(Paint.Style.FILL);

        sellCircleStrokePaint = new Paint();
//        sellCircleStrokePaint.setAntiAlias(true);
        sellCircleStrokePaint.setStyle(Paint.Style.FILL);

        sellBoxBgPaint = new Paint();
//        sellBoxBgPaint.setAntiAlias(true);
        sellBoxBgPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isShowMatch()) {  //全屏
            try {
                Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
                @SuppressLint("DrawAllocation") DisplayMetrics d = new DisplayMetrics();
                display.getMetrics(d);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(d.heightPixels / 4, MeasureSpec.AT_MOST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftStart = getPaddingLeft();
        topStart = getPaddingTop();
        rightEnd = getMeasuredWidth() - getPaddingRight();
        bottomEnd = getMeasuredHeight() - getPaddingBottom();

        double maxBuyVolume;
        double minBuyVolume;
        double maxSellVolume;
        double minSellVolume;

        if (!buyDataList.isEmpty()) {
            maxBuyVolume = buyDataList.get(0).getVolume();
            minBuyVolume = buyDataList.get(buyDataList.size() - 1).getVolume();
        } else {
            maxBuyVolume = minBuyVolume = 0;
        }

        if (!sellDataList.isEmpty()) {
            maxSellVolume = sellDataList.get(sellDataList.size() - 1).getVolume();
            minSellVolume = sellDataList.get(0).getVolume();
        } else {
            maxSellVolume = minSellVolume = 0;
        }

        maxVolume = Math.max(maxBuyVolume, maxSellVolume);
        double minVolume = Math.min(minBuyVolume, minSellVolume);

        resetStrokePaint(abscissaTextCol, abscissaTextSize, 0);

        //计算很坐标价格标示
        if (!buyDataList.isEmpty()) {
            buyLeftPrice = setPrecision(buyDataList.get(0).getPrice(), priceDeep);
            buyRightPrice = setPrecision(buyDataList.get(buyDataList.size() - 1).getPrice(), priceDeep);
        } else if (!sellDataList.isEmpty()) {
            buyLeftPrice = setPrecision(sellDataList.get(0).getPrice(), priceDeep);
            buyRightPrice = setPrecision(sellDataList.get(sellDataList.size() - 1).getPrice(), priceDeep);
        } else {
            buyLeftPrice = "0";
            buyRightPrice = "0";
        }



        if (!sellDataList.isEmpty()) {
            sellRightPrice = setPrecision(sellDataList.get(sellDataList.size() - 1).getPrice(), priceDeep);
            sellLeftPrice = setPrecision(sellDataList.get(0).getPrice(), priceDeep);
        } else if (!buyDataList.isEmpty()) {
            sellRightPrice = setPrecision(buyDataList.get(buyDataList.size() - 1).getPrice(), priceDeep);
            sellLeftPrice = setPrecision(buyDataList.get(0).getPrice(), priceDeep);
        } else {
            sellRightPrice = "0";
            sellLeftPrice = "0";
        }

        strokePaint.getTextBounds(buyLeftPrice, 0, (buyLeftPrice+"").length(), textRect);
        depthImgHeight = bottomEnd - topStart - textRect.height() - dp2px(4);
        avgVolumeSpace = maxVolume / ordinateNum;
        avgOrdinateSpace = depthImgHeight / ordinateNum;
        double avgHeightPerVolume = depthImgHeight / (maxVolume - minVolume);


        //按数量比例绘制宽度
        double buyAreaSize = 0;
        double sellAreaSize = 0;
        if (buyDataList != null) {
            if (buyDataList.size() == 1) {
                buyAreaSize = buyDataList.size();
            } else if (buyDataList.size() > 1) {
                buyAreaSize = buyDataList.size() - 1;
            } else {
                buyAreaSize = 0;
            }
        }
        if (sellDataList != null) {
            if (sellDataList.size() == 1) {
                sellAreaSize = sellDataList.size();
            } else if (sellDataList.size() > 1) {
                sellAreaSize = sellDataList.size() - 1;
            } else {
                sellAreaSize = 0;
            }
        }

        if(isBisectionWidth){
            //等分宽度
            double avgBuyWidthPerSize ;
            double avgSellWidthPerSize ;

            if(sellDataList != null && buyDataList != null && !sellDataList.isEmpty() && buyDataList.isEmpty()){
                avgBuyWidthPerSize = 0;
                avgSellWidthPerSize = (rightEnd - leftStart) / sellAreaSize;
            }else if(sellDataList != null && buyDataList != null && sellDataList.isEmpty() && !buyDataList.isEmpty()){
                avgBuyWidthPerSize = (rightEnd - leftStart) / buyAreaSize;
                avgSellWidthPerSize = 0;
            } else {
                avgBuyWidthPerSize = (rightEnd - leftStart) / 2 / buyAreaSize;
                avgSellWidthPerSize = (rightEnd - leftStart) / 2 / sellAreaSize;
            }

//            Log.i("DEPTH_TEST", "avgBuyWidthPerSize:" + avgBuyWidthPerSize);
//            Log.i("DEPTH_TEST", "avgSellWidthPerSize:" + avgSellWidthPerSize);
            //买 左->右标记
            for (int i = 0; i < buyDataList.size(); i++) {
                if (buyDataList.size() == 1) {
                    if (sellDataList.size() == 0) {
                        buyDataList.get(i).setX(rightEnd / 2 * 1);
                    } else if (sellDataList.size() == 1) {
                        buyDataList.get(i).setX(rightEnd / 4 * 1);
                    } else {
                        buyDataList.get(i).setX(leftStart + ((float) avgBuyWidthPerSize));
                    }
                } else {
                    buyDataList.get(i).setX(leftStart + (float) avgBuyWidthPerSize * i - dp2px(2f)); //留1dp方便买1卖1点击
                }
                buyDataList.get(i).setY(topStart + (float) ((maxVolume - buyDataList.get(i).getVolume()) * avgHeightPerVolume));
            }

            //卖 右->左标记
            for (int i = sellDataList.size() - 1; i >= 0; i--) {
                if (sellDataList.size() == 1) {
                    if (buyDataList.size() == 0) {
                        sellDataList.get(i).setX(rightEnd / 2 * 1);
                    } else if (buyDataList.size() == 1) {
                        sellDataList.get(i).setX(rightEnd / 4 * 3);
                    } else {
                        sellDataList.get(i).setX(rightEnd - ((float) avgSellWidthPerSize / 2));
                    }
                } else {
                    sellDataList.get(i).setX(rightEnd - (float) (avgSellWidthPerSize * (sellDataList.size() - 1 - i)) + dp2px(2f));  //留1dp方便买1卖1点击
                }
                sellDataList.get(i).setY(topStart + (float) ((maxVolume - sellDataList.get(i).getVolume()) * avgHeightPerVolume));
            }
        }else{
            //根据买卖盘深度数据量计算平分区域量（5个点则分4块，买卖需要分开计算）
            double avgWidthPerSize = (rightEnd - leftStart) / (buyAreaSize + sellAreaSize);
//            Log.i("DEPTH_TEST", "avgWidthPerSize:" + avgWidthPerSize);
            //买 左->右标记
            for (int i = 0; i < buyDataList.size(); i++) {
                if (buyDataList.size() == 1) {
                    if (sellDataList.size() == 0) {
                        buyDataList.get(i).setX(rightEnd / 2 * 1);
                    } else if (sellDataList.size() == 1) {
                        buyDataList.get(i).setX(rightEnd / 4 * 1);
                    } else {
                        buyDataList.get(i).setX(leftStart + ((float) avgWidthPerSize / 2 - dp2px(6f)));
                    }
                } else {
                    buyDataList.get(i).setX(leftStart + (float) avgWidthPerSize * i - dp2px(2f)); //留1dp方便买1卖1点击
                }
                buyDataList.get(i).setY(topStart + (float) ((maxVolume - buyDataList.get(i).getVolume()) * avgHeightPerVolume));
            }

            //卖 右->左标记
            for (int i = sellDataList.size() - 1; i >= 0; i--) {
                if (sellDataList.size() == 1) {
                    if (buyDataList.size() == 0) {
                        sellDataList.get(i).setX(rightEnd / 2 * 1);
                    } else if (buyDataList.size() == 1) {
                        sellDataList.get(i).setX(rightEnd / 4 * 3);
                    } else {
                        sellDataList.get(i).setX(rightEnd - ((float) avgWidthPerSize / 2 - dp2px(6f)));
                    }
                } else {
                    sellDataList.get(i).setX(rightEnd - (float) (avgWidthPerSize * (sellDataList.size() - 1 - i)) + dp2px(2f));  //留1dp方便买1卖1点击
                }
                sellDataList.get(i).setY(topStart + (float) ((maxVolume - sellDataList.get(i).getVolume()) * avgHeightPerVolume));
            }
        }

        //待修改，绘制深度图标识（视情况编写）
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buyDataList.isEmpty() && sellDataList.isEmpty()) {
            return;
        }
        drawDepthLineAndBg(canvas);  //绘制深度图
        drawCoordinateValue(canvas); //绘制横/纵坐标
        drawDetailData(canvas); //绘制点击的弹窗
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            longPressDownX = event.getX();
            longPressDownY = event.getY();
            dispatchDownX = event.getX();


            if (isLongPressSwitch) {
                postDelayed(longPressRunnable, LONG_PRESS_TIME_OUT);
            }


            getClickDepth(longPressDownX);
            invalidate();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //长按控制
            float dispatchMoveX = event.getX();
            float dispatchMoveY = event.getY();
            float diffDispatchMoveX = Math.abs(dispatchMoveX - longPressDownX);
            float diffDispatchMoveY = Math.abs(dispatchMoveY - longPressDownY);
            float moveDistanceX = Math.abs(event.getX() - dispatchDownX);

            getParent().requestDisallowInterceptTouchEvent(true);

            if (isHorizontalMove || (diffDispatchMoveX > diffDispatchMoveY + dp2px(5)
                    && diffDispatchMoveX > moveLimitDistance)) {
                isHorizontalMove = true;
                removeCallbacks(longPressRunnable);

                if (isLongPress && moveDistanceX > 2) {
                    getClickDepth(event.getX());
                    if (clickDepth != null) {
                        invalidate();
                    }
                }
                dispatchDownX = event.getX();
                return isLongPress || super.dispatchTouchEvent(event);

            } else if (!isHorizontalMove && diffDispatchMoveY > diffDispatchMoveX + dp2px(5)
                    && diffDispatchMoveY > moveLimitDistance) {
                removeCallbacks(longPressRunnable);
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isHorizontalMove = false;
            removeCallbacks(longPressRunnable);
            if (!isShowDetailLongPress) {
                isShowDetail = false;
                invalidate();
            }
            getParent().requestDisallowInterceptTouchEvent(false);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            isHorizontalMove = false;
            removeCallbacks(longPressRunnable);
            if (!isShowDetailLongPress) {
                isShowDetail = false;
                invalidate();
            }
        }

        return isLongPress || super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                singleClickDownX = event.getX();
                singleClickDownY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float diffTouchMoveX = event.getX() - singleClickDownX;
                float diffTouchMoveY = event.getY() - singleClickDownY;
                if (diffTouchMoveY < moveLimitDistance && diffTouchMoveX < moveLimitDistance) {
                    isShowDetail = true;
                    getClickDepth(singleClickDownX);
                    if (clickDepth != null) {
                        invalidate();
                    }
                }
                if (!isShowDetailSingleClick) {
                    postDelayed(singleClickDisappearRunnable, DISAPPEAR_TIME);
                }
                break;
        }
        return true;
    }

    //获取单击位置数据
    private void getClickDepth(float clickX) {
        clickDepth = null;
        if (sellDataList.isEmpty()) {
            for (int i = 0; i < buyDataList.size(); i++) {
                if (i + 1 < buyDataList.size() && clickX >= buyDataList.get(i).getX()
                        && clickX < buyDataList.get(i + 1).getX()) {
                    clickDepth = buyDataList.get(i);
                    break;
                } else if (i == buyDataList.size() - 1 && clickX >= buyDataList.get(i).getX()) {
                    clickDepth = buyDataList.get(i);
                    break;
                }
            }
        } else if (clickX < sellDataList.get(0).getX()) {
            for (int i = 0; i < buyDataList.size(); i++) {
                if (i + 1 < buyDataList.size() && clickX >= buyDataList.get(i).getX() && clickX < buyDataList.get(i + 1).getX()) {
                    clickDepth = buyDataList.get(i);
                    break;
                } else if (i == buyDataList.size() - 1 && clickX >= buyDataList.get(i).getX() && clickX < sellDataList.get(0).getX()) {
                    clickDepth = buyDataList.get(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < sellDataList.size(); i++) {
                if (clickX >= sellDataList.get(sellDataList.size() - 1).getX() - dp2px(4)) { //特殊处理卖盘最右边的点击触控
                    clickDepth = sellDataList.get(i);
                }else{
                    if (i + 1 < sellDataList.size() && clickX >= sellDataList.get(i).getX() && clickX < sellDataList.get(i + 1).getX()) {
                        clickDepth = sellDataList.get(i);
                        break;
                    } else if (i == sellDataList.size() - 1 && clickX >= sellDataList.get(i).getX()) {
                        clickDepth = sellDataList.get(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 画底部X,Y轴的值
     * @param canvas
     */
    private void drawCoordinateValue(Canvas canvas) {
        if (sellRightPrice != null && sellLeftPrice != null && buyLeftPrice != null && buyRightPrice != null) {
            //横轴
            resetStrokePaint(abscissaTextCol, abscissaTextSize, 0);
            textPaint1.getTextBounds(sellRightPrice, 0, (sellRightPrice + "").length(), textRect);
            float textWidth = textRect.width();
            float leftFirstPriceX = leftStart + dp2px(1f);
            float rightFirstPriceX;
            float rightEndPriceX;
            if (sellDataList.isEmpty()) {
                rightFirstPriceX = 0;
            } else {
                rightFirstPriceX = sellDataList.get(0).getX() + dp2px(2f);
            }
            if (buyDataList.isEmpty()) {
                rightEndPriceX = 0;
            } else {
                rightEndPriceX = buyDataList.get((buyDataList.size() - 1)).getX() - textRect.width() - dp2px(1f);
            }

            if (buyDataList != null && buyDataList.size() - 1 >= 0 && buyDataList.get(buyDataList.size() - 1) != null) {
                //左边价格(左)
                if(isBisectionWidth){
                    canvas.drawText(buyLeftPrice, leftFirstPriceX, bottomEnd + dp2px(2f), textPaint1);
                }else{
                    if (sellDataList.isEmpty()) {
                        canvas.drawText(buyLeftPrice, leftFirstPriceX, bottomEnd + dp2px(2f), textPaint1);
                    } else if (sellDataList.size() == 1 && rightFirstPriceX >= leftFirstPriceX + textWidth) {
                        canvas.drawText(buyLeftPrice, leftFirstPriceX, bottomEnd + dp2px(2f), textPaint1);
                    } else if (rightFirstPriceX >= leftFirstPriceX + textWidth) {
                        canvas.drawText(buyLeftPrice, leftFirstPriceX, bottomEnd + dp2px(2f), textPaint1);
                    }
                }

                float x;
                if (buyDataList.size() == 1) {
                    if (sellDataList.size() > 0 && sellDataList.get(0) != null && sellDataList.size() == 1) {
                        x = rightEnd / 2 - textWidth - dp2px(2f);
                    } else {
                        x = 0;
                    }
                } else {
                    x = rightEndPriceX;
                }
                if (x != 0) {
                    //左边价格(右)
                    canvas.drawText(buyRightPrice, x, bottomEnd + dp2px(2f), textPaint1);
                }
            }
            if (sellDataList != null && buyDataList != null && sellDataList.size() > 0 && sellDataList.get(0) != null) {
                float x;
                if (sellDataList.size() == 1) {
                    if (buyDataList.size() - 1 >= 0 && buyDataList.get(buyDataList.size() - 1) != null && buyDataList.size() == 1) {
                        x = rightEnd / 2 + dp2px(2f);
                    } else {
                        x = 0;
                    }
                } else {
                    x = rightFirstPriceX;
                }
                if (x != 0) {
                    //右边价格(左)
                    canvas.drawText(sellLeftPrice, x, bottomEnd + dp2px(2f), textPaint1);
                }

                //右边价格(右)
                if(isBisectionWidth){
                    canvas.drawText(sellRightPrice, rightEnd - textWidth - dp2px(1f), bottomEnd + dp2px(2f), textPaint1);
                }else{
                    if (buyDataList.isEmpty()) {
                        canvas.drawText(sellRightPrice, rightEnd - textWidth - dp2px(1f), bottomEnd + dp2px(2f), textPaint1);
                    } else if (buyDataList.size() == 1 && rightEndPriceX + textWidth <= rightEnd - textWidth - dp2px(1f)) {
                        canvas.drawText(sellRightPrice, rightEnd - textWidth - dp2px(1f), bottomEnd + dp2px(2f), textPaint1);
                    } else if (rightEndPriceX + textWidth <= rightEnd - textWidth - dp2px(1f)) {
                        canvas.drawText(sellRightPrice, rightEnd - textWidth - dp2px(1f), bottomEnd + dp2px(2f), textPaint1);
                    }
                }
            }

            //纵轴
            resetStrokePaint(ordinateTextCol, ordinateTextSize, 0);
            textPaint1.getTextBounds(maxVolume + "", 0, (maxVolume + "").length(), textRect);
            for (int i = 0; i < ordinateNum; i++) {
                String ordinateStr = formatNum(maxVolume - i * avgVolumeSpace);
                canvas.drawText(ordinateStr,
                        rightEnd - textPaint1.measureText(ordinateStr),
                        (float) (topStart + textRect.height() + i * avgOrdinateSpace),
                        textPaint1);
            }
        }
    }

    /**
     * 画买卖盘深度图
     * @param canvas
     */
    private void drawDepthLineAndBg(Canvas canvas) {
        float buyProportion;
        float sellProportion;
        if (!buyDataList.isEmpty() && !sellDataList.isEmpty()) {
            if(isBisectionWidth){
                buyProportion = 0.5f;
                sellProportion = 0.5f;
            } else {
                buyProportion  = (float) buyDataList.size() / ((float) buyDataList.size() + (float) sellDataList.size());;
                sellProportion  = (float) sellDataList.size() / ((float)buyDataList.size() + (float) sellDataList.size());
            }
        } else if(!buyDataList.isEmpty()){
            if(isBisectionWidth){
                buyProportion = 1f;
                sellProportion = 0f;
            } else {
                buyProportion  = (float) buyDataList.size() / ((float) buyDataList.size() + (float) sellDataList.size());;
                sellProportion = 0.5f;
            }
        } else if(!sellDataList.isEmpty()){
            if(isBisectionWidth){
                buyProportion = 0f;
                sellProportion = 1f;
            } else {
                buyProportion = 0.5f;
                sellProportion  = (float) sellDataList.size() / ((float)buyDataList.size() + (float) sellDataList.size());
            }
        } else {
            buyProportion = 0.5f;
            sellProportion = 0.5f;
        }

        if (!buyDataList.isEmpty() && !sellDataList.isEmpty()) {
            //买方背景
            linePath.reset();
            if(buyDataList.size() == 1){
                linePath.moveTo(0, buyDataList.get(0).getY());
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), buyDataList.get(0).getY());  //横高
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), (float) (topStart + depthImgHeight)); //竖右
            }else{
                for (int i = 0; i < buyDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    } else {
                        linePath.lineTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    }
                }
                if (!buyDataList.isEmpty() && buyDataList.get(buyDataList.size() - 1).getY() < topStart + depthImgHeight) {
                    linePath.lineTo(buyDataList.get(buyDataList.size() - 1).getX(), (float) (topStart + depthImgHeight));
                }
            }
            linePath.lineTo(leftStart, (float) (topStart + depthImgHeight));
            linePath.close();

            Shader mShaderBuy = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                    new int[]{buyBgStartCol, buyBgCenterCol, buyBgEndCol},
                    new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
            fillPaint.setShader(mShaderBuy);
            fillPaint.setStrokeJoin(Paint.Join.ROUND);
            fillPaint.setStyle(Paint.Style.FILL);

            canvas.drawPath(linePath, fillPaint);

            //买方线条
            linePath.reset();
            if(buyDataList.size() == 1){
                linePath.moveTo(0, buyDataList.get(0).getY() + dp2px(1f));
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), buyDataList.get(0).getY() + dp2px(1f));  //横高
            } else{
                for (int i = 0; i < buyDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    } else {
                        linePath.lineTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    }
                }
            }
            resetStrokePaint(buyLineCol, 0, buyLineStrokeWidth);
            canvas.drawPath(linePath, strokePaint);


            //卖方背景
            linePath.reset();
            if(sellDataList.size() == 1){
                linePath.moveTo(rightEnd, sellDataList.get(0).getY());
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), sellDataList.get(0).getY());
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), (float) (topStart + depthImgHeight));
            }else{
                for (int i = sellDataList.size() - 1; i >= 0; i--) {
                    if (i == sellDataList.size() - 1) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
                if (!sellDataList.isEmpty() && sellDataList.get(0).getY() < (float) (topStart + depthImgHeight)) {
                    linePath.lineTo(sellDataList.get(0).getX(), (float) (topStart + depthImgHeight));
                }
            }

            linePath.lineTo(rightEnd, (float) (topStart + depthImgHeight));
            linePath.close();


            Shader mShaderSell = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                    new int[]{sellBgStartCol, sellBgCenterCol, sellBgEndCol},
                    new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
            fillPaint.setShader(mShaderSell);
            fillPaint.setAntiAlias(true);//抗锯齿
            fillPaint.setStrokeJoin(Paint.Join.ROUND);
            fillPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
            fillPaint.setStyle(Paint.Style.FILL);

            canvas.drawPath(linePath, fillPaint);

            //卖方线条
            linePath.reset();

            if(sellDataList.size() == 1){
                linePath.moveTo(rightEnd, sellDataList.get(0).getY() + dp2px(1f));
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), sellDataList.get(0).getY() + dp2px(1f));  //横高
            } else{
                for (int i = 0; i < sellDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
            }
            resetStrokePaint(sellLineCol, 0, sellLineStrokeWidth);
            canvas.drawPath(linePath, strokePaint);
        } else if (!buyDataList.isEmpty()) {
            //买方背景
            linePath.reset();
            if(buyDataList.size() == 1){
                linePath.moveTo(0, buyDataList.get(0).getY());
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), buyDataList.get(0).getY());  //横高
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), (float) (topStart + depthImgHeight)); //竖右
            }else{
                for (int i = 0; i < buyDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    } else {
                        linePath.lineTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    }
                }
                if (!buyDataList.isEmpty() && buyDataList.get(buyDataList.size() - 1).getY() < topStart + depthImgHeight) {
                    linePath.lineTo(buyDataList.get(buyDataList.size() - 1).getX(), (float) (topStart + depthImgHeight));
                }
            }
            linePath.lineTo(leftStart, (float) (topStart + depthImgHeight));
            linePath.close();

            Shader mShaderBuy = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                    new int[]{buyBgStartCol, buyBgCenterCol, buyBgEndCol},
                    new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
            fillPaint.setShader(mShaderBuy);
            fillPaint.setStrokeJoin(Paint.Join.ROUND);
            fillPaint.setStyle(Paint.Style.FILL);

            canvas.drawPath(linePath, fillPaint);

            //买方线条
            linePath.reset();
            if(buyDataList.size() == 1){
                linePath.moveTo(0, buyDataList.get(0).getY() + dp2px(1f));
                linePath.lineTo(((rightEnd * buyProportion) - dp2px(2f)), buyDataList.get(0).getY() + dp2px(1f));  //横高
            } else{
                for (int i = 0; i < buyDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    } else {
                        linePath.lineTo(buyDataList.get(i).getX(), buyDataList.get(i).getY());
                    }
                }
            }
            resetStrokePaint(buyLineCol, 0, buyLineStrokeWidth);
            canvas.drawPath(linePath, strokePaint);

        } else if (!sellDataList.isEmpty()) {
            //卖方背景
            linePath.reset();
            if(sellDataList.size() == 1){
                linePath.moveTo(rightEnd, sellDataList.get(0).getY());
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), sellDataList.get(0).getY());
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), (float) (topStart + depthImgHeight));
            }else{
                for (int i = sellDataList.size() - 1; i >= 0; i--) {
                    if (i == sellDataList.size() - 1) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
                if (!sellDataList.isEmpty() && sellDataList.get(0).getY() < (float) (topStart + depthImgHeight)) {
                    linePath.lineTo(sellDataList.get(0).getX(), (float) (topStart + depthImgHeight));
                }
            }

            linePath.lineTo(rightEnd, (float) (topStart + depthImgHeight));
            linePath.close();


            Shader mShaderSell = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                    new int[]{sellBgStartCol, sellBgCenterCol, sellBgEndCol},
                    new float[]{0, 0.5f, 1.0f}, Shader.TileMode.CLAMP);
            fillPaint.setShader(mShaderSell);
            fillPaint.setAntiAlias(true);//抗锯齿
            fillPaint.setStrokeJoin(Paint.Join.ROUND);
            fillPaint.setStrokeCap(Paint.Cap.ROUND);//线条结束处绘制一个半圆
            fillPaint.setStyle(Paint.Style.FILL);

            canvas.drawPath(linePath, fillPaint);

            //卖方线条
            linePath.reset();

            if(sellDataList.size() == 1){
                linePath.moveTo(rightEnd, sellDataList.get(0).getY() + dp2px(1f));
                linePath.lineTo((rightEnd - (rightEnd * sellProportion)) + dp2px(2f), sellDataList.get(0).getY() + dp2px(1f));  //横高
            } else{
                for (int i = 0; i < sellDataList.size(); i++) {
                    if (i == 0) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
            }
            resetStrokePaint(sellLineCol, 0, sellLineStrokeWidth);
            canvas.drawPath(linePath, strokePaint);
        }
    }

    /**
     * 画按压后的文本窗
     * @param canvas
     */
    private void drawDetailData(Canvas canvas) {

        if(clickDepth != null){
            getClickDepth(longPressDownX);
        }
        if (!isShowDetail || clickDepth == null) {
            return;
        }
        //游标线
        if (isShowDetailLine) {
            resetStrokePaint(detailLineCol, 0, detailLineWidth);
            canvas.drawLine(clickDepth.getX(), topStart, clickDepth.getX(), topStart + (float) depthImgHeight,
                    strokePaint);
        }

        buyCirclePaint.setColor(buyLineCol);
        buyCircleStrokePaint.setColor(detailCircleStrokeColor);
        buyBoxBgPaint.setColor(buyLineCol);

        sellCirclePaint.setColor(sellLineCol);
        sellCircleStrokePaint.setColor(detailCircleStrokeColor);
        sellBoxBgPaint.setColor(sellLineCol);

        if(sellDataList.isEmpty() || clickDepth.getX() < sellDataList.get(0).getX()){
            //买
            canvas.drawCircle(clickDepth.getX(), clickDepth.getY(), dp2px(detailCircleStrokeWidth), buyCircleStrokePaint);
            canvas.drawCircle(clickDepth.getX(), clickDepth.getY(), dp2px(detailCircleRadius), buyCirclePaint);
        }else if (buyDataList.isEmpty() || clickDepth.getX() >= sellDataList.get(0).getX()) {
            //卖
            canvas.drawCircle(clickDepth.getX(), clickDepth.getY(), dp2px(detailCircleStrokeWidth), sellCircleStrokePaint);
            canvas.drawCircle(clickDepth.getX(), clickDepth.getY(), dp2px(detailCircleRadius), sellCirclePaint);
        }


        resetStrokePaint(detailTextCol, detailTextSize, 0);

        String clickPriceStr = detailPriceTitle + setPrecision(clickDepth.getPrice(),priceDeep);
        String clickVolumeStr = detailVolumeTitle + formatNum(clickDepth.getVolume());

        strokePaint.getTextBounds(clickPriceStr, 0, (clickPriceStr+"").length(), textRect);
        int priceStrWidth = textRect.width();
        int priceStrHeight = textRect.height();
        strokePaint.getTextBounds(clickVolumeStr, 0, (clickVolumeStr+"").length(), textRect);
        int volumeStrWidth = textRect.width();
        int maxWidth = Math.max(priceStrWidth, volumeStrWidth);

        float bgLeft, bgTop, bgRight, bgBottom, priceStrX, priceStrY, volumeStrY;
        if (clickDepth.getX() <= maxWidth + dp2px(15)) {
            bgLeft = clickDepth.getX() + dp2px(10);
            bgRight = clickDepth.getX() + dp2px(20) + maxWidth;
            priceStrX = clickDepth.getX() + dp2px(15);

        } else {
            bgLeft = clickDepth.getX() - dp2px(20) - maxWidth;
            bgRight = clickDepth.getX() - dp2px(10);
            priceStrX = clickDepth.getX() - dp2px(15) - maxWidth;
        }

        if (clickDepth.getY() < topStart + dp2px(7) + priceStrHeight) {
            bgTop = topStart;
            bgBottom = topStart + dp2px(20) + priceStrHeight * 2;
            priceStrY = topStart + dp2px(5) + priceStrHeight;
            volumeStrY = topStart + dp2px(10) + priceStrHeight * 2;

        } else if (clickDepth.getY() > topStart + depthImgHeight - dp2px(7) - priceStrHeight) {
            bgTop = topStart + (float) depthImgHeight - dp2px(20) - priceStrHeight * 2;
            bgBottom = topStart + (float) depthImgHeight;
            priceStrY = topStart + (float) depthImgHeight - dp2px(15) - priceStrHeight;
            volumeStrY = topStart + (float) depthImgHeight - dp2px(10);

        } else {
            bgTop = clickDepth.getY() - dp2px(10) - priceStrHeight;
            bgBottom = clickDepth.getY() + dp2px(10) + priceStrHeight;
            priceStrY = clickDepth.getY() - dp2px(2);
            volumeStrY = clickDepth.getY() + priceStrHeight;
        }

        RectF rectF = new RectF(bgLeft, bgTop, bgRight, bgBottom);
        if(sellDataList.isEmpty() || clickDepth.getX() < sellDataList.get(0).getX()){
            canvas.drawRoundRect(rectF, 6, 6, buyBoxBgPaint);
            canvas.drawText(clickPriceStr, priceStrX, priceStrY, textPaint2);
            canvas.drawText(clickVolumeStr, priceStrX, volumeStrY, textPaint2);
        }else if (buyDataList.isEmpty() || clickDepth.getX() >= sellDataList.get(0).getX()) {
            canvas.drawRoundRect(rectF, 6, 6, sellBoxBgPaint);
            canvas.drawText(clickPriceStr, priceStrX, priceStrY, textPaint2);
            canvas.drawText(clickVolumeStr, priceStrX, volumeStrY, textPaint2);
        }

    }

    /**
     * 设置小数位精度
     *
     * @param num
     * @param scale 保留几位小数
     */
    private String setPrecision(Double num, int scale) {
        return FormatUtil.parseDoubleMaxFillingZero_X(num, scale);
    }

    /**
     * 按量级格式化数量
     */
    private String formatNum(double num) {
        if (num < 1) {
            return setPrecision(num, getAmountDeep());
        } else if (num < 10) {
            return setPrecision(num, getAmountDeep());
        } else if (num < 100) {
            return setPrecision(num, getAmountDeep());
        } else {
            return setPrecision(num / 1000, getAmountDeep()) + "K";
        }
    }

    private void resetStrokePaint(int colorId, int textSize, float strokeWidth) {
        strokePaint.setColor(colorId);
        strokePaint.setTextSize(sp2px(textSize));
        strokePaint.setStrokeWidth(dp2px(strokeWidth));

        textPaint1.setColor(colorId);
        textPaint1.setTextSize(sp2px(textSize));
        textPaint1.setStrokeWidth(dp2px(strokeWidth));

        textPaint2.setColor(colorId);
        textPaint2.setTextSize(sp2px(textSize));
        textPaint2.setStrokeWidth(dp2px(strokeWidth));
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void setBuyLineColor(int color) {
        this.buyLineCol = color;
    }

    public void setSellLineColor(int color) {
        this.sellLineCol = color;
    }


    public void setBuyBgStartColor(int color) {
        this.buyBgStartCol = color;
    }

    public void setBuyBgCenterColor(int color) {
        this.buyBgCenterCol = color;
    }

    public void setBuyBgEndColor(int color) {
        this.buyBgEndCol = color;
    }

    public void setSellBgStartColor(int color) {
        this.sellBgStartCol = color;
    }

    public void setSellBgCenterColor(int color) {
        this.sellBgCenterCol = color;
    }

    public void setSellBgEndColor(int color) {
        this.sellBgEndCol = color;
    }

    public void setDetailBgColor(int color) {
        this.detailBgCol = color;
    }

    public void setDetailTextColor(int color) {
        this.detailTextCol = color;
    }

    public void setDetailTextSize(int size) {
        this.detailTextSize = size;
    }

    public void setDetailCircleRadius(float radius) {
        this.detailCircleRadius = radius;
    }

    public void setDetailCircleColor(int detailCircleColor) {
        this.detailCircleColor = detailCircleColor;
    }

    public void setDetailCircleStrokeColor(int detailCircleStrokeColor) {
        this.detailCircleStrokeColor = detailCircleStrokeColor;
    }

    public void setDetailCircleStrokeWidth(float detailCircleStrokeWidth) {
        this.detailCircleStrokeWidth = detailCircleStrokeWidth;
    }

    public void setBisectionWidth(boolean bisectionWidth) {
        isBisectionWidth = bisectionWidth;
    }
}
