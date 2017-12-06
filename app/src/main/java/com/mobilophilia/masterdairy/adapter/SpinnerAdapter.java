package com.mobilophilia.masterdairy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.manager.beans.DairyDetailsBean;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by mukesh on 19/09/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    private List<DairyDetailsBean> data;
    private FragmentActivity activity;
    private LayoutInflater inflater;

    TextView dairyName;

    public SpinnerAdapter(FragmentActivity activity, List<DairyDetailsBean> data) {
        this.activity = activity;
        this.data = data;
        this.inflater = LayoutInflater.from(activity);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, null, false);
            holder = new ViewHolder();

            holder.dairyName = (TextView) convertView.findViewById(R.id.sp_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DairyDetailsBean dataSet = data.get(position);

        holder.dairyName.setText("" + dataSet.getDairyName());
        return convertView;
    }

    static class ViewHolder {
        TextView dairyName;

    }
}