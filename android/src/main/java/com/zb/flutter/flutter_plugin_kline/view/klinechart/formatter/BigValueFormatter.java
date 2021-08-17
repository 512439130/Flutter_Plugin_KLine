package com.zb.flutter.flutter_plugin_kline.view.klinechart.formatter;

import android.content.Context;

import com.zb.flutter.flutter_plugin_kline.R;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.base.IValueFormatter;
import com.zb.flutter.flutter_plugin_kline.view.klinechart.utils.FormatUtil;


/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 对较大数据进行格式化
 * @update
 */
public class BigValueFormatter implements IValueFormatter {

    //必须是排好序的
    private int[] values = {1000,1000000};

    private int mDecimal = 2; //默认保留2位小数
    private Context mContext;

    public BigValueFormatter(Context context){
        mContext = context;
    }

    @Override
    public String format(float value,int mDecimal) {
        String[] units = new String[]{mContext.getResources().getString(R.string.txt_kline_unit_k), mContext.getResources().getString(R.string.txt_kline_unit_m)};
        String unit = "";
        int i = values.length - 1;
        while (i>=0)
        {
            if(value>values[i]) {
                value /= values[i];
                unit = units[i];
                break;
            }
            i--;
        }
//        System.out.println("mDecimal:"+mDecimal);
        return FormatUtil.parseDoubleMaxFillingZero_X(value, mDecimal) + unit;

    }
}
