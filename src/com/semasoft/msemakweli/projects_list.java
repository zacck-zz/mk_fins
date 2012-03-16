package com.semasoft.msemakweli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class projects_list extends ListActivity implements OnClickListener,
		Runnable {
	
	private String constituencies[];
	public String projectsin[];
	public String indexin[];
	public String NoOfProjects;
	public Bundle details;

	String pickProj[] = null;
	String area;

	ImageButton btnGo;
	AutoCompleteTextView actvConstituency;

	ProgressDialog pdProjects;

	JSONObject obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.projects_list);
		
		Toast.makeText(projects_list.this, "Please wait while the list of constiuencies is collected", Toast.LENGTH_SHORT).show();
		pickConts p = new pickConts();
		p.execute();

		actvConstituency = (AutoCompleteTextView) findViewById(R.id.ac_constituency);
		

		btnGo = (ImageButton) findViewById(R.id.btnGo);

		btnGo.setOnClickListener(this);

	}

	public void onClick(View v) {

		if (v == btnGo) {
			String constituency = actvConstituency.getText().toString();
			if (constituency.equals("")) {
				neterror("Please enter the name of the constituency that you want to see projects on.");
			} else {
				pdProjects = ProgressDialog.show(projects_list.this, "",
						"Receiving Data...", false);

				Thread getConstituencies = new Thread(this);
				getConstituencies.start();
			}
		}
	}

	public void run() {

		// create a client
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://semasoftltd.com/webs/selected_projects.php");
		HttpResponse response = null;

		// do the posting
		try {

			String constituency = actvConstituency.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("const", constituency));

			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
			post.setEntity(ent);

			response = httpclient.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					final JSONArray entries = new JSONArray(
							convertStreamToString(instream));

					if (entries.length() == 0) {
						// customerID = "There are no users.\n";
					} else {
						projectsin = new String[entries.length()];
						indexin = new String[entries.length()];
						NoOfProjects = "" + entries.length();

						for (int i = 0; i < entries.length(); i++) {
							obj = entries.getJSONObject(i);
							projectsin[i] = obj.getString("Project_Name");
							indexin[i] = obj.getString("index");
						}

						handlerWin.sendEmptyMessage(0);
					}

				}

			} else {
				// handler.sendEmptyMessage(0);
				neterror("Network connection error 2.");
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	private Handler handlerWin = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			pdProjects.dismiss();

			setListAdapter(new ArrayAdapter<String>(getBaseContext(),
					R.layout.list_item_proj, projectsin));

			TextView tv = (TextView) findViewById(R.id.tv);
			tv.setText("Projects in constituency: (" + NoOfProjects + ")");

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getApplicationContext(),
							((TextView) view).getText(), Toast.LENGTH_SHORT)
							.show();

					Intent i = new Intent(projects_list.this, mkviewTWO.class);
					i.putExtra("index", indexin[position]);
					startActivity(new Intent(i));

				}
			});

		}
	};

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void neterror(String nerr) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Notification");
		alertDialog.setMessage(nerr);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
			}
		});
		alertDialog.show();
	}

	// reads the input stream to a string
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

	public class pickConts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);'
			//Toast.makeText(projects_list.this, "The List Has been collected now choose your constituency", Toast.LENGTH_LONG).show();
			actvConstituency.requestFocus();
			actvConstituency.setEnabled(true);
			ArrayAdapter<String> constituencyListAdapter = new ArrayAdapter<String>(
					projects_list.this, R.layout.list_item, constituencies);
			actvConstituency.setAdapter(constituencyListAdapter);
		}
		

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			//super.onProgressUpdate(values);
			

		}


		@Override
		protected Void doInBackground(Void... params) {
			
			//deactivate
			actvConstituency.setEnabled(false);
			//Toast.makeText(projects_list.this, "Please wait while the list of constiuencies is collected", Toast.LENGTH_LONG).show();

			// create a client
			HttpClient cli = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://semasoftltd.com/webs/cons.php");
			HttpResponse response = null;
			try {
				response = cli.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					String s = EntityUtils.toString(response.getEntity());
					JSONArray ja = new JSONArray(s);
					constituencies = new String[ja.length()];
					for (int i = 0; i < ja.length(); i++) {
						JSONObject c = ja.getJSONObject(i);
						constituencies[i] = c.getString("Constituency");
						Log.v("CONST", c.getString("Constituency")+"this");
						if (i/100.0==1.0||i/100.0==2.0){
							publishProgress();
						}
						
					}

				}

			} catch (Exception e) {

			}

			return null;
		}
	}

}
