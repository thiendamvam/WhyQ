package whyq.activity;

import whyq.WhyqMain;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class ProfileWhyQActivty extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile);
	}
	public void back(View v){
//	       WhyqMain.back();
		finish();
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
