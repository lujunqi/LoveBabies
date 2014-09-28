package com.xqj.lovebabies.fragments;

/**
 * 晨检 体温枪
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.commons.EWenCodeFormat;
import com.xqj.lovebabies.commons.EWenData;
import com.xqj.lovebabies.databases.dbs_ewen_data;
import com.zxing.activity.CaptureActivity;

@SuppressLint("ValidFragment")
public class FragmentEwen extends Fragment {

	public static final int EEEIG = 20;
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private EditText ewen = null;
	// Intent request codes
	private static ImageView blue;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothReceiver bluetoothReceiver = null;
	private BluetoothSocket btSocket = null;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private String ewenqiang_address = "x";
	private boolean startDiscoveryFlag = true;// 自动连接
	private boolean blueToothFlag = true;// 在连接状态下强制关闭连接

	private View main_view = null;
	public Context context = null;
	public Map<String, Object> userInfo = new HashMap<String, Object>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		main_view = inflater.inflate(R.layout.fragment_ewen, null);

		blue = ActivityMain.main_action_bar.getCmd_imagebutton_more();
		blue.setVisibility(View.VISIBLE);
		// blue.setImageResource(R.drawable.bluetooth);
		// blue.setTag("OFF");
		blue.setOnClickListener(btnClick);
		// Log.d("line91", userInfo + "-------------" + ActivityMain.);

		context = main_view.getContext();

		initView();
		initBlue();

		return main_view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// blue.setVisibility(View.GONE);
	}

	private void initView() {
		SharedPreferences sp = context.getSharedPreferences("SP",
				Context.MODE_PRIVATE);
		String ewenqiang_address_name = sp.getString("ewenqiang_address_name",
				"没有选择体温枪");
		// if ("没有选择体温枪".equals(ewenqiang_address_name)) {
		// blue.setVisibility(View.GONE);
		// }
		TextView ewen_info = (TextView) main_view.findViewById(R.id.ewen_info);
		ewen_info.setText(userInfo.get("BABY_NAME") + "["
				+ ewenqiang_address_name + "]");
		ewen = (EditText) main_view.findViewById(R.id.ewen);
		Button fragment_ewen_btn = (Button) main_view
				.findViewById(R.id.fragment_ewen_btn);
		fragment_ewen_btn.setOnClickListener(fragment_ewen_btn_click);
	}

	OnClickListener btnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ("ON".equals(blue.getTag())) {
				Toast.makeText(context, "断开自动连接", Toast.LENGTH_LONG).show();
				blueToothFlag = false;
				return;
			}
			blueToothFlag = true;
			blue.setImageResource(R.drawable.bluetooth);
			blue.setTag("OFF");
			SharedPreferences sp = context.getSharedPreferences("SP",
					Context.MODE_PRIVATE);
			ewenqiang_address = sp.getString("ewenqiang_address", "x");
			if ("x".equals(ewenqiang_address)) {
				Toast.makeText(context, "请选择体温枪", Toast.LENGTH_SHORT).show();
			} else {
				// 如果本地蓝牙没有开启，则强制开启
				if (!mBluetoothAdapter.isEnabled()) {
					mBluetoothAdapter.enable();
				}
				mBluetoothAdapter.startDiscovery();
			}
			Toast.makeText(context, "开启蓝牙设备连接..", Toast.LENGTH_LONG).show();
		}
	};

	private OnClickListener fragment_ewen_btn_click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			CheckBox teeth = (CheckBox) main_view.findViewById(R.id.teeth);
			Set<String> checkResult = new HashSet<String>();
			Map<String, String> result = new HashMap<String, String>();
			if (teeth.isChecked()) {
				checkResult.add("3:1");
				checkResult.remove("3:0");
			} else {
				checkResult.add("3:0");
				checkResult.remove("3:1");
			}
			CheckBox eye = (CheckBox) main_view.findViewById(R.id.eye);
			if (eye.isChecked()) {
				checkResult.add("4:1");
				checkResult.remove("4:0");
			} else {
				checkResult.add("4:0");
				checkResult.remove("4:1");
			}
			CheckBox skin = (CheckBox) main_view.findViewById(R.id.skin);
			if (skin.isChecked()) {
				checkResult.add("5:1");
				checkResult.remove("5:0");
			} else {
				checkResult.add("5:0");
				checkResult.remove("5:1");
			}
			CheckBox medicine = (CheckBox) main_view
					.findViewById(R.id.medicine);
			if (medicine.isChecked()) {
				checkResult.add("6:1");
				checkResult.remove("6:0");
			} else {
				checkResult.add("6:0");
				checkResult.remove("6:1");
			}
			CheckBox dangerous = (CheckBox) main_view
					.findViewById(R.id.dangerous);
			if (dangerous.isChecked()) {
				checkResult.add("7:1");
				checkResult.remove("7:0");
			} else {
				checkResult.add("7:0");
				checkResult.remove("7:1");
			}
			String cr = "";
			for (String c : checkResult) {
				if ("".equals(cr)) {
					cr += c;
				} else {
					cr += "," + c;
				}
			}
			result.put("checkResult", cr); // 检测异常项目编号
			EditText ewen = (EditText) main_view.findViewById(R.id.ewen);
			result.put("temperature", ewen.getText() + ""); // 额温
			EditText meno = (EditText) main_view.findViewById(R.id.meno);
			result.put("remark", meno.getText() + ""); // 备注
			result.put("qrCode", userInfo.get("QR_CODE") + "");// 二维码
			result.put("baby_name", userInfo.get("BABY_NAME") + "");// 宝宝名称
			result.put("babyId", userInfo.get("BABY_ID") + "");// 宝宝ID

			// 保存
			dbs_ewen_data db = new dbs_ewen_data();
			db.saveData(result, db.FILE_UPLOAD_PATH + userInfo.get("BABY_ID")
					+ ".obj");
			Toast.makeText(context, "晨检数据已保存", Toast.LENGTH_SHORT).show();
			Intent openCameraIntent = new Intent(getActivity(),
					CaptureActivity.class);

			startActivityForResult(openCameraIntent, 0);

		}

	};
	private IntentFilter intentFilter = new IntentFilter(
			BluetoothDevice.ACTION_FOUND);

	public void initBlue() {
		// 创建一个消息传送对象
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 创建一个BluetoothReceiver对象

		bluetoothReceiver = new BluetoothReceiver();
		ActivityMain.fragmentEwen.context.registerReceiver(bluetoothReceiver,
				intentFilter);
		if (mBluetoothAdapter == null) {
			Toast.makeText(context, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 接收广播
	private class BluetoothReceiver extends BroadcastReceiver {
		@SuppressLint("NewApi")
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("line240", action + "===");
			Toast.makeText(context, "正在匹配蓝牙设备", Toast.LENGTH_SHORT).show();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				Log.d("line69",
						bluetoothDevice.getAddress() + "=="
								+ bluetoothDevice.getName() + "==="
								+ bluetoothDevice.getBondState());
				// 未配对
				if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
					try {
						Method createBondMethod = BluetoothDevice.class
								.getMethod("createBond");
						createBondMethod.invoke(bluetoothDevice);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				String address = bluetoothDevice.getAddress();
				Log.d("line235", ewenqiang_address + "================="
						+ address);
				if (ewenqiang_address.equals(address)) {
					mBluetoothAdapter.cancelDiscovery();
					if (bluetoothDevice.getBondState() == 12) {
						startDiscoveryFlag = false;
						conection(address);
					}

				}
				Log.d("line245", "=================" + address);
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int connectState = bluetoothDevice.getBondState();
				Log.d("line139", "============" + connectState);

			}
		}
	}

	@SuppressLint("NewApi")
	public void conection(String address) {
		Log.d("lin256", ewenqiang_address + "==");
		Toast.makeText(ActivityMain.fragmentEwen.context, "准备建立连接",
				Toast.LENGTH_SHORT).show();
		if (ewenqiang_address.equals(address)) {
			Log.d("lin83", ewenqiang_address + "==");
			BluetoothDevice device = mBluetoothAdapter
					.getRemoteDevice(ewenqiang_address);
			try {
				int sdk = Integer.parseInt(Build.VERSION.SDK);
				if (sdk >= 10) {
					btSocket = device
							.createInsecureRfcommSocketToServiceRecord(MY_UUID);
				} else {
					btSocket = device
							.createRfcommSocketToServiceRecord(MY_UUID);
				}

			} catch (IOException e1) {
				Log.d("line88",
						"ON RESUME: BT connection established, data transfer link open.",
						e1);
				e1.printStackTrace();
			}
			try {
				if (!ActivityMain.setKey.contains("BLUE")) {
					ActivityMain.setKey.add("BLUE");
					btSocket.connect();
					GetDataTask task = new GetDataTask(btSocket);

					byte[] sends = { (-2), (-3), (-86), (-96), 13, 10 };
					task.write(sends);
					task.execute();
					blue.setImageResource(R.drawable.bluetooth);
					blue.setTag("OFF");
				}

			} catch (IOException e) {
				Log.d("line110", "====================", e);
				try {
					btSocket.close();
				} catch (IOException e2) {
					Log.e("line96",
							"ON RESUME: Unable to close socket during connection failure",
							e2);
				}
			}
		}
	}

	private class GetDataTask extends AsyncTask<String, String, String> {
		int[] com = { 0, 0, 0, 0, 0, 0, 0 };
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private String TAG = "line_ConnectedThread";

		public GetDataTask(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			// 获得bluetoothsocket输入输出流
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "没有创建临时sockets", e);
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[16];
			EWenData dt = new EWenData();
			// 继续听InputStream同时连接
			while (true) {
				try {
					mmInStream.read(buffer);
					int[] f = EWenCodeFormat.bytesToHexStringTwo(buffer, 8);
					dt.analysis(f);
					com[0] = dt.getHead();
					com[1] = dt.getDataone();
					com[2] = dt.getDatatwo();
					com[3] = dt.getThree();
					com[4] = dt.getFour();
					com[5] = dt.getFive();
					com[6] = dt.getSex();
					Log.d("line167", com[0] + "=" + com[1] + "=" + com[2] + "="
							+ com[3] + "=" + com[4] + "=" + com[5] + "="
							+ com[6] + "================");
					// if ((com[0] == 254 || com[0] == 253) && com[5] == 13
					// && com[6] == 10) {
					if (com[1] == 26) { // 摄氏度
						StringTokenizer token = new StringTokenizer(com[3] + ""
								+ "," + com[4] + "", ",");
						int gaot = 500;
						int dit = 500;
						while (token.hasMoreTokens()) {
							String tt = token.nextToken();

							if (gaot == 500) {
								gaot = Integer.parseInt(tt);
							} else {
								dit = Integer.parseInt(tt);
							}
						}
						double ew = (double) (gaot * 256 + dit) / 10;
						// 发送数据
						if (com[2] == 1 || com[2] == 0) {
							publishProgress("EWEN", "" + ew, com[2] + "");
						}
						Log.e(TAG, "disconnected==" + ew + "+" + com[0]);
					}
					Log.e(TAG, "disconnected==" + 349 + "+" + com[1]);
					if (com[1] == 21) { // 华氏度 F
						StringTokenizer token = new StringTokenizer(com[3] + ""
								+ "," + com[4] + "", ",");
						int gaot = 500;
						int dit = 500;
						while (token.hasMoreTokens()) {
							String tt = token.nextToken();

							if (gaot == 500) {
								gaot = Integer.parseInt(tt);
							} else {
								dit = Integer.parseInt(tt);
							}
						}
						double ew = (double) (gaot * 256 + dit) / 10;
						BigDecimal b = new BigDecimal((ew - 32) / 1.8);
						ew = b.setScale(1, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						// 摄氏度(℃)=（华氏度(℉)-32）÷1.8。
						Log.e(TAG, "disconnected==" + ew + "+" + com[0]);
						if (com[2] == 1 || com[2] == 0) {
							publishProgress("EWEN", "" + ew, com[2] + "");
						}
					}

					StringTokenizer token = new StringTokenizer(com[3] + ""
							+ "," + com[4] + "", ",");
					int gaot = 500;
					int dit = 500;
					while (token.hasMoreTokens()) {
						String tt = token.nextToken();

						if (gaot == 500) {
							gaot = Integer.parseInt(tt);
						} else {
							dit = Integer.parseInt(tt);
						}
					}

					double ew = (double) (gaot * 256 + dit) / 10;
					Log.e(TAG, "[448]disconnected==" + ew + "+" + com[0]);
					publishProgress("STATE", "ON", ew + "," + com[1]);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					// 连接出问题
					Log.e(TAG, "disconnected", e);
					publishProgress("STATE", "OFF");

					break;
				}
			}

			return null;
		}

		private class startDiscoveryTask extends
				AsyncTask<String, String, String> {
			@Override
			protected String doInBackground(String... arg0) {
				while (startDiscoveryFlag && blueToothFlag) {
					mBluetoothAdapter.startDiscovery();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

		}

		public void onProgressUpdate(String... progress) {
			String state = progress[0];
			if ("STATE".equals(state)) {
				String oc = progress[1];
				if ("OFF".equals(oc)) {
					Toast.makeText(context, "已经断开连接", Toast.LENGTH_SHORT)
							.show();
					blue.setImageResource(R.drawable.bluetooth);
					blue.setTag("OFF");
					startDiscoveryFlag = true;
					ActivityMain.setKey.remove("BLUE");
					new startDiscoveryTask().execute();

				}
				if ("ON".equals(oc)) {
					ActivityMain.setKey.add("BLUE");
					// Toast.makeText(ActivityMain.fragmentEwen.context,
					// "获取数据" + progress[2], Toast.LENGTH_SHORT).show();
					blue.setImageResource(R.drawable.bluetooth_attach);
					blue.setTag("ON");

				}
			}
			if ("EWEN".equals(state)) {
				FragmentEwen fragmentEwen = ActivityMain.fragmentEwen;

				Log.d("line495", ewen.getText() + "=========" + progress[1]);
				fragmentEwen.ewen.setText(progress[1]);
				if ("0".equals(progress[2])) {// 目标温度
					ImageView ewen_type = (ImageView) fragmentEwen.main_view
							.findViewById(R.id.ewen_type);
					ewen_type.setBackgroundResource(R.drawable.wuti);
				}
				if ("1".equals(progress[2])) {// 人体温度
					ImageView ewen_type = (ImageView) fragmentEwen.main_view
							.findViewById(R.id.ewen_type);
					ewen_type.setBackgroundResource(R.drawable.renti);
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}
	}

}
