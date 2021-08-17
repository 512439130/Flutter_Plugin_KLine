package com.zb.flutter.flutter_plugin_kline.view.klinechart.entity;

/**

 * 获取首页行情图表
 */
public class ChartData {
    //数组下标：0，时间戳（long） 1，开盘的交易id(String)  2，关盘的交易id(String) 3，高开(double) 4，关闭(double) 5，高(double) 6，低(double) 7，总数量(double)
    private String[][] chartData;//当前货币类型

    public String[][] getChartData() {
        return chartData;
    }

    public void setChartData(String[][] chartData) {
        this.chartData = chartData;
    }

    @Override
    public String toString() {
        return "chartData=" + chartData
                +" | ";
    }
}
