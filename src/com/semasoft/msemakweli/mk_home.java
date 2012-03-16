package com.semasoft.msemakweli;

import com.semasoft.msemakweli.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mk_home extends Activity {
	

	@Override
	public void onBackPressed() {		
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button BView_one = (Button)findViewById(R.id.btnViewReports);
		BView_one.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.semasoft.msemakweli.PROJECTSLIST"));
				
			}
		});
		Button BAbout = (Button)findViewById(R.id.btnAbout);
		BAbout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent ("com.semasoft.msemakweli.MKABOUT"));
				
			}
		});
		Button BSearch = (Button)findViewById(R.id.btnSearch);
		BSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.semasoft.msemakweli.PROJECTSLIST"));
				
			}
		});
		Button BMap = (Button)findViewById(R.id.btnViewMap);
		BMap.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent("com.semasoft.msemakweli.MKMAP"));
			}
		});
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	

}
