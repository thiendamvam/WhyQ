package whyq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.whyq.R;

public class ProfileWhyQActivty extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile);
	}
	public void onBack(View v){
//	       WhyqMain.back();
		finish();
	}
	public void signoutClicked(View v){
		Log.d("signoutClicked","signoutClicked");
		Intent i = new Intent(ProfileWhyQActivty.this, WhyqLogout.class);
		startActivity(i);
		overridePendingTransition(R.anim.issue_list_show, R.anim.issue_list_show);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
//	        WhyqMain.back();
	    	finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
