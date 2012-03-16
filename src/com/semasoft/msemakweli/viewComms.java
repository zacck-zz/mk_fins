package com.semasoft.msemakweli;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;


public class viewComms extends ListActivity {
	
	public String names[];
	public String commsFromDb[];
	//public String times[];


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);
		
		//create http client to gather comments
		try{
		HttpClient getComment = new DefaultHttpClient();
		HttpGet pullComm = new HttpGet();
		pullComm.setURI(new URI("http://semasoftltd.com/webs/getComments.php"));
		HttpResponse comments = getComment.execute(pullComm);
		if (comments.getStatusLine().getStatusCode() == 200) {
			HttpEntity entit = comments.getEntity();
			if (entit !=null){
				InputStream commStream = entit.getContent();
				JSONArray commentAR = new JSONArray(
						convertStreamToString(commStream));
				if (commentAR.length()==0){
					
				}else{
					names = new String[commentAR.length()];
					commsFromDb = new String [commentAR.length()];
					//times = new String [commentAR.length()];
					
					for (int i = 0; i < commentAR.length(); i++) {
						JSONObject OComm = commentAR.getJSONObject(i);
						names[i] = OComm.getString("name");
						commsFromDb[i] = OComm.getString("suggestion");
						//times[i] = OComm.getString("time");
					}
					
					
					ArrayList<HashMap<String, String>> commList = new ArrayList<HashMap<String, String>>();

					for (int j=0; j<commentAR.length(); j++){
						HashMap<String, String> objComm = new HashMap<String, String>();
						objComm.put("name", names[j]);
						Log.v("coms", names[j] );
						objComm.put("suggestion", commsFromDb[j]);
						Log.v("coms", commsFromDb[j] );
						//objComm.put("time", times[j]);
						//Log.v("coms", names[j] );
						commList.add(objComm);						
						
					}
					

					SimpleAdapter adapter =new SimpleAdapter(
							this,
							commList,
							R.layout.comment_row,
							new String[]{"name","suggestion","name"},
							new int[]{R.id.CommName, R.id.CommText, R.id.CommTime}
							);
					
					setListAdapter(adapter);
					
						
					
					
					
					
					
					
					
				}
				
			}
			
		}
		
		
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
		
	}
	
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	

}
