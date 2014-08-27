package com.honeywell.scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import com.honeywell.decodemanager.DecodeManager;
import com.honeywell.decodemanager.barcode.CommonDefine;
import com.honeywell.decodemanager.barcode.DecodeResult;

public class HoneywellScanner extends CordovaPlugin {

    private final int SCANKEY = 0x94;
    private final int SCANTIMEOUT = 5000;
    private DecodeManager mDecodeManager = null;
    long mScanAccount = 0;
    private boolean scanInProgress = false;
    private boolean initialized = false;

    private static final String TRIGGER = "trigger";
    private static final String INITIALIZE = "initialize";

    private static final String LOG_TAG = "HoneywellScanner";

    private CallbackContext callbackContext;

    public HoneywellScanner() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "on destroy");


        if (mDecodeManager != null) {
            try {
                mDecodeManager.release();
                mDecodeManager = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        this.callbackContext = callbackContext;
        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        this.callbackContext.sendPluginResult(result);

        if(action.equals(INITIALIZE)) {
            initialize();

        } else if(action.equals(TRIGGER)) {
            trigger();
        } else {
            return false;
        }
        return true;
    }

    public void initialize() {
        Log.i(LOG_TAG, "init decoder");
        mDecodeManager = new DecodeManager(this.cordova.getActivity().getApplicationContext(), ScanResultHandler);
    }

    public void trigger() {
        Log.i(LOG_TAG, "trigger");

        if (scanInProgress) {
            Log.i(LOG_TAG, "trigger");
            return;
        }

        Log.i(LOG_TAG, "trigger");

        try {
            Log.i(LOG_TAG, "do scan");
            scanInProgress = true;
            mDecodeManager.enableSymbology(CommonDefine.SymbologyID.SYM_QR);
            mDecodeManager.doDecode(SCANTIMEOUT);

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void cancelScan() throws Exception {
        Log.i(LOG_TAG, "cancel");

        scanInProgress = false;
        mDecodeManager.cancelDecode();
    }

    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DecodeManager.MESSAGE_DECODER_COMPLETE:
                mScanAccount++;
                String strDecodeResult = "";
                DecodeResult decodeResult = (DecodeResult) msg.obj;

                byte codeid = decodeResult.codeId;
                byte aimid = decodeResult.aimId;
                int iLength = decodeResult.length;

                strDecodeResult = "Decode Result::"+ decodeResult.barcodeData + "\r\n" + "codeid::"+ "(" + String.valueOf((char) codeid) +"/"+  String.valueOf((char) aimid)+")" + "\r\n" + "Length:: " + iLength
                        + "  " + "Count:: " + mScanAccount + "\r\n";

                Log.i(LOG_TAG, "success: " + strDecodeResult);
                scanInProgress = false;

                PluginResult result1 = new PluginResult(PluginResult.Status.OK, decodeResult.barcodeData);
                result1.setKeepCallback(false);
                callbackContext.sendPluginResult(result1);

                break;

            case DecodeManager.MESSAGE_DECODER_FAIL: {
                Log.i(LOG_TAG, "Decode Result::Scan fail");
                scanInProgress = false;

                PluginResult result2 = new PluginResult(PluginResult.Status.OK, "");
                result2.setKeepCallback(false);
                callbackContext.sendPluginResult(result2);
            }
            break;
            case DecodeManager.MESSAGE_DECODER_READY:
            {
                ArrayList<java.lang.Integer> arry =  mDecodeManager.getSymConfigActivityOpeartor().getAllSymbologyId();
                boolean b = arry.isEmpty();
                Log.i(LOG_TAG, "MESSAGE_DECODER_READY");
                initialized = true;

                PluginResult result3 = new PluginResult(PluginResult.Status.OK, "ready");
                result3.setKeepCallback(false);
                callbackContext.sendPluginResult(result3);
            }
            break;
            default:
                super.handleMessage(msg);
                break;
            }
        }
    };

}