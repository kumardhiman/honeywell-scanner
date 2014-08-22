package com.honeywell.scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

public class HoneywellScanner extends CordovaPlugin {
    public static final int REQUEST_CODE = 0x0ba7c0de;

    private static final String TRIGGER = "trigger";
    private static final String TRIGGER_INTENT = "com.honeywell.scanner.TRIGGER";
    private static final String CANCELLED = "cancelled";
    private static final String FORMAT = "format";
    private static final String TEXT = "text";
    private static final String DATA = "data";
    private static final String TYPE = "type";

    private static final String LOG_TAG = "HoneywellScanner";

    private CallbackContext callbackContext;

    public HoneywellScanner() {
    }

    // @Override
    // public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
    //     this.callbackContext = callbackContext;

    //     if(action.equals(TRIGGER)) {
    //         trigger();
    //     } else {
    //         return false;
    //     }
    //     return true;
    // }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals(TRIGGER)) {
            final long duration = args.getLong(0);
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    trigger();
                }
            });
            return true;
        }
        return false;
    }

    public void trigger() {
        Intent intentTrigger = new Intent(TRIGGER_INTENT);
        intentTrigger.addCategory(Intent.CATEGORY_DEFAULT);

        intentTrigger.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult((CordovaPlugin) this, intentTrigger, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, intent.getStringExtra("SCAN_RESULT"));
                    obj.put(FORMAT, intent.getStringExtra("SCAN_RESULT_FORMAT"));
                    obj.put(CANCELLED, false);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                this.callbackContext.success(obj);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TEXT, "");
                    obj.put(FORMAT, "");
                    obj.put(CANCELLED, true);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                this.callbackContext.success(obj);
            } else {
                this.callbackContext.error("Unexpected error");
            }
        }
    }



}