package whyq.utils.share;

import com.whyq.R;

import whyq.WhyqApplication;
import whyq.activity.WhyqFriendsFacebookActivity;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FacebookPostActivity extends Activity implements
		android.view.View.OnClickListener, IServiceListener {

	private ShareHandler shareHandler;
	private String shareMessage;
	private TextView tvNumberChar;
	private EditText message;
	private Context co0ntext;
	private TextView tvUserLoggedIn;
	private SharedPreferencesManager _sharedPrefManager;
	private FacebookPostActivity context;

	@Override
	public void onResume() {
		shareMessage = "";
		message.setText(shareMessage + "\n "
				+ Constants.LINK_SHARE_FACEBOOK_GLOBAL);
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = FacebookPostActivity.this;
		shareHandler = new ShareHandler(context);
		View v = LayoutInflater.from(context).inflate(R.layout.facebook_view,
				null);
		Button btnSend = (Button) v.findViewById(R.id.facebook_buttonSend);
		tvNumberChar = (TextView) v.findViewById(R.id.facebook_numchar);
		message = (EditText) v.findViewById(R.id.facebook_message);
		message.setText("");
		tvNumberChar.setText("" + (1000 - message.length()));
		message.addTextChangedListener(mTextEditorWatcher);
		tvUserLoggedIn = (TextView) v.findViewById(R.id.tvUserLoggedin);
		_sharedPrefManager = new SharedPreferencesManager(WhyqApplication
				.Instance().getApplicationContext());
//		String name = _sharedPrefManager.loadFacebookName();
//		if (name == null)
//			name = "";
//		tvUserLoggedIn.setText(name);
		btnSend.setOnClickListener(this);
		setContentView(v);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setFinishOnTouchOutside(true);
		}
		getWindow().setLayout(540, 600);
	}

	
	public boolean checkFBLogin(){
		String accessToken = getAccessToken();
		if (accessToken == null) {
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
					exeOrderCheckFacebook(accessToken);
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
		} else {
			exeOrderCheckFacebook(accessToken);
		}
	
		return false;
	}
	protected void exeOrderCheckFacebook(String accessToken) {
		// TODO Auto-generated method stub
		Service service = new Service(FacebookPostActivity.this);
		service.pushOrderCheck(WhyqApplication.Instance().getRSAToken(), "", "", message.getText().toString(), "");
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		if (viewId == R.id.facebook_buttonSend) {
			postToWall();
		}
	}

	private void cancel() {
		// TODO Auto-generated method stub
		finish();
	}

	private void postToWall() {
		// TODO Auto-generated method stub
		if (shareMessage == null) {
			Toast.makeText(context, "Please input something to share!", 3000).show();
		} else {
			if (shareMessage.equals("")) {

			} else {
				if (!shareMessage.matches("null")) {
					new AsyncShare(ShareHandler.TWITTER, shareMessage).execute();
					message.setText("");
				}
				// dismiss();
			}
		}

	}

	class AsyncShare extends AsyncTask<Void, Void, String> {
		int type;
		String message;

		public AsyncShare(int typeShare, String shareMesage) {
			// TODO Auto-generated constructor stub
			type = typeShare;
			message = shareMesage;
		}

		@Override
		protected String doInBackground(Void... params) {
			boolean result = false;
			if (type == ShareHandler.TWITTER) {
				result = shareHandler.postFacebook(message);
			} else {

			}
			if (result) {
				return "Success";
			} else {
				return "Failer";
			}
		}

		@Override
		protected void onPostExecute(String sResponse) {
			if (sResponse.equals("Success")) {
				Toast.makeText(context, "Share successful", 1000);
			} else {
				Toast.makeText(context,
						"Can not share for now \n Please check your network!",
						1000);
			}
			finish();
		}
	}

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// This sets a textview to the current length
			tvNumberChar.setText(String.valueOf(1000 - s.length()));
		}

		public void afterTextChanged(Editable s) {
		}
	};

	public void onCancelClicked(View v) {
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		
	}
}
