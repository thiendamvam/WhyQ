/**
 * 
 */
package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import twitter4j.auth.AccessToken;
import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.interfaces.IFacebookLister;
import whyq.interfaces.IServiceListener;
import whyq.interfaces.LoginTWDelegate;
import whyq.model.ResponseData;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import whyq.utils.facebook.FacebookConnector;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.share.twitter.TwitterActivity;
import com.whyq.R;

/**
 * @author Linh Nguyen This activity supports to authenticate the user by
 *         user-name and password. Also, it can communicate to FB and Twitter to
 *         do authentication as well
 */
public class LoginHome extends FragmentActivity implements IServiceListener,
		LoginTWDelegate, IFacebookLister {

	static final int LOGIN_TWITTER = 1;
	private static final String PENDING_REQUEST_BUNDLE_KEY = "com.whyq:PendingRequest";
	private Button facebookLogin;
	private Button twitterLogin;
	public static boolean isLoginFb = false;
	public static boolean isTwitter = false;
	private ProgressDialog loadingDialog;
//	private ProgressBar progressBar;
	private SharedPreferences prefs;
	private FacebookConnector facebookConnector;
	private boolean pendingRequest;
	public static Context context;
	private Session session;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_home);

		TextView textView = (TextView) findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if (textView != null) {
			textView.setTypeface(tf);
		}
		context = LoginHome.this;
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		facebookLogin = (Button) findViewById(R.id.btnFbLogin);
		twitterLogin = (Button) findViewById(R.id.btnLoginTw);
		// login = (Button) findViewById(R.id.loginPerm);
//		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("Loading...");
		dismissLoadingDialog();
		
		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
				this, getApplicationContext(), new String[] { Constants.EMAIL,
						Constants.PUBLISH_STREAM });
		// Login with Facebook button
		facebookLogin.setOnClickListener(new View.OnClickListener() {

			

			public void onClick(final View v) {
				if(Util.checkInternetConnection()){

					// TODO Auto-generated method stub

					session = Util.createSession();
					if (session.isOpened()) {
						exeLoginFacebook(session.getAccessToken());
					} else {
						Util.onLogoutFacebook();
						StatusCallback callback = new StatusCallback() {
							public void call(Session session, SessionState state,
									Exception exception) {
								if (exception != null) {
									new AlertDialog.Builder(context)
											.setTitle(R.string.login_text1)
											.setMessage(exception.getMessage())
											.setPositiveButton(R.string.ok, null)
											.show();
									session = Util.createSession();
								}
							}
						};
						pendingRequest = true;
						session.openForRead(new Session.OpenRequest(LoginHome.this)
								.setCallback(callback));
						
					}
				
				}else{
					Util.showNetworkError(context);
				}
			}
		});

		// Twitter Login button
		twitterLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(Util.checkInternetConnection()){

					isTwitter = true;
					SharedPreferencesManager shareManager = new SharedPreferencesManager(
							WhyqApplication.Instance().getApplicationContext());
					AccessToken a = shareManager.loadTwitterToken();
					if (a != null) {
						String token = a.getToken();
						String tokenSecret = a.getTokenSecret();
						exeLoginTwitter(token, tokenSecret);
					} else {
						Intent i = new Intent(LoginHome.this, TwitterActivity.class);
						startActivityForResult(i, LOGIN_TWITTER);
					}

				
				}else{
					Util.showNetworkError(context);
				}
			}
		});

		String token = WhyqApplication.Instance().getRSAToken();
		if (token == null || token.equals("")) {

		} else {
			Intent intent = new Intent(LoginHome.this, WhyqMain.class);
			startActivity(intent);
			finish();
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == LOGIN_TWITTER) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				String token = data.getStringExtra("token");
				String tokenSecret = data.getStringExtra("token_secret");
				// HashMap<String, String>params = new HashMap<String,
				// String>();
				// params.put("token", token);
				// params.put("token_secret", tokenSecret);
				// exeSendmessage
				exeLoginTwitter(token, tokenSecret);
			}
		} else if (session
				.onActivityResult(this, requestCode, resultCode, data)
				&& pendingRequest && this.session.getState().isOpened()) {
			exeLoginFacebook(session.getAccessToken());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		pendingRequest = savedInstanceState.getBoolean(
				PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}
	@Override
	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	private void showLoadingDialog() {

//		progressBar.setVisibility(View.VISIBLE);
		loadingDialog.show();
	}

	private void dismissLoadingDialog() {

//		if (progressBar.getVisibility() == View.VISIBLE) {
//			progressBar.setVisibility(View.GONE);
//		}
		loadingDialog.dismiss();
	}

	public void onClickedSignup(View v) {
		Intent intent = new Intent(LoginHome.this, JoinWhyqActivity.class);
		startActivity(intent);
	}

	public void onClickedLogin(View v) {
		Intent intent = new Intent(LoginHome.this, LoginWhyqActivity.class);
		startActivity(intent);
	}

	public void onClickedSkipLogin(View v) {
//		Intent intent = new Intent(LoginHome.this, WhyqMain.class);
//		startActivity(intent);

//		Intent intent = new Intent(LoginHome.this, WhyqMain.class);
//		startActivity(intent);
		Service service = new Service(LoginHome.this);
		try {
			service.loginasGuest(Util.encryptToken(Util.generateDeviceId()));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pendingRequest = false;
	
	}

	public void exeLoginFacebook(String accessToken) {
		Service service = new Service(LoginHome.this);
		service.loginFacebook(accessToken);
		showLoadingDialog();
		pendingRequest = false;
	}

	public void exeLoginTwitter(String oauthToken, String oauthTokenSecret) {
		Service service = new Service(LoginHome.this);
		showLoadingDialog();
		service.loginTwitter(oauthToken, oauthTokenSecret);
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

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		
		dismissLoadingDialog();
		if (result.isSuccess() == true
				&& result.getAction() == ServiceAction.ActionLoginFacebook) {
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				User user = (User) data.getData();
				// if(user.isLogined()){
				ListActivity.isLogin = true;
				ListActivity.loginType = 1;
				isLoginFb = true;
				Log.d("LoginHome by Facebook", "result: ");
				WhyqApplication.Instance().setToken(user);
				XMLParser.storeUserAccount(WhyqApplication.Instance().getApplicationContext(),
				 user);
				Intent intent = new Intent(LoginHome.this, WhyqMain.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// }else{
				//
				// }
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(context, data.getMessage());
			} else {
				Util.showDialog(context, data.getMessage());
			}

		} else if (result.isSuccess() == true
				&& result.getAction() == ServiceAction.ActionLoginTwitter) {

			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				User user = (User) data.getData();
				// if(user.isLogined()){
				WhyqApplication.Instance().setToken(user);
				XMLParser.storeUserAccount(WhyqApplication.Instance().getApplicationContext(),
						 user);
				ListActivity.isLogin = true;
				ListActivity.loginType = 2;
				// dismissLoadingDialog();
				Intent intent = new Intent(LoginHome.this, WhyqMain.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// }
				// }else{
				// String mes = user.getMessageLogin();
				// android.app.AlertDialog.Builder builder = new
				// android.app.AlertDialog.Builder(context);
				// builder.setTitle(context.getString(R.string.app_name_title));
				// builder.setMessage(mes);
				// final android.app.AlertDialog alertError = builder.create();
				// alertError.setButton("Login", new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// alertError.dismiss();
				// }
				// });
				// alertError.setButton2("Cancel", new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// alertError.dismiss();
				// }
				// });
				// alertError.show();
				// }
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(context, data.getMessage());
			} else {
				Util.showDialog(context, data.getMessage());
			}

		}else if(result.isSuccess()&& result.getAction()==ServiceAction.ActionLoginasGuest){
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				User user = (User)data.getData();
					WhyqApplication.Instance().setToken(user);
					XMLParser.storeUserAccount(WhyqApplication.Instance().getApplicationContext(),
							 user);
					ListActivity.isLogin = true;
					ListActivity.loginType = 2;
					Intent intent = new Intent(LoginHome.this, WhyqMain.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

			}else if(data.getStatus().equals("401")){
				
				Util.loginAgain(context, data.getMessage());
			}else{
				Util.showDialog(context, data.getMessage());
			}

		}else if(!result.isSuccess()&& result.getAction()==ServiceAction.ActionLoginasGuest){
			Util.showDialog(context,"Can not login for now\nTry again!");
		}
	}

	@Override
	public void onLoginTWSuccess(AccessToken accessToken) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoginTWError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompled(boolean b) {
		// TODO Auto-generated method stub
		if (b) {
			Session session = Session.getActiveSession();
			exeLoginFacebook(session.getAccessToken());
		}
	}
}