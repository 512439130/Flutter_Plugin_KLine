package com.zb.module.kline_module.view.klinechart.adapter;
import com.zb.module.kline_module.view.klinechart.KLineEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author SoMustYY
 * @create 2019/5/23 3:22 PM
 * @organize 卓世达科
 * @describe 数据适配器
 * @update
 */
public class KLineChartAdapter extends BaseKLineChartAdapter {

    private List<KLineEntity> datas = new ArrayList<>();

    public KLineChartAdapter() {

    }

    @Override
    public int getCount() {
        if(datas != null  && datas.size() > 0 ){
            return datas.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(datas != null  && datas.size() > 0 && datas.get(position) != null){
            return datas.get(position);
        }else{
            return new KLineEntity();
        }
    }

    @Override
    public String getAllDate(int position) {
        if(datas != null  && datas.size() > 0 && datas.get(position) != null && datas.get(position).getAllDate() != null){
            return datas.get(position).getAllDate();
        }else{
            return "--";
        }
    }

    @Override
    public String getDate(int position) {
        if(datas != null  && datas.size() > 0 && datas.get(position) != null && datas.get(position).getDate() != null){
            return datas.get(position).getDate();
        }else{
            return "--";
        }
    }
    @Override
    public String getSingleTime(int position) {
        if(datas != null  && datas.size() > 0 && datas.get(position) != null && datas.get(position).getSingleTime() != null){
            return datas.get(position).getSingleTime();
        }else{
            return "--";
        }
    }
    @Override
    public String getWeekTime(int position) {
        if(datas != null  && datas.size() > 0 && datas.get(position) != null && datas.get(position).getWeekTime() != null){
            return datas.get(position).getWeekTime();
        }else{
            return "--";
        }
    }
    @Override
    public String getTimeType() {
        if(datas != null  && datas.size() > 0 && datas.get(0) != null && datas.get(0).getTimeType() != null){
            return datas.get(0).getTimeType();
        }else{
            return "single";
        }
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.clear();
            datas.addAll(data);
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.clear();
            datas.addAll(0, data);
        }else{
            datas = data;
        }

    }

    /**
     * 向尾部添加数据
     */
    public void setData(List<KLineEntity> list) {
        if (list != null && !list.isEmpty()) {
            datas.clear();
            datas.addAll(list);
            notifyDataSetChanged();
        }else{
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public List<KLineEntity> getDatas(){
        return datas;
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, KLineEntity data) {
        datas.set(position, data);
        notifyDataSetChanged();
    }

    /**
     * 数据清除
     */
    public void clearData() {
        datas.clear();
        notifyDataSetChanged();
    }
}
