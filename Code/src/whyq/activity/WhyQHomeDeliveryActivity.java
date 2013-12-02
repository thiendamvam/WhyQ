package whyq.activity;

import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.whyq.R;

public class WhyQHomeDeliveryActivity extends FragmentActivity implements
		IServiceListener {
	private EditText etOtherAddress;
	private EditText etPhoneNumber;
	private EditText etHours;
	private EditText etMinutes;
	private CheckBox cbASAP;
	private String storeId;
	private ProgressBar progressBar;
	private Context context;
	private String listItem;
	private String note;
	private String longitude;
	private String latgitude;
	private Service servivice;
	private GoogleMap mMap;
	private Object store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_home_delivery);
		TextView headerTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		headerTitle.setText("Home Deliver");
		storeId = getIntent().getStringExtra("store_id");
		etOtherAddress = (EditText) findViewById(R.id.etOtherAddress);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		etHours = (EditText) findViewById(R.id.etHours);
		etMinutes = (EditText) findViewById(R.id.etMinutes);
		cbASAP = (CheckBox) findViewById(R.id.cbASAP);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		context = this;
		// Util.checkLocationSetting(context);
		Bundle bundle = Util.getLocation(context);
		if (bundle != null) {
			longitude = bundle.getString("lon");
			latgitude = bundle.getString("lat");
		}
		servivice = new Service(this);
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Enable MyLocation Button in the Map
		mMap.setMyLocationEnabled(true);

		// Add lots of markers to the map.
		addMarkersToMap();

	}

	private void addMarkersToMap() {
		try {

			LatLng STORE_POSITION = new LatLng(Double.parseDouble(latgitude), Double.parseDouble(longitude));
			// TODO Auto-generated method stub
			store = mMap.addMarker(new MarkerOptions()
					.position(STORE_POSITION )
					.title("")
					.snippet("")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			LatLngBounds bounds = new LatLngBounds.Builder()
			.include(STORE_POSITION).build();
//			mMap.moveCamera(CameraUpdateFactory
//					.newLatLngBounds(bounds, 50));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					STORE_POSITION, 15));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// @Override
	// protected boolean isRouteDisplayed() {
	// // TODO Auto-generated method stub
	// return false;
	// }
	public void onBack(View v) {
		finish();
	}

	public void onDoneClicked(View v) {

		checkInputData();
	}

	public boolean checkInputData() {
		if (cbASAP.isChecked()) {
			showDialog();
			new asyncExeOrderSend().execute();
			return true;
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			String otherAddress = etOtherAddress.getText().toString();
			String phoneNumber = etPhoneNumber.getText().toString();
			String hours = etHours.getText().toString();
			String minutes = etMinutes.getText().toString();
			if (otherAddress.length() == 0) {
				etOtherAddress.setFocusable(true);
				etOtherAddress.startAnimation(shake);
				etOtherAddress.requestFocus();
				return false;
			} else if (phoneNumber.length() == 0) {
				etPhoneNumber.setFocusable(true);
				etPhoneNumber.startAnimation(shake);
				etPhoneNumber.requestFocus();
				return false;
			} else if (hours.length() == 0) {
				etHours.setFocusable(true);
				etHours.startAnimation(shake);
				etHours.requestFocus();
				return false;
			} else if (minutes.length() == 0) {
				etMinutes.setFocusable(true);
				etMinutes.startAnimation(shake);
				etMinutes.requestFocus();
				return false;
			} else {
				showDialog();
				new asyncExeOrderSend().execute();
				return true;
			}
		}

	}

	class asyncExeOrderSend extends
			AsyncTask<HashMap<String, String>, Void, HashMap<String, String>> {
		public asyncExeOrderSend() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(HashMap<String, String> location) {
			// TODO Auto-generated method stub
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem = bundle.getString("list_items");
			note = bundle.getString("note");
			if (location != null) {
				longitude = location.get("lon");
				latgitude = location.get("lon");
			}
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "2");
			params.put("list_items", listItem);
			params.put("deliver_to", "" + etOtherAddress.getText().toString());
			params.put("time_zone", Util.getTimeZone(""));
			if (cbASAP.isChecked()) {
				params.put("time_deliver", "ASAP");
			} else {
				params.put("time_deliver", getTimeInpu());
			}
			params.put("deliver_latitude", longitude);
			params.put("deliver_longitude", latgitude);
			params.put("phone_deliver", etPhoneNumber.getText().toString());
			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());
			servivice.orderSend(params);
		}

		private String getTimeInpu() {
			// TODO Auto-generated method stub
			String hours = etHours.getText().toString();
			String minutes = etMinutes.getText().toString();
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			return hours + ":" + minutes;
		}

		@Override
		protected HashMap<String, String> doInBackground(
				HashMap<String, String>... params) {
			// TODO Auto-generated method stub

			return Util.getLatLonFromAddress(etOtherAddress.getText()
					.toString());
		}
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if (result.getAction() == ServiceAction.ActionOrderSend
				&& result.isSuccess()) {
			ResponseData data = (ResponseData) result.getData();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					Util.showDialog(context, data.getMessage());
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(context, data.getMessage());
				} else {
					Util.showDialog(context, data.getMessage());
				}
			}
		}
	}

	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.GONE);
	}

}
