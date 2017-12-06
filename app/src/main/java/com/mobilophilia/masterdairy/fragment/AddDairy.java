package com.mobilophilia.masterdairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.database.EnterNameEntry;
import com.mobilophilia.masterdairy.database.ExpenseBean;
import com.mobilophilia.masterdairy.manager.beans.DairyDetailsBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by mukesh on 10/09/17.
 */

public class AddDairy extends Fragment implements View.OnClickListener {
    private View rootView;
    private DBHelper dbHelper;
    private EditText dairyName;
    private EditText dairyPhone;
    private EditText dairyCareBy;
    private EditText dairyAddress;
    private String errorMessage;
    private Button btnSubmit;


    public AddDairy() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_dairy, container, false);

        dbHelper = new DBHelper(getActivity());
        dairyName = (EditText) rootView.findViewById(R.id.dairy_name);
        dairyPhone = (EditText) rootView.findViewById(R.id.dairy_phone);
        dairyCareBy = (EditText) rootView.findViewById(R.id.dairy_care_taker);
        dairyAddress = (EditText) rootView.findViewById(R.id.dairy_address);


        btnSubmit = (Button) rootView.findViewById(R.id.btn_save_dairy);
        btnSubmit.setOnClickListener(this);


        Util.setBlankAndIsEnable(dairyCareBy, false);
        Util.setBlankAndIsEnable(dairyPhone, false);
        Util.setBlankAndIsEnable(dairyAddress, false);

        Util.iSenableButton(getActivity(), btnSubmit, false);


        dairyName.addTextChangedListener(new TextWatcher() {
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
                if (!Util.isEmpty(dairyName.getText().toString())) {
                    Util.setBlankAndIsEnable(dairyCareBy, true);
                    Util.setBlankAndIsEnable(dairyPhone, false);
                    Util.setBlankAndIsEnable(dairyAddress, false);
                } else {
                    Util.setBlankAndIsEnable(dairyCareBy, false);
                    Util.setBlankAndIsEnable(dairyPhone, false);
                    Util.setBlankAndIsEnable(dairyAddress, false);
                }
            }
        });


        dairyCareBy.addTextChangedListener(new TextWatcher() {
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
                if (!Util.isEmpty(dairyCareBy.getText().toString())) {
                    Util.setBlankAndIsEnable(dairyPhone, true);
                    Util.setBlankAndIsEnable(dairyAddress, false);
                } else {
                    Util.setBlankAndIsEnable(dairyPhone, false);
                    Util.setBlankAndIsEnable(dairyAddress, false);
                }
            }
        });



        dairyPhone.addTextChangedListener(new TextWatcher() {
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

                String phone = dairyPhone.getText().toString().trim();
                if (!Util.isEmpty(phone) && phone.length()>9) {
                    Util.setBlankAndIsEnable(dairyAddress, true);
                }else{
                    Util.iSenableButton(getActivity(), btnSubmit, false);
                    Util.setBlankAndIsEnable(dairyAddress, false);
                }
            }
        });



        dairyAddress.addTextChangedListener(new TextWatcher() {
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
                String phone = dairyPhone.getText().toString().trim();
                if (!Util.isEmpty(phone) && phone.length()>9) {
                    if (!Util.isEmpty(dairyAddress.getText().toString())) {
                        Util.iSenableButton(getActivity(), btnSubmit, true);
                    } else {
                        Util.iSenableButton(getActivity(), btnSubmit, false);
                    }
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_save_dairy:
                if (feildValidation()) {
                    saveData();
                } else {
                    errorMessage(errorMessage);
                }
                break;
            default:
                break;
        }
    }

    private boolean feildValidation() {
        boolean rtn = true;
        String name = dairyName.getText().toString().trim();
        String careBy = dairyCareBy.getText().toString().trim();
        String phone = dairyPhone.getText().toString().trim();
        String address = dairyAddress.getText().toString().trim();

        if (Util.isEmpty(name)) {
            errorMessage = Constants.VALIDATION_DAIRY_NAME;
            rtn = false;
        }
        if (Util.isEmpty(careBy)) {
            errorMessage = Constants.VALIDATION_DAIRY_CARE_BY;
            rtn = false;
        }
        if (Util.isEmpty(phone)) {
            errorMessage = Constants.ERROR_VALIDATION_PHONE;
            rtn = false;
        }

        if (!Util.isValidPhoneNumber(phone)) {
            errorMessage = Constants.ERROR_VALIDATION_PHONE;
            rtn = false;
        }

        if (Util.isEmpty(address)) {
            errorMessage = Constants.VALIDATION_DAIRY_ADDRESS;
            rtn = false;
        }

        return rtn;
    }

    private void saveData() {
        String name = dairyName.getText().toString().trim();
        String careBy = dairyCareBy.getText().toString().trim();
        String phone = dairyPhone.getText().toString().trim();
        String address = dairyAddress.getText().toString().trim();
        DairyDetailsBean bean = new DairyDetailsBean();
        bean.setDairyName(name);
        bean.setDairyCareBy(careBy);
        bean.setDairyPhone(phone);
        bean.setDairyAddress(address);

        DBHelper dbHelper = new DBHelper(getActivity());
        dbHelper.addDairyDatail(bean);

        reSetValues();

    }


    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    private void reSetValues() {
        Util.setBlankAndIsEnable(dairyName, true);
        Util.setBlankAndIsEnable(dairyCareBy, false);
        Util.setBlankAndIsEnable(dairyPhone, false);
        Util.setBlankAndIsEnable(dairyAddress, false);
        Util.iSenableButton(getActivity(), btnSubmit, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

