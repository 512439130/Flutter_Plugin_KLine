package com.zb.module.kline_module.view.klinechart.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.zb.module.kline_module.view.klinechart.base.IAdapter;


/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe k线图的数据适配器
 * @update
 */
public abstract class BaseKLineChartAdapter implements IAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public void notifyDataSetChanged() {
        if (getCount() > 0) {
            mDataSetObservable.notifyChanged();
        } else {
            mDataSetObservable.notifyInvalidated();
        }
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
}
