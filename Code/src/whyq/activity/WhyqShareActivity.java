package whyq.activity;

import java.io.File;
import java.util.List;

import whyq.WhyqApplication;
import whyq.interfaces.FragmentDialogListener;
import whyq.interfaces.IServiceListener;
import whyq.model.BillPushNotification;
import whyq.model.FriendFacebook;
import whyq.model.OrderCheckData;
import whyq.model.ResponseData;
import whyq.model.ShareData;
import whyq.model.TransferData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.share.ShareHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.whyq.R;

public class WhyqShareActivity extends Activity implements IServiceListener, FragmentDialogListener{

	private static final int GET_IMAGE = 0;
	private static final int FACEBOOK = 1;
	private static final int TAG_FRIENDS = 4;
	private TextView tvTitle;
	private EditText etMessage;
	private String avatarPath;
	private ImageButton btnCaptureImage;
	private SharedPreferencesManager sharePreferences;
	private String billId;
	private String accessToken;
	private ToggleButton tglShareWhyq;
	private ToggleButton tgleShareFb;
	private ProgressBar prgBar;
	private String facebookIdTag;
	private Context context;
	private boolean isComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_share);
		context = this;
		isComment = getIntent().getBooleanExtra("is_comment", false);
		BillPushNotification pushNotificationData = null;
		if(getIntent().getExtras()!=null)
			pushNotificationData = (BillPushNotification)getIntent().getExtras().getSerializable("push_data");
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		etMessage = (EditText)findViewById(R.id.etMessage);
		btnCaptureImage = (ImageButton)findViewById(R.id.btnCaptureImage);
		tglShareWhyq = (ToggleButton) findViewById(R.id.tglShareWhyq);
		tgleShareFb = (ToggleButton) findViewById(R.id.tglShareFB);
		
		if(isComment)
			tgleShareFb.setVisibility(View.GONE);
		findViewById(R.id.btnDone).setVisibility(View.INVISIBLE);
		tvTitle.setText("Share");
		prgBar = (ProgressBar)findViewById(R.id.prgBar);
		sharePreferences = new SharedPreferencesManager(WhyqApplication
				.Instance().getApplicationContext());
		if(pushNotificationData!=null){
			billId = pushNotificationData.getBillId();
			findViewById(R.id.btnDone).setVisibility(View.INVISIBLE);

			
		}else{
			
		}
	}


	public void setProgressBar(boolean isShow){
		prgBar.setVisibility(isShow?View.VISIBLE:View.INVISIBLE);
	}
	public void onBack(View v){
		finish();
	}
	public void onTag(View v){
		checkLoginFacebook(false);
	}
	
	public void showTagDialog(String fbId){
		WhyqTagFriendsDialog fragment = new WhyqTagFriendsDialog();
		Bundle bundle = new Bundle();
		bundle.putString("accessToken", fbId);
//		fragment.setArguments(bundle);
//		getFragmentManager().beginTransaction().add(fragment, "add_tag").commit();
		Intent i = new Intent(context, WhyqTagFriendsDialog.class);
		i.putExtras(bundle);
		startActivityForResult(i, TAG_FRIENDS);
	}
	public void onInviteClicked(View v){
		
	}
	
	public void onSend(View v){
		
		if(etMessage.getText().toString().equals("")){
			Toast.makeText(WhyqShareActivity.this, "Please input your comment", Toast.LENGTH_LONG).show();
		}else{
			checkLoginFacebook(true);
		}
	}
	
	private void checkLoginFacebook(final boolean isSend) {
		// TODO Auto-generated method stub

		accessToken = getAccessToken();
		if (accessToken != null) {
			Facebook fb = new Facebook(Constants.FACEBOOK_APP_ID);
			fb.setAccessToken(accessToken);
			if(isSend){
				shareWhyq(accessToken);
			}else{
				showTagDialog(accessToken);
			}

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
					if(isSend){
						shareWhyq(accessToken);
					}else{
						showTagDialog(accessToken);
					}
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


	public void shareWhyq(String facebookId){
		if(!tgleShareFb.isChecked())
			facebookId = null;
		Service service  = new Service(WhyqShareActivity.this);
		service.pushOrderCheck(WhyqApplication.Instance().getRSAToken(), billId, facebookIdTag, etMessage.getText().toString()	,avatarPath );
		setProgressBar(true);
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
		try {

			Log.d("onActivityResult Join","onActivityResult "+requestCode);
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
		        }else if(requestCode==TAG_FRIENDS){
		        		TransferData tfdata = (TransferData) data.getSerializableExtra("data");
			        	if(tfdata!=null)
			        		facebookIdTag = convertData(tfdata.getData());	
		        	
		        }
		    }
		    
		    ImageActivity.imagePath = "";
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		setProgressBar(false);
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


	@Override
	public void onCompleted(Object data) {
		// TODO Auto-generated method stub
		facebookIdTag = convertData(data);
		Toast.makeText(context, "Completed"+data, Toast.LENGTH_LONG).show();
	}


	private String convertData(Object data) {
		// TODO Auto-generated method stub

		if(data!=null){
			List<FriendFacebook> list = (List<FriendFacebook>) data;
			String result="";
			for(int i = 0; i<list.size();i++){
				FriendFacebook item = list.get(i);
				if(i==0)
					result+=""+item.getFacebookId();
				else
					result+=","+item.getFacebookId();
			}
			Log.d("convertData","convertData"+result);
			return result;
		}else{
			return null;
		}
	}
}
