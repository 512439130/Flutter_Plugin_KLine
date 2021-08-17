package com.zb.module.kline_module.view.klinechart.formatter;

import com.zb.module.kline_module.view.klinechart.base.IValueFormatter;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe Value格式化类
 * @update
 */
public class ValueFormatter implements IValueFormatter {
    private int decimal = 4;

    public ValueFormatter(int decimal){
        this.decimal = decimal;
    }


    @Override
    public String format(float value, int mDecimal) {
        String format = "%." + decimal + "f";
        return String.format(format, value);
    }
}
