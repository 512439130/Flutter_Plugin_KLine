package com.zb.module.kline_module.view.klinechart.base;

import android.database.DataSetObserver;

/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 数据适配器
 * @update
 */
public interface IAdapter {
    /**
     * 获取点的数目
     *
     * @return
     */
    int getCount();

    /**
     * 通过序号获取item
     *
     * @param position 对应的序号
     * @return 数据实体
     */
    Object getItem(int position);

    /**
     * 通过序号获取时间  YYYY.MM.dd HH:mm
     *
     * @param position
     * @return
     */
    String getAllDate(int position);

    /**
     * 通过序号获取时间 MM.dd HH:mm
     *
     * @param position
     * @return
     */
    String getDate(int position);
    /**
     * 通过single时间 HH:mm
     *
     * @param position
     * @return
     */
    String getSingleTime(int position);

    /**
     * 通过Week时间 MM/dd HH:mm
     *
     * @param position
     * @return
     */
    String getWeekTime(int position);

    String getTimeType();
    /**
     * 注册一个数据观察者
     *
     * @param observer 数据观察者
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     *
     * @param observer 数据观察者
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 当数据发生变化时调用
     */
    void notifyDataSetChanged();
}
