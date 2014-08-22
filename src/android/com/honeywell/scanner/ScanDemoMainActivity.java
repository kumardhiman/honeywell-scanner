// package com.honeywell.scanner;


// import android.app.Activity;

// import android.os.Bundle;
// import android.os.Handler;
// import android.os.Message;
// import android.os.RemoteException;
// import android.view.KeyEvent;
// import android.view.Menu;
// import android.view.MenuItem;
// import android.view.MotionEvent;
// import android.view.View;
// import android.view.Window;
// import android.view.WindowManager;
// import android.widget.Button;
// import android.widget.EditText;
// import java.io.IOException;
// import java.util.ArrayList;

// import com.honeywell.decodemanager.DecodeManager;
// import com.honeywell.decodemanager.barcode.DecodeResult;


// public final class ScanDemoMainActivity extends Activity {

// 	private final int ID_SCANSETTING = 0x12;
// 	private final int ID_CLEAR_SCREEN = 0x13;

// 	public static final int SymbologySettingMenu=0x7f050001;
// 	public static final int ClearScreenMenu=0x7f050002;
// 	public static final int PROMOT_CLICK_SCAN_BUTTON=0x7f050004;

// 	private DecodeManager mDecodeManager = null;
// 	private EditText mDecodeResultEdit = null;
//     private final int SCANKEY = 0x94;
// 	private final int SCANTIMEOUT = 5000;
// 	long mScanAccount = 0;
// 	private boolean mbKeyDown = true;

// 	// private String strResultM;
// 	/** Called when the activity is first created. */
// 	@Override
// 	public void onCreate(Bundle savedInstanceState) {
// 		super.onCreate(savedInstanceState);

// 		requestWindowFeature(Window.FEATURE_NO_TITLE);
// 		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
// 				WindowManager.LayoutParams.FLAG_FULLSCREEN);

// 		// setContentView(R.layout.main);
// 		// initializeUI();

// 	}

// 	@Override
// 	protected void onResume() {
// 		super.onResume();

// 		if (mDecodeManager == null) {
// 			mDecodeManager = new DecodeManager(this,ScanResultHandler);
// 		}

// 		// SoundManager.getInstance();
// 		// SoundManager.initSounds(getBaseContext());
// 		// SoundManager.loadSounds();
// 	}

// 	@Override
// 	public boolean onCreateOptionsMenu(Menu menu) {
// 		super.onCreateOptionsMenu(menu);
// 		menu.add(0, ID_SCANSETTING, 0, SymbologySettingMenu);
// 		menu.add(0, ID_CLEAR_SCREEN, 1, ClearScreenMenu);

// 		return true;
// 	}

// 	@Override
// 	public boolean onOptionsItemSelected(MenuItem item) {
// 		switch (item.getItemId()) {

// 		case ID_SCANSETTING: {
// 			mDecodeManager.getSymConfigActivityOpeartor().start();
// 			 return true;
// 		}
// 		case ID_CLEAR_SCREEN: {

// 			String strPromotScan = this.getResources().getString(PROMOT_CLICK_SCAN_BUTTON);
// 			mDecodeResultEdit.setText(strPromotScan);

// 			return true;
// 		}
// 		default:
// 			return false;
// 		}
// 	}

// 	@Override
// 	protected void onPause() {
// 		super.onPause();

// 		if (mDecodeManager != null) {
// 			try {
// 				mDecodeManager.release();
// 				mDecodeManager = null;
// 			} catch (IOException e) {
// 				// TODO Auto-generated catch block
// 				e.printStackTrace();
// 			}
// 		}
// 	}

// 	@Override
// 	public void onDestroy() {
// 		super.onDestroy();

// 		// SoundManager.cleanup();
// 		if (mDecodeManager != null) {
// 			try {

// 				mDecodeManager.release();
// 				mDecodeManager = null;
// 			} catch (IOException e) {
// 				// TODO Auto-generated catch block
// 				e.printStackTrace();
// 			}
// 		}
// 	}

// 	private void DoScan() throws Exception {
// 		try {
// 			mDecodeManager.doDecode(SCANTIMEOUT);
// 		} catch (RemoteException e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
// 	}

// 	private Handler ScanResultHandler = new Handler() {
// 		public void handleMessage(Message msg) {
// 			switch (msg.what) {
// 			case DecodeManager.MESSAGE_DECODER_COMPLETE:
// 				mScanAccount++;
// 				String strDecodeResult = "";
// 				DecodeResult decodeResult = (DecodeResult) msg.obj;
// 				// SoundManager.playSound(1, 1);

// 				byte codeid = decodeResult.codeId;
// 				byte aimid = decodeResult.aimId;
// 				int iLength = decodeResult.length;

// 				strDecodeResult = "Decode Result::"+ decodeResult.barcodeData + "\r\n" + "codeid::"+ "(" + String.valueOf((char) codeid) +"/"+  String.valueOf((char) aimid)+")" + "\r\n" + "Length:: " + iLength
// 						+ "  " + "Count:: " + mScanAccount + "\r\n";

// 				mDecodeResultEdit.setText(strDecodeResult);
// 				break;

// 			case DecodeManager.MESSAGE_DECODER_FAIL: {
// 				// SoundManager.playSound(2, 1);
// 				mDecodeResultEdit.setText("Decode Result::Scan fail");

// 			}
// 			break;
// 			case DecodeManager.MESSAGE_DECODER_READY:
// 			{
// 				ArrayList<java.lang.Integer> arry =  mDecodeManager.getSymConfigActivityOpeartor().getAllSymbologyId();
// 				boolean b = arry.isEmpty();
// 			}
// 			break;
// 			default:
// 				super.handleMessage(msg);
// 				break;
// 			}
// 		}
// 	};
// 	private void cancleScan() throws Exception {
// 		mDecodeManager.cancelDecode();
// 	}
// }