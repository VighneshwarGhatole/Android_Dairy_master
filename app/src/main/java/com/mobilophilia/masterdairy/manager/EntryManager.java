package com.mobilophilia.masterdairy.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobilophilia.masterdairy.common.Constants;
import com.mobilophilia.masterdairy.common.Log;
import com.mobilophilia.masterdairy.common.MessageEvent;
import com.mobilophilia.masterdairy.common.RestClient;
import com.mobilophilia.masterdairy.common.Util;
import com.mobilophilia.masterdairy.database.EnterNameEntry;
import com.mobilophilia.masterdairy.database.ExpenseBean;
import com.mobilophilia.masterdairy.database.MyEntries;
import com.mobilophilia.masterdairy.database.SetPriceEntry;
import com.mobilophilia.masterdairy.manager.beans.UpdateEntryBean;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Hanji on 7/26/2017.
 */

public class EntryManager {

    private String TAG = "EntryManager-->";

    public void editEntry(final Context context, UpdateEntryBean entry) {

        JSONObject dataObj = new JSONObject();
        JSONArray nameArray = new JSONArray();
        JSONArray priceArray = new JSONArray();
        JSONArray entryArray = new JSONArray();
        JSONArray expArray = new JSONArray();

        try {
            JSONObject entryObj = new JSONObject();
            entryObj.put("code", entry.getUpdateCode());
            entryObj.put("crl", entry.getUpdateClr());
            entryObj.put("fat", entry.getUpdateFat());
            entryObj.put("agent_id”", entry.getAgentId());
            entryObj.put("snf", entry.getUpdatedSnf());
            entryObj.put("ltr", entry.getUpdateLtr());
            entryObj.put("price", entry.getUpdatePrice());
            entryObj.put("total", entry.getUpdateTotal());
            entryObj.put("more", entry.getUpdateTimeInterval());
            entryObj.put("created_at", entry.getCurrentTime());
            entryArray.put(0, entryObj);

            dataObj.put("customer", nameArray);
            dataObj.put("price", priceArray);
            dataObj.put("entry", entryArray);
            dataObj.put("expense", expArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG + "Input -Data for saveEntry", dataObj.toString());
        RestClient.post(context, Constants.ENTRY, dataObj.toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e(TAG + "Response saveEntry", response.toString());
                    int status = response.getInt("status");
                    if (status == Constants.SERVICE_STATUS) {
                        String message = response.getString("message");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.ENTRY_SUCCESS, message));
                    } else {
                        String message = response.getString("message");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.ENTRY_FAILURE, message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (throwable != null && throwable instanceof SocketTimeoutException) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.NETWORK_TIME_OUT));
                } else {
                    if (errorResponse != null) {
                        if (errorResponse.has("message")) {
                            try {
                                String message = errorResponse.getString("message");
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.ENTRY_FAILURE, message));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                    throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
            }
        });
    }

}