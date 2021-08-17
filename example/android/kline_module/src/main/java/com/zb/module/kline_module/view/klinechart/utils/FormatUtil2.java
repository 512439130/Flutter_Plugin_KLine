package com.zb.module.kline_module.view.klinechart.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 格式化工具类
 * @update
 */
public class FormatUtil2 {
    private static DecimalFormat sDecimalFormat = new DecimalFormat();

    public static double parseDouble(String value){
        double result = 0.0;
        try {
            value = value.replaceAll(",",".");
            result = Double.parseDouble(value);
            //去掉科学计数法显示
//            NumberFormat NF = NumberFormat.getInstance();
//            NF.setMaximumFractionDigits(10);
//            NF.setGroupingUsed(false);
//            result = Double.valueOf(NF.format(result));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String parseDoubleMax2(String number){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        sDecimalFormat.setDecimalFormatSymbols(symbols);
        sDecimalFormat.applyPattern("#.00");
        if(KlineEditTextUtil.isNumeric(sDecimalFormat.format(parseDouble(number)))){
            return BigDecimal.valueOf(FormatUtil2.parseDouble(sDecimalFormat.format(parseDouble(number)))).stripTrailingZeros().toPlainString();
        }else{
            return "--";
        }

    }
}
