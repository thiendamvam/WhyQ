package whyq.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

	public void onBack(View v) {
		// WhyqMain.back();
		finish();
	}

	public void signoutClicked(View v) {
		Log.d("signoutClicked", "signoutClicked");
		Intent i = new Intent(ProfileWhyQActivty.this, WhyqLogout.class);
		startActivity(i);
		overridePendingTransition(R.anim.issue_list_show,
				R.anim.issue_list_show);
	}

	public void onFAQClicked(View v) {
		Intent i = new Intent(ProfileWhyQActivty.this,
				WhyqProflleFAQActivity.class);
		i.putExtra("title", "FAQ");
		startActivity(i);
	}

	public void onNotifyClicked(View v) {
		Intent i = new Intent(ProfileWhyQActivty.this,
				WhyqProfileNotifyPersionalActivity.class);
		i.putExtra("title", "Notify Persionals");
		startActivity(i);
	}

	public void onEditClicked(View v) {
		Intent i = new Intent(ProfileWhyQActivty.this,
				WhyqProfileEditAccountActivity.class);
		i.putExtra("title", "Edit Account");
		startActivity(i);
	}

	public void onRateClicked(View v) {
		Intent i = new Intent(ProfileWhyQActivty.this,
				WhyqProfileChildBasic.class);
		i.putExtra("title", "WHY Q V2.1.1 Rate it");
		startActivity(i);
	}

	public void onTeamsClicked(View v) {
		Intent i = new Intent(ProfileWhyQActivty.this,
				WhyqProfileChildBasic.class);
		i.putExtra("title", "Terms and conditions");
		startActivity(i);
	}

	public void onSupportClicked(View v) {
		Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not
		// ACTION_SEND
		intent.setType("text/plain");
		String subject = "Support";
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		String message="/n/nSend from my device";
		intent.putExtra(Intent.EXTRA_TEXT, message);
		String to = "support@whyq.net.au";
		intent.setData(Uri.parse("mailto:" + to)); // or just "mailto:" for
		// blank
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make
		// such that when
		// user returns to
		// your app, your
		// app is displayed,
		// instead of the
		// email app.
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// WhyqMain.back();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
