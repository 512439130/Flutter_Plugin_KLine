package com.zb.module.kline_module.view.klinechart.entity;

import com.zb.module.kline_module.view.klinechart.entity.ICandle;
import com.zb.module.kline_module.view.klinechart.entity.IKDJ;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe KDJ指标(随机指标)接口
 * @update
 */
public interface IKLine extends ICandle, IMACD, IKDJ, IRSI, IVolume, IWR {
}
