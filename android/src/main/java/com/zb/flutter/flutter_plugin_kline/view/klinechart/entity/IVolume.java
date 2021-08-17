package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;


/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 成交量接口
 * @update
 */
public interface IVolume {

    /**
     * 开盘价
     */
    double getOpenPrice();

    /**
     * 收盘价
     */
    double getClosePrice();

    /**
     * 成交量
     */
    double getVol();

    /**
     * 五(月，日，时，分，5分等)均量
     */
    float getMA5Volume();

    /**
     * 十(月，日，时，分，5分等)均量
     */
    float getMA10Volume();
}
