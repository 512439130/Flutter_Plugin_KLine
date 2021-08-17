package com.zb.flutter.flutter_plugin_kline.platform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.zb.flutter.flutter_plugin_kline.R;
import com.zb.flutter.flutter_plugin_kline.util.DeviceUtil;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.DataHelper;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.KLineEntity;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.adapter.KLineChartAdapter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.ChildStatus;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.draw.MainIndexStatus;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.ChartData;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter.DateFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.utils.FormatUtil;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.view.KLineChartView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

/**
 * @author SoMustYY
 * @create 2021/4/2 11:47 AM
 * @organize ZB
 * @describe
 * @update
 */
public class MyKLineView implements PlatformView {
    private static final String TAG = "MyKLineView";
    private Context mContext;
    private KLineChartView kLineChartView;

    private KLineChartAdapter mKLineChartAdapter;

    private boolean isFirstInit = true;

    public MyKLineView(Context context, BinaryMessenger messenger, int viewId, Map<String, Object> params) {
        this.mContext = context;
        if (params != null) {
            if (params.containsKey("data")) {
                String klineJsonData = (String) params.get("data");
                Log.d(TAG, "init-klineJsonData:" + klineJsonData);
                initKLineView(klineJsonData);
            }
            if (params.containsKey("theme")) {
                String theme = (String) params.get("theme");
                Log.d(TAG, "init-theme:" + theme);
                updateKlineColor(theme);
            } else {
                updateKlineColor("0"); // 不传默认白天
            }
        }
    }

    private void initKLineView(String klineJsonData) {
        Log.d(TAG, "initKLineView");
        kLineChartView = new KLineChartView(mContext);
        mKLineChartAdapter = new KLineChartAdapter();
        kLineChartView.setAdapter(mKLineChartAdapter);
        kLineChartView.setDateTimeFormatter(new DateFormatter());
        kLineChartView.setGridRows(4);
        kLineChartView.setGridColumns(4);
        kLineChartView.setScrollOnFling(true);  //是否开启加速滑动
        kLineChartView.setInnerLayerSlidingConflict(true); //是否内层处理滑动冲突
        kLineChartView.setTouchFollowPoint(false); //不跟随坐标点
        kLineChartView.setCandleSolid(true);//实心
        kLineChartView.setScaleXMax(5f);
        kLineChartView.setScaleXMin(1f);
        kLineChartView.setCandleHollowSwitch(true);//设置是否空心MACD开关
        kLineChartView.setScaleX(1.5f);  //缩放速率
        kLineChartView.setPriceDecimal(8);
        kLineChartView.setAmountDecimal(8);
        kLineChartView.setScaleEnable(true);
        refreshKLineData(klineJsonData);
    }

    public void updateKlineColor(String state) {
        Log.d(TAG, "updateKlineColor-state:" + state);
        if (kLineChartView != null) {
            switch (state) {
                case "0":
                    kLineChartView.setBackgroundColor(mContext.getResources().getColor(R.color.custom_attr_theme_color_light));
                    kLineChartView.setCircleBackgroundColor(mContext.getResources().getColor(R.color.custom_attr_theme_color_light));
                    kLineChartView.setGridLineColor(mContext.getResources().getColor(R.color.custom_attr_input_box_50_bg_color_light));
                    kLineChartView.setTextColor(mContext.getResources().getColor(R.color.zb_color_999999));
                    kLineChartView.setTimerLineColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setTimerLineCircleColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setTimerLineCircleStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_circle_stroke_color_light));
                    kLineChartView.setTimerStartColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_start_text_color_light));
                    kLineChartView.setTimerEndColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_end_text_color_light));
                    kLineChartView.setRedColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setGreenColor(mContext.getResources().getColor(R.color.zb_color_green));
                    //当前最新价
                    kLineChartView.setCurrentFrameStrokePaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_light));
                    kLineChartView.setCurrentFrameBgPaint(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setCurrentFrameTextPaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_light));
                    kLineChartView.setCurrentLineColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setNormalColor(mContext.getResources().getColor(R.color.custom_attr_keyword_txt_color_light));
                    kLineChartView.setWhiteColor(mContext.getResources().getColor(R.color.zb_color_white));
                    kLineChartView.setSelectPointColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_point_color_light));
                    kLineChartView.setSelectPointStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_point_stroke_color_light));
                    kLineChartView.setSelectedXLineColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_line_x_color_light));
                    kLineChartView.setSelectedYLineColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_line_y_color_light));
                    //XY轴动态值
                    kLineChartView.setSelectXYFrameStrokePaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_stroke_color_light));
                    kLineChartView.setSelectXYFrameBgPaint(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_light));
                    kLineChartView.setSelectXYFrameTextPaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_stroke_color_light));
                    kLineChartView.setSelectorBackgroundColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selector_box_background_light));
                    kLineChartView.setSelectorStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selector_box_stroke_light));
                    kLineChartView.setSelectorTextColor(mContext.getResources().getColor(R.color.zb_color_999999));
                    kLineChartView.setDIFColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_light));
                    kLineChartView.setDEAColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_light));
                    kLineChartView.setMACDColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_light));
                    kLineChartView.setKColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_light));
                    kLineChartView.setDColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_light));
                    kLineChartView.setJColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_light));
                    //wr
                    kLineChartView.setRColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_light));
                    kLineChartView.setRSI1Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_light));
                    kLineChartView.setRSI2Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_light));
                    kLineChartView.setRSI3Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_light));
                    kLineChartView.setMa5Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_light));
                    kLineChartView.setMa10Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_light));
                    kLineChartView.setMa30Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_light));
                    break;
                case "1":
                    kLineChartView.setBackgroundColor(mContext.getResources().getColor(R.color.custom_attr_theme_color_night));
                    kLineChartView.setCircleBackgroundColor(mContext.getResources().getColor(R.color.custom_attr_theme_color_night));
                    kLineChartView.setGridLineColor(mContext.getResources().getColor(R.color.custom_attr_input_box_50_bg_color_night));
                    kLineChartView.setTextColor(mContext.getResources().getColor(R.color.zb_color_999999));
                    kLineChartView.setTimerLineColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setTimerLineCircleColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setTimerLineCircleStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_circle_stroke_color_night));
                    kLineChartView.setTimerStartColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_start_text_color_night));
                    kLineChartView.setTimerEndColor(mContext.getResources().getColor(R.color.custom_color_kline_timer_end_text_color_night));
                    kLineChartView.setRedColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setGreenColor(mContext.getResources().getColor(R.color.zb_color_green));
                    //当前最新价
                    kLineChartView.setCurrentFrameStrokePaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_night));
                    kLineChartView.setCurrentFrameBgPaint(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setCurrentFrameTextPaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_night));
                    kLineChartView.setCurrentLineColor(mContext.getResources().getColor(R.color.zb_color_red));
                    kLineChartView.setNormalColor(mContext.getResources().getColor(R.color.custom_attr_keyword_txt_color_night));
                    kLineChartView.setWhiteColor(mContext.getResources().getColor(R.color.zb_color_white));
                    kLineChartView.setSelectPointColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_point_color_night));
                    kLineChartView.setSelectPointStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_point_stroke_color_night));
                    kLineChartView.setSelectedXLineColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_line_x_color_night));
                    kLineChartView.setSelectedYLineColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selected_line_y_color_night));
                    //XY轴动态值
                    kLineChartView.setSelectXYFrameStrokePaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_stroke_color_night));
                    kLineChartView.setSelectXYFrameBgPaint(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_bg_color_night));
                    kLineChartView.setSelectXYFrameTextPaintColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_frame_stroke_color_night));
                    kLineChartView.setSelectorBackgroundColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selector_box_background_night));
                    kLineChartView.setSelectorStrokeColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_selector_box_stroke_night));
                    kLineChartView.setSelectorTextColor(mContext.getResources().getColor(R.color.zb_color_999999));
                    kLineChartView.setDIFColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_night));
                    kLineChartView.setDEAColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_night));
                    kLineChartView.setMACDColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_night));
                    kLineChartView.setKColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_night));
                    kLineChartView.setDColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_night));
                    kLineChartView.setJColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_night));
                    //wr
                    kLineChartView.setRColor(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_night));
                    kLineChartView.setRSI1Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_night));
                    kLineChartView.setRSI2Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_night));
                    kLineChartView.setRSI3Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_night));
                    kLineChartView.setMa5Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma5_night));
                    kLineChartView.setMa10Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma10_night));
                    kLineChartView.setMa30Color(mContext.getResources().getColor(R.color.custom_color_kline_chart_ma30_night));
                    break;
            }

            if (mKLineChartAdapter != null) {
                mKLineChartAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private List<KLineEntity> createKLineData(String klinejsondata) {
        List<KLineEntity> list = new ArrayList<>();
        if (klinejsondata != null) {
            ChartData chartData = new Gson().fromJson(klinejsondata, ChartData.class);
            for (String[] aKlineData : chartData.getChartData()) {
                KLineEntity mKlineData = new KLineEntity();
                long time = Long.parseLong(aKlineData[0]);
                SimpleDateFormat allTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat timeSdf = new SimpleDateFormat("MM-dd HH:mm");
                SimpleDateFormat singleTimeSdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat singleWeekTimeSdf = new SimpleDateFormat("MM/dd");
                mKlineData.setAllDate(allTimeSdf.format(time * 1000L));
                mKlineData.setDate(timeSdf.format(time * 1000L));
                mKlineData.setSingleTime(singleTimeSdf.format(time * 1000L));
                mKlineData.setWeekTime(singleWeekTimeSdf.format(time * 1000L));
                mKlineData.setTime(time);
                mKlineData.setTimeType("single");
                mKlineData.setOpenPrice(Double.parseDouble(aKlineData[3]));
                mKlineData.setClosePrice(Double.parseDouble(aKlineData[4]));
                mKlineData.setHighPrice(Double.parseDouble(aKlineData[5]));
                mKlineData.setLowPrice(Double.parseDouble(aKlineData[6]));
                mKlineData.setVol(Double.parseDouble(FormatUtil.parseDoubleMax8(Double.parseDouble(aKlineData[7]))));
                list.add(mKlineData);
            }
        }
        return list;
    }

    synchronized public void refreshKLineData(String klineJsonData) {
        List<KLineEntity> list = createKLineData(klineJsonData);
        Log.d(TAG, "refreshKLineData");
        if (kLineChartView != null && mKLineChartAdapter != null) {
            kLineChartView.justShowLoading();

            //1.排序
            DataHelper.kLineSortAscending(list);
            //2.计算指标数据
            DataHelper.calculate(list);
            //3.填充数据
            mKLineChartAdapter.setData(list);

            int kLineDataSize = list.size();
            int oneScreenMaxCandleSize = kLineChartView.getOneScreenMaxCandleSize();
            int differSize;
            float candleWidth = kLineChartView.getCandleWidth();
            if (kLineDataSize < oneScreenMaxCandleSize) {
                Log.d(TAG, "K线数据量不足，顶到左边显示");
                differSize = oneScreenMaxCandleSize - kLineDataSize;
                float rightWidth = differSize * candleWidth + kLineChartView.getMinRightWidth() - (kLineChartView.getCircleRadius() / 2);
                kLineChartView.setOverScrollRange(rightWidth);
                kLineChartView.setLessData(true);
            } else {
                Log.d(TAG, "K线数据量足，正常显示");
                kLineChartView.setLessData(false);
                resetKlineChartViewOverScrollRange(DeviceUtil.dp2px(mContext, 85));
            }
            kLineChartView.refreshEnd();
            if (isFirstInit) {
                kLineChartView.startAnimation();
                isFirstInit = false;
            }
        }
    }

    /**
     * 设置K线右测空隙距离
     * @param width
     */
    private void resetKlineChartViewOverScrollRange(float width) {
        Log.d(TAG, "resetKlineChartViewOverScrollRange");
        if (kLineChartView != null) {
            kLineChartView.setOverScrollRange(width);
            kLineChartView.setRightWidth(width);
        }
    }

    /**
     * 更新K线主图指标类型
     * @param state
     */
    public void updateKlineMainIndexState(String state) {
        MainIndexStatus mainIndexStatus;
        switch (state) {
            case "0":
                mainIndexStatus = MainIndexStatus.MA;
                break;
            case "1":
                mainIndexStatus = MainIndexStatus.EMA;
                break;
            case "2":
                mainIndexStatus = MainIndexStatus.BOLL;
                break;
            case "-1":
            default:
                mainIndexStatus = MainIndexStatus.NONE;
                break;
        }
        if (kLineChartView != null) {
            kLineChartView.hideSelectData();
            kLineChartView.changeMainDrawType(mainIndexStatus);
        }
    }

    /**
     * 更新K线副图指标类型
     * @param state
     */
    public void updateKlineChildIndexState(String state) {
        int childState = Integer.parseInt(state);
        if (kLineChartView != null) {
            kLineChartView.hideSelectData();
            if (childState != ChildStatus.NONE) {
                kLineChartView.setChildDraw(childState);
            } else {
                kLineChartView.hideChildDraw();
            }
        }
    }

    /**
     * 更新K线滑动模式
     * @param state
     */
    public void updateKLineScrollState(String state){
        if(kLineChartView != null){
            switch (state){
                default:
                case "0":
                    kLineChartView.setScrollOnFling(true);
                    break;
                case "1":
                    kLineChartView.setScrollOnFling(false);
                    break;
            }
        }
    }

    /**
     * 更新K线蜡烛主题
     * @param state
     */
    public void updateKLineCandleThemeState(String state) {
        if (kLineChartView != null) {
            kLineChartView.hideSelectData();
            switch (state){
                case "0":
                    kLineChartView.setCandleSolid(true);
                    break;
                case "1":
                    kLineChartView.setCandleSolid(false);
                    break;
            }
        }
    }

    /**
     * 更新K线按压主题
     * @param status
     */
    public void updateKLinePressState(String status) {
        if (kLineChartView != null) {
            kLineChartView.hideSelectData();
            switch (status){
                case "0":
                    kLineChartView.setTouchFollowPoint(true);
                    break;
                case "1":
                    kLineChartView.setTouchFollowPoint(false);
                    break;
            }
        }
    }

    @Override
    public View getView() {
        Log.d(TAG, "getView");
        return kLineChartView;
    }

    @Override
    public void onFlutterViewAttached(@NonNull View flutterView) {
        Log.d(TAG, "onFlutterViewAttached");
    }

    @Override
    public void onFlutterViewDetached() {
        Log.d(TAG, "onFlutterViewDetached");
    }

    @Override
    public void dispose() {
        Log.d(TAG, "dispose");
    }

    @Override
    public void onInputConnectionLocked() {
        Log.d(TAG, "onInputConnectionLocked");
    }

    @Override
    public void onInputConnectionUnlocked() {
        Log.d(TAG, "onInputConnectionUnlocked");
    }
}