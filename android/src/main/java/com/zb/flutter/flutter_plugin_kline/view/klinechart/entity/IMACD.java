package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe MACD指标(指数平滑移动平均线)接口
 * @update
 */
public interface IMACD {


    /**
     * DEA值
     */
    float getDea();

    /**
     * DIF值
     */
    float getDif();

    /**
     * MACD值
     */
    float getMacd();

}
