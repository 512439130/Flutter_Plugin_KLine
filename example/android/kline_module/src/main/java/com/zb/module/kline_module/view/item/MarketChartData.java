package com.zb.module.kline_module.view.item;

import java.text.SimpleDateFormat;

/**
 * @author SoMustYY
 * @create 2021-04-01 14:20
 * @organize 卓世达科
 * @describe 展开小K线Model
 * @update
 */
public class MarketChartData {

    private String openId = "";
    private String closeId = "";
    private long time = 0;
    private double openPrice = 0;
    private double closePrice = 0;
    private double lowPrice = 0;
    private double highPrice = 0;
    private double vol = 0;

    public MarketChartData(String[] data) {
        setTime(Long.parseLong(data[0]));
        setOpenPrice(Double.parseDouble(data[3]));
        setClosePrice(Double.parseDouble(data[4]));
        setHighPrice(Double.parseDouble(data[5]));
        setLowPrice(Double.parseDouble(data[6]));
        setVol(Double.parseDouble(data[7]));
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCloseId() {
        return closeId;
    }

    public void setCloseId(String closeId) {
        this.closeId = closeId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public String getTime6() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(time * 1000);
    }

    public String getTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time * 1000);
    }

    public String getTime3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time * 1000);
    }

    public String getTime4() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(time * 1000);
    }

    public String getTime5() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.time * 1000L);
    }
}
