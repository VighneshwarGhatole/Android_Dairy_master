package com.mobilophilia.masterdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.database.EnterNameEntry;
import com.mobilophilia.masterdairy.manager.beans.DairyDetailsBean;
import com.mobilophilia.masterdairy.timer.TimeoutActivity;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mukesh on 12/09/17.
 */

public class UpdateDairyName extends TimeoutActivity implements View.OnClickListener {

    private DBHelper dbHelper;
    private EditText dairyName;
    private EditText dairyPhone;
    private EditText dairyCareBy;
    private EditText dairyAddress;
    private String errorMessage;
    private Button btnSubmit;
    private Button btnCancel;
    private int recordId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_update_dairy_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(UpdateDairyName.this);
        dairyName = (EditText)findViewById(R.id.dairy_name);
        dairyPhone = (EditText) findViewById(R.id.dairy_phone);
        dairyCareBy = (EditText) findViewById(R.id.dairy_care_taker);
        dairyAddress = (EditText)findViewById(R.id.dairy_address);


        btnSubmit = (Button) findViewById(R.id.btn_update_dairy_name);
        btnSubmit.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel_dairy);
        btnCancel.setOnClickListener(this);


        Util.setBlankAndIsEnable(dairyCareBy, true);
        Util.setBlankAndIsEnable(dairyPhone, true);
        Util.setBlankAndIsEnable(dairyAddress, true);

        Util.iSenableButton(UpdateDairyName.this, btnSubmit, true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getInt(Constants.RECORD_ID);
            DairyDetailsBean nameBean = dbHelper.getDairyDetail(recordId);
            dairyName.setText(""+nameBean.getDairyName());
            dairyCareBy.setText(nameBean.getDairyCareBy());
            dairyPhone.setText(nameBean.getDairyPhone());
            dairyAddress.setText(nameBean.getDairyAddress());
        }


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
                if (!Util.isEmpty(dairyPhone.getText().toString())) {
                    Util.setBlankAndIsEnable(dairyAddress, true);
                } else {
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
                if (!Util.isEmpty(dairyAddress.getText().toString())) {
                    Util.iSenableButton(UpdateDairyName.this, btnSubmit, true);
                } else {
                    Util.iSenableButton(UpdateDairyName.this, btnSubmit, false);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UpdateDairyName.this.finish();
        }
        return super.onOptionsItemSelected(item);
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

        DBHelper dbHelper = new DBHelper(UpdateDairyName.this);
        dbHelper.updateDairy(bean);


    }





    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_update_dairy_name:
                Util.hideKeyboard(this);
                if(feildValidation()){
                    saveData();
                    errorMessage("Name has been updated successfully.");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",Constants.ACTIVITY_RESULT_CODE_DAIRY_NAME);
                    setResult(Activity.RESULT_OK,returnIntent);
                    UpdateDairyName.this.finish();
                }else{
                    errorMessage(errorMessage);
                }
                break;
            case R.id.btn_cancel_dairy:
                finish();
                break;
            default:
                break;
        }
    }



    private void errorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onTimeout() {
        new MainActivity().autologout();
    }

    @Override
    protected long getTimeoutInSeconds() {
        return Constants.TIMER;
    }
}
