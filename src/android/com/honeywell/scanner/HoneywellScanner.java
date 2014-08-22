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

import java.util.ArrayList;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.honeywell.decodemanager.DecodeManager;
import com.honeywell.decodemanager.barcode.CommonDefine;
import com.honeywell.decodemanager.barcode.DecodeResult;

public class HoneywellScanner extends CordovaPlugin {
    public static final int REQUEST_CODE = 0x0ba7c0de;

    private final int ID_CLEAR_SCREEN = 0x13;

    public static final int SymbologySettingMenu=0x7f050001;
    public static final int ClearScreenMenu=0x7f050002;
    public static final int PROMOT_CLICK_SCAN_BUTTON=0x7f050004;

    private DecodeManager mDecodeManager = null;
    private EditText mDecodeResultEdit = null;
    private final int SCANKEY = 0x94;
    private final int SCANTIMEOUT = 5000;
    long mScanAccount = 0;
    private boolean mbKeyDown = true;

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

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if(action.equals(TRIGGER)) {
            trigger();
        } else {
            return false;
        }
        return true;
    }

    public void trigger() {

        mDecodeManager = new DecodeManager(this.cordova.getActivity().getApplicationContext(), ScanResultHandler);

        try {
            mDecodeManager.SymConfigActivityOpeartor.start();
            // mDecodeManager.doDecode(SCANTIMEOUT);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Handler ScanResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DecodeManager.MESSAGE_DECODER_COMPLETE:
                mScanAccount++;
                String strDecodeResult = "";
                DecodeResult decodeResult = (DecodeResult) msg.obj;
                // SoundManager.playSound(1, 1);

                byte codeid = decodeResult.codeId;
                byte aimid = decodeResult.aimId;
                int iLength = decodeResult.length;

                strDecodeResult = "Decode Result::"+ decodeResult.barcodeData + "\r\n" + "codeid::"+ "(" + String.valueOf((char) codeid) +"/"+  String.valueOf((char) aimid)+")" + "\r\n" + "Length:: " + iLength
                        + "  " + "Count:: " + mScanAccount + "\r\n";

                mDecodeResultEdit.setText(strDecodeResult);
                break;

            case DecodeManager.MESSAGE_DECODER_FAIL: {
                // SoundManager.playSound(2, 1);
                mDecodeResultEdit.setText("Decode Result::Scan fail");

            }
            break;
            case DecodeManager.MESSAGE_DECODER_READY:
            {
                ArrayList<java.lang.Integer> arry =  mDecodeManager.getSymConfigActivityOpeartor().getAllSymbologyId();
                boolean b = arry.isEmpty();
            }
            break;
            default:
                super.handleMessage(msg);
                break;
            }
        }
    };

}