package com.honeywell.scanner;


import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import java.util.ArrayList;

import com.honeywell.decodemanager.DecodeManager;
import com.honeywell.decodemanager.barcode.DecodeResult;


public final class ScanDemoMainActivity extends Activity {

	private final int ID_SCANSETTING = 0x12;
	private final int ID_CLEAR_SCREEN = 0x13;

	private DecodeManager mDecodeManager = null;
	private EditText mDecodeResultEdit = null;
    private final int SCANKEY = 0x94;
	private final int SCANTIMEOUT = 5000;
	long mScanAccount = 0;
	private boolean mbKeyDown = true;

	// private String strResultM;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		initializeUI();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
				try {
					if (mbKeyDown) {
						DoScan();
						mbKeyDown = false;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return true;
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			return true;
        case KeyEvent.KEYCODE_UNKNOWN:
        	if(event.getScanCode() == SCANKEY || event.getScanCode() == 87 || event.getScanCode() == 88) {
				try {
					if (mbKeyDown) {
						DoScan();
						mbKeyDown = false;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			try {
				mbKeyDown = true;
				cancleScan();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			return true;

        case KeyEvent.KEYCODE_UNKNOWN:
        	if(event.getScanCode() == SCANKEY  || event.getScanCode() == 87 || event.getScanCode() == 88) {
    			try {
    				mbKeyDown = true;
    				cancleScan();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	return true;
		default:
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mDecodeManager == null) {
			mDecodeManager = new DecodeManager(this,ScanResultHandler);
		}

		SoundManager.getInstance();
		SoundManager.initSounds(getBaseContext());
		SoundManager.loadSounds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ID_SCANSETTING, 0, R.string.SymbologySettingMenu);
		menu.add(0, ID_CLEAR_SCREEN, 1, R.string.ClearScreenMenu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case ID_SCANSETTING: {
			mDecodeManager.getSymConfigActivityOpeartor().start();
			 return true;
		}
		case ID_CLEAR_SCREEN: {

			String strPromotScan = this.getResources().getString(R.string.PROMOT_CLICK_SCAN_BUTTON);
			mDecodeResultEdit.setText(strPromotScan);

			return true;
		}
		default:
			return false;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

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
	public void onDestroy() {
		super.onDestroy();

		SoundManager.cleanup();
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

	private void initializeUI() {
		final Button button = (Button) findViewById(R.id.scanbutton);
		mDecodeResultEdit = (EditText) findViewById(R.id.edittext_scanresult);
		button.setOnTouchListener(new Button.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					button.setBackgroundResource(R.drawable.android_pressed);
						try {
							DoScan();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					break;
				case MotionEvent.ACTION_UP:
					try {
						button.setBackgroundResource(R.drawable.android_normal);
						cancleScan();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				return true;
			}
		});
	}

	private void DoScan() throws Exception {
		try {
			mDecodeManager.doDecode(SCANTIMEOUT);
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
				SoundManager.playSound(1, 1);

				byte codeid = decodeResult.codeId;
				byte aimid = decodeResult.aimId;
				int iLength = decodeResult.length;

				strDecodeResult = "Decode Result::"+ decodeResult.barcodeData + "\r\n" + "codeid::"+ "(" + String.valueOf((char) codeid) +"/"+  String.valueOf((char) aimid)+")" + "\r\n" + "Length:: " + iLength
						+ "  " + "Count:: " + mScanAccount + "\r\n";

				mDecodeResultEdit.setText(strDecodeResult);
				break;

			case DecodeManager.MESSAGE_DECODER_FAIL: {
				SoundManager.playSound(2, 1);
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
	private void cancleScan() throws Exception {
		mDecodeManager.cancelDecode();
	}
}