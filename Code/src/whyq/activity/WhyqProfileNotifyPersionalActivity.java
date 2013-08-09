package whyq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.whyq.R;

public class WhyqProfileNotifyPersionalActivity  extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_notification_screen);
	}
	public void onDoneClicked(View v){
		
	}
	public void onBack(View v){
		finish();
	}
}
