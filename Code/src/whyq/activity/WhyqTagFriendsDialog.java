package whyq.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import whyq.adapter.FriendAdapter;
import whyq.interfaces.FragmentDialogListener;
import whyq.model.FriendFacebook;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.BaseRequestListener;
import whyq.utils.facebook.sdk.AsyncFacebookRunner;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.facebook.sdk.Util;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.internal.ch;
import com.whyq.R;

public class WhyqTagFriendsDialog extends DialogFragment {

	private ListView lvFriends;
	private Context context;
	private List<FriendFacebook> friendList;
	private FragmentDialogListener listener;
	private List<FriendFacebook> friendTagList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, 1);

		friendList = new ArrayList<FriendFacebook>();
		friendTagList = new ArrayList<FriendFacebook>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater
				.inflate(R.layout.tags_friend_screen, container, false);
		context = getActivity();
		lvFriends = (ListView) v.findViewById(R.id.lvFriends);
		Bundle bundle = getArguments();
		if (bundle.containsKey("accessToken")) {
			String accessToken = bundle.getString("accessToken");
			exeGetData(accessToken);
		}
		return v;
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(context);
	}

	private void exeGetData(String accessToken) {
		// TODO Auto-generated method stub

		if (accessToken != null) {
			Facebook mfacebook = new Facebook(Constants.FACEBOOK_APP_ID);
			mfacebook.setAccessToken(accessToken);
			final AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mfacebook);
			mAsyncRunner.request("me/friends", new Bundle(), new FriendListRequestListener());
		} else {

		}

	}

	public class FriendListRequestListener extends BaseRequestListener {

		private Object _error;

		public void onComplete(final String response) {
			_error = null;
			Log.d("friends",""+response.toString());
			try {
				JSONObject json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						// Do stuff here with your friends array,
						// which is an array of JSONObjects.
						friendList = getFrindsFromJson(friends);
						bindFriends(friendList);
					}
				});

			} catch (JSONException e) {
				_error = "JSON Error in response";
			} catch (FacebookError e) {
				_error = "Facebook Error: " + e.getMessage();
			}

			if (_error != null) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(context, "Error occurred:  " + _error,
								Toast.LENGTH_LONG).show();
					}
				});
			}
		}


		protected List<FriendFacebook> getFrindsFromJson(JSONArray friends) {
			// TODO Auto-generated method stub
			int length = friends.length();
			for(int i=0;i<length;i++){
				FriendFacebook item = new FriendFacebook();
				JSONObject obj = friends.optJSONObject(i);
				if(obj!=null){
					item.setFirstName(obj.optString("name"));
					item.setFacebookId(obj.optString("id"));
					friendList.add(item);
				}
			}
			return friendList;
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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listener = (FragmentDialogListener) activity;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		listener.onCompleted(friendTagList);
	}

	private List<FriendFacebook> getFriends() {
		// TODO Auto-generated method stub
		List<FriendFacebook> list = new ArrayList<FriendFacebook>();
		for (int i = 0; i < 10; i++) {
			FriendFacebook item = new FriendFacebook();
			list.add(item);
		}
		return list;
	}

	private void bindFriends(List<FriendFacebook> friendList) {
		// TODO Auto-generated method stub
		FriendAdapter adapter = new FriendAdapter(context, friendList);
		lvFriends.setAdapter(adapter);
	}
	
	public void onChecked(View v){
		CheckBox cbx = (CheckBox)v;
		if(cbx.isChecked()){
			friendTagList.add((FriendFacebook)v.getTag());
		}
	}
}
