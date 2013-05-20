/**
 * 
 */
package whyq.activity;

import whyq.PermpingMain;
import whyq.TabGroupActivity;
import whyq.model.User;
import whyq.utils.PermUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * @author Linh Nguyen
 *
 */
public class MyDiaryActivityGroup extends TabGroupActivity {
	public static MyDiaryActivityGroup context;
	public static boolean isTabChanged = false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLocalActivityManager().startActivity( "MyDiaryActivity", new Intent(this, MyDiaryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		setTabGroup(this);
		replaceView(view);
		context = this;
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		if(isTabChanged) {
			createUI();
		}
		
	}
	
	public void createUI() {
		User user = PermUtils.isAuthenticated(getApplicationContext());
		setTabGroup(this);
        if (user != null) {
        	View view = getLocalActivityManager().startActivity( "MyDiaryActivity", new Intent(this, MyDiaryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
    		replaceView(view);
    		clearHistory();
    	} else {
			PermpingMain.showLogin();
		}
	}
}
