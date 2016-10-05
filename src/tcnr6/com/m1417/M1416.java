package tcnr6.com.m1417;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class M1416 extends Activity {
	private Button b001, b002, b003;
	String sqlctl;
	String TAG = "tcnr6==>";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m1416);
		setupViewComponent();
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
	}

	private void setupViewComponent() {
		// TODO Auto-generated method stub
		b001 = (Button) findViewById(R.id.button1);
		b002 = (Button) findViewById(R.id.button2);
		b003 = (Button) findViewById(R.id.button3);
		b001.setOnClickListener(getDBRecord);
		b002.setOnClickListener(getDBRecord);
		b003.setOnClickListener(getDBRecord);

	}

	private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				sqlctl = "SELECT * FROM member ORDER BY id ASC";
				Mysqlsel(sqlctl);
				break;
			case R.id.button2:
				sqlctl = "SELECT * FROM area ORDER BY id ASC";
				Mysqlsel(sqlctl);
				break;
			case R.id.button3:
				sqlctl = "SELECT member.id, member.name, member.grp, member.address, area.location FROM member, area WHERE member.id = area.fid ORDER BY  member.id ASC";
				         //SELECT "選擇的欄位" FROM "哪張資料表" WHERE 尋找條件(member.id)=值(area.fid) ORDER BY 排序欄位  ASC=遞增 DSC=遞減
				Mysqlsel(sqlctl);
				break;
			}
		}

		private void Mysqlsel(String sqlctl) {
			TableLayout user_list = (TableLayout) findViewById(R.id.user_list);
			user_list.removeAllViews();
			user_list.setStretchAllColumns(true);
			TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			try {
				String result = DBConnector.executeQuery(sqlctl);
				/**************************************************************************
				 * SQL 結果有多筆資料時使用JSONArray 只有一筆資料時直接建立JSONObject物件 JSONObject
				 * jsonData = new JSONObject(result);
				 **************************************************************************/
				JSONArray jsonArray = new JSONArray(result);
				// ---
				Log.d(TAG, jsonArray.toString());
				// --
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonData = jsonArray.getJSONObject(i);

					// // 取出 jsonObject 中的字段的值的空格
					Iterator itt = jsonData.keys();
					TableRow tr = new TableRow(M1416.this);
					tr.setLayoutParams(row_layout);
					tr.setGravity(Gravity.CENTER_HORIZONTAL);
					while (itt.hasNext()) {
						String key = itt.next().toString();
						String value = jsonData.getString(key);
						if (value == null) {
							continue;
						} else if ("".equals(value.trim())) {
							continue;
						} else {
							jsonData.put(key, value.trim());
						}
						// --
						Log.d(TAG, "i:" + i + " key:" + key + " value:" + value);
						// --
						TextView tv = new TextView(M1416.this);// tv 繼承TextView
						tv.setId(i); // 寫入配置碼ID 代號
						tv.setText(value);
						tv.setLayoutParams(view_layout);
						tr.addView(tv);
					}
					user_list.addView(tr);
				}

			} catch (Exception e) {

			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.m1405s, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.m_return) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
