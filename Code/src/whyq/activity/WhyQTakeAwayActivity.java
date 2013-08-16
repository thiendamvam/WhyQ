package whyq.activity;

import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;

import android.app.Activity;
import android.content.Context;
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
	private TextView tvCarTime;
	private TextView tvBycicalTime;
	private TextView tvWalkTime;
	private Button btnDone;
	private Service service;
	private Context context;
	private String storeId;
	private String listItem;
	private String note;
	private ProgressBar progressBar;

	public WhyQTakeAwayActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_take_away_screen);

		etHours = (EditText) findViewById(R.id.etHours);
		etMinutes = (EditText) findViewById(R.id.etMinutes);
		cbLeaveNow = (CheckBox) findViewById(R.id.cbLeaveNow);
		tvCarTime = (TextView) findViewById(R.id.tvCarTime);
		tvBycicalTime = (TextView) findViewById(R.id.tvBycicalTime);
		tvWalkTime = (TextView) findViewById(R.id.tvWalkTime);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
		context = this;
		service = new Service(this);
		// btnDone = (Button)findViewById(R.id.btnDone);
		// btnDone.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btnDone:
			exeDone();
			break;

		default:
			break;
		}
	}

	private void exeDone() {
		// TODO Auto-generated method stub
		// store_id, deliver_type(1: tike away, 4: dinein),
		// list_items,deliver_to(empty for typle=1 and 0 if type =4),
		// time_zone,time_deliver(if not empty, oly for type =1),note(commend),
		// token
		if(checkInputData()||cbLeaveNow.isChecked()){
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem =  bundle.getString("list_items");
			note =  bundle.getString("note");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "1");
			params.put("list_items", listItem);
			params.put("deliver_to", "");
			params.put("time_zone", Util.getTimeZone(""));
			if(cbLeaveNow.isChecked()){
				params.put("time_deliver", "ASAP");				
			}else{
				params.put("time_deliver", ""+etHours.getText().toString()+":"+etMinutes.getText().toString());
			}

			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());
			
			showDialog();
			service.orderSend(params);
		}else{
//			Util.showDialog(context, "Please input data");
			etHours.setError("Please input Hours");
			etMinutes.setError("Please input minutes");
		}
	}
	public boolean checkInputData(){
		boolean status = true;
		if(etHours.getText().toString().equals("") || etMinutes.getText().toString().equals(""))
		{
			return false;
		}
		return status;
	}
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		if(result.getAction() == ServiceAction.ActionOrderSend && result.isSuccess()){
			ResponseData data = (ResponseData)result.getData();
			hideDialog();
			if(data!=null){
				if(data.getStatus().equals("200")){
					Util.showDialog(context, data.getMessage());
				}else if(data.getStatus().equals("401")){
					Util.loginAgain(context, data.getMessage());
				}else{
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
