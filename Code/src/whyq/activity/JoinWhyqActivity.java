/**
 * 
 */
package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.WhyqApplication;
import whyq.interfaces.JoinPerm_Delegate;
import whyq.model.User;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

/**
 * @author Linh Nguyen
 * This activity supports to create new Perming account.
 */
public class JoinWhyqActivity extends Activity implements TextWatcher, JoinPerm_Delegate {
	Button createAccount;
	EditText name;
	EditText nickName;
	EditText userName;
	EditText email;
	EditText password;
	EditText confirmPassword;
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.whyq_join);
        
        TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        name = (EditText) findViewById(R.id.acccountName);
		//name.addTextChangedListener(this);
        //nickName = (EditText) findViewById(R.id.nickName);
        email = (EditText) findViewById(R.id.acccountEmail);
		//email.addTextChangedListener(this);
		password = (EditText) findViewById(R.id.acccountPassword);
		//password.addTextChangedListener(this);
		confirmPassword = (EditText) findViewById(R.id.accountConfirmPassword);
        //confirmPassword.addTextChangedListener(this);
        
        createAccount = (Button) findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Send request to server to create new account along with its params
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);				
				nameValuePairs.add(new BasicNameValuePair("type", prefs.getString(Constants.LOGIN_TYPE, "")));
				if (prefs.getString(Constants.LOGIN_TYPE, "").equals(Constants.FACEBOOK_LOGIN)) {// Facebook
					nameValuePairs.add(new BasicNameValuePair("oauth_token", prefs.getString(Constants.ACCESS_TOKEN, "")));
					nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("username", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("cpassword", confirmPassword.getText().toString()));
				} else if(prefs.getString(Constants.LOGIN_TYPE, "").equals(Constants.TWITTER_LOGIN)) { // Twitter
					nameValuePairs.add(new BasicNameValuePair("oauth_token", prefs.getString(OAuth.OAUTH_TOKEN, "")));
					nameValuePairs.add(new BasicNameValuePair("oauth_token_secret", prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "")));
					nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("username", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("cpassword", confirmPassword.getText().toString()));
				} else { // Perm
					nameValuePairs.add(new BasicNameValuePair("oauth_token", ""));
					nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("username", name.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("cpassword", confirmPassword.getText().toString()));
				}
				
				XMLParser parser = new XMLParser(XMLParser.JOIN_PERM, JoinWhyqActivity.this, API.createAccountURL, nameValuePairs);
				User user = parser.getUser();
			}
		});
	}
	
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (name.getText().toString().equals("")) {
			name.setError("Name is required!");
		}
		if (email.getText().toString().equals("")) {
			email.setError("Email is required!");
		}
		if (password.getText().toString().equals("")) {
			password.setError("Password is required!");
		}
		if (confirmPassword.getText().toString().equals("") || !confirmPassword.getText().toString().equals(password.getText().toString())) {
			confirmPassword.setError("Confirm password is invalid!");
		}
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(User user) {
		// TODO Auto-generated method stub
		if (user != null) {
			// Store the user object to PermpingApplication
			WhyqApplication state = (WhyqApplication) getApplicationContext();
			state.setUser(user);
			if(getApplicationContext() != null && email != null && password != null) {
				if(email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
					XMLParser.storePermpingAccount(getApplicationContext(), email.getText().toString(), password.getText().toString());
				}
			}			
			if(user != null) FollowerActivity.isLogin = true;		
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Joined perm failed! Please try again.", Toast.LENGTH_LONG);
	    	toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
	    	toast.show();

		}
		this.finish();
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "Joined perm failed! Please try again.", Toast.LENGTH_LONG);
    	toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
    	toast.show();
    	this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
