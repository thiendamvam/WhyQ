package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.mockup.MockupDataLoader;
import whyq.model.BillItem;
import whyq.utils.ImageWorker;
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

public class CheckedBillActivity extends ImageWorkerActivity {

	private BillAdapter mAdapter;
	private LoadDataTask mLoadDataTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_checked_bill);

		setTitle(R.string.checked_bills);

		findViewById(R.id.radioGroupContainer).getLayoutParams().height = WhyqApplication.sBaseViewHeight;
		findViewById(R.id.radioGroup).getLayoutParams().width = WhyqApplication.sScreenWidth * 3 / 5;

		mAdapter = new BillAdapter(this, mImageWorker);
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(CheckedBillActivity.this,
						CommentActivity.class));
			}
		});

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.execute();

		// Service service = new Service();
		// service.addListener(new IServiceListener() {
		//
		// @Override
		// public void onCompleted(Service service, ServiceResponse result) {
		// if (result != null) {
		// Log.d("FriendFacebook", String.valueOf(result));
		// }
		// }
		// });
		// service.getFriendsFacebook(getToken());
	}

	class LoadDataTask extends AsyncTask<Void, Void, List<BillItem>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setLoading(true);
		}

		@Override
		protected List<BillItem> doInBackground(Void... params) {
			Log.d("LoadDataTask", "Start loading.. ");
			return MockupDataLoader.loadBills();
		}

		@Override
		protected void onPostExecute(List<BillItem> result) {
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

	static class BillAdapter extends BaseAdapter {

		private static final int PHOTO_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<BillItem> mItems;
		private ImageWorker mImageWorker;

		public BillAdapter(Context context, ImageWorker imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<BillItem>();
			this.mImageWorker = imageWorker;
		}

		public void setItems(List<BillItem> items) {
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
						R.layout.bill_list_item, parent, false);
			}

			ViewHolder holder = getViewHolder(convertView);
			BillItem item = mItems.get(position);
			holder.name.setText(item.getName());
			if (item.getCount() > 1) {
				holder.unit.setText("check bill " + item.getCount() + "times");
			} else {
				holder.unit.setText("check bill " + item.getCount() + "time");
			}
			holder.price.setText("$ " + item.getPrice());

			mImageWorker.loadImage(item.getPhotoUrl(), holder.photo,
					PHOTO_SIZE, PHOTO_SIZE);

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
			ImageView photo;
			TextView name;
			TextView unit;
			Button price;

			public ViewHolder(View view) {
				view.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				photo = (ImageView) view.findViewById(R.id.photo);
				photo.getLayoutParams().width = PHOTO_SIZE;
				photo.getLayoutParams().height = PHOTO_SIZE;
				name = (TextView) view.findViewById(R.id.name);
				unit = (TextView) view.findViewById(R.id.unit);
				price = (Button) view.findViewById(R.id.price);
			}
		}

	}
}
