package whyq.activity;

import whyq.WhyqMain;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class ProfileWhyQActivty extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile);
	}
	public void back(){
	       WhyqMain.back();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
