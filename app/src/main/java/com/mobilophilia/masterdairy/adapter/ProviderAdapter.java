package com.mobilophilia.masterdairy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Log;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.manager.AppManager;
import com.mobilophilia.masterdairy.manager.beans.AgentBean;
import com.mobilophilia.masterdairy.manager.beans.ProviderBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by mukesh on 13/09/17.
 */

public class ProviderAdapter extends RecyclerView.Adapter {


    private AgentSpinnerAdapter spAd;
    private List<ProviderBean> providerData;
    private Context context;
    public static final int DETAIL_TYPE = 0;
    public static final int ADD_DETAIL_TYPE = 1;
    public static final int SHOW_DETAIL_TYPE = 2;
    public static final int SHOW_LEVEL_TYPE = 3;
    private DBHelper dbHelper;
    private String dairyName;

    public ProviderAdapter(Context context, List<ProviderBean> dataSet) {
        this.providerData = dataSet;
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (providerData != null) {
            ProviderBean object = providerData.get(position);
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
            case DETAIL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_provider_dairy_detail, parent, false);
                return new ProviderAdapter.DetailViewHolder(view);
            case ADD_DETAIL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_provider_add_detail, parent, false);
                return new ProviderAdapter.AddDetailViewHolder(view);
            case SHOW_DETAIL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_price, parent, false);
                return new ProviderAdapter.ShowDetailViewHolder(view);
            case SHOW_LEVEL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_provider_leval, parent, false);
                return new ProviderAdapter.LevelViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ProviderBean object = providerData.get(position);
        if (object != null) {
            switch (object.getContentType()) {
                case DETAIL_TYPE:
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyName.setText("Kapila dairy");
                    /*((ProviderAdapter.DetailViewHolder) holder).detailsDairyLtr.setText(""+object.getLtr());
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyAvgFat.setText(""+object.getAvgFat());
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyAvgSnf.setText(""+object.getAvgSnf());
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyAvgPrice.setText(""+object.getAvgPrice());
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyClr.setText(""+object.getAvgClr());
                    ((ProviderAdapter.DetailViewHolder) holder).detailsDairyTotalPrice.setText(""+object.getTotalPrice());*/
                    break;
                case ADD_DETAIL_TYPE:
                    Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgFat, false);
                    Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf, false);
                    Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                    Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                    Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                    Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);

                    ((ProviderAdapter.AddDetailViewHolder) holder).spDairyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("onItemSelected", "position--> " + position);
                            spAd.notifyDataSetChanged();
                            dairyName = AppManager.getInstance().getLoginManager().AgentList.get(position).getFname();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d("onNothingSelected", "onNothingSelected");
                        }
                    });

                    ((AddDetailViewHolder) holder).saveData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("setOnClickListener", "setOnClickListener--> ");
                            String ltr = ((AddDetailViewHolder) holder).editLtr.getText().toString().trim();
                            String fat = ((AddDetailViewHolder) holder).editAvgFat.getText().toString().trim();
                            String snf = ((AddDetailViewHolder) holder).editAvgSnf.getText().toString().trim();
                            String price = ((AddDetailViewHolder) holder).editPrice.getText().toString().trim();
                            String chillingEx = ((AddDetailViewHolder) holder).editChillingEx.getText().toString().trim();
                            String otherEx = ((AddDetailViewHolder) holder).editOtherEx.getText().toString().trim();
                            ((AddDetailViewHolder) holder).lvlTotal.setText("122");
                            String total = ((AddDetailViewHolder) holder).lvlTotal.getText().toString().trim();

                            ProviderBean bean = new ProviderBean();
                            bean.setDariyName(dairyName);
                            bean.setLtr(ltr);
                            bean.setAvgFat(fat);
                            bean.setAvgSnf(snf);
                            bean.setAvgPrice(price);
                            bean.setChillingEx(chillingEx);
                            bean.setOtherEx(otherEx);
                            bean.setTotalPrice(total);
                            dbHelper.addProviderEntry(bean);

                            ((AddDetailViewHolder) holder).editLtr.setText("");
                            ((AddDetailViewHolder) holder).editAvgFat.setText("");
                            ((AddDetailViewHolder) holder).editAvgSnf.setText("");
                            ((AddDetailViewHolder) holder).editPrice.setText("");
                            ((AddDetailViewHolder) holder).editChillingEx.setText("");
                            ((AddDetailViewHolder) holder).editOtherEx.setText("");
                            ((ProviderAdapter.AddDetailViewHolder) holder).lvlTotal.setText("");
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.PROVIDER_DATA_SAVE));
                        }
                    });


                    ((ProviderAdapter.AddDetailViewHolder) holder).editLtr.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            String ltr = ((ProviderAdapter.AddDetailViewHolder) holder).editLtr.getText().toString().trim();
                            if (!Util.isEmpty(ltr)) {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgFat, true);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            } else {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgFat, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }

                        }
                    });

                    ((ProviderAdapter.AddDetailViewHolder) holder).editAvgFat.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            String fat = ((ProviderAdapter.AddDetailViewHolder) holder).editAvgFat.getText().toString().trim();
                            if (!Util.isEmpty(fat)) {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf, true);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            } else {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }

                        }
                    });

                    ((AddDetailViewHolder) holder).editAvgSnf.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String snf = ((ProviderAdapter.AddDetailViewHolder) holder).editAvgSnf.getText().toString().trim();
                            if (!Util.isEmpty(snf)) {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, true);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            } else {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editPrice, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }
                        }
                    });


                    ((ProviderAdapter.AddDetailViewHolder) holder).editPrice.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String price = ((ProviderAdapter.AddDetailViewHolder) holder).editPrice.getText().toString().trim();
                            if (!Util.isEmpty(price)) {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, true);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            } else {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx, false);
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }
                        }
                    });

                    ((AddDetailViewHolder) holder).editChillingEx.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String chillingPrice = ((ProviderAdapter.AddDetailViewHolder) holder).editChillingEx.getText().toString().trim();
                            if (!Util.isEmpty(chillingPrice)) {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, true);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            } else {
                                Util.setBlankAndIsEnable(((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx, false);
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }
                        }
                    });

                    ((AddDetailViewHolder) holder).editOtherEx.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String otherEx = ((ProviderAdapter.AddDetailViewHolder) holder).editOtherEx.getText().toString().trim();
                            if (!Util.isEmpty(otherEx)) {
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, true);
                            } else {
                                Util.iSenableButton(context, ((ProviderAdapter.AddDetailViewHolder) holder).saveData, false);
                            }
                        }
                    });

                    break;
                case SHOW_DETAIL_TYPE:
                    ((ProviderAdapter.ShowDetailViewHolder) holder).dairyName.setText("" + Util.capitalizeString(object.getDariyName()));
                    ((ProviderAdapter.ShowDetailViewHolder) holder).litre.setText("" + object.getLtr());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).avgFat.setText("" + object.getAvgFat());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).avgSnf.setText("" + object.getAvgSnf());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).price.setText("" + object.getAvgPrice());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).chillingEx.setText("" + object.getChillingEx());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).otherEx.setText("" + object.getOtherEx());
                    ((ProviderAdapter.ShowDetailViewHolder) holder).total.setText("" + object.getTotalPrice());
                    break;
                case SHOW_LEVEL_TYPE:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return providerData.size();
    }

    public static class ShowDetailViewHolder extends RecyclerView.ViewHolder {

        public TextView dairyName, litre, avgFat, avgSnf, price, chillingEx, otherEx, total;


        public ShowDetailViewHolder(View itemView) {
            super(itemView);

            dairyName = (TextView) itemView.findViewById(R.id.lv1_one);
            litre = (TextView) itemView.findViewById(R.id.lv1_two);
            avgFat = (TextView) itemView.findViewById(R.id.lv1_three);
            avgSnf = (TextView) itemView.findViewById(R.id.lv1_four);
            price = (TextView) itemView.findViewById(R.id.lv1_five);
            chillingEx = (TextView) itemView.findViewById(R.id.lv1_six);
            otherEx = (TextView) itemView.findViewById(R.id.lv1_seven);
           // total = (TextView) itemView.findViewById(R.id.lv1_eight);
        }
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {

        public LevelViewHolder(View itemView) {
            super(itemView);

        }
    }


    public static class DetailViewHolder extends RecyclerView.ViewHolder {

        public TextView detailsDairyName, detailsDairyLtr, detailsDairyAvgFat;
        public TextView detailsDairyAvgSnf, detailsDairyAvgPrice, detailsDairyClr, detailsDairyTotalPrice;

        public DetailViewHolder(View itemView) {
            super(itemView);
            detailsDairyName = (TextView) itemView.findViewById(R.id.pro_dairy_name);
            detailsDairyLtr = (TextView) itemView.findViewById(R.id.pro_dairy_ltr);
            detailsDairyAvgFat = (TextView) itemView.findViewById(R.id.pro_dairy_avg_fat);
            detailsDairyAvgSnf = (TextView) itemView.findViewById(R.id.pro_dairy_avg_snf);
            detailsDairyAvgPrice = (TextView) itemView.findViewById(R.id.pro_dairy_avg_price);
            detailsDairyClr = (TextView) itemView.findViewById(R.id.pro_dairy_clr);
            detailsDairyTotalPrice = (TextView) itemView.findViewById(R.id.pro_dairy_total_price);
        }
    }

    public class AddDetailViewHolder extends RecyclerView.ViewHolder {
        public Spinner spDairyName;
        public EditText editLtr, editAvgFat, editAvgSnf;
        public EditText editPrice, editChillingEx, editOtherEx;
        public TextView lvlTotal;
        public Button saveData;

        public AddDetailViewHolder(View itemView) {
            super(itemView);

            spDairyName = (Spinner) itemView.findViewById(R.id.spinner_add_dairy_name);
            editLtr = (EditText) itemView.findViewById(R.id.pro_input_litre);
            editAvgFat = (EditText) itemView.findViewById(R.id.pro_input_avg_fat);
            editAvgSnf = (EditText) itemView.findViewById(R.id.pro_input_avg_snf);
            editPrice = (EditText) itemView.findViewById(R.id.pro_input_price);
            editChillingEx = (EditText) itemView.findViewById(R.id.pro_input_chilling_ex);
            editOtherEx = (EditText) itemView.findViewById(R.id.pro_input_other_ex);
            lvlTotal = (TextView) itemView.findViewById(R.id.pro_input_chilling_ex);

            saveData = (Button) itemView.findViewById(R.id.btn_provider_save);


             spAd = new AgentSpinnerAdapter((FragmentActivity) context,AppManager.getInstance().getLoginManager().AgentList);
            spDairyName.setAdapter(spAd);


        }
    }


}
