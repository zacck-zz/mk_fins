package com.semasoft.msemakweli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Share extends Activity {

	TextView tv;
	Facebook facebook = new Facebook("249110901770081");
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	Button BPost;
	long expires;
	String access_token;
	String uppy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		tv = (TextView)findViewById(R.id.tvShareData);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			uppy = b.getString("up");
		}else{
			uppy = "empty :( !!";
		}
		tv.setText(uppy);
		
		BPost = (Button)findViewById(R.id.btPost);
		

		mPrefs = getPreferences(MODE_PRIVATE);

		// facebook set up
		// check for token and when it expires
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		// call authorize if the token has not expired
		if (!facebook.isSessionValid()) {
			// authorize
			facebook.authorize(this, new String[] { "publish_stream", },
					new DialogListener() {
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
						}

						public void onCancel() {
						}

						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub

						}

						public void onError(DialogError e) {
							// TODO Auto-generated method stub

						}
					});
		}
		
		BPost.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Post to facebook
				
				try {
					String response = facebook.request("me");
					Bundle upDets = new Bundle();
					upDets.putString("message", uppy);
					upDets.putString("description", "test test test");
					upDets.putString(Facebook.TOKEN, access_token);
					response = facebook.request("me/feed", upDets, "POST");
					if (response == null || response.equals("")
							|| response.equals("false")) {
						Toast.makeText(
								Share.this,
								"Our Monkey did not deliver your" + "\n"
										+ " update please log in and try again"
										+ "\n" + "Stupid Monkeys",
								Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(Share.this, "The report has Been shared", Toast.LENGTH_LONG).show();
						startActivity(new Intent(Share.this, mk_home.class));
					}
					

				} catch (FileNotFoundException e) {

					Log.v("FaceBook", e.toString());
				} catch (MalformedURLException e) {
					Log.v("FaceBook", e.toString());

				} catch (IOException e) {
					Log.v("FaceBook", e.toString());
				}
				
			}
		});

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


	
}
