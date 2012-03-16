package com.semasoft.msemakweli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mkviewTWO extends Activity {

	public String projectname[];
	public String ExOut[];
	public String ActDone[];
	public String Impl[];
	public String EstCost[];
	public String TotAmt[];
	public String Rem[];
	public String Index;

	TextView tvProjName;
	TextView tvExOut;
	TextView tvActDone;
	TextView tvImpl;
	TextView tvEstCost;
	TextView tvTotAmt;
	TextView tvRem;

	String projLocation;
	String update;

	Button btShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewreports_2);

		Button btPic = (Button) findViewById(R.id.btnPicture2);
		Button btComm;
		btShare = (Button) findViewById(R.id.btnShare);
		btComm = (Button) findViewById(R.id.btnComment);

		tvProjName = (TextView) findViewById(R.id.tvProjectName);
		tvExOut = (TextView) findViewById(R.id.tvExpectedOutput);
		tvActDone = (TextView) findViewById(R.id.tvActivityToBeDone);
		tvImpl = (TextView) findViewById(R.id.tvImplementationStatus);
		tvEstCost = (TextView) findViewById(R.id.tvEstimatedCost);
		tvTotAmt = (TextView) findViewById(R.id.tvTotalAmount);
		tvRem = (TextView) findViewById(R.id.tvRemarks);

		String nameP;

		Bundle Bindex = getIntent().getExtras();
		if (Bindex != null) {
			Index = Bindex.getString("index");

		}

		// gather the rest of the project data
		HttpClient projData = new DefaultHttpClient();
		HttpPost postIndex = new HttpPost(
				"http://semasoftltd.com/webs/project_metadata.php");
		HttpResponse restData = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("index", Index));

			UrlEncodedFormEntity ent1 = new UrlEncodedFormEntity(params);
			postIndex.setEntity(ent1);

			restData = projData.execute(postIndex);

			if (restData.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity1 = restData.getEntity();
				if (entity1 != null) {
					InputStream instream1 = entity1.getContent();
					JSONArray entries1 = new JSONArray(
							convertStreamToString(instream1));
					if (entries1.length() == 0) {
						// customerID = "There are no users.\n";
					} else {
						projectname = new String[entries1.length()];
						ExOut = new String[entries1.length()];
						ActDone = new String[entries1.length()];
						Impl = new String[entries1.length()];
						EstCost = new String[entries1.length()];
						TotAmt = new String[entries1.length()];
						Rem = new String[entries1.length()];

						for (int i = 0; i < entries1.length(); i++) {
							JSONObject obj1 = entries1.getJSONObject(i);
							projectname[i] = obj1.getString("Project_Name");
							ExOut[i] = obj1.getString("Expected_output");
							ActDone[i] = obj1.getString("Activity_To_Be_Done");
							Impl[i] = obj1.getString("Implementation_Status");
							EstCost[i] = obj1.getString("Estimated_Cost");
							TotAmt[i] = obj1.getString("Total_Amount");
							Rem[i] = obj1.getString("Remarks");

							tvProjName.setText(" " + projectname[i]);
							tvExOut.setText(" " + ExOut[i]);
							tvActDone.setText(" " + ActDone[i]);
							tvImpl.setText(" " + Impl[i]);
							tvEstCost.setText(" Ksh. " + EstCost[i]);
							tvTotAmt.setText(" Ksh. " + TotAmt[i]);
							tvRem.setText(" " + Rem[i]);

							projLocation = obj1.getString("Location1");

							update = "Project Name: "+projectname[i] + "\n" + "Expected Output: "+ExOut[i] + "\n"
									+"Activity to be Done: "+ ActDone[i] + "\n" +"Implementation : "+ Impl[i] + "\n"
									+ "Estimated Cost In Ksh. " + EstCost[i] + "\n" + " Ksh. "
									+"Total Amount: "+ TotAmt[i]+ "\n" +"Remarks: "+Rem[i];

						}

					}

				}

			}
		} catch (UnsupportedEncodingException e) {
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
		}

		btComm.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mkviewTWO.this, mkComment.class);
				i.putExtra("pname", tvProjName.getText().toString());
				startActivity(i);
			}
		});

		btShare.setOnClickListener(new View.OnClickListener() {
			Bundle b = new Bundle();

			public void onClick(View v) {
				Intent n = new Intent(mkviewTWO.this, Share.class);
				b.putString("up", update);
				n.putExtras(b);
				startActivity(n);
			}

		});
	}
	

	@Override
	protected void onPause() {
		super.onPause();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.project_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.proj_photos:
			showPhotos();
			return true;
		case R.id.proj_location:
			showLocation();
			return true;
		case R.id.proj_comments:
			showComments();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showComments() {
		Intent comm = new Intent(mkviewTWO.this, viewComms.class);
		startActivity(new Intent(comm));

	}

	private void showLocation() {
		Intent i = new Intent(mkviewTWO.this, mkviewTWO_location.class);
		i.putExtra("projectName", tvProjName.getText());
		i.putExtra("projLocation", projLocation);
		startActivity(new Intent(i));
	}

	public void showPhotos() {

	}

}