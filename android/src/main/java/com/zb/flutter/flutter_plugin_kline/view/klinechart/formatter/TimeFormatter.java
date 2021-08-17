package com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter;


import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IDateTimeFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.utils.DateUtil;

import java.util.Date;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 时间格式化器
 * @update
 */
public class TimeFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.shortTimeFormat.format(date);
    }
}
