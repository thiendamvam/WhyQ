package whyq.utils.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.facebook.BaseRequestListener;
import whyq.utils.facebook.sdk.AsyncFacebookRunner;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.FacebookError;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ShareHandler implements ShareListener {
	private String shareMessage;
	Twitter _twitter;
	SharedPreferencesManager _shareManager;
	private String linkshareFacebook = null;
	private String linkshareTwitter = null;
	public static int TWITTER = 1;
	public static int FACEBOOK = 2;
	public Context context;
	public static String errorMesage = "";
	private Context mContext;
	private StatusUpdate mStatus;
	private Status status;

	public ShareHandler(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;
		_shareManager = new SharedPreferencesManager(context);
		_twitter = _shareManager.loadTwitter();
	}

	@Override
	public void success() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}

	// public static Handler handlePostTwitter = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 1) {
	// Toast.makeText(
	// WhyqApplication.Instance().getApplicationContext(),
	// "Share successful", Toast.LENGTH_SHORT).show();
	// } else {
	// Toast.makeText(
	// WhyqApplication.Instance().getApplicationContext(),
	// (String) msg.obj, Toast.LENGTH_SHORT).show();
	// }
	// }
	// };

	// public boolean postTwitter(Context context, String message, String
	// storyID) {
	// String msg = limitContent(message, "");
	// mStatus = new StatusUpdate(msg);
	// // mStatus.setMedia(Config.FILE_SHARE_THUMNAIL_GLOBAL);
	// String sdcard_thumb = "";
	// synchronized (IssueListActivity.db) {
	// IssueListActivity.db.open();
	// sdcard_thumb = IssueListActivity.db.getTwitterThumb(storyID);
	// IssueListActivity.db.close();
	// }
	// // File dir = Environment.getExternalStorageDirectory();
	// File yourFile = new File("", sdcard_thumb);
	// mStatus.setMedia(yourFile);
	// try {
	// status = _twitter.updateStatus(mStatus);
	// return true;
	// } catch (TwitterException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return false;
	// }
	// }

	public boolean postFacebook(String accessToken, String message) {
		// message = IssueAdapter.currentDesription;

		Facebook facebook = new Facebook(Constants.FACEBOOK_APP_ID);
		facebook.setAccessToken(accessToken);
		if (facebook != null) {
			String namePost = "";// = IssueAdapter.currentDesription;

			String msg = limitContent(message, " " + linkshareFacebook);
			// FacebookController.updateMessage(facebook, msg);
			AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
					facebook);
			Bundle params = new Bundle();
			params.putString("message", msg);
			params.putString("name", namePost);
			params.putString("caption", Constants.LINK_SHARE_FACEBOOK_GLOBAL);
			String disription = "";
			String thumb = "";
			// params.putString("link", Config.LINK_SHARE_FACEBOOK_GLOBAL);
			params.putString("description", disription);

			Log.d("WallPostListener", "thumb is:" + thumb);
			if (thumb != null && !thumb.equals("") && !thumb.equals("null"))
				params.putString("picture", thumb);
			// else
			// params.putString("picture",
			// "http://blogs.news.com.au/images/uploads/black.jpg");
			mAsyncFbRunner.request("me/feed", params, "POST",
					new WallPostListener());
			return true;
		}
		return false;
	}

	private Handler mRunOnUi = new Handler();

	private final class WallPostListener extends BaseRequestListener {


		@Override
		public void onComplete(final String response) {

			// TODO Auto-generated method stub

			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					Log.d("WallPostListener", "" + response);
					Toast.makeText(context, "Posted to Facebook",
							Toast.LENGTH_SHORT).show();
				}
			});

		
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			
		}
	}

	static public String limitContent(String message, String extra) {
		String result = message;
		String mark = "\"";
		Boolean endIsLyric = false;
		if (result.endsWith(mark))
			endIsLyric = true;
		int limit = (200000 - extra.length());
		if (result.length() > limit) {
			if (endIsLyric) {
				result = result.substring(0, limit - (3 + mark.length()));
				result += ("..." + mark);
			} else {
				result = result.substring(0, limit - 3);
				result += ("...");
			}
		}
		result += "\n" + extra;
		Log.v("Post Tweet", result);
		return result;
	}
}
