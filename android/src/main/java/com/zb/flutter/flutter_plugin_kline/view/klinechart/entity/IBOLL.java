package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;
/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 布林线指标接口
 * @update
 */
public interface IBOLL {
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
}
