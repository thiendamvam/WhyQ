package whyq.activity;

import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.Distance;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class WhyQTakeAwayActivity extends Activity implements OnClickListener,
		IServiceListener {

	private EditText etHours;
	private EditText etMinutes;
	private CheckBox cbLeaveNow;
	private Button btnCarTime;
	private Button btnBycicalTime;
	private Button btnWalkTime;
	private Button btnDone;
	private Service service;
	private Context context;
	private String storeId;
	private String listItem;
	private String note;
	private ProgressBar progressBar;
	private int getDistanceStep = 1;
	private HashMap<String, String> params;
	private TextView tvheader;

	public WhyQTakeAwayActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_take_away_screen_new_design);

		tvheader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvheader.setText("Take away");
		etHours = (EditText) findViewById(R.id.etHours);
		etMinutes = (EditText) findViewById(R.id.etMinutes);
		cbLeaveNow = (CheckBox) findViewById(R.id.cbLeaveNow);
		btnCarTime = (Button) findViewById(R.id.btnCarTime);
		btnBycicalTime = (Button) findViewById(R.id.btnBycicalTime);
		btnWalkTime = (Button) findViewById(R.id.btnWalkTime);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		
		
		context = this;
		service = new Service(this);
		params = new HashMap<String, String>();
		new asyncGetLocatoin().execute();
		// btnDone = (Button)findViewById(R.id.btnDone);
		// btnDone.setOnClickListener(this);
	}

	class asyncGetLocatoin extends AsyncTask<Bundle, Void, Bundle> {

		public asyncGetLocatoin() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Bundle doInBackground(Bundle... params) {
			// TODO Auto-generated method stub
			Bundle bundle = Util.getLocation(context);
			return bundle;
		}

		@Override
		protected void onPostExecute(Bundle bundle) {
			// TODO Auto-generated method stub
			exeGetDistance(bundle);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btnDone:
			// exeDone();
			break;

		default:
			break;
		}
	}

	public void exeGetDistance(Bundle bundle) {
		// TODO Auto-generated method stub
		if (bundle != null) {
			String longitude = bundle.getString("lon");
			String latgitude = bundle.getString("lat");
			longitude="1200";
			latgitude = "1002";
			params.put("origins", longitude+","+latgitude);
			params.put("destinations", ListDetailActivity.bundle.getString("lat")+","+ListDetailActivity.bundle.getString("lon"));
			params.put("language", "fr-EN");
			params.put("sensor", "false");

			exeGetDistance(params,"bicycling");

		}
	}

	public void exeGetDistance(HashMap<String, String> params, String mode) {
		// TODO Auto-generated method stub
		params.put("mode", mode);
		service.getDistance(params);
	}

	public void onBack(View v) {
		finish();
	}

	public void onDoneClicked(View v) {
		// TODO Auto-generated method stub
		// store_id, deliver_type(1: tike away, 4: dinein),
		// list_items,deliver_to(empty for typle=1 and 0 if type =4),
		// time_zone,time_deliver(if not empty, oly for type =1),note(commend),
		// token
		if (checkInputData() || cbLeaveNow.isChecked()) {
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem = bundle.getString("list_items");
			note = bundle.getString("note");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "1");
			params.put("list_items", listItem);
			params.put("deliver_to", "");
			params.put("time_zone", Util.getTimeZone(""));
			if (cbLeaveNow.isChecked()) {
				params.put("time_deliver", "ASAP");
			} else {
				params.put("time_deliver", "" + getTimeInpu());
			}

			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());

			showDialog();
			service.orderSend(params);
		} else {
			// Util.showDialog(context, "Please input data");
			etHours.setError("Please input Hours");
			etMinutes.setError("Please input minutes");
		}
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

	public boolean checkInputData() {
		boolean status = true;
		if (etHours.getText().toString().equals("")
				|| etMinutes.getText().toString().equals("")) {
			return false;
		}
		return status;
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		if (result.getAction() == ServiceAction.ActionOrderSend
				&& result.isSuccess()) {
			ResponseData data = (ResponseData) result.getData();
			hideDialog();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					Util.showDialog(context, data.getMessage());
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(context, data.getMessage());
				} else {
					Util.showDialog(context, data.getMessage());
				}
			}
		}else if(result.getAction()== ServiceAction.ActionGetDistance&& result.isSuccess()){
			ResponseData data = (ResponseData) result.getData();
			hideDialog();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					Distance resultData = (Distance)data.getData();
//					Util.showDialog(context, data.getMessage());
					if(getDistanceStep  == 1){
						getDistanceStep = 2;
						exeGetDistance(params,"driving");
						updateBicyclingTime(resultData);
					}else if(getDistanceStep == 2){
						getDistanceStep = 3;
						exeGetDistance(params,"walking");
						updateDrivingTime(resultData);
					}else if(getDistanceStep == 3){
						getDistanceStep = 1;
						updateWalkingTime(resultData);
					}
				} 
//				else if (data.getStatus().equals("401")) {
//					Util.loginAgain(context, data.getMessage());
//				} else {
//					Util.showDialog(context, data.getMessage());
//				}
			}
		}
	}

	private void updateDrivingTime(Distance resultData) {
		// TODO Auto-generated method stub
		if(resultData.getValue()!=null){
			btnCarTime.setText(resultData.getValue());
		}
	}

	private void updateBicyclingTime(Distance resultData) {
		// TODO Auto-generated method stub
		if(resultData.getValue()!=null){
			btnBycicalTime.setText(resultData.getValue());
		}
	}

	private void updateWalkingTime(Distance resultData) {
		// TODO Auto-generated method stub
		if(resultData.getValue()!=null){
			btnWalkTime.setText(resultData.getValue());
			
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
