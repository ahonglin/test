package tcnr6.com.m1417;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


import android.util.Log;


public class DBConnector {
	static String TAG = "tcnr6==>";
	static String result = "",line;
	static String connect_ip="http://tcnr6hung.16mb.com/android_mysql_connect/";
//	static String connect_ip="http://belovedcck.96.lt/";//班長
	static InputStream is;
	static int code;
	
	// 宣告類別變數以方便存取，並判斷是否連線成功
	 public static int httpstate = 0;
	 
	
	public static String executeQuery(String query_string) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(connect_ip+"sqlConn.php");

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("query_string", query_string));
			
			// query_string -> 給php 使用的參數
			Log.d(TAG, "Value=" + params.toString());

			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// -----------------------------------------------------------------   
			   // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
			   httpstate = httpResponse.getStatusLine().getStatusCode();
			   // -----------------------------------------------------------------
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();

			Log.d(TAG, "inputStream=" + inputStream.toString());

			BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;

			while ((line = bufReader.readLine()) != null) {
				builder.append(line + "\n");
			}
			inputStream.close();
			result = builder.toString();
			Log.d(TAG, "result=" + result);

		} catch (Exception e) {
			Log.d(TAG, "Exception=" + e.toString());
		}
		return result;

	}

	public static String executeInsert(String string, ArrayList<NameValuePair> nameValuePairs) {
		   is = null;
		   result = null;
		   line = null;
		   try {
		    Thread.sleep(500); //  延遲Thread 睡眠0.5秒
		   } catch (InterruptedException e) {
		    e.printStackTrace();
		   }
		  //---- 連結MySQL------------------- 
		    try {
		     HttpClient httpclient = new DefaultHttpClient();
		     HttpPost httppost = new HttpPost(connect_ip+"sqlInsert.php");

		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
		       HTTP.UTF_8));
		     HttpResponse response = httpclient.execute(httppost); //

		     
		     HttpEntity entity = response.getEntity();
		     is = entity.getContent();
		     Log.d(TAG, "pass 1:"+"connection success ");
		    } catch (Exception e) {
		     Log.d(TAG, "Fail 1"+e.toString());
		    }
		    try {
		     BufferedReader reader = new BufferedReader(new InputStreamReader(
		       is, "utf-8"), 8);
		     StringBuilder sb = new StringBuilder();
		     while ((line = reader.readLine()) != null) {
		      sb.append(line + "\n");
		     }
		     is.close();
		     result = sb.toString();
		     Log.d(TAG, "pass 2:"+"connection success ");
		    } catch (Exception e) {
		     Log.d(TAG, "Fail 2:"+e.toString());
		    }
		    try {
		     JSONObject json_data = new JSONObject(result);
		     code = (json_data.getInt("code"));

		     if (code == 1) {
		      Log.d(TAG, "pass 3:"+"Inserted Successfully");
		     } else {
		      Log.d(TAG, "pass 3:"+"Sorry, Try Again");
		     }
		    } catch (Exception e) {
		     Log.d(TAG, "Fail 3:"+e.toString());
		    }
		  return result;
		
	}
	public static String executeUpdate(String string, ArrayList<NameValuePair> nameValuePairs) {

		  String update_code=null;
		   is = null;
		   result = null;
		   line = null;
		  try {
		   Thread.sleep(500);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		//
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(connect_ip+"sqlUpdate.php");//設定要傳到哪裡
		  
		  try {
		   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
		  } catch (UnsupportedEncodingException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		  try {
			  
		   HttpResponse response;//
		   response = httpClient.execute(httpPost); //執行
		   HttpEntity entity = response.getEntity();
		   try {
		    is = entity.getContent(); // InputStream is = null;

		   } catch (IllegalStateException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   } catch (ClientProtocolException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   } catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   }

		  } catch (IOException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		  Log.d(TAG,"pass 1:"+ "connection success");

		  try {
		   BufferedReader reader = new BufferedReader(
		     new InputStreamReader(is, "utf8"), 8);
		   StringBuilder sb = new StringBuilder();
		   while ((line = reader.readLine()) != null) {
		    sb.append(line + "\n");
		   }
		   is.close();
		   result = sb.toString();
		   Log.d(TAG,"pass 2:"+ "connection success");
		  } catch (Exception e) {
		   Log.d(TAG,"Fail 2:"+ e.toString());
		  }
		  try {
		   JSONObject json_data = new JSONObject(result);
		   code = (json_data.getInt("code"));
		   if (code == 1) {
		    update_code= "updata Successfully";
		   } else {
		    update_code=  "Sorry,Try Again";
		   }
		  } catch (Exception e) {
		   Log.d(TAG,"Fail 3:"+ e.toString());
		  }
		  return update_code;
		 }

//	public static String executeUpdate(String string, ArrayList<NameValuePair> nameValuePairs) {
//		   String update_code=null;
//		   is = null;
//		   result = null;
//		   line = null;
//		   try {
//		    Thread.sleep(500); //  延遲Thread 睡眠0.5秒
//		   } catch (InterruptedException e) {
//		    e.printStackTrace();
//		   }
//		  //---- 連結MySQL------------------- 
//		    try {
//		     HttpClient httpclient = new DefaultHttpClient();
//		     HttpPost httppost = new HttpPost(connect_ip+"sqlUpdate.php");
//
//		     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
//		       HTTP.UTF_8));
//		     HttpResponse response = httpclient.execute(httppost); ///
//		     HttpEntity entity = response.getEntity();
//		     is = entity.getContent();
//		     Log.d(TAG, "pass 1:"+"connection success ");
//		    } catch (Exception e) {
//		     Log.d(TAG, "Fail 1"+e.toString());
//		    }
//		    try {
//		     BufferedReader reader = new BufferedReader(new InputStreamReader(
//		       is, "utf-8"), 8);
//		     StringBuilder sb = new StringBuilder();
//		     while ((line = reader.readLine()) != null) {
//		      sb.append(line + "\n");
//		     }
//		     is.close();
//		     result = sb.toString();
//		     Log.d(TAG, "pass 2:"+"connection success ");
//		    } catch (Exception e) {
//		     Log.d(TAG, "Fail 2:"+e.toString());
//		    }
//		    try {
//		     JSONObject json_data = new JSONObject(result);
//		     code = (json_data.getInt("code"));
//
//		     if (code == 1) {
//		    	 update_code="Update Successfully";
//		      Log.d(TAG, "pass 3:"+"Update Successfully");
//		     } else {
//		    	 update_code="Sorry, Try Again";
//		      Log.d(TAG, "pass 3:"+"Sorry, Try Again");
//		     }
//		    } catch (Exception e) {
//		     Log.d(TAG, "Fail 3:"+e.toString());
//		    }
//		  return update_code;
//		
//	}
	public static String executeDelet(String string, ArrayList<NameValuePair> nameValuePairs) {
		   is = null;
		   result = null;
		   line = null;
		  String mysql_code=null;
		  Log.d(TAG, "value="+nameValuePairs);
		  try {
		   Thread.sleep(500);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		//--------------------------------------------------------------------------------------

		  
		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(connect_ip+"sqlDelete.php");
		  
		  try {
		   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
		     HTTP.UTF_8));
		  } catch (UnsupportedEncodingException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		  try {
		   HttpResponse response;
		   response = httpClient.execute(httpPost);
		   HttpEntity entity = response.getEntity();
		   try {
		    is = entity.getContent();
		   } catch (IllegalStateException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   } catch (ClientProtocolException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   } catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		   }

		  } catch (IOException e1) {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
		  }
		  Log.d(TAG,"pass 1:"+ "connection success");

		  try {
		   BufferedReader reader = new BufferedReader(
		     new InputStreamReader(is, "utf8"), 8);
		   StringBuilder sb = new StringBuilder();
		   while ((line = reader.readLine()) != null) {
		    sb.append(line + "\n");
		   }
		   is.close();
		   result = sb.toString();
		   Log.d(TAG,"pass 2:"+ "connection success");
		  } catch (Exception e) {
		   Log.d(TAG,"Fail 2:"+ e.toString());
		  }
		  try {
		   JSONObject json_data = new JSONObject(result);
		   code = (json_data.getInt("code"));
		   if (code == 1) {
		    mysql_code= "updata Successfully";
		   } else {
		    mysql_code=  "Sorry,Try Again";
		   }

		  } catch (Exception e) {
		   Log.d(TAG,"Fail 3:"+ e.toString());
		  }
		  return mysql_code;
		 }	

}
