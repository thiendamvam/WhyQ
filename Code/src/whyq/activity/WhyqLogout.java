package whyq.activity;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.interfaces.LogoutDelegate;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.whyq.R;

public class WhyqLogout extends Activity implements IServiceListener{
	
	private Service service;
	private ProgressBar prgBar;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout);
		service = new Service(this );
		prgBar = (ProgressBar)findViewById(R.id.prgBar);
	}
	public void onLogoutOnClicked(View v){
		service.logout();
		showLoading();
	}

	public void onCancelClicked(View v){
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		endLoading();
		Log.d("Logout",""+result.getAction()+"status"+result.isSuccess());
//		if(result.isSuccess()){
			WhyqApplication.Instance().setToken(null);
			Intent i = new Intent(WhyqLogout.this, LoginWhyqActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			
//		}
	}
	private void showLoading() {
		// TODO Auto-generated method stub
		prgBar.setVisibility(View.VISIBLE);
	}
	private void endLoading() {
		// TODO Auto-generated method stub
		prgBar.setVisibility(View.INVISIBLE);
		
	}
}
