package whyq.activity;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.whyq.R;

public class WhyqFindMenuActivity extends Activity implements IServiceListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_find_menu);
	}

	public void findFromFaccebookClicked(View v) {
		Facebook mFacebook;
		mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
		final Activity activity = this;
		mFacebook.authorize(activity, new String[] { "email", "status_update",
				"user_birthday" }, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				WhyqUtils permutils = new WhyqUtils();
				String accessToken = values.getString(Facebook.TOKEN);
				permutils.saveFacebookToken("oauth_token", accessToken,
						getApplication());
				exeLoginFacebook(accessToken);
			}

			@Override
			public void onFacebookError(FacebookError error) {

			}

			@Override
			public void onError(DialogError e) {

			}

			@Override
			public void onCancel() {
				// cancel press or back press
			}
		});
	}

	public void exeLoginFacebook(String accessToken) {
		Service service = new Service(this);
		service.loginFacebook(accessToken);
	}

	public void lnFindFromFW(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}

	public void searchByNameClicked(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.isSuccess() == true
				&& result.getAction() == ServiceAction.ActionLoginFacebook) {
			ListActivity.isLogin = true;
			ListActivity.loginType = 1;
			Log.d("LoginHome by Facebook", "result: " + result.getData());
			User user = (User) result.getData();
			XMLParser.storePermpingAccount(
					WhyqApplication._instance.getApplicationContext(), user);
			Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
