package whyq.activity;


import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;

import com.whyq.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class ListDetailActivity extends Activity implements IServiceListener{

	private Service service;
	ProgressDialog dialog;
	private String id;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_detail_screen_about);
		id = getIntent().getStringExtra("id");
		service = new Service(this);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Progressing...");
		getDetailData();
		
	}

	private void getDetailData() {
		// TODO Auto-generated method stub
		showDialog();
		service.getBusinessDetail(id);
	}
	private void showDialog(){
		dialog.show();
	}
	private void hideDialog(){
		dialog.dismiss();
	}
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		
	}
}
