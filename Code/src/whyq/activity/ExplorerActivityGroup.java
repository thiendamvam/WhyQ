package whyq.activity;

import whyq.TabGroupActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class ExplorerActivityGroup extends TabGroupActivity {
	public static boolean isTabChanged = false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//View view = getLocalActivityManager().startActivity( "ExplorerActivity", new Intent(this, ExplorerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		setTabGroup(this);
		//replaceView(view);
		createUI();
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
		View view = getLocalActivityManager().startActivity( "ExplorerActivity", new Intent(this, ExplorerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		setTabGroup(this);
		replaceView(view);
		clearHistory();
	}
	

}
