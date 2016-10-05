package tcnr6.com.m1417;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import tcnr6.com.m1417.providers.FriendsContentProvider;





public class M1405list extends ListActivity {

//	private static final String DB_FILE="friends.db";
//	private static final String DB_TABLE="member";
//	public static final int VERSION = 1; 
//	private SQLiteDatabase mFriendDB;
//	private FriendDBHelper DBHelper;
	
	
//  	private ArrayList<String> recSet;
	
	// --------------------
	private static ContentResolver mContRes;
	private String[] MYCOLUMN = new String[] { "id", "name", "grp", "address" };
	int tcount;
	// ---------------------
	/** Called when the activity is first created. */
	String msg ="";
	List<Map<String, Object>>mList;
	private TextView count_t;
	
	MyAlertDialog myAlertDialog;
	protected static final int BUTTON_POSITIVE=-1,
			                   BUTTON_NEGATIVE=-2,
			                   BUTTON_NEUTRAL=-3;
	String TAG="tcnr6==>";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1405list);
		Log.d(TAG, "xxxxxxx");
		
		
		
		ListRec();
	}
	
	

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
	
	private void ListRec() {
		// TODO Auto-generated method stub
		count_t=(TextView)findViewById(R.id.m1405list_T001);
		myAlertDialog=new MyAlertDialog(M1405list.this);
		mList=new ArrayList<Map<String,Object>>();
		

		// -------------------------
		mContRes = getContentResolver();
		//---------------------------
		Cursor c = mContRes.query(FriendsContentProvider.CONTENT_URI, MYCOLUMN,
				null, null, null);
		c.moveToFirst(); // 一定要寫，不然會出錯
		tcount = c.getCount();
		// ---------------------------
		count_t.setTextColor(Color.BLUE);
		count_t.setText("顯示資料： 共" + tcount + " 筆");
		// ---------------------------------------------------------
		 for (int i = 0; i < tcount; i++) {
			   Map<String, Object> item = new HashMap<String, Object>();
			   c.moveToPosition(i);
			   
			   item.put("imgView", R.drawable.userconfig);
			   item.put("txtView", c.getString(0) + " " + c.getString(1) + " "
						+ c.getString(2) + " " + c.getString(3));
			   mList.add(item);
			}
			c.close();

		  SimpleAdapter adapter=new SimpleAdapter(this, mList, 
					R.layout.list_item,
					new String[]{"imgView","txtView"},
					new int[]{R.id.imgView,R.id.txtView});
		  setListAdapter(adapter);
			
			ListView listView=getListView();
			listView.setTextFilterEnabled(true);
		    listView.setOnItemClickListener(listViewOnItemClkLis);
		  
		
		
	}
	AdapterView.OnItemClickListener listViewOnItemClkLis=new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			  String s = "你按下第 "+Integer.toString(position+1)+"筆  "+((TextView) view.findViewById(R.id.txtView)).getText()
			          .toString() + "  檢視";
			  count_t.setText(s);
		}
		
	};


	
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
			Intent intent=new Intent();
			intent.setClass(M1405list.this,M1405insert.class);
			startActivity(intent);
			break;
		case R.id.m_update:
			Intent intentu=new Intent();
			intentu.setClass(M1405list.this,M1405update.class);
			startActivity(intentu);
			break;
		case R.id.m_query:
			Intent intentq=new Intent();
			intentq.setClass(M1405list.this,M1405query.class);
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
						Toast.makeText(M1405list.this, msg, Toast.LENGTH_SHORT).show();
						c.close();
						onCreate(null); // 重構
			break;
		case R.id.m_list:
			Intent intentlist=new Intent();
			intentlist.setClass(M1405list.this,M1405list.class);
			startActivity(intentlist);
			break;
		case R.id.m_spinner:
			Intent intentspin=new Intent();
			intentspin.setClass(M1405list.this,M1405spinner.class);
			startActivity(intentspin);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void u_cleanDialog() {
		
		
		myAlertDialog.setTitle("警告");
		myAlertDialog.setIcon(android.R.drawable.btn_star_big_on);
		myAlertDialog.setMessage("確定要刪除所有資料?");
		myAlertDialog.setCancelable(false);
		myAlertDialog.setButton(BUTTON_POSITIVE,"Cancel",btnYes);//-1(button位置,顯示字串,設定監聽變數)
		myAlertDialog.setButton(BUTTON_NEGATIVE,"Yes", btnYes);//-2
		myAlertDialog.setButton(BUTTON_NEUTRAL,"No",btnYes);//-3
		myAlertDialog.show();
	}

	private DialogInterface.OnClickListener btnYes = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch(which){
			case BUTTON_POSITIVE://cancel
				
				break;
			case BUTTON_NEGATIVE://Yes
				Uri uri = FriendsContentProvider.CONTENT_URI;
				mContRes.delete(uri, null, null); // 刪除所有資料
				msg = "資料表已空 !";
				Toast.makeText(M1405list.this, msg, Toast.LENGTH_SHORT).show();

				break;
			case BUTTON_NEUTRAL://No
				msg = "放棄刪除所有資料 !";
				Toast.makeText(M1405list.this, msg, Toast.LENGTH_SHORT).show();
				break;
			
			}
		}
		
	};

}
