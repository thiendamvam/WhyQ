package whyq.activity;

import java.io.File;
import java.util.ArrayList;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.Bill;
import whyq.model.BillPushNotification;
import whyq.model.OrderCheckData;
import whyq.model.ResponseData;
import whyq.model.ShareData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.share.ShareHandler;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

public class WhyqShareActivity extends Activity implements IServiceListener {

	private static final int GET_IMAGE = 0;
	private static final int FACEBOOK = 1;
	private TextView tvTitle;
	private EditText etMessage;
	private String avatarPath;
	private ImageButton btnCaptureImage;
	private SharedPreferencesManager sharePreferences;
	private String billId;
	private String accessToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_share);
		BillPushNotification pushNotificationData = null;
		if(getIntent().getExtras()!=null)
			pushNotificationData = (BillPushNotification)getIntent().getExtras().getSerializable("push_data");
		
		if(pushNotificationData!=null){
			billId = pushNotificationData.getBillId();
			findViewById(R.id.btnDone).setVisibility(View.INVISIBLE);
			tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
			etMessage = (EditText)findViewById(R.id.etMessage);
			btnCaptureImage = (ImageButton)findViewById(R.id.btnCaptureImage);
			tvTitle.setText("Share");
			sharePreferences = new SharedPreferencesManager(WhyqApplication
					.Instance().getApplicationContext());
			
		}else{
			
		}
	}


	public void onBack(View v){
		finish();
	}
	public void onTag(View v){
		
	}
	public void onSend(View v){
		
		if(etMessage.getText().toString().equals("")){
			Toast.makeText(WhyqShareActivity.this, "Please input your comment", Toast.LENGTH_LONG).show();
		}else{
			accessToken = getAccessToken();
			if (accessToken != null) {
				Facebook fb = new Facebook(Constants.FACEBOOK_APP_ID);
				fb.setAccessToken(accessToken);
				shareWhyq(accessToken);

			}else{
				final Facebook mFacebook;
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
						mFacebook.setAccessExpiresIn("0");
						mFacebook.setAccessToken(accessToken);
						mFacebook.setAccessExpires(mFacebook.getAccessExpires());
						shareWhyq(accessToken);
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

		}
	}
	
	public void shareWhyq(String facebookId){
		Service service  = new Service(WhyqShareActivity.this);
		service.pushOrderCheck(WhyqApplication.Instance().getRSAToken(), billId, facebookId, etMessage.getText().toString()	,avatarPath );
	}
	
	protected void exePostFacebook(String accessToken, OrderCheckData data) {
		// TODO Auto-generated method stub
		ShareHandler shareHandler = new ShareHandler(WhyqShareActivity.this);
		ShareData shareData = new ShareData();
		shareData.setCaption("");
		shareData.setDescription("");
		shareData.setLink(data.getLink());
		shareData.setMessage(etMessage.getText().toString());
		shareData.setName("WHY Q");
		shareData.setPicture(data.getImage());
		shareData.setThumb("");
		shareHandler.postFacebook(accessToken,shareData);
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}
	public void onCaptureImage(View v){
		Intent intent = new Intent(WhyqShareActivity.this, ImageActivity.class);
		startActivityForResult(intent,GET_IMAGE);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("onActivityResult Join","");
	    if (resultCode == RESULT_OK) {
	        if (requestCode == GET_IMAGE ) {
	        	avatarPath = data.getStringExtra("path");
	        	Log.d("onActivityResult Join",""+avatarPath);
	        	if(avatarPath!=null){
	        		 File imgFile = new  File(avatarPath);
	        		if(imgFile.exists()){
	        			btnCaptureImage.setImageURI(Uri.fromFile(imgFile));
	        		}
	        	}
	        }
	    }
	    
	    ImageActivity.imagePath = "";
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		if(result.isSuccess()&&result.getAction()==ServiceAction.ActionOrderCheck){
			if(accessToken!=null){
				ResponseData data = (ResponseData)result.getData();
				if(data.getStatus().equals("200")){
					OrderCheckData responseData = (OrderCheckData)data.getData();
					exePostFacebook(accessToken, responseData);
				}else if(data.getStatus().equals("401")){
					Util.loginAgain(getParent(), data.getMessage());
				}else if(data.getStatus().equals("204")){
				}else{
					
				}
				
				
			}
		}else{
			
		}
	}
}
