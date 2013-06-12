package whyq.activity;

import whyq.interfaces.IServiceListener;
import whyq.interfaces.LogoutDelegate;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.whyq.R;

public class WhyqLogout extends Activity implements IServiceListener{
	
	private Service service;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout);
		service = new Service(this );
	}
	public void onLogoutOnClicked(View v){
		service.logout();
	}
	public void onCancelClicked(View v){
		
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		int i=0;
		i++;
		Log.d("Logout",""+result.getAction());
	}
}
