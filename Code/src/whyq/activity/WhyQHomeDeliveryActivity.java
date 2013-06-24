package whyq.activity;


import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.whyq.R;

public class WhyQHomeDeliveryActivity extends MapActivity implements IServiceListener {
	private EditText etOtherAddress;
	private EditText etPhoneNumber;
	private EditText etHours;
	private EditText etMinutes;
	private CheckBox cbASAP;
	private String storeId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_home_delivery);
		TextView headerTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		headerTitle.setText("Home Deliver");
		storeId = getIntent().getStringExtra("store_id");
		etOtherAddress = (EditText)findViewById(R.id.etOtherAddress);
		etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
		etHours = (EditText)findViewById(R.id.etPhoneNumber);
		etMinutes = (EditText)findViewById(R.id.etMinutes);
		cbASAP = (CheckBox)findViewById(R.id.cbASAP);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	public void onBack(){
		finish();
	}
	public void onDoneClicked(View v){

		checkInputData();
	}
    public boolean checkInputData()
    {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        
		String otherAddress = etOtherAddress.getText().toString();
		String phoneNumber = etPhoneNumber.getText().toString();
		String hours = etHours.getText().toString();
		String minutes = etMinutes.getText().toString();
        if (otherAddress.length() == 0)
        {
        	etOtherAddress.setFocusable(true);
        	etOtherAddress.startAnimation(shake);
        	etOtherAddress.requestFocus();
        	return false;
        } else if (phoneNumber.length() == 0)
        {
        	etPhoneNumber.setFocusable(true);
        	etPhoneNumber.startAnimation(shake);
        	etPhoneNumber.requestFocus();
        	return false;
        }else if (hours.length() == 0)
        {
        	etHours.setFocusable(true);
        	etHours.startAnimation(shake);
        	etHours.requestFocus();
        	return false;
        }else if (minutes.length() == 0)
        {
        	etMinutes.setFocusable(true);
        	etMinutes.startAnimation(shake);
        	etMinutes.requestFocus();
        	return false;
        } 
        else
        {
        	Service service = new Service(WhyQHomeDeliveryActivity.this);
        	service.orderDelivery(storeId, otherAddress,phoneNumber,hours,minutes);
        	return true;
        }
    }

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		
	}
}
