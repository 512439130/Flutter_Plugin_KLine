package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe KDJ指标(随机指标)接口
 * @update
 */
public interface IKDJ {

    /**
     * K值
     */
    float getK();

    /**
     * D值
     */
    float getD();

    /**
     * J值
     */
    float getJ();

}