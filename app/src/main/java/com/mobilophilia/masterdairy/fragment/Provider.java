package com.mobilophilia.masterdairy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.activity.LoginActivity;
import com.mobilophilia.masterdairy.activity.PriceTableActivity;
import com.mobilophilia.masterdairy.adapter.AgentSpinnerAdapter;
import com.mobilophilia.masterdairy.adapter.ProviderAdapter;

import com.mobilophilia.masterdairy.adapter.SpinnerAdapter;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Log;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.database.EnterNameEntry;
import com.mobilophilia.masterdairy.database.MyEntries;
import com.mobilophilia.masterdairy.database.SetPriceEntry;
import com.mobilophilia.masterdairy.manager.AppManager;
import com.mobilophilia.masterdairy.manager.beans.ProviderBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukesh on 12/09/17.
 */

public class Provider extends Fragment implements View.OnClickListener {
    //private RecyclerView recyclerView;
    //private ProviderAdapter mAdapter;
    private DBHelper dbHelper;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;

//    private List<ProviderBean> dairyList = new ArrayList<>();


    public Spinner spDairyName;
    public EditText editLtr, editAvgFat, editAvgSnf;
    public EditText editPrice, editChillingEx, editOtherEx;
    public TextView lvlTotal;
    public TextView noSpinner;
    public Button saveData;
    private SpinnerAdapter spAd;
    private TableLayout tableLayoutView;
    private String dairyName;
    private LinearLayout llLable;


    public Provider() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_provider, container, false);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        tableLayoutView = (TableLayout) rootView.findViewById(R.id.table_view_added);
        llLable = (LinearLayout) rootView.findViewById(R.id.view_av);

        /// RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        spDairyName = (Spinner) rootView.findViewById(R.id.spinner_add_dairy_name);
        editLtr = (EditText) rootView.findViewById(R.id.pro_avt_input_ltr);
        editAvgFat = (EditText) rootView.findViewById(R.id.pro_input_avg_fat);
        editAvgSnf = (EditText) rootView.findViewById(R.id.pro_input_avg_snf);
        editPrice = (EditText) rootView.findViewById(R.id.pro_input_avg_price);
        editChillingEx = (EditText) rootView.findViewById(R.id.pro_input_chilling_ex);
        editOtherEx = (EditText) rootView.findViewById(R.id.pro_input_other_ex);
        lvlTotal = (TextView) rootView.findViewById(R.id.pro_input_total);

        noSpinner = (TextView) rootView.findViewById(R.id.pro_no_value_spinner);

        saveData = (Button) rootView.findViewById(R.id.btn_provider_save);
        saveData.setOnClickListener(this);
        Util.hideKeyboard(getActivity());

        dbHelper = new DBHelper(getActivity());
        if (dbHelper.getDairyNameList(false).size() > 0) {
            noSpinner.setVisibility(View.GONE);
            spDairyName.setVisibility(View.VISIBLE);
        } else {
            noSpinner.setVisibility(View.VISIBLE);
            spDairyName.setVisibility(View.GONE);
        }


        AppManager.getInstance().getLoginManager().getManagerData(getActivity(), new DBHelper(getActivity()).timeStampForManagerData());


        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d("position", "positionposition " + position);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("onLongItemClick", "onLongItemClick " + position);
                    }
                })
        );*/


        spDairyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemSelected", "position--> " + position);
                // spAd.notifyDataSetChanged();
                dairyName = dbHelper.getDairyNameList(false).get(position).getDairyName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("onNothingSelected", "onNothingSelected");
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AppManager.getInstance().getLoginManager().getManagerData(getActivity(), new DBHelper(getActivity()).timeStampForManagerData());
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        editAvgFat.addTextChangedListener(new TextWatcher() {
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
                String clr = editAvgFat.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double fat = Double.parseDouble(clr);
                    if (fat < 1) {
                        Util.setBlankAndIsEnable(editAvgSnf, false);
                        Util.setBlankAndIsEnable(editPrice, false);
                        Util.setBlankAndIsEnable(editChillingEx, false);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    } else {
                        Util.setBlankAndIsEnable(editAvgSnf, true);
                        Util.setBlankAndIsEnable(editPrice, false);
                        Util.setBlankAndIsEnable(editChillingEx, false);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(editAvgSnf, false);
                    Util.setBlankAndIsEnable(editPrice, false);
                    Util.setBlankAndIsEnable(editChillingEx, false);
                    Util.setBlankAndIsEnable(editOtherEx, false);
                    Util.setBlankAndIsEnable(editLtr, false);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });




        editAvgSnf.addTextChangedListener(new TextWatcher() {
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
                String clr = editAvgSnf.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double fat = Double.parseDouble(clr);
                    if (fat < 1) {
                        Util.setBlankAndIsEnable(editPrice, false);
                        Util.setBlankAndIsEnable(editChillingEx, false);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    } else {
                        Util.setBlankAndIsEnable(editPrice, true);
                        Util.setBlankAndIsEnable(editChillingEx, false);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(editPrice, false);
                    Util.setBlankAndIsEnable(editChillingEx, false);
                    Util.setBlankAndIsEnable(editOtherEx, false);
                    Util.setBlankAndIsEnable(editLtr, false);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });

        editPrice.addTextChangedListener(new TextWatcher() {
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
                String clr = editPrice.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double fat = Double.parseDouble(clr);
                    if (fat < 1) {
                        Util.setBlankAndIsEnable(editChillingEx, false);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    } else {
                        Util.setBlankAndIsEnable(editChillingEx, true);
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(editChillingEx, false);
                    Util.setBlankAndIsEnable(editOtherEx, false);
                    Util.setBlankAndIsEnable(editLtr, false);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });

        editChillingEx.addTextChangedListener(new TextWatcher() {
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
                String clr = editChillingEx.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double fat = Double.parseDouble(clr);
                    if (fat < 1) {
                        Util.setBlankAndIsEnable(editOtherEx, false);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    } else {
                        Util.setBlankAndIsEnable(editOtherEx, true);
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(editOtherEx, false);
                    Util.setBlankAndIsEnable(editLtr, false);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });

        editOtherEx.addTextChangedListener(new TextWatcher() {
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
                String clr = editOtherEx.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double fat = Double.parseDouble(clr);
                    if (fat < 1) {
                        Util.setBlankAndIsEnable(editLtr, false);
                        Util.setTextBlank(lvlTotal);
                    } else {
                        Util.setBlankAndIsEnable(editLtr, true);
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(editLtr, false);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });

        editLtr.addTextChangedListener(new TextWatcher() {
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




                String price = editPrice.getText().toString().trim();
                String ltr = editLtr.getText().toString().trim();
                if (!Util.isEmpty(price) && !Util.isEmpty(ltr)) {
                    Double pri = Double.parseDouble(price);
                    Double lt = Double.parseDouble(ltr);
                    if (lt > 1) {
                        String ltrText  = ((TextView) rootView.findViewById(R.id.pro_total_ltr)).getText().toString().trim();
                        Double ltrD = Double.parseDouble(ltrText);

                        if(ltrD>lt){
                            float ltrttl = new DBHelper(getActivity()).getProviderEntryLtr();
                            if(ltrttl<lt){
                                Double total = pri*lt;
                                lvlTotal.setText(""+total);
                            }else{
                                errorMessage("You can enter only "+  Util.round0ffTwoPlace(ltrD-ltrttl) + "or less");
                            }

                        }else{
                            //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                            errorMessage("You can enter only "+ ltrText + " or less");
                        }


                    } else {
                        Util.setTextBlank(lvlTotal);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setTextBlank(lvlTotal);
                }
            }
        });



        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.PROVIDER_DATA_SAVE:
                // providerList.clear();
                //providerList = dbHelper.getProviderEntry();
                // mAdapter = new ProviderAdapter(getActivity(), providerList);
                // recyclerView.setAdapter(mAdapter);
                break;
            case MessageEvent.MANAGER_INFO_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                Util.dismissBarDialog();

                providerData(AppManager.getInstance().getLoginManager().entryProvider);

                dbHelper = new DBHelper(getActivity());
                spAd = new SpinnerAdapter(getActivity(), dbHelper.getDairyNameList(false));
                spDairyName.setAdapter(spAd);

                if (dbHelper.getDairyNameList(false).size() > 0) {
                    noSpinner.setVisibility(View.GONE);
                    spDairyName.setVisibility(View.VISIBLE);
                } else {
                    noSpinner.setVisibility(View.VISIBLE);
                    spDairyName.setVisibility(View.GONE);
                }


                List<ProviderBean> providerList = dbHelper.getProviderEntry();
                if (providerList.size() > 0) {
                    tableLayoutView.removeAllViews();
                    makeTableforEnterData(providerList);
                }
                break;
            case MessageEvent.MANAGER_INFO_FAILURE:
                swipeRefreshLayout.setRefreshing(false);
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;
            case MessageEvent.SERVER_ERROR_OCCURRED:
                swipeRefreshLayout.setRefreshing(false);
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void providerData(List<MyEntries> entryProvider) {

        float fat = 0.0f;
        float snf = 0.0f;
        float clr = 0.0f;
        float price = 0.0f;
        float tltr = 0.0f;
        float tamount = 0.0f;

        if (entryProvider.size() > 0) {

            for (int i = 0; i < entryProvider.size(); i++) {
                fat = fat + Float.parseFloat(entryProvider.get(i).getAvgfatProvider());
                snf = snf + Float.parseFloat(entryProvider.get(i).getAvgsnfProvider());
                clr = clr + Float.parseFloat(entryProvider.get(i).getAvgclrProvider());
                price = price + Float.parseFloat(entryProvider.get(i).getAvgPriceProvider());
                tltr = tltr + Float.parseFloat(entryProvider.get(i).getTotalLtrProvider());
                tamount = tamount + Float.parseFloat(entryProvider.get(i).getTotalProvider());
            }


            ((TextView) rootView.findViewById(R.id.pro_avg_fat)).setText("" + Util.round0ffTwoPlaceF((fat / entryProvider.size())));
            ((TextView) rootView.findViewById(R.id.pro_avg_snf)).setText("" + Util.round0ffTwoPlaceF((snf / entryProvider.size())));
            ((TextView) rootView.findViewById(R.id.pro_avg_clr)).setText("" + Util.round0ffTwoPlaceF((clr / entryProvider.size())));
            ((TextView) rootView.findViewById(R.id.pro_avg_price)).setText("" + Util.round0ffTwoPlaceF((price / entryProvider.size())));
            ((TextView) rootView.findViewById(R.id.pro_total_ltr)).setText("" + Util.round0ffTwoPlaceF(tltr));
            ((TextView) rootView.findViewById(R.id.pro_total_amount)).setText("" + Util.round0ffTwoPlaceF(tamount));


        }
        setDataOnEditText(entryProvider);

        /*TextView avgFat = (TextView) rootView.findViewById(R.id.pro_avg_fat);
        TextView avgSnf = (TextView) rootView.findViewById(R.id.pro_avg_snf);
        TextView avgClr = (TextView) rootView.findViewById(R.id.pro_avg_clr);
        TextView avgPrice = (TextView) rootView.findViewById(R.id.pro_avg_price);
        TextView totalLtr = (TextView) rootView.findViewById(R.id.pro_total_ltr);
        TextView totalAmount = (TextView) rootView.findViewById(R.id.pro_total_amount);*/


    }


    private void makeTableforEnterData(List<ProviderBean> priceList) {

        if (priceList.size() > 0) {
            llLable.setVisibility(View.VISIBLE);
        } else {
            llLable.setVisibility(View.GONE);
        }
        //
        for (int i = 0; i < priceList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row_price, null);
            if (i % 2 == 0) {
                row.setBackgroundColor(getResources().getColor(R.color.back_theme));
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.con_theme));
            }
            final ProviderBean spe = priceList.get(i);

            final TextView lfat = (TextView) row.findViewById(R.id.lv1_one);
            lfat.setText(Util.capitalizeString(spe.getDariyName()));

            final TextView hfat = (TextView) row.findViewById(R.id.lv1_two);
            hfat.setText(spe.getAvgFat());

            final TextView lsnf = (TextView) row.findViewById(R.id.lv1_three);
            lsnf.setText(spe.getAvgSnf());

            final TextView hsnf = (TextView) row.findViewById(R.id.lv1_four);
            hsnf.setText(spe.getAvgPrice());

            final TextView hltr = (TextView) row.findViewById(R.id.lv1_eight);
            hltr.setText(spe.getLtr());

            final TextView fatInterval = (TextView) row.findViewById(R.id.lv1_six);
            fatInterval.setText(spe.getOtherEx());

            final TextView snfInterval = (TextView) row.findViewById(R.id.lv1_seven);
            snfInterval.setText(spe.getTotalPrice());

            final TextView sprice = (TextView) row.findViewById(R.id.lv1_five);
            sprice.setText(spe.getChillingEx());


            tableLayoutView.addView(row);

           /* row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordId = spe.getId();
                    Intent priceIntent = new Intent(getActivity(), PriceTableActivity.class);
                    priceIntent.putExtra(Constants.RECORD_ID_TABLE, recordId);
                    startActivity(priceIntent);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    launchMessageDialog(spe, lfat, hfat, lsnf, hsnf, sprice, fatInterval,snfInterval);
                    return true;
                }
            });*/
        }
    }


    private void setDataOnEditText(List<MyEntries> entryProvider) {


        float fat = 0.0f;
        float snf = 0.0f;
        //float clr = 0.0f;
        float price = 0.0f;
        float tltr = 0.0f;
        float tamount = 0.0f;

        if (entryProvider.size() > 0) {

            for (int i = 0; i < entryProvider.size(); i++) {
                fat = fat + Float.parseFloat(entryProvider.get(i).getAvgfatProvider());
                snf = snf + Float.parseFloat(entryProvider.get(i).getAvgsnfProvider());
                price = price + Float.parseFloat(entryProvider.get(i).getAvgPriceProvider());
                tltr = tltr + Float.parseFloat(entryProvider.get(i).getTotalLtrProvider());
                tamount = tamount + Float.parseFloat(entryProvider.get(i).getTotalProvider());
            }
            editAvgFat.setText("" + Util.round0ffTwoPlaceF((fat / entryProvider.size())));
            editAvgSnf.setText("" + Util.round0ffTwoPlaceF((snf / entryProvider.size())));
            editPrice.setText("" + Util.round0ffTwoPlaceF((price / entryProvider.size())));
        }

        }


    private void saveData() {

        String ltr = editLtr.getText().toString().trim();
        String fat = editAvgFat.getText().toString().trim();
        String snf = editAvgSnf.getText().toString().trim();
        String price = editPrice.getText().toString().trim();
        String chillingEx = editChillingEx.getText().toString().trim();
        String otherEx = editOtherEx.getText().toString().trim();
        //lvlTotal.setText(""+lvlTotal.getText().toString().trim());
        String total = lvlTotal.getText().toString().trim();

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

        resetText();
        List<ProviderBean> providerList = dbHelper.getProviderEntry();
        if (providerList.size() > 0) {
            tableLayoutView.removeAllViews();
            makeTableforEnterData(providerList);
        }

    }

    private void resetText() {

        editLtr.setText("");
        editAvgFat.setText("");
        editAvgSnf.setText("");
        editPrice.setText("");
        editChillingEx.setText("");
        editOtherEx.setText("");
        lvlTotal.setText("");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_provider_save:
                saveData();
                break;
            case R.id.btn_cancel:
                Util.hideKeyboard(getActivity());
                // reSetField();
                break;

            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

}
