package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.mockup.MockupDataLoader;
import whyq.model.ActivityItem;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.SpannableUtils;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class UserBoardActivity extends ImageWorkerActivity {

	public static final String ARG_USER_ID = "arg_user_id";
	public static final String ARG_FIRST_NAME = "arg_first_name";
	public static final String ARG_AVATAR = "arg_avatar";
	private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
	private ActivitiesAdapter mAdapter;
	private String mUserFirstName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_board);

		initCategory();

		final Intent i = getIntent();
		mUserFirstName = i.getStringExtra(ARG_FIRST_NAME);
		if (mUserFirstName != null) {
			setTitle(mUserFirstName);
		}
		final String avatar = i.getStringExtra(ARG_AVATAR);
		if (avatar != null) {
			ImageView imageView = (ImageView) findViewById(R.id.avatar);
			LayoutParams LP = imageView.getLayoutParams();
			LP.width = AVATAR_SIZE;
			LP.height = AVATAR_SIZE;
			mImageWorker.loadImage(avatar, imageView, AVATAR_SIZE, AVATAR_SIZE);
		}

		mAdapter = new ActivitiesAdapter(this);
		((ListView) findViewById(R.id.listview)).setAdapter(mAdapter);

		// final Service service = getService();
		//
		// final String userId = i.getStringExtra(ARG_USER_ID);
		// service.getUserActivities(getEncryptedToken(), userId);

		new AsyncTask<Void, Void, List<ActivityItem>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				setLoading(true);
			}

			@Override
			protected List<ActivityItem> doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
					return MockupDataLoader.loadActivities();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<ActivityItem> result) {
				setLoading(false);
				super.onPostExecute(result);
				mAdapter.setItems(result);
			}
		}.execute();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
	}

	private void initCategory() {
		bindCategory(R.id.check_bill, R.drawable.icon_cat_coffee, "2 places",
				"checked bills", new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(UserBoardActivity.this,
								CheckedBillActivity.class));
					}
				});
		bindCategory(R.id.history, R.drawable.icon_cat_cutlery, "15",
				"History", new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
		bindCategory(R.id.saving, R.drawable.icon_cat_wine, "0", "Saving",
				new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});
		bindCategory(R.id.comment, R.drawable.icon_cat_wine, "2", "Comments",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(UserBoardActivity.this,
								CommentActivity.class));
					}
				});
	}

	private void bindCategory(int id, int iconRes, String textTop,
			String textBottom, OnClickListener listener) {
		View categoryView = findViewById(id);
		categoryView.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		categoryView.setOnClickListener(listener);
		ImageView icon = (ImageView) categoryView.findViewById(R.id.icon);
		icon.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		icon.setImageResource(iconRes);
		TextView textviewTop = (TextView) categoryView
				.findViewById(R.id.textviewTop);
		textviewTop.setText(textTop);
		TextView textviewBottom = (TextView) categoryView
				.findViewById(R.id.textviewBottom);
		textviewBottom.setText(textBottom);
	}

	@Override
	protected View getTitleView() {
		LinearLayout titleView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.navigation_title_include_avatar, null);
		View title = super.getTitleView();
		LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		titleView.addView(title, 1, LP);
		return titleView;
	}

	class ActivitiesAdapter extends BaseAdapter {

		private Context mContext;
		private List<ActivityItem> mItems;

		public ActivitiesAdapter(Context context) {
			this.mContext = context;
			this.mItems = new ArrayList<ActivityItem>();
		}

		public void setItems(List<ActivityItem> items) {
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
						R.layout.activities_list_item, parent, false);
			}

			ViewHolder holder = getViewHolder(convertView);
			ActivityItem item = mItems.get(position);

			final String activity = mUserFirstName + " " + item.getAction()
					+ " " + item.getSubject();
			holder.activity.setText(SpannableUtils.stylistTextBold(
					activity,
					item.getAction(),
					mContext.getResources().getColor(
							android.R.color.darker_gray)));
			holder.activity.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.icon_quote, 0, 0, 0);

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
			TextView activity;

			public ViewHolder(View view) {
				activity = (TextView) view.findViewById(R.id.activity);
			}
		}

	}
}
