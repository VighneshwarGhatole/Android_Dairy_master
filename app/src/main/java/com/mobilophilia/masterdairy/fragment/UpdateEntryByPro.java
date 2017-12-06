package com.mobilophilia.masterdairy.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.adapter.AgentSpinnerAdapter;
import com.mobilophilia.masterdairy.adapter.SpinnerAdapter;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Log;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.database.MyEntries;
import com.mobilophilia.masterdairy.database.SetPriceEntry;
import com.mobilophilia.masterdairy.manager.AppManager;
import com.mobilophilia.masterdairy.manager.beans.UpdateEntryBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mukesh on 20/09/17.
 */

public class UpdateEntryByPro extends Fragment implements View.OnClickListener {

    private View rootView;
    private DBHelper dbHelper;
    private EditText inputCode;
    private EditText inputClr;
    private EditText inputFat;
    private EditText inputPrice;
    private EditText inputLtr;
    private TextView inputTotal;
    private String errorMessage;
    private Button btnSubmit;
    private String selectedAgentId;
    private int timeInterval = 0;
    private TextView calPrice;
    private TextView reportOnlyDate;


    private int mStartYear;
    private int mStartMonth;
    private int mStartDay;
    private Spinner mySpinnerTime;
    private Spinner agentSpinner;
    private AgentSpinnerAdapter spAd;

    public UpdateEntryByPro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_entry_bypro, container, false);


        dbHelper = new DBHelper(getActivity());

        inputCode = (EditText) rootView.findViewById(R.id.input_code);
        inputClr = (EditText) rootView.findViewById(R.id.input_clr);
        inputFat = (EditText) rootView.findViewById(R.id.input_fat);
        inputPrice = (EditText) rootView.findViewById(R.id.input_price);
        inputLtr = (EditText) rootView.findViewById(R.id.input_ltr);
        inputTotal = (TextView) rootView.findViewById(R.id.input_total);
        calPrice = (TextView) rootView.findViewById(R.id.cal_price);

        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        reportOnlyDate = (TextView) rootView.findViewById(R.id.input_date_select);
        reportOnlyDate.setOnClickListener(this);

        Util.setBlankAndIsEnable(inputCode, true);
        Util.setBlankAndIsEnable(inputClr, false);
        Util.setBlankAndIsEnable(inputFat, false);
        Util.setBlankAndIsEnable(inputPrice, false);
        Util.setBlankAndIsEnable(inputLtr, false);
        Util.iSenableButton(getActivity(), btnSubmit, false);

        inputCode.addTextChangedListener(new MyTextWatcher(inputCode));
        inputClr.addTextChangedListener(new MyTextWatcher(inputClr));
        inputFat.addTextChangedListener(new MyTextWatcher(inputFat));
        inputPrice.addTextChangedListener(new MyTextWatcher(inputPrice));
        inputLtr.addTextChangedListener(new MyTextWatcher(inputLtr));


        mySpinnerTime = (Spinner) rootView.findViewById(R.id.spinner_time);
        ArrayAdapter<String> adapterMe = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.sp_item, Constants.timing);
        mySpinnerTime.setAdapter(adapterMe);
        mySpinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeInterval = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        agentSpinner = (Spinner) rootView.findViewById(R.id.spinner_agent);
        spAd = new AgentSpinnerAdapter(getActivity(), AppManager.getInstance().getLoginManager().AgentList);
        agentSpinner.setAdapter(spAd);

        agentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedAgentId = AppManager.getInstance().getLoginManager().AgentList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }


    private void reSetValues() {
        Util.setBlankAndIsEnable(inputCode, false);
        Util.setBlankAndIsEnable(inputClr, false);
        Util.setBlankAndIsEnable(inputFat, false);
        Util.setBlankAndIsEnable(inputLtr, false);
        Util.setTextBlank(reportOnlyDate);
        Util.setTextBlank(inputTotal);
        Util.setTextBlank(calPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_submit:
                if (feildValidation()) {
                    String date = reportOnlyDate.getText().toString().trim();
                    if (!Util.isEmpty(date)) {
                        saveMyEntry();
                    } else {
                        errorMessage = Constants.SELECT_DATE;
                        errorMessage(errorMessage);
                    }
                } else {
                    errorMessage = Constants.FIELD_MAINDATORY;
                    errorMessage(errorMessage);
                }
                break;
            case R.id.input_date_select:
                getReportDate(reportDateSetListener);
                break;
            default:
                break;
        }
    }

    private void saveMyEntry() {
        Util.hideKeyboard(getActivity());
        //Util.launchBarDialog(getActivity(), Constants.PROGRESS_DIALOG_ENTRY);

        UpdateEntryBean updateEntry = new UpdateEntryBean();
        String code = inputCode.getText().toString().trim();
        String clr = inputClr.getText().toString().trim();
        String fat = inputFat.getText().toString().trim();
        String price = inputPrice.getText().toString().trim();
        String ltr = inputLtr.getText().toString().trim();
        String totalAmount = inputTotal.getText().toString().trim();

        updateEntry.setUpdateCode(code);
        updateEntry.setUpdateClr(clr);
        updateEntry.setUpdateFat(fat);
        updateEntry.setUpdatePrice(price);
        updateEntry.setUpdatedSnf("5.6");
        updateEntry.setUpdateLtr(ltr);
        updateEntry.setAgentId(selectedAgentId);
        updateEntry.setUpdateTotal(totalAmount);
        updateEntry.setUpdateTimeInterval(timeInterval);
        // updateEntry.setUpdateTimeInterval(Util.getTime());
        updateEntry.setCurrentTime(reportOnlyDate.getText().toString());

        AppManager.getInstance().getEntryManager().editEntry(getActivity(), updateEntry);
    }


    private void getReportDate(DatePickerDialog.OnDateSetListener dateListenee) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog reporDialog = new DatePickerDialog(getActivity(), dateListenee, year, month, day);
        reporDialog.getDatePicker().setMaxDate(new Date().getTime());
        reporDialog.show();
    }

    private void updateReportDateDisplay() {
        mStartMonth = mStartMonth + 1;
        String dates = "";
        if (mStartDay < 10) {
            dates = "0" + mStartDay + "-";
        } else {
            dates = "" + mStartDay + "-";
        }
        if (mStartMonth < 10) {
            dates = dates + "0" + mStartMonth + "-" + mStartYear;
        } else {
            dates = dates + "" + mStartMonth + "-" + mStartYear;
        }
        reportOnlyDate.setText(dates);
        Util.setBlankAndIsEnable(inputCode, true);
    }

    private DatePickerDialog.OnDateSetListener reportDateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mStartYear = year;
                    mStartMonth = monthOfYear;
                    mStartDay = dayOfMonth;
                    updateReportDateDisplay();
                }
            };


    private boolean feildValidation() {
        boolean rtn = true;
        if (Util.isEmpty(inputCode.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CODE;
            rtn = false;
        } else if (Util.isEmpty(inputClr.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CLR;
            rtn = false;
        } else if (Util.isEmpty(inputFat.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_FAT;
            rtn = false;
        } else if (Util.isEmpty(inputLtr.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_LTR;
            rtn = false;
        }
        return rtn;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.ENTRY_SUCCESS:
                Util.dismissBarDialog();
                reSetValues();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;
            case MessageEvent.ENTRY_FAILURE:
                Util.dismissBarDialog();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;
            case MessageEvent.NETWORK_TIME_OUT:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;

            case MessageEvent.SERVER_ERROR_OCCURRED:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    // Costom class TextWatcher for All editfield

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_code:
                    validateCode();
                    break;
                case R.id.input_clr:
                    validateCLR();
                    break;
                case R.id.input_fat:
                    validateFat();
                    break;
                case R.id.input_price:
                    validatePrice();
                    break;
                case R.id.input_ltr:
                    validateLtr();
                    break;
            }
        }
    }


    private void validateCode() {
        if (!Util.isEmpty(inputCode.getText().toString())) {
            Util.setBlankAndIsEnable(inputClr, true);
            Util.setBlankAndIsEnable(inputFat, false);
            Util.setBlankAndIsEnable(inputPrice, false);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
        } else {
            Util.setBlankAndIsEnable(inputClr, false);
            Util.setBlankAndIsEnable(inputFat, false);
            Util.setBlankAndIsEnable(inputFat, false);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            //errorMessage = Constants.ERROR_CODE_INVALID_BLANK;
            //errorMessage(errorMessage);
        }
    }

    private void validateCLR() {

        String clr = inputClr.getText().toString().trim();
        if (!Util.isEmpty(clr)) {
            Double lclr = Double.parseDouble(clr);
            if (lclr < 1) {
                errorMessage = Constants.ERROR_VALIDATION_CLR;
                errorMessage(errorMessage);
                Util.setBlankAndIsEnable(inputFat, false);
                Util.setBlankAndIsEnable(inputPrice, false);
                Util.setBlankAndIsEnable(inputLtr, false);
                Util.setTextBlank(inputTotal);
                Util.iSenableButton(getActivity(), btnSubmit, false);
            } else {
                Util.setBlankAndIsEnable(inputFat, true);
                Util.setBlankAndIsEnable(inputLtr, false);
                Util.setBlankAndIsEnable(inputPrice, false);
                Util.setTextBlank(inputTotal);
                Util.setTextBlank(calPrice);
                Util.iSenableButton(getActivity(), btnSubmit, false);
            }
        } else {
            Util.setBlankAndIsEnable(inputFat, false);
            Util.setBlankAndIsEnable(inputPrice, false);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            Util.iSenableButton(getActivity(), btnSubmit, false);
        }
    }

    private void validateFat() {
        if (!Util.isEmpty(inputFat.getText().toString())) {
            Util.setBlankAndIsEnable(inputPrice, true);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            Util.iSenableButton(getActivity(), btnSubmit, false);
        } else {
            Util.setBlankAndIsEnable(inputPrice, false);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            Util.iSenableButton(getActivity(), btnSubmit, false);
        }
    }

    private void validatePrice() {
        if (!Util.isEmpty(inputPrice.getText().toString())) {
            Util.setBlankAndIsEnable(inputLtr, true);
            Util.setTextBlank(inputTotal);
            Util.iSenableButton(getActivity(), btnSubmit, false);
        } else {
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            Util.iSenableButton(getActivity(), btnSubmit, false);
        }
    }

    private void validateLtr() {
        if (!Util.isEmpty(inputLtr.getText().toString())) {
            String ltr = inputLtr.getText().toString().trim();
            String price = inputPrice.getText().toString().trim();
            if (!Util.isEmpty(price)) {
                Double valueLtr = Double.parseDouble(ltr);
                Double valuePrice = Double.parseDouble(price);
                Double total = valueLtr * valuePrice;
                inputTotal.setText("" + total);
                Util.iSenableButton(getActivity(), btnSubmit, true);
            }

        } else {
            Util.iSenableButton(getActivity(), btnSubmit, false);
            Util.setTextBlank(inputTotal);
        }
    }


}
