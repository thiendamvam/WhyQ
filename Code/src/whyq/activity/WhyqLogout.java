package whyq.activity;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.StatusCallback;
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
		
		//Logout facebook in case logged in facebook
		try {
			Util.facebookLogout();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void onCancelClicked(View v){

		finish();
		overridePendingTransition(R.anim.issue_list_hide,
				R.anim.issue_list_hide);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		endLoading();
		Log.d("Logout",""+result.getAction()+"status"+result.isSuccess());
//		if(result.isSuccess()){
			Util.onLogoutFacebook();
			WhyqApplication.Instance().clearToken();
			WhyqApplication.Instance().setToken(null);
			XMLParser.storeUserAccount(WhyqApplication.Instance().getApplicationContext(), null);
			Intent intent = new Intent(WhyqLogout.this, LoginHome.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
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
