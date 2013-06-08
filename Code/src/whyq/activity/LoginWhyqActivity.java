/**
 * 
 */
package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.controller.AuthorizeController;
import whyq.interfaces.Login_delegate;
import whyq.utils.Constants;
import whyq.utils.RSA;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.FacebookConnector;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

/**
 * @author Linh Nguyen
 * This activity supports to authenticate the user
 * by user-name and password. Also, it can communicate 
 * to FB and Twitter to do authentication as well
 */
public class LoginWhyqActivity extends Activity implements Login_delegate {
	
	EditText email;
	EditText password;
	Button facebookLogin;
	Button twitterLogin;
	Button login;
	public static boolean isLoginFb = false;
	public static boolean isTwitter = false;
//	private ProgressDialog loadingDialog;
	ProgressBar progressBar;
	private WhyqMain login_delegate;
	SharedPreferences prefs;
	private FacebookConnector facebookConnector;
	public static Context context;
//	public LoginPermActivity(Login_delegate loginDelegate){
//		login_delegate = loginDelegate;
//	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.whyq_login);
        context = LoginWhyqActivity.this;
//        context = WhyqApplication.Instance().getApplicationContext();
        TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        login_delegate = new WhyqMain();
        email         = (EditText) findViewById(R.id.permEmail);
        password      = (EditText) findViewById(R.id.permPassword);
//        facebookLogin = (Button) findViewById(R.id.loginfb);
//        twitterLogin  = (Button) findViewById(R.id.logintw);
        login         = (Button) findViewById(R.id.loginPerm);
        progressBar = new ProgressBar(context);
    
        
                
        // Login button
        login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(checkInputData()){
					showLoadingDialog("Progress", "Please wait");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
//					nameValuePairs.add(new BasicNameValuePair("type", Constants.LOGIN_TYPE));
//					nameValuePairs.add(new BasicNameValuePair("oauth_token", ""));
					nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
					String pass =null;
					try {
						RSA rsa = new RSA();
						pass = rsa.RSAEncrypt(password.getText().toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
					nameValuePairs.add(new BasicNameValuePair("password", pass));
					AuthorizeController authorizeController = new AuthorizeController(LoginWhyqActivity.this);
					authorizeController.authorize(v.getContext(), nameValuePairs);
				}
			}
		
		});
        
        // 
        facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID, 
        		this, getApplicationContext(), new String[] {Constants.EMAIL, Constants.PUBLISH_STREAM});
        //Login with Facebook button
//        facebookLogin.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(final View v) {
//				// TODO Auto-generated method stub
//				// Clear FB info to show the login again
////				try {
////					facebookConnector.getFacebook().logout(v.getContext());
////				} catch (MalformedURLException me) {
////					me.printStackTrace();
////				} catch (IOException ioe) {
////					ioe.printStackTrace();
////				}
////				
////				PermpingApplication state = (PermpingApplication) context.getApplicationContext();
////				try {
////					if (!facebookConnector.getFacebook().isSessionValid()) {
////						AuthListener authListener = new AuthListener() {
////							
////							public void onAuthSucceed() {							
////								//Edit Preferences and update facebook access token
////								SharedPreferences.Editor editor = prefs.edit();
////								editor.putString(Constants.LOGIN_TYPE, Constants.FACEBOOK_LOGIN);
////								editor.putString(Constants.ACCESS_TOKEN, facebookConnector.getFacebook().getAccessToken());
////								editor.putLong(Constants.ACCESS_EXPIRES, facebookConnector.getFacebook().getAccessExpires());
////								editor.commit();
////							
////
////								// Check on server
////								List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
////								nameValuePairs.add(new BasicNameValuePair("type", Constants.FACEBOOK_LOGIN));
////								nameValuePairs.add(new BasicNameValuePair("oauth_token", prefs.getString(Constants.ACCESS_TOKEN, "")));
////								nameValuePairs.add(new BasicNameValuePair("email", ""));
////								nameValuePairs.add(new BasicNameValuePair("password", ""));
////								AuthorizeController authorizeController = new AuthorizeController(LoginPermActivity.this);
////								authorizeController.authorize(v.getContext(), nameValuePairs);
//////								boolean existed = AuthorizeController.authorize(getApplicationContext(), nameValuePairs, LoginPermActivity.this);
////								isLoginFb = true;
////							}
////							
////							public void onAuthFail(String error) {
////								// TODO Auto-generated method stub							
////							}
////						};
////						
////						SessionEvents.addAuthListener(authListener);
////						facebookConnector.login();
////						
////					}
////		
////				} catch (Exception e) {
////					// TODO: handle exception
////					Logger.appendLog(e.toString(), "facebooklog");
////				}
//			
//			    Facebook mFacebook;
//			    String token = null;
//				mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
//				final Activity activity = LoginWhyqActivity.this;//getParent();
//				mFacebook.authorize( activity, new String[] { "email", "status_update",
//						"user_birthday" }, new DialogListener() {
//					@Override
//					public void onComplete(Bundle values) {
//						//Log.d("", "=====>"+values.toString());
//						WhyqUtils permutils = new WhyqUtils();
//						String accessToken = values.getString("access_token");
//						permutils.saveFacebookToken("oauth_token", accessToken, getApplication());
////						// Check on server
//						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//						nameValuePairs.add(new BasicNameValuePair("type", Constants.FACEBOOK_LOGIN));
//						nameValuePairs.add(new BasicNameValuePair("oauth_token", accessToken));
//						nameValuePairs.add(new BasicNameValuePair("email", ""));
//						nameValuePairs.add(new BasicNameValuePair("password", ""));
//						AuthorizeController authorizeController = new AuthorizeController(LoginWhyqActivity.this);
//						authorizeController.authorize(v.getContext(), nameValuePairs);
////						boolean existed = AuthorizeController.authorize(getApplicationContext(), nameValuePairs, LoginPermActivity.this);
//						isLoginFb = true;
//					}
//
//					@Override
//					public void onFacebookError(FacebookError error) {
//
//					}
//
//					@Override
//					public void onError(DialogError e) {
//
//					}
//
//					@Override
//					public void onCancel() {
//						// cancel press or back press
//					}
//				});
//			}
//        });
        
        // Twitter Login button
//        twitterLogin.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				isTwitter = true;
//				Intent i = new Intent(v.getContext(), PrepareRequestTokenActivity.class);
//				v.getContext().startActivity(i);	
//			}
//		});
	}
	@Override
	public void onResume(){
		super.onResume();
//		if(isTwitter){
//			PermUtils permUtils = new PermUtils();
//			AccessToken accessToken = permUtils.getTwitterAccess(getApplicationContext());
//			if(accessToken != null){
//				String token = accessToken.getToken();//prefs.getString(OAuth.OAUTH_TOKEN, "");
//				String secret = accessToken.getTokenSecret();//prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
//				String oauth_verifier = prefs.getString(OAuth.OAUTH_VERIFIER, "");
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
//				nameValuePairs.add(new BasicNameValuePair("type", "twitter"));
//				nameValuePairs.add(new BasicNameValuePair("oauth_token", token));
//				nameValuePairs.add(new BasicNameValuePair("oauth_token_secret", secret));
//				nameValuePairs.add(new BasicNameValuePair("oath_verifier", oauth_verifier));
//				AuthorizeController authorize = new AuthorizeController(LoginPermActivity.this);
//				authorize.authorize(context, nameValuePairs);
//				
//			}
//		}
	}
	public void onPause() {
		super.onPause();
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
	
    public boolean checkInputData()
    {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        String user = email.getText().toString();
        String pass = password.getText().toString();
        if (user.length() == 0)
        {
        	email.setFocusable(true);
        	email.startAnimation(shake);
        	email.requestFocus();
        	return false;
        } else if (pass.length() == 0)
        {
        	password.setFocusable(true);
        	password.startAnimation(shake);
        	password.requestFocus();
        	return false;
        } else
        {
        	return true;
        }
    }
@Override
public void on_success() {
	// TODO Auto-generated method stub
	//Logger.appendLog("test log", "LoginSuccess");
	if(isLoginFb){
		ListActivity.isLogin = true;
		isLoginFb = false;
		if(WhyqMain.getCurrentTab() == 0) {
			((ListActivityGroup)ListActivityGroup.group).createFollowerActivity();
		} else {
			WhyqMain.back();
		}
	}else if(isTwitter){
		ListActivity.isLogin = true;
//		Intent intent = new Intent(context, PermpingMain.class);
//		context.startActivity(intent);
		isTwitter = false;
		if(WhyqMain.getCurrentTab() == 0) {
			((ListActivityGroup)ListActivityGroup.group).createFollowerActivity();
		} else {
			WhyqMain.back();
		}
	}else{
		ListActivity.isLogin = true;
		dismissLoadingDialog();
		Intent intent = new Intent(LoginWhyqActivity.this, WhyqMain.class);
		startActivity(intent);
//		if(WhyqMain.getCurrentTab() == 4) {
//			((ProfileActivityGroup)ProfileActivityGroup.group).createUI();
//		} else {
//			if(WhyqMain.getCurrentTab() == 0) {
//				((ListActivityGroup)ListActivityGroup.group).createFollowerActivity();
//			} else {
//				WhyqMain.back();
//			}
//		}
	}

}

@Override
public void on_error() {
	// TODO Auto-generated method stub
	//Logger.appendLog("test log", "loginerror");
	if(isLoginFb){
		isLoginFb = false;
		Intent intent = new Intent(getApplicationContext(), JoinWhyqActivity.class);
		getParent().startActivity(intent);
	}else if(isTwitter){
		isTwitter = false;
		Intent intent = new Intent(context, JoinWhyqActivity.class);
		context.startActivity(intent);
	}else{
		dismissLoadingDialog();
		Toast toast = Toast.makeText(getApplicationContext(), "Authentication failed!. Please try again!", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
		toast.show();
		
	}
}
private void showLoadingDialog(String title, String msg) {
//	loadingDialog = new ProgressDialog(getParent());
//	loadingDialog.setMessage(msg);
//	loadingDialog.setTitle(title);
//	loadingDialog.setCancelable(true);
//	loadingDialog.show();
	progressBar.setVisibility(View.VISIBLE);
}

private void dismissLoadingDialog() {
//	if (loadingDialog != null && loadingDialog.isShowing())
//		loadingDialog.dismiss();
	if(progressBar.getVisibility()==View.VISIBLE){
		progressBar.setVisibility(View.GONE);
	}
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event)
{		
    if ((keyCode == KeyEvent.KEYCODE_BACK))
    {
//        WhyqMain.back();
    	finish();
        return true;
    }
    return super.onKeyDown(keyCode, event);
}
}