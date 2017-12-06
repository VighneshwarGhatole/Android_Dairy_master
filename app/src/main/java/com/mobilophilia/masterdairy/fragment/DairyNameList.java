package com.mobilophilia.masterdairy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.activity.UpdateDairyName;
import com.mobilophilia.masterdairy.activity.UpdateName;
import com.mobilophilia.masterdairy.adapter.DairyNameListAdapter;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.RecyclerItemClickListener;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.manager.beans.DairyDetailsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukesh on 12/09/17.
 */

public class DairyNameList extends Fragment {

    private List<DairyDetailsBean> dairyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DairyNameListAdapter mAdapter;
    private DBHelper dbHelper;

    public DairyNameList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dairy_name_list, container, false);
        dbHelper = new DBHelper(getActivity());
        dairyList.clear();
        dairyList = dbHelper.getDairyList();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_dairy_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DairyNameListAdapter(dairyList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (dairyList.get(position).getContentType() != 0) {
                            Intent priceIntent = new Intent(getActivity(), UpdateDairyName.class);
                            priceIntent.putExtra(Constants.RECORD_ID, dairyList.get(position).getId());
                            startActivityForResult(priceIntent, Constants.ACTIVITY_RESULT_CODE_DAIRY_NAME);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (dairyList.get(position).getContentType() != 0) {
                            launchMessageDialog(dairyList.get(position).getId());
                        }
                    }
                })
        );




        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        dairyList.clear();
        dairyList = dbHelper.getDairyList();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DairyNameListAdapter(dairyList);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void launchMessageDialog(final int id) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_update_delete_price_set);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCancelable(true);

        TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText(Constants.DIALOG_MSG_DELETE_NAME);

        Button btnNo = (Button) dialog.findViewById(R.id.btn_update);
        btnNo.setText(Constants.DIALOG_BUTTON_TEXT_NO);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnYes = (Button) dialog.findViewById(R.id.btn_delete);
        btnYes.setText(Constants.DIALOG_BUTTON_TEXT_YES);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteDairy(id);
                Toast.makeText(getActivity(), Constants.TOAST_MSG_SUCCES_NAME_DELETE, Toast.LENGTH_LONG).show();
                dairyList.clear();
                dairyList = dbHelper.getDairyList();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new DairyNameListAdapter(dairyList);
                recyclerView.setAdapter(mAdapter);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

}


