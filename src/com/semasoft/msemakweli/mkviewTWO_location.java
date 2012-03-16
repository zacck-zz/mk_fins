package com.semasoft.msemakweli;

import java.util.regex.Pattern;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mkviewTWO_location extends MapActivity implements OnClickListener {

	MapView theMapView;
	MapController myMapController;

	MyLocationOverlay myLocOverlay;

	Button btnOK;

	String projectName;
	String projLocation;
	String projLat;
	String projLong;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewreports_2_location);

		Bundle Bindex = getIntent().getExtras();
		if (Bindex != null) {
			projectName = Bindex.getString("projectName");
			projLocation = Bindex.getString("projLocation");
		}

		setTitle(projectName + " Location");

		btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);

		theMapView = (MapView) findViewById(R.id.theMapView);
		theMapView.setBuiltInZoomControls(true);

		myMapController = theMapView.getController();
		myMapController.setCenter(new GeoPoint(-1286048, 36815529));
		myMapController.setZoom(9);

		initMyLocation();
		
		projLocationCentre();
		//Toast.makeText(getApplicationContext(), projLocation+"",Toast.LENGTH_SHORT).show();

	}

	private void projLocationCentre() {

		Double convertL;
		String [] gotLoc;
		String finalLoc = "";
		for (int i=1;i<(projLocation.length()-1);i++){
			finalLoc = finalLoc + projLocation.charAt(i);
		}
		
		gotLoc = splitLoc(finalLoc);

		Toast.makeText(getApplicationContext(), gotLoc[0]+""+gotLoc[1], Toast.LENGTH_LONG).show();
		
		convertL = Double.parseDouble(gotLoc[0]);
		int projLatE6 = (int) (convertL*1E6);
		convertL = Double.parseDouble(gotLoc[1]);
		int projLongE6 = (int) (convertL*1E6);
		
		GeoPoint gpProjLocation = new GeoPoint(projLatE6, projLongE6);

		myMapController.animateTo(gpProjLocation);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnOK) {
			finish();
		}

	}

	private void initMyLocation() {
		myLocOverlay = new MyLocationOverlay(this, theMapView);
		theMapView.getOverlays().add(myLocOverlay);
		myLocOverlay.enableMyLocation();

		myLocOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				myMapController.animateTo(myLocOverlay.getMyLocation());
			}
		});

		// myMapController.setCenter(myLocOverlay.getMyLocation());
		// myMapController.setZoom(10);

	}
	
    public String [] splitLoc(String msg){
    	// Create a pattern to match breaks
        Pattern p = Pattern.compile("[,\\s]+");
        // Split input with the pattern
        String[] result = p.split(msg);

        String [] Loc = new String [2];
        Loc[0]= result[0];
        Loc[1]= result[1];
       
        return Loc;
           
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
