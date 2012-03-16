package com.semasoft.msemakweli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class map extends MapActivity implements OnClickListener {

	LinearLayout lytSearch;
	ImageButton btnSearch;
	ImageButton btnGo; /* This is the GO button */
	AutoCompleteTextView ac_const; // auto

	MapView theMapView;
	MapController myMapController;
	JSONObject paro_obj, child_obj;
	JSONArray JArray;

	MyLocationOverlay myLocOverlay;

	static final String[] PROVINCES = new String[] { "Githunguri", "Kabete",
			"Kiambaa", "Lari", "Limuru", "Gatanga", "Gatundu North",
			"Gatundu South", "Juja", "Gichugu", "Kirinyaga Central", "Mwea",
			"Ndia", "Kandara", "Kigumo", "Maragua", "Kangema", "Kiharu",
			"Mathioya", "Kinangop", "Kipiripiri", "Ol'Kalou", "Kieni",
			"Mathira", "Mukwurweini", "Nyeri Town", "Othaya", "Bahari",
			"Ganze", "Kaloleni", "Magarini", "Malindi", "Kinango", "Matuga",
			"Msambweni", "Lamu East", "Lamu West", "Changamwe", "Kisauni",
			"Likoni", "Mvita", "Mwatate", "Taveta", "Voi", "Wundanyi", "Bura",
			"Galole", "Garsen", "Manyatta", "Runyenjes", "Gachoka", "Siakago",
			"Isiolo North", "Isiolo South", "Kitui Central", "Kitui South",
			"Kitui West", "Mutito", "Mwingi North", "Mwingi South", "Kangundo",
			"Kathiani", "Machakos Town", "Masinga", "Mwala", "Yatta", "Kaiti",
			"Kibwezi", "Kilome", "Makueni", "Mbooni", "Laisamis", "North-Horr",
			"Saku", "Moyale", "Central Imenti", "North Imenti", "South Imenti",
			"Igembe North (Nto", "Igembe South", "Tigania East",
			"Tigania West", "Nithi", "Tharaka", "Dagoreti", "Embakasi",
			"Kamukunji", "Kasarani", "Langata", "Makadara", "Starehe",
			"Westlands", "Dujis", "Fafi", "Lagdera", "Ijara",
			"Mandera Central", "Mandera East", "Mandera West", "Wajir East",
			"Wajir North", "Wajir South", "Wajir West", "Ndhiwa", "Rangwe",
			"Karachuonyo", "Kasipul Kabondo", "Gwassi", "Mbita", "Bobasi",
			"Bomachoge", "South Mugirango", "Bonchari", "Kitutu Chache",
			"Nyaribari Chache", "Nyaribari Masaba", "Kisumu Rural",
			"Kisumu Town East", "Kisumu Town West", "Muhoroni", "Nyakach",
			"Nyando", "Kuria", "Migori", "Nyatike", "Rongo", "Uriri",
			"Kitutu Masaba", "North Mugirango", "West Mugirango", "Bondo",
			"Rarieda", "Alego Usonga", "Gem", "Ugenya", "Baringo Central",
			"Baringo East", "Baringo North", "Eldama Ravine", "Mogotio",
			"Bomet", "Chepalungu", "Sotik", "Kajiado Central", "Kajiado North",
			"Kajiado South", "Buret", "Konoin", "Ainamoi", "Belgut",
			"Kipkelion", "Laikipia East", "Laikipia West", "Keiyo North",
			"Keiyo South", "Marakwet East", "Marakwet West", "Kuresoi", "Molo",
			"Naivasha", "Nakuru Town", "Rongai", "Subukia", "Emgwen", "Mosop",
			"Aldai", "Tinderet", "Narok North", "Narok South", "Kilgoris",
			"Samburu East", "Samburu West", "Cherengany", "Kwanza", "Saboti",
			"Turkana Central", "Turkana North", "Turkana South",
			"Eldoret East", "Eldoret North", "Eldoret South", "Kacheliba",
			"Kapenguria", "Sigor", "Bumula", "Kanduyi", "Kimilili", "Sirisia",
			"Webuye", "Mt. Elgon", "Budalangi", "Butula", "Funyula", "Nambale",
			"Amagoro", "Butere", "Khwisero", "Matungu", "Mumias", "Ikolomani",
			"Lurambi", "Malava", "Shinyalu", "Lugari", "Emuhaya", "Hamisi",
			"Sabatia", "Vihiga" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		lytSearch = (LinearLayout) findViewById(R.id.ly_Search);
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnGo = (ImageButton) findViewById(R.id.btnGo);
		ac_const = (AutoCompleteTextView) findViewById(R.id.ac_constituency);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, PROVINCES);
		ac_const.setAdapter(adapter);

		theMapView = (MapView) findViewById(R.id.theMapView);
		theMapView.setBuiltInZoomControls(true);

		myMapController = theMapView.getController();
		myMapController.setCenter(new GeoPoint(-1286048, 36815529));
		myMapController.setZoom(9);

		initMyLocation();

		btnSearch.setOnClickListener(this);
		btnGo.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSearch) {
			int visCheck;
			visCheck = lytSearch.getVisibility();
			if (visCheck == 8) {
				lytSearch.setVisibility(0);
			} else {
				lytSearch.setVisibility(8);
			}
		}

		if (v == btnGo) {
			MapProjects neggey = new MapProjects();
			neggey.execute();

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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class MapProjects extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			String constName = ac_const.getText().toString();

			HttpClient mapClient = new DefaultHttpClient();
			HttpPost postConst = new HttpPost(
					"http://semasoftltd.com/dwebservices/getPoints.php");
			try {
				List<NameValuePair> consty = new ArrayList<NameValuePair>(1);
				consty.add(new BasicNameValuePair(constName, constName));
				postConst.setEntity(new UrlEncodedFormEntity(consty));
				HttpResponse broughBack = mapClient.execute(postConst);
				String jjss = EntityUtils.toString(broughBack.getEntity());
				paro_obj = new JSONObject(jjss);
				JArray = paro_obj.getJSONArray("geopoints");
				if(jjss!=null){
				Toast.makeText(getBaseContext(), jjss.toString(), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getBaseContext(), "emptyness", Toast.LENGTH_LONG).show();
				}
				
			} catch (Exception exception) {
			}

			return null;
		}

	}

}
