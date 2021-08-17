package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 蜡烛图实体接口
 * @update
 */
public interface ICandle {

    /**
     * 开盘价
     */
    double getOpenPrice();

    /**
     * 最高价
     */
    double getHighPrice();

    /**
     * 最低价
     */
    double getLowPrice();

    /**
     * 收盘价
     */
    double getClosePrice();

    /**
     * 成交量
     */
    double getVol();

    // 以下为MA数据
    /**
     * 五(月，日，时，分，5分等)均价
     */
    float getMA5Price();

    /**
     * 十(月，日，时，分，5分等)均价
     */
    float getMA10Price();

    /**
     * 二十(月，日，时，分，5分等)均价
     */
    float getMA20Price();

    /**
     * 三十(月，日，时，分，5分等)均价
     */
    float getMA30Price();

    /**
     * 六十(月，日，时，分，5分等)均价
     */
    float getMA60Price();

    // 以下为BOLL数据
    /**
     * 上轨线
     */
    float getUp();

    /**
     * 中轨线
     */
    float getMb();

    /**
     * 下轨线
     */
    float getDn();

    double getEMA5Value();
    double getEMA10Value();
    double getEMA30Value();
}
