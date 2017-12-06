package com.mobilophilia.masterdairy.activity;

/**
 * Created by yogen on 12-07-2017.
 */

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.masterdairy.R;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Log;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.DBHelper;
import com.mobilophilia.masterdairy.database.EnterNameEntry;
import com.mobilophilia.masterdairy.drawer.FragmentDrawer;
import com.mobilophilia.masterdairy.fragment.AddEntry;
import com.mobilophilia.masterdairy.fragment.DairyDetails;
import com.mobilophilia.masterdairy.fragment.EnterName;
import com.mobilophilia.masterdairy.fragment.Entry;
import com.mobilophilia.masterdairy.fragment.Expense;
import com.mobilophilia.masterdairy.fragment.MenuReport;
import com.mobilophilia.masterdairy.fragment.Provider;
import com.mobilophilia.masterdairy.fragment.SetPriceFragment;
import com.mobilophilia.masterdairy.fragment.UpdateEntryByPro;
import com.mobilophilia.masterdairy.timer.TimeoutActivity;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends TimeoutActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private List<EnterNameEntry> customerList;

    private Toolbar mToolbar;
    private ImageView dwlCode;
    private FragmentDrawer drawerFragment;
   // private ImageView syncdata;
    private boolean isSync = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.add_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dwlCode = (ImageView) mToolbar.findViewById(R.id.ic_dwl);
       // syncdata = (ImageView) mToolbar.findViewById(R.id.sync_btn);


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
        getPermission();

        Util.createAppFolder(getAgentId());

        dwlCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerList.size() > 0) {
                    String path = Util.createAppFolder(getAgentId());
                    Util.createCodeListPDF(MainActivity.this, customerList, path);
                }
            }
        });
       /* if (isSync) {
            syncdata.setVisibility(View.VISIBLE);
        } else {
            syncdata.setVisibility(View.GONE);
        }

        syncdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBHelper(MainActivity.this).synchDataServer(MainActivity.this);
            }
        });*/


    }

    private String getAgentId() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        return id;
    }


    private void updateSyncTime(String time, boolean isSync) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isSync) {
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, isSync);
        } else {
            editor.putString(Constants.SP_SYNC_TIME_KEY, time);
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, isSync);
        }
        editor.apply();
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new Entry();
                title = getString(R.string.title_entry);
                break;
            case 1:
                fragment = new DairyDetails();
                title = getString(R.string.title_dairy_detail);
                break;
            case 2:
                fragment = new EnterName();
                title = getString(R.string.title_name);
                break;
            case 3:
                fragment = new Provider();
                title = getString(R.string.title_provider);
                break;
            case 4:
                fragment = new UpdateEntryByPro();
                title = getString(R.string.title_update_entry);
                break;
            case 5:
                fragment = new Expense();
                title = getString(R.string.title_expenses);
                break;
            case 6:
                fragment = new SetPriceFragment();
                title = getString(R.string.title_price);
                break;
            case 7:
                fragment = new MenuReport();
                title = getString(R.string.title_report);
                break;
            case 8:
                logoutCleanUp();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
        downLoadBtn(position);
    }

    private void downLoadBtn(int on) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        customerList = new ArrayList<>();
        customerList = dbHelper.getEnteredNameListForReport();
        if (on == 0 && customerList.size() > 0) {
            dwlCode.setVisibility(View.VISIBLE);
        } else {
            dwlCode.setVisibility(View.GONE);
        }
    }

    public void logoutCleanUp() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        isSync = false;
        if (isSync) {
            Toast.makeText(getApplicationContext(), getString(R.string.logout_msg), Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.SP_ID_KEY, 0);
            editor.putString(Constants.SP_AGENT_TOKEN_KEY, "");
            editor.putString(Constants.SP_AGENT_ID_KEY, "");
            editor.putString(Constants.SP_AGENT_NAME_KEY, "");
            editor.putString(Constants.SP_AGENT_PHONE_KEY, "");
            editor.putString(Constants.SP_AGENT_EMAIL_KEY, "");
            editor.putString(Constants.SP_AGENT_ADDRESS_KEY, "");
            editor.putString(Constants.SP_SYNC_TIME_KEY, "");
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, false);
            editor.apply();
           //  new DBHelper(this).deletedatabase(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void autologout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_CREATE_APP_FOLDER);
            }
        }
    }


    @Override
    protected void onTimeout() {
        autologout();
    }

    @Override
    protected long getTimeoutInSeconds() {
        return Constants.TIMER;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.SYNC_ICON_ON:
               // syncdata.setVisibility(View.VISIBLE);
                updateSyncTime("", true);
                break;
            case MessageEvent.SYNC_ICON_OFF:
                //syncdata.setVisibility(View.GONE);
                updateSyncTime(DBHelper.timeStamp(), false);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CalculatedSNF", "calculatedSNF>= ");
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
}
