package whyq.activity;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class WhyqProfileEditAccountActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
	}
	public void onDoneClicked(View v){
		
	}
	public void onBack(View v){
		finish();
	}
}
