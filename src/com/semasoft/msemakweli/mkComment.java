package com.semasoft.msemakweli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mkComment extends Activity{
	
	Bitmap takepicBm;
	String where;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_report);
		Bundle Bname = getIntent().getExtras();
		String pname = Bname.getString("pname");
		
		
		Button takeP;
		Button btDone;
		TextView tvpname = (TextView)findViewById(R.id.tvPname);
		final EditText etName = (EditText)findViewById(R.id.etName);
		final EditText etComm = (EditText)findViewById(R.id.etComment);
		takeP = (Button)findViewById(R.id.btnPicture2);
		btDone = (Button)findViewById(R.id.btnDone);
		tvpname.setText(pname);
		
		//path for the Camera
		 where = Environment.getExternalStorageState();
		 
		
		
		
		takeP.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"),0);
			}
		});
		
		btDone.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				HttpClient suggestCl = new DefaultHttpClient();
			    HttpPost postSugg = new HttpPost("http://semasoftltd.com/webs/inputComm.php");
			    
			    try{
			    	List<NameValuePair> suggestions = new ArrayList<NameValuePair>(2);
			    	suggestions.add(new BasicNameValuePair("name", etName.getText().toString()));
			    	suggestions.add(new BasicNameValuePair("suggestion", etComm.getText().toString()));
			    	
			    	postSugg.setEntity(new UrlEncodedFormEntity(suggestions));
			    	HttpResponse response = suggestCl.execute(postSugg);

			    } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
			    	
			    }
				
				Toast msg = Toast.makeText(mkComment.this, "Your Comment has been posted ", Toast.LENGTH_LONG);

				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

				msg.show();
				
			}
		});
		
		takeP.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(mkComment.this, picCapture.class));
			}
		});
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			takepicBm = (Bitmap)extras.get("data");			
		}
		
	}
	public void storePic(Bitmap bit){
		Calendar Cal = Calendar.getInstance();
		OutputStream outS = null;
		String name = Cal.toString();
		String path = Environment.getExternalStorageDirectory().toString();
		File pic = new File(path, name+".png");
		try{
			outS = new FileOutputStream(pic);
			bit.compress(Bitmap.CompressFormat.PNG, 100, outS);
			outS.flush();
			outS.close();
		}catch (Exception e){
			
		}
		
		finally{
			Toast.makeText(getBaseContext(), "The picture Has been saved.... Smile more", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	

}
