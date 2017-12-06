package com.mobilophilia.masterdairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.manager.beans.DairyDetailsBean;

import java.util.List;

/**
 * Created by mukesh on 12/09/17.
 */

public class DairyNameListAdapter extends RecyclerView.Adapter {


    private List<DairyDetailsBean> namesList;
    public static final int HEADER_TYPE = 0;
    public static final int CONTENT_TYPE = 1;

    public DairyNameListAdapter(List<DairyDetailsBean> dataSet) {
        this.namesList = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (namesList != null) {
            DairyDetailsBean object = namesList.get(position);
            if (object != null) {
                return object.getContentType();
            }
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_row, parent, false);
                return new DairyNameListAdapter.HeaderViewHolder(view);
            case CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dairy_name_list, parent, false);
                return new DairyNameListAdapter.ContentViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DairyDetailsBean object = namesList.get(position);
        if (object != null) {
            switch (object.getContentType()) {
                case HEADER_TYPE:
                    ((DairyNameListAdapter.HeaderViewHolder) holder).tvHeaderText.setText(object.getHeaderText());
                    break;
                case CONTENT_TYPE:
                    ((DairyNameListAdapter.ContentViewHolder) holder).dairyName.setText("" + Util.capitalizeString(object.getDairyName()));
                    ((DairyNameListAdapter.ContentViewHolder) holder).dairyCareBy.setText("" + Util.capitalizeString(object.getDairyCareBy()));
                    ((DairyNameListAdapter.ContentViewHolder) holder).dairyPhone.setText("" + object.getDairyPhone());
                    ((DairyNameListAdapter.ContentViewHolder) holder).dairyAddress.setText("" + object.getDairyAddress());
                    ((DairyNameListAdapter.ContentViewHolder) holder).timeStamp.setText("" + object.getHeaderText());
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderText = (TextView) itemView.findViewById(R.id.tv_group_by);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView dairyName, dairyCareBy, dairyPhone, dairyAddress, timeStamp;

        public ContentViewHolder(View view) {
            super(view);

            dairyName = (TextView) view.findViewById(R.id.dairy_name_tv);
            dairyCareBy = (TextView) view.findViewById(R.id.dairy_care_by_tv);
            dairyPhone = (TextView) view.findViewById(R.id.dairy_phone_tv);
            dairyAddress = (TextView) view.findViewById(R.id.dairy_address_tv);
            timeStamp = (TextView) view.findViewById(R.id.time_stamp_tv);
        }
    }
}
