package whyq.activity;

import whyq.model.BillPushNotification;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;

public class WhyqShareActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_share);
		BillPushNotification pushNotificationData = (BillPushNotification)getIntent().getExtras().getSerializable("push_data");
	}
}
