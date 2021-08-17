package com.zb.module.kline_module.view.klinechart.base;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe Value格式化接口
 * @update
 */
public interface IValueFormatter {
    /**
     * 格式化value
     *
     * @param value 传入的value值
     * @return 返回字符串
     */
    String format(float value, int mDecimal);
}
