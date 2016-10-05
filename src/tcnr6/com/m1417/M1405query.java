package tcnr6.com.m1417;

import android.app.Activity;
import android.content.ContentResolver;

import android.database.Cursor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tcnr6.com.m1417.providers.FriendsContentProvider;


public class M1405query extends Activity implements OnClickListener {

	private EditText E001,E002,E003,E004;
	private Button B001,B002,B003;
	private TextView count_t,mEdtList;
	
	private static ContentResolver mContRes;
	String[] MYCOLUMN = new String[] { "id", "name", "grp", "address" };
	
//    private FriendDBHelper DBHelper;
//<!-------------------------------------->	
//	private static final String DB_FILE="friends.db";
//	private static final String DB_TABLE="member";
//	public static final int VERSION = 1; 
//	private SQLiteDatabase mFriendDB;
//<!--------------------------------------->	
//	private EditText mEdtName, mEdtGrp, mEdtAddr, mEdtList;
//	private Button mBtnAdd, mBtnQuery, mBtnList;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1405query);
		setupviewcomponent();
		
		
	}



	private void setupviewcomponent() {
		// TODO Auto-generated method stub
		E001=(EditText)findViewById(R.id.editName);
		E002=(EditText)findViewById(R.id.editGrp);
		E003=(EditText)findViewById(R.id.editAddr);
		count_t=(TextView)findViewById(R.id.count_t);
		mEdtList = (TextView) findViewById(R.id.edtList);
		B001=(Button)findViewById(R.id.btnBack);
		
		// -----------------------------------------------
		mContRes = getContentResolver();
		// -----------------------------------------------
		
		B001.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
//		String result = null;
//		// 查詢name跟在e001上打得是否有有此筆資料
//		String tname = E001.getText().toString().trim();
//		if (tname.length() != 0) {
//			String rec = DBHelper.FindRec(tname);
//			if (rec != null) {
//				result = "找到的資料為 ：\n" + rec;
//			}else{
//				result = "找不到指定的編號：" + tname;
//			}
//			Toast.makeText(M1405query.this, result, Toast.LENGTH_LONG).show();
//		}else{
//			Toast.makeText(M1405query.this,"輸入錯誤 白癡!" ,Toast.LENGTH_SHORT).show();
//		}
		
		Cursor c = null;
		//public Cursor query(Uri uri, String[] projection, 
		//String selection,String[] selectionArgs, String sortOrder) 

		if (!E001.getText().toString().equals("")) {
			// -----------使用姓名查詢 條件:類似 -------------
			String myselection = " name LIKE ?";
			String[] myargs = { "%" + E001.getText().toString() + "%" };

			// -----------使用姓名查詢 條件:= ----------------
			// String myselection =" name = ?";
			// String[] myargs = { e001.getText().toString()};
			// -------------------------------------------------

			String myorder = "grp ASC"; // 排序欄位
			c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, myselection, myargs, myorder);

		} else if (!E002.getText().toString().equals("")) {
			String myselection =" grp LIKE ?";
			String[] myargs = {"%" + E002.getText().toString() + "%"};
			String  myorder = "grp ASC"; //排序欄位
			c =mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, myselection, myargs, myorder);


		} else if (!E003.getText().toString().equals("")) {
			String myselection =" address LIKE ?";
			String[] myargs = {"%" + E003.getText().toString() + "%"};
			String  myorder = "grp ASC"; //排序欄位
			c =mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN, myselection, myargs, myorder);


		}
		if (c == null)
			return;

		if (c.getCount() == 0) {
			count_t.setText("");
			Toast.makeText(M1405query.this, "沒有這筆資料", Toast.LENGTH_LONG)
					.show();
		} else {
			c.moveToFirst();
			mEdtList.setText(c.getString(0) + " " + c.getString(1) + " "
					+ c.getString(2)+ " " + c.getString(3) );

			while (c.moveToNext())
				mEdtList.append("\n" + c.getString(0) + " "
						+ c.getString(1) + " " + c.getString(2)+ " " + c.getString(3));

		}
		count_t.setText("共計:" + Integer.toString(c.getCount()) + "筆");
		c.close();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		onCreate(null);//從刷onCreate()
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
			M1405query.this.finish();//結束這頁面 原來頁面不會刷新
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
