package com.zb.flutter.flutter_plugin_kline.view.item;

import java.util.List;

/**
 * @author SoMustYY
 * @create 2021-04-01 14:20
 * @organize 卓世达科
 * @describe 展开小K线均线Model
 * @update
 */
public class MALineEntity {

	/** 线表示数据 */
	private List<Double> lineData;

	/** 线的标题 */
	private String title;

	/** 线表示颜色 */
	private int lineColor;


	public List<Double> getLineData() {
		return lineData;
	}

	public void setLineData(List<Double> lineData) {
		this.lineData = lineData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

}
