package com.zb.flutter.flutter_plugin_kline.view.klinechart;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.entity.IKLine;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe K线实体
 * @update
 */
public class KLineEntity implements IKLine {

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
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

    public String getAllDate() {
        return allDate;
    }

    public void setAllDate(String allDate) {
        this.allDate = allDate;
    }

    public String getSingleTime() {
        return singleTime;
    }

    public void setSingleTime(String singleTime) {
        this.singleTime = singleTime;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(String weekTime) {
        this.weekTime = weekTime;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String allDate) {
        this.date = allDate;
    }

    @Override
    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    @Override
    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    @Override
    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Override
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

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    public void setMA5Price(float MA5Price) {
        this.MA5Price = MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    public void setMA10Price(float MA10Price) {
        this.MA10Price = MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    public void setMA20Price(float MA20Price) {
        this.MA20Price = MA20Price;
    }

    @Override
    public float getMA30Price() {
        return MA30Price;
    }

    public void setMA30Price(float MA30Price) {
        this.MA30Price = MA30Price;
    }

    @Override
    public float getMA60Price() {
        return MA60Price;
    }

    public void setMA60Price(float MA60Price) {
        this.MA60Price = MA60Price;
    }

    public double getEMA5Value() {
        return EMA5Value;
    }

    public void setEMA5Value(double EMA5Value) {
        this.EMA5Value = EMA5Value;
    }

    public double getEMA10Value() {
        return EMA10Value;
    }

    public void setEMA10Value(double EMA10Value) {
        this.EMA10Value = EMA10Value;
    }

    public double getEMA30Value() {
        return EMA30Value;
    }

    public void setEMA30Value(double EMA30Value) {
        this.EMA30Value = EMA30Value;
    }

    @Override
    public float getDea() {
        return dea;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    public void setMacd(float macd) {
        this.macd = macd;
    }

    @Override
    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    @Override
    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    @Override
    public float getJ() {
        return j;
    }

    public void setJ(float j) {
        this.j = j;
    }

    @Override
    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    @Override
    public float getRsi() {
        return rsi;
    }

    public void setRsi(float rsi) {
        this.rsi = rsi;
    }

    @Override
    public float getUp() {
        return up;
    }

    public void setUp(float up) {
        this.up = up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    public void setMb(float mb) {
        this.mb = mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    public void setDn(float dn) {
        this.dn = dn;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    public void setMA5Volume(float MA5Volume) {
        this.MA5Volume = MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    public void setMA10Volume(float MA10Volume) {
        this.MA10Volume = MA10Volume;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String _ID = "";
    private String cacheName = "";
    private String openId = "";
    private String closeId = "";

    private long time;
    private String allDate;  //YYYY.MM.dd HH:mm
    private String date;  //MM.dd HH:mm
    private String singleTime;  //HH:mm
    private String weekTime;  //MM/dd HH:mm

    private String timeType;
    private double openPrice = 0;
    private double closePrice = 0;
    private double lowPrice = 0;
    private double highPrice = 0;
    private double vol = 0;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float MA30Price;

    public float MA60Price;

    public double EMA5Value;

    public double EMA10Value;

    public double EMA30Value;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float r;

    public float rsi;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;

    @Override
    public String toString() {
        return "KLineEntity{" +
                "_ID='" + _ID + '\'' +
                ", cacheName='" + cacheName + '\'' +
                ", openId='" + openId + '\'' +
                ", closeId='" + closeId + '\'' +
                ", time=" + time +
                ", allDate='" + allDate + '\'' +
                ", date='" + date + '\'' +
                ", singleTime='" + singleTime + '\'' +
                ", weekTime='" + weekTime + '\'' +

                ", timeType='" + timeType + '\'' +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", lowPrice=" + lowPrice +
                ", highPrice=" + highPrice +
                ", vol=" + vol +
                ", MA5Price=" + MA5Price +
                ", MA10Price=" + MA10Price +
                ", MA20Price=" + MA20Price +
                ", MA30Price=" + MA30Price +
                ", MA60Price=" + MA60Price +
                ", EMA5Value=" + EMA5Value +
                ", EMA10Value=" + EMA10Value +
                ", EMA30Value=" + EMA30Value +
                ", dea=" + dea +
                ", dif=" + dif +
                ", macd=" + macd +
                ", k=" + k +
                ", d=" + d +
                ", j=" + j +
                ", r=" + r +
                ", rsi=" + rsi +
                ", up=" + up +
                ", mb=" + mb +
                ", dn=" + dn +
                ", MA5Volume=" + MA5Volume +
                ", MA10Volume=" + MA10Volume +
                '}';
    }
}
