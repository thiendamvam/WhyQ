package whyq.activity;

import java.util.ArrayList;

import twitter4j.http.AccessToken;
import whyq.WhyqApplication;
import whyq.controller.WhyqListController;
import whyq.interfaces.IFacebookLister;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import whyq.utils.facebook.SessionLoginFragment;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.share.twitter.TwitterActivity;
import com.whyq.R;

public class WhyqFindMenuActivity extends FragmentActivity implements IServiceListener, IFacebookLister {

	private TextView tvTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_find_menu);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Find Friends");
	}
	public void findFromTwitterClicked(View v){
		SharedPreferencesManager shareManager = new SharedPreferencesManager(
				WhyqApplication.Instance().getApplicationContext());
		AccessToken twitterAccess = shareManager.loadTwitterToken();
//		mTwitterAccess = getTwitterAccess();


//		String token_secret = mTwitterAccess.getTokenSecret();
		Log.d("WhyqFindMenu", "Twitter Access ======>" + twitterAccess);
		if(twitterAccess!=null){
			
//			String token = mTwitterAccess.getToken();
//			String token_secret = mTwitterAccess.getTokenSecret();
//			exeLoginTwitter(token, token_secret);
			Intent iFriendsTwitter = new Intent(WhyqFindMenuActivity.this,WhyqFriendsTwitterActivity.class);
			startActivity(iFriendsTwitter);
		}else{
			Intent iTwitter = new Intent(WhyqFindMenuActivity.this,TwitterActivity.class);
			startActivity(iTwitter);
//			startActivityForResult(iTwitter, LOGIN_TWITTER);	
		}
	}
	
	public void findFromFaccebookClicked(View v) {
//		if (getAccessToken() == null) {
//			Facebook mFacebook;
//			mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
//			final Activity activity = this;
//			mFacebook.authorize(activity, new String[] { "email", "status_update",
//					"user_birthday" }, new DialogListener() {
//				@Override
//				public void onComplete(Bundle values) {
//					WhyqUtils permutils = new WhyqUtils();
//					String accessToken = values.getString(Facebook.TOKEN);
//					permutils.saveFacebookToken("oauth_token", accessToken,
//							getApplication());
//					exeLoginFacebook(accessToken);
//				}
//
//				@Override
//				public void onFacebookError(FacebookError error) {
//
//				}
//
//				@Override
//				public void onError(DialogError e) {
//
//				}
//
//				@Override
//				public void onCancel() {
//					// cancel press or back press
//				}
//			});
//		} else {
//			Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
//			startActivity(intent);
//			finish();
//		}
		Session session = Session.getActiveSession();
		
		if(session!=null){
			if(session.isOpened()){
				showFacebookLogin();
			}else{
				gotoFacebookFriend();
			}
		}else{
			showFacebookLogin();
		}
	}

	private void gotoFacebookFriend() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
		startActivity(intent);
		finish();
	}
	private void showFacebookLogin() {
		// TODO Auto-generated method stub
		SessionLoginFragment fragment = new SessionLoginFragment();
		getSupportFragmentManager().beginTransaction().add(fragment, "facebook_login").commit();
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
		Intent intent = new Intent(WhyqFindMenuActivity.this, WhyqFriendsActivity.class);
		intent.putExtra("is_search_by_name", true);
		startActivity(intent);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.isSuccess() == true
				&& result.getAction() == ServiceAction.ActionLoginFacebook) {
			ListActivity.isLogin = true;
			ListActivity.loginType = 1;
			Log.d("LoginHome by Facebook", "result: " + result.getData());
			
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				User user = (User) data.getData();
				XMLParser.storePermpingAccount(
						WhyqApplication._instance.getApplicationContext(), user);
				Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
				startActivity(intent);
				finish();
			}else if(data.getStatus().equals("401")){
				Util.loginAgain(getParent(), data.getMessage());
			}else{
//				Util.showDialog(getParent(), data.getMessage());
			}
			

		}
	}
	
	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}
	
	public void onBack(View v){
		finish();
	}
	@Override
	public void onCompled(boolean b) {
		// TODO Auto-generated method stub
		if(b){
			gotoFacebookFriend();
		}
	}
}
