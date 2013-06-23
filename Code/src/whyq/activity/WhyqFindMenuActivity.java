package whyq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whyq.R;

public class WhyqFindMenuActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.whyq_find_menu);

	}

	public void findFromFaccebookClicked(View v) {

		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}

	public void lnFindFromFW(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}

	public void searchByNameClicked(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}
}
