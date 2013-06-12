package whyq.activity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.mockup.MockupDataLoader;
import whyq.model.FacebookFriend;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.ImageWorker;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class FacebookFriendsActivity extends NavigationActivity {

	private FacebookFriendsAdapter mAdapter;
	private ImageWorker mImageWorker;
	private LoadDataTask mLoadDataTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook_friends);

		setTitle(R.string.friend_from_facebook);

		findViewById(R.id.searchField).getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		
		mImageWorker = new ImageWorker(this);
		mImageWorker.initCache(this, WhyqApplication.DISK_CACHE_DIR, 0.25f);

		mAdapter = new FacebookFriendsAdapter(this, mImageWorker);
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(FacebookFriendsActivity.this,
						UserBoardActivity.class));
			}
		});

		 mLoadDataTask = new LoadDataTask();
		 mLoadDataTask.execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		mImageWorker.setExitTasksEarly(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mImageWorker.setExitTasksEarly(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageWorker.clearCache();
	}

	class LoadDataTask extends AsyncTask<Void, Void, List<FacebookFriend>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setLoading(true);
		}

		@Override
		protected List<FacebookFriend> doInBackground(Void... params) {
			Log.d("LoadDataTask", "Start loading.. ");
			return MockupDataLoader.loadFacebookFriends();
		}

		@Override
		protected void onPostExecute(List<FacebookFriend> result) {
			super.onPostExecute(result);
			setLoading(false);
			if (isCancelled()) {
				return;
			}

			if (mAdapter != null) {
				mAdapter.setItems(result);
			}

		}

	}

	static class FacebookFriendsAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<FacebookFriend> mItems;
		private ImageWorker mImageWorker;

		public FacebookFriendsAdapter(Context context, ImageWorker imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<FacebookFriend>();
			this.mImageWorker = imageWorker;
		}

		public void setItems(List<FacebookFriend> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.friend_list_item, parent, false);
			}

			ViewHolder holder = getViewHolder(convertView);
			FacebookFriend item = mItems.get(position);
			holder.name.setText(item.getUser().getName());
			if (item.isFriend()) {
				holder.action.setVisibility(View.GONE);
			} else if (item.isWhyQMember()) {
				holder.action.setText(R.string.friend_all);
				holder.action.setVisibility(View.VISIBLE);
			} else {
				holder.action.setText(R.string.invite);
				holder.action.setVisibility(View.VISIBLE);
			}

			mImageWorker.loadImage(item.getUser().getAvatar().getUrl(),
					holder.avatar, AVATAR_SIZE, AVATAR_SIZE);

			return convertView;
		}

		private ViewHolder getViewHolder(View view) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view);
				view.setTag(holder);
			}
			return holder;
		}

		class ViewHolder {
			ImageView avatar;
			TextView name;
			Button action;

			public ViewHolder(View view) {
				view.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				avatar.getLayoutParams().width = AVATAR_SIZE;
				avatar.getLayoutParams().height = AVATAR_SIZE;
				name = (TextView) view.findViewById(R.id.name);
				action = (Button) view.findViewById(R.id.action);
			}
		}

	}
}
