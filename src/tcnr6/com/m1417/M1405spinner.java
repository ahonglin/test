package tcnr6.com.m1417;




import java.sql.Date;
import java.text.SimpleDateFormat;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import tcnr6.com.m1417.providers.FriendsContentProvider;

public class M1405spinner extends Activity {
	private EditText E001, E002, E003, E004;
	private TextView count_t;
	private Spinner S001;

	// private static final String DB_FILE="friends.db";
	// private static final String DB_TABLE="member";
	// public static final int VERSION = 1;
	// private SQLiteDatabase mFriendDB;
	// private FriendDBHelper DBHelper;

	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "grp", "address" };
	int tcount;
	int up_item = 0;	
	String msg = "";

	MyAlertDialog myAlertDialog;
	protected static final int BUTTON_POSITIVE = -1, BUTTON_NEGATIVE = -2, BUTTON_NEUTRAL = -3;
	String TAG = "tcnr6==>",showip;
	
	// ----------------------------------------
	 String nowindex;
	 // ----------定時更新--------------------------------
	 private Long startTime;
	 private Handler handler = new Handler();

	 int autotime = 30;// 要幾秒的時間 更新匯入MySQL資料
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 private TextView nowtime;
	 int old_index = 0;
	    int update_time=0;
	 // ------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1405spinner);
		Log.d(TAG, "xxxxxxx");
		count_t = (TextView) findViewById(R.id.m1405list_T001);
		myAlertDialog = new MyAlertDialog(M1405spinner.this);

		setupviewcomponent();

	}

	private void setupviewcomponent() {
		// TODO Auto-generated method stub
		E001 = (EditText) findViewById(R.id.editName);
		E002 = (EditText) findViewById(R.id.editGrp);
		E003 = (EditText) findViewById(R.id.editAddr);
		E004 = (EditText) findViewById(R.id.editID);
		count_t = (TextView) findViewById(R.id.count_t);
		S001 = (Spinner) findViewById(R.id.m1405spinner_S001);

		  nowtime = (TextView) findViewById(R.id.now_time);
		  nowtime.setTextSize(18);
		  // -------------------------
		  // 取得目前時間
		  startTime = System.currentTimeMillis();
		  // 設定Delay的時間
		  handler.postDelayed(updateTimer, 1000); // 延遲
		  Date curDate = new Date(System.currentTimeMillis()); //  獲取當前時間
		  String str = formatter.format(curDate);
		  nowtime.setText(getString(R.string.now_time) + str);
		  Log.d(TAG, "setupViewComponent()=" + str);
		  // ---------------------------------------
		  sqliteupdate(); // 抓取SQLite資料
		  // ----------------------------------------
		
//		// -------------------------
//		mContRes = getContentResolver();
//		//-----------------------------
//		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
//		c.moveToFirst(); // 一定要寫，不然會出錯
//		tcount = c.getCount();
//		// ---------------------------
//		count_t.setTextColor(Color.BLUE);
//		count_t.setText("顯示資料： 共" + tcount + " 筆");
//		E004.setTextColor(Color.RED);
//		// -------------------------
//
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_style);
//		for (int i = 0; i < tcount; i++) {
//			c.moveToPosition(i);
//			adapter.add(c.getString(0) + "-" + c.getString(1) + "," + c.getString(2) + "," + c.getString(3));
//		}
//		c.close();
//
//		adapter.setDropDownViewResource(R.layout.spinner_style);
//		S001.setAdapter(adapter);
//		S001.setOnItemSelectedListener(S001on);
		// S001.setSelection(position=下拉視窗跳到第幾筆
		// , animate);
		
	}

	// --------SQLite資料------------------------------------------------
	private void sqliteupdate() {
		mContRes = getContentResolver();
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();
		// ---------------------------
		count_t.setTextColor(Color.BLUE);
		count_t.setText("顯示資料： 共" + tcount + " 筆");
		E004.setTextColor(Color.RED);
		// -------------------------
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_style);

		for (int i = 0; i < tcount; i++) {
			c.moveToPosition(i);
			adapter.add(c.getString(0) + "-" + c.getString(1) + "," + c.getString(2) + "," + c.getString(3));
		}
		c.close();
       
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(R.layout.spinner_style);
		S001.setAdapter(adapter);
		S001.setOnItemSelectedListener(S001on);
		// S001.setSelection(position=下拉視窗跳到第幾筆
		// , animate);
		showip = NetwordDetect();
		// mSpnName.setSelection(0, true); //spinner 小窗跳到第幾筆
		// ----------------------------------------
		// 宣告鈴聲
		ToneGenerator	toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
		toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
		toneG.release();
	}

	// -----------------------------------------------------------
	// 固定要執行的方法
	private Runnable updateTimer = new Runnable() {
		@Override
		public void run() {
			Long spentTime = System.currentTimeMillis() - startTime;
			// 計算目前已過分鐘數
			Long minius = (spentTime / 1000) / 60;
			// 計算目前已過秒數
			Long seconds = (spentTime / 1000) % 60;
			handler.postDelayed(this, autotime * 1000); // 真正延遲的時間
			Date curDate = new Date(System.currentTimeMillis()); //  獲取當前時間
			String str = formatter.format(curDate);
			Log.d(TAG, "run:" + str);
			// -------執行匯入MySQL
			db_mySQL();
			++update_time;

			nowtime.setText(getString(R.string.now_time) +"(每"+autotime+"秒)"+ str + " " +"\n已更新:"+ minius + ":" + (seconds-1)+" ("+update_time+"次)");
			S001.setSelection(old_index, true); // spinner 小窗跳到第幾筆
			// ---------------------------
		}


	};

	// -------------------------------------
	
	private String NetwordDetect() {
		 ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		 String IPaddress = Finduserip.NetwordDetect(CM, wm);
		 return IPaddress;
	}

	private Spinner.OnItemSelectedListener S001on = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			int iSelect = S001.getSelectedItemPosition(); // 找到按何項
			old_index = iSelect;
			// -----------------------------------
			String countStr = "資料：共" + tcount + " 筆," + "你按下第  " + Integer.toString(iSelect + 1) + "項"; // 起始為0
			count_t.setText(countStr);

			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯
			c.moveToPosition(position);
			// -------目前所選的item---
			up_item = position;
			// ---------------------------

			E004.setTextColor(getResources().getColor(R.drawable.Red));
			E004.setText(c.getString(0));// id
			E001.setText(c.getString(1));// name
			E002.setText(c.getString(2));// grp
			E003.setText(c.getString(3));// address
			c.close();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			E001.setText("");
			E002.setText("");
			E003.setText("");
			E004.setText("");
		}

	};
	// ---------------------------------------------------------
	// string to int(字串轉整數)
	// 1. int intValue = Integer.valueOf("12345");
	// 2. int intValue = Integer.parseInt("12345");
	// int to String(整數轉字串)
	// 1. String stringValue = Integer.toString(12345);
	// 2. String stringValue = String.valueOf(12345);
	// 3. String stringValue = new String(""+12345);
	// 目前資料筆數 => recSet.size()
	// ---------------------------------------------------------------------


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		onCreate(null);// 從刷onCreate()
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	// -----------------------------------
	private String u_chinano(int input_i) {
		String c_number = "";
		String china_no[] = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		c_number = china_no[input_i];

		return c_number;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1405, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			finish();
			break;
		case R.id.m_add:
			Intent intent = new Intent();
			intent.setClass(M1405spinner.this, M1405insert.class);
			startActivity(intent);
			break;
		case R.id.m_update:
			
			Intent intentu = new Intent();
			intentu.setClass(M1405spinner.this, M1405update.class);
			intentu.putExtra("up_item", up_item);
			startActivity(intentu);

			break;
		case R.id.m_query:
			Intent intentq = new Intent();
			intentq.setClass(M1405spinner.this, M1405query.class);
			startActivity(intentq);
			break;
		case R.id.m_clear:
			u_cleanDialog();
			break;
		case R.id.m_addpatch:
			// ---------------------------
			Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
			c.moveToFirst(); // 一定要寫，不然會出錯
			String msg = null;
			// -------------------------
			ContentValues newRow = new ContentValues();
			for (int i = 0; i < 10; i++) {
				newRow.put("name", "路人" + u_chinano(i));
				newRow.put("grp", "第" + u_chinano((int) (Math.random() * 4 + 1)) + "組");
				newRow.put("address", "台中市西區工業一路" + (100 + i) + "號");
				mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
			}
			c.moveToFirst(); // 一定要寫，不然會出錯
			tcount = c.getCount();
			// ---------------------------
			count_t.setTextColor(Color.BLUE);
			count_t.setText("顯示資料： 共" + tcount + " 筆");
			msg = "新增記錄  成功 ! ";
			Toast.makeText(M1405spinner.this, msg, Toast.LENGTH_SHORT).show();
			c.close();
			onCreate(null); // 重構

			break;
		case R.id.m_list:
			Intent intentlist = new Intent();
			intentlist.setClass(M1405spinner.this, M1405list.class);
			startActivity(intentlist);
			break;
		case R.id.m_spinner:
			Intent intentspin = new Intent();
			intentspin.setClass(M1405spinner.this, M1405spinner.class);
			startActivity(intentspin);
			break;
		case R.id.m_1406:
			Intent intentSQL = new Intent();
			intentSQL.setClass(M1405spinner.this, M1416.class);
			startActivity(intentSQL);
			break;
		case R.id.m_import:

			db_mySQL();
			onCreate(null);
			break;

		}
		return super.onOptionsItemSelected(item);
	}
//<!-----------從mySQL抓資料新增到SQLite------------->
	private void db_mySQL() {
		// --------------執行緒區段--------------------------
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		// --------------執行緒區段--------------------------
		// -------------------------
		mContRes = getContentResolver();
		//-----------------------------		
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯	
		try {
			String result = DBConnector.executeQuery("SELECT * FROM member");
			/**************************************************************************
			 * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
			 * jsonData = new JSONObject(result);
			 **************************************************************************/
			 String r = result.toString().trim();
			   //以下程式碼一定要放在前端藍色程式碼執行之後，才能取得狀態碼
			   //存取類別成員 DBConnector.httpstate 判定是否回應 200(連線要求成功)
			   Log.d(TAG, "httpstate="+DBConnector.httpstate );
			   if (DBConnector.httpstate == 200) {
			    Uri uri = FriendsContentProvider.CONTENT_URI;
			    mContRes.delete(uri, null, null);
			    Toast.makeText(getBaseContext(), "已經完成由伺服器會入資料",
			      Toast.LENGTH_LONG).show();
			    } else {
			    Toast.makeText(getBaseContext(), "伺服器無回應，請稍後在試",
			      Toast.LENGTH_LONG).show();
			   }
			
			JSONArray jsonArray = new JSONArray(result);
			// ---
			Log.d(TAG, jsonArray.toString());
			if(jsonArray.length()>0){
				Uri uri = FriendsContentProvider.CONTENT_URI;
				mContRes.delete(uri, null, null); // 刪除所有資料	
			}
			
			ContentValues newRow = new ContentValues();
			
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);
          
			  // 取出 jsonObject 中的字段的值的空格
				Iterator itt = jsonData.keys();
				while (itt.hasNext()) {
					String key = itt.next().toString();
					String value = jsonData.getString(key);
					if (value == null) {
						continue;
					} else if ("".equals(value.trim())) {
						continue;
					} else {
						newRow.put(key, value.trim());
					}
					
				}
				
				mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);

				count_t.setTextColor(Color.BLUE);
				count_t.setText("顯示資料：共加入" + Integer.toString(jsonArray.length()) + " 筆");
               
				
				
			}

		} catch (Exception e) {
			Log.d(TAG,"error->"+ e.toString());
		}
		c.close();
//		onCreate(null); // 重構
		sqliteupdate();
		
	}

	private void u_cleanDialog() {

		myAlertDialog.setTitle("警告");
		myAlertDialog.setIcon(android.R.drawable.btn_star_big_on);
		myAlertDialog.setMessage("確定要刪除所有資料?");
		myAlertDialog.setCancelable(false);
		myAlertDialog.setButton(BUTTON_POSITIVE, "Cancel", btnYes);// -1(button位置,顯示字串,設定監聽變數)
		myAlertDialog.setButton(BUTTON_NEGATIVE, "Yes", btnYes);// -2
		myAlertDialog.setButton(BUTTON_NEUTRAL, "No", btnYes);// -3
		myAlertDialog.show();
	}

	private DialogInterface.OnClickListener btnYes = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch (which) {
			case BUTTON_POSITIVE:// cancel

				break;
			case BUTTON_NEGATIVE:// Yes
				Uri uri = FriendsContentProvider.CONTENT_URI;
				mContRes.delete(uri, null, null); // 刪除所有資料
				msg = "資料表已空 !";
				Toast.makeText(M1405spinner.this, msg, Toast.LENGTH_SHORT).show();
               onCreate(null);
				break;
			case BUTTON_NEUTRAL:// No
				msg = "放棄刪除所有資料 !";
				Toast.makeText(M1405spinner.this, msg, Toast.LENGTH_SHORT).show();
				break;

			}
		}

	};

}
