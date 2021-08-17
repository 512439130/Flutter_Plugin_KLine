package com.zb.module.kline_module.view.klinechart.formatter;

import com.zb.module.kline_module.view.klinechart.base.IDateTimeFormatter;
import com.zb.module.kline_module.view.klinechart.utils.DateUtil;

import java.util.Date;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 时间格式化器
 * @update
 */
public class DateFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date != null) {
            return DateUtil.DateFormat.format(date);
        } else {
            return "";
        }
    }
}
