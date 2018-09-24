package com.wk.wktransportation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wk.wktransportation.R;
import com.wk.wktransportation.entity.Tempertumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/8/22 21:00
 * **********************************
 **/
public class TemperatureAdapter extends BaseAdapter{
    private List<Tempertumer> data = new ArrayList<>();
    private Context context;
    public void setData(List<Tempertumer> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public TemperatureAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        TagHolder tagHolder = null;
        TemperatureHolder temperatureHolder = null;
        int type = 0;
        if (data.get(position).getDatetime() == null){
            type = 1;
        }else {
            type = 2;
        }
        if (convertView == null){
            if (type ==1){
                view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
                tagHolder = new TagHolder();
                tagHolder.textView = view.findViewById(android.R.id.text1);
                view.setTag(tagHolder);
            }else {
                view = LayoutInflater.from(context).inflate(R.layout.temperature_list_item,null);
                temperatureHolder = new TemperatureHolder();
                temperatureHolder.textView1 = view.findViewById(R.id.text1);
                temperatureHolder.textView2 = view.findViewById(R.id.text2);
                view.setTag(temperatureHolder);
            }
        }else {
            if (type == 1){
                if (convertView.getTag() instanceof  TagHolder){
                    tagHolder = (TagHolder) convertView.getTag();
                    view = convertView;
                }else {
                    view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
                    tagHolder = new TagHolder();
                    tagHolder.textView = view.findViewById(android.R.id.text1);
                    view.setTag(tagHolder);
                }

            }else {
                if (convertView.getTag() instanceof TemperatureHolder){
                    temperatureHolder = (TemperatureHolder) convertView.getTag();
                    view = convertView;
                }else {
                    temperatureHolder = new TemperatureHolder();
                    view = LayoutInflater.from(context).inflate(R.layout.temperature_list_item,null);
                    temperatureHolder.textView1 = view.findViewById(R.id.text1);
                    temperatureHolder.textView2 = view.findViewById(R.id.text2);
                    view.setTag(temperatureHolder);
                }

            }

        }
        if (type == 1){
            tagHolder.textView.setText(data.get(position).getIncubatornumber());
        }else {
            temperatureHolder.textView1.setText(data.get(position).getDatetime());
            temperatureHolder.textView2.setText(data.get(position).getTempertumer());
        }
        return view;
    }
    public class TemperatureHolder{
        public TextView textView1;
        public TextView textView2;
    }
    public class TagHolder{
        public TextView textView;
    }
}
