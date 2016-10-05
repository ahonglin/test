package tcnr6.com.m1417;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import tcnr6.com.m1417.providers.FriendsContentProvider;

public class M1405update extends Activity implements OnClickListener {
	private Button btnBack, btnNext, btnDel, btnUpdate;
	private EditText E001, E002, E003, E004;
	private TextView count_t;
	// ---------------
	private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "name", "grp", "address" };
	int tcount;
	// ---------------

	// private FriendDBHelper DBHelper;
	// //<!-------------------------------------->
	// private static final String DB_FILE="friends.db";
	// private static final String DB_TABLE="member";
	// public static final int VERSION = 1;
	//// private SQLiteDatabase mFriendDB;
	// //<!--------------------------------------->
	private int index = 0;
	// private ArrayList<String> recSet;
	String msg = "";
	String TAG="tcnr6==>";

	private float x, y, x1, x2, y1, y2;
	int range, angle, angleThreshold = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1405update);
		setupviewcomponent();

	}

	private void setupviewcomponent() {
		// ----------------------------------------------
		mContRes = getContentResolver();
		// ----------------------------------------------
		E001 = (EditText) findViewById(R.id.editName);
		E002 = (EditText) findViewById(R.id.editGrp);
		E003 = (EditText) findViewById(R.id.editAddr);
		E004 = (EditText) findViewById(R.id.editID);
		count_t = (TextView) findViewById(R.id.count_t);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnDel = (Button) findViewById(R.id.btnDel);

		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDel.setOnClickListener(this);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		range = displayMetrics.widthPixels * 1 / 4;

		// ---------檢查是否由上層指定修改的資料------
		Intent intent = this.getIntent();
		int iselectno = intent.getIntExtra("up_item", 0);
		if (iselectno != 0) {
			index = iselectno;
			iselectno = 0;
		}
		// ----------------------------------------------
		showRec(index);
	}

	// ----------------------------------------
	private ContentValues FillRec() { // user define method
		ContentValues contVal = new ContentValues();
		contVal.put("id", E004.getText().toString());
		contVal.put("name", E001.getText().toString());
		contVal.put("grp", E002.getText().toString());
		contVal.put("address", E003.getText().toString());
		return contVal;
	}
	// ---------------------------------------

	private void showRec(int idx) {// 顯示資料內容
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, null, null, null);
		if (c == null)
			return;

		if (c.getCount() != 0) {// 資料不等於0
			// if(idx>=recSet.size()){//假如要顯示的資料位置大於List<>就重頭
			// idx=0;
			// }
			String countStr = "顯示資料:第" + (idx + 1) + "筆/總共" + c.getCount() + "筆";
			tcount = c.getCount();
			count_t.setText(countStr);
			c.moveToPosition(index);

			E004.setTextColor(getResources().getColor(R.drawable.Red));
			E004.setText(c.getString(0));// id
			E001.setText(c.getString(1));// name
			E002.setText(c.getString(2));// grp
			E003.setText(c.getString(3));// address

		} else {
			E001.setText("");
			E002.setText("");
			E003.setText("");
			E004.setText("");
			Toast.makeText(M1405update.this, "查無資料", Toast.LENGTH_SHORT).show();
			String stHead = "顯示資料：0 筆";

			count_t.setText(stHead);

		}
		// Bundle bundle = this.getIntent().getExtras();
		// String myID=bundle.getString("KEY_ID");
		// String myName=bundle.getString("KEY_NAME");
		// String myGroup=bundle.getString("KEY_GROUP");
		// String myAddr=bundle.getString("KEY_ADDRESS");
		// E001.setText(myName);E002.setText(myGroup);E003.setText(myAddr);E004.setText(myID);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String tid, tname, tgrp, taddr;
		int rowsAffected;
		Uri uri;
		String s_id;
		String whereClause;
		String[] selectionArgs;
		// -------------------------------------------

		switch (v.getId()) {
		case R.id.btnBack:// 上一筆
			ctlBack();
			break;
		case R.id.btnNext:// 下一筆
			ctlNext();
			break;
		case R.id.btnUpdate:// 更新資料
			// 資料更新
			uri = FriendsContentProvider.CONTENT_URI;
			ContentValues contVal = new ContentValues();
			contVal = FillRec();//取得要修改的數據
			tid = E004.getText().toString().trim();
			tname=E001.getText().toString().trim();
			tgrp=E002.getText().toString().trim();
			taddr=E003.getText().toString().trim();
			
			whereClause = "id='" + tid + "'";
			selectionArgs = null;
			rowsAffected = mContRes.update(uri, contVal, whereClause, selectionArgs);

			if (rowsAffected == -1) {
				msg = "資料表已空, 無法修改 !";
			} else if (rowsAffected == 0) {
				msg = "找不到欲修改的記錄, 無法修改 !";
			} else {
				msg = "第 " + (index + 1) + " 筆記錄  已修改 ! \n" + "共 " + rowsAffected + " 筆記錄   被修改 !";

				showRec(index);
			}
			// 如果輸入法打開則關閉，如果沒打開則打開
			InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			
			mySQL_update(tid,tname,tgrp,taddr);
			break;
		case R.id.btnDel:// 刪除資料
			// 刪除資料
			uri = FriendsContentProvider.CONTENT_URI;
			tid = E004.getText().toString().trim();
			whereClause = "id='" + tid + "'";
			selectionArgs = null;
			rowsAffected = mContRes.delete(uri, whereClause, selectionArgs);

			if (rowsAffected == -1) {
				msg = "資料表已空, 無法刪除 !";
			} else if (rowsAffected == 0) {
				msg = "找不到欲刪除的記錄, 無法刪除 !";
			} else {
				msg = "第 " + (index + 1) + " 筆記錄  已刪除 ! \n" + "共 " + rowsAffected + " 筆記錄   被刪除 !";

				showRec(0);
			}
			mySQL_delete(tid);
			break;
		}

	}

	private void mySQL_delete(String tid) {
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
		  nameValuePairs.add(new BasicNameValuePair("id", tid));

		  String result = DBConnector.executeDelet("delet", nameValuePairs);
		  Log.d(TAG, "result:" + result);
		  Toast.makeText(getApplicationContext(), "mySQL:"+result, Toast.LENGTH_LONG).show();
		
	}

	private void mySQL_update(String tid, String tname, String tgrp, String taddr) {
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
		  nameValuePairs.add(new BasicNameValuePair("id", tid));
		  nameValuePairs.add(new BasicNameValuePair("name", tname));
		  nameValuePairs.add(new BasicNameValuePair("grp", tgrp));
		  nameValuePairs.add(new BasicNameValuePair("address", taddr));
		  String result = DBConnector.executeUpdate("update", nameValuePairs);
		  Log.d(TAG, "result:" + result);
		  Toast.makeText(getApplicationContext(), "mySQL:"+result, Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) { // 可以用log.d看參數
		x = event.getX();
		y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x1 = event.getX();
			y1 = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:

			return true;
		case MotionEvent.ACTION_UP:
			x2 = x;
			y2 = y;
			float xbar = Math.abs(x2 - x1);// x
			float ybar = Math.abs(y2 - y1);// y
			Double z = Math.sqrt(xbar * xbar + ybar * ybar);// 開根號 得出斜邊長
			angle = Math.round((float) (Math.asin(ybar / z) / Math.PI * 180));

			if (x1 != 0 && y1 != 0) {
				if (x1 - x2 > range) {// 向左滑動
					ctlNext();
				}
				if (x2 - x1 > range) { // 向右滑動
					ctlBack();

				}
				if (y2 - y1 > range && angle > angleThreshold) { // 向下滑動
					ctlLast();
				}
				if (y1 - y2 > range && angle > angleThreshold) { // 向上滑動
					ctlFirst();
				}

			}

		}

		return super.onTouchEvent(event);
	}

	private void ctlBack() {// 上一筆
		index--;
		if (index < 0) {
			index = tcount - 1;
		}
		showRec(index);
	}

	private void ctlNext() {// 下一筆
		index++;
		if (index >= tcount) {
			index = 0;
		}
		showRec(index);
	}

	private void ctlFirst() {
		index = 0;
		showRec(index);
	}

	private void ctlLast() {
		index = tcount - 1;
		showRec(index);
	}
	// private void ctlDel(int rowsAffected) {
	// // TODO Auto-generated method stub
	// msg="";
	// if(rowsAffected==-1){
	// msg = "資料表已空, 無法刪除!";
	// }else if(rowsAffected==0){
	// msg = "找不到要刪除的記錄, 無法刪除!";
	// }else{
	// msg = "第" + (index + 1) + "筆記錄 已刪除! \n" + "共" + rowsAffected + " 筆記錄
	// 被刪除!";
	// recSet = DBHelper.getRecSet();
	// showRec(index);//顯示刪除資料的下一筆
	// }
	// Toast.makeText(M1405update.this, msg, Toast.LENGTH_LONG).show();
	// }
	// private void ctlUpdate(int rowsAffected) {
	// msg="";
	// if(rowsAffected==-1){
	// msg="資料庫沒有資料可以修改";
	// }else if(rowsAffected==0){
	// msg="找不到要修改的資料";
	// }else{
	// msg = "第" + (index + 1) + "筆記錄 已修改! \n" + "共" + rowsAffected + " 筆記錄
	// 被修改!";
	// recSet = DBHelper.getRecSet();
	// showRec(index);//顯示修改位置的資料
	// }
	// Toast.makeText(M1405update.this, msg, Toast.LENGTH_LONG).show();
	// }
	// @Override
	// protected void onResume() {//從新開啟DataBase
	// // TODO Auto-generated method stub
	//
	// showRec(index);
	// super.onResume();
	// }

	// @Override
	// protected void onPause() {//關閉 並清除DataBase
	// // TODO Auto-generated method stub
	// super.onPause();
	// if (DBHelper != null) {
	// DBHelper.close();
	// DBHelper = null;
	// }
	// recSet.clear();
	// }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.m1405s, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case R.id.m_return:
			M1405update.this.finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

}
