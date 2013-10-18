package whyq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whyq.R;

public class WhyqProfileNotifyPersionalActivity  extends Activity{

	private CheckBox cbReceiveNotification;
	private CheckBox cbAllowUserToSee;
	private CheckBox cbSoundsNotification;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_notification_screen);
		TextView tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Notification");
		cbReceiveNotification = (CheckBox)findViewById(R.id.cbxReceiveNotification);
		cbAllowUserToSee = (CheckBox)findViewById(R.id.cbxAllowUserToSeeFriends);
		cbSoundsNotification = (CheckBox)findViewById(R.id.cbxSoundsNotification);
	}
	public void onDoneClicked(View v){
		
	}
	public void onBack(View v){
		finish();
	}
}
