package com.mobilophilia.masterdairy.adapter;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.manager.beans.AgentBean;

import java.util.List;

/**
 * Created by mukesh on 27/09/17.
 */

public class AgentSpinnerAdapter extends BaseAdapter {
    private List<AgentBean> data;
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private String AgentId;


    public AgentSpinnerAdapter(FragmentActivity activity, List<AgentBean> data) {
        this.activity = activity;
        this.data = data;
        this.inflater = LayoutInflater.from(activity);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.SP_MY_DAIRY_MK, activity.MODE_PRIVATE);
        AgentId= sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);

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
        AgentSpinnerAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, null, false);
            holder = new AgentSpinnerAdapter.ViewHolder();

            holder.agentName = (TextView) convertView.findViewById(R.id.sp_item);

            convertView.setTag(holder);
        } else {
            holder = (AgentSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        AgentBean dataSet = data.get(position);

        holder.agentName.setText(Util.capitalizeString(AgentId)+ "-" + Util.capitalizeString(dataSet.getFname()));
        return convertView;
    }

    static class ViewHolder {
        TextView agentName;

    }
}