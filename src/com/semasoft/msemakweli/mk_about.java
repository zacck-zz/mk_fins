package com.semasoft.msemakweli;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mk_about extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		Button AbOk = (Button)findViewById(R.id.btnOK);
		AbOk.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent ("android.intent.action.HOME"));
				
			}
		});
		
	}
	

	@Override
	protected void onPause() {
		super.onPause();
	}
	

}
