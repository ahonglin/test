package tcnr6.com.m1417;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tcnr6.com.m1417.providers.FriendsContentProvider;


public class M1405insert extends Activity implements OnClickListener {

	private EditText E001,E002,E003,E004;
	private Button B001,B002,B003;
	private TextView count_t;
	String TAG="tcnr6==>",showip;
	// --------------
		private static ContentResolver mContRes;
		private String[] MYCOLUMN = new String[] { "id", "name", "grp", "address" };

		// ----------------
	
//    private FriendDBHelper DBHelper;
////<!-------------------------------------->	
//	private static final String DB_FILE="friends.db";
//	private static final String DB_TABLE="member";
//	public static final int VERSION = 1; 
//	private SQLiteDatabase mFriendDB;
//<!--------------------------------------->	




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1405insert);
		setupviewcomponent();
		
		
	}



	private void setupviewcomponent() {
		// ----------------------------------------------------
		mContRes = getContentResolver();
		// ----------------------------------------------------
		E001 = (EditText) findViewById(R.id.editName);
		E002 = (EditText) findViewById(R.id.editGrp);
		E003 = (EditText) findViewById(R.id.editAddr);
		count_t = (TextView) findViewById(R.id.count_t);
		B001 = (Button) findViewById(R.id.btnBack);

		B001.setOnClickListener(this);
		
		showip = NetwordDetect();
		E003.setText(showip);
	}
	

	private String NetwordDetect() {
		 ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		 String IPaddress = Finduserip.NetwordDetect(CM, wm);
		 return IPaddress;
	}



	@Override
	public void onClick(View v) {
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		// int count = c.getCount();
		// 查詢name跟在e001上打得是否有有此筆資料
		String tname = E001.getText().toString().trim();//取得輸入的字並去除空白
		String tgrp = E002.getText().toString().trim();
		String taddress = E003.getText().toString().trim();
		if (tname.equals("") || tgrp.equals("")||taddress.equals("")) {
			Toast.makeText(getApplicationContext(),"有空白欄位還沒填喔",Toast.LENGTH_SHORT).show();
		    return;
		}
		String msg = null;
		
		// -------------------------
		ContentValues newRow = new ContentValues();
		newRow.put("name", tname);
		newRow.put("grp", tgrp);
		newRow.put("address", taddress);
		mContRes.insert(FriendsContentProvider.CONTENT_URI, newRow);
		// -------------------------
		E001.setText("");
		E001.setHint("請輸入下一筆");//設定背景字串
		E002.setText("");
		E003.setText(showip);
		msg = "新增記錄 成功 ! \n" + "目前資料表共有 " + (c.getCount() + 1) + " 筆記錄 !";
//		msg = "新增記錄 失敗 !";
		mySQL_insert(tname,tgrp,taddress);
		
		Toast.makeText(M1405insert.this, msg, Toast.LENGTH_SHORT).show();
		count_t.setText("共計:" + Integer.toString(c.getCount() + 1) + "筆");
		//如果輸入法打開則關閉，如果沒打開則打開
		InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			
			
	}
	private void mySQL_insert(String tname, String tgrp, String taddress) {
		// -------------抓取遠端資料庫設定執行續------------------------------
		StrictMode.setThreadPolicy(new StrictMode
				.ThreadPolicy.Builder()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.build());
		StrictMode.setVmPolicy(new StrictMode
				.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.penaltyLog()
				.penaltyDeath()
				.build());
		// ---------------------------------------------------------------------
		 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		  nameValuePairs.add(new BasicNameValuePair("name", tname));
		  nameValuePairs.add(new BasicNameValuePair("grp", tgrp));
		  nameValuePairs.add(new BasicNameValuePair("address", taddress));
		  try {
		   Thread.sleep(500); //  延遲Thread 睡眠0.5秒
		  } catch (InterruptedException e) {
		   e.printStackTrace();
		  }

		  String result = DBConnector.executeInsert("SELECT * FROM member", nameValuePairs);
		  Log.d(TAG, "result:" + result);
		
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.m1405s, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.m_return) {
			M1405insert.this.finish();//結束這頁面 原來頁面不會刷新
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
