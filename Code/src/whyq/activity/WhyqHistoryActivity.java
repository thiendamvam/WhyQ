package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.model.BillItem;
import whyq.model.BusinessInfo;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.Util;
import android.content.Context;
import android.os.Bundle;
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

public class WhyqHistoryActivity extends ImageWorkerActivity {

	public static final String ARG_USER_ID = "userid";

	private BillAdapter mAdapter;
	
	private static final int[] STATUS_MAP = new int[] {R.drawable.waiting_for_payment, R.drawable.declined, R.drawable.delivered, R.drawable.dismissed, R.drawable.waiting_to_be_accepted,  R.drawable.paid};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_history);

		setTitle(R.string.history);

		mAdapter = new BillAdapter(this, mImageWorker);
		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});

		setLoading(true);
		String userId = getIntent().getStringExtra(ARG_USER_ID);
		getService().getHistories(getEncryptedToken(), userId);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null && result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetHistories) {
			mAdapter.setItems(DataParser.parseBills(String.valueOf(result
					.getData())));
		}
	}

	static class BillAdapter extends BaseAdapter {

		private static final int PHOTO_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<BillItem> mItems;
		private ImageViewHelper mImageWorker;

		public BillAdapter(Context context, ImageViewHelper imageWorker) {
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
						R.layout.whyq_bill_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			ViewHolder holder = getViewHolder(convertView);
			BillItem item = mItems.get(position);
			if (item.getStatus_bill() > 0 && item.getStatus_bill() <= 6) {
				holder.circleIcon.setImageResource(STATUS_MAP[item.getStatus_bill() - 1]);
			} else {
				holder.circleIcon.setImageResource(R.drawable.circle);
			}
			holder.circleIcon.setVisibility(View.VISIBLE);
			BusinessInfo bi = item.getBusiness_info();
			if (bi != null) {
				holder.name.setText(bi.getName_store());
				mImageWorker.downloadImage(bi.getLogo(), holder.photo);
			}
			holder.unit.setText("Order id: " + item.getId());
			holder.price.setVisibility(View.GONE);

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
			ImageView circleIcon;
			ImageView photo;
			TextView name;
			TextView unit;
			Button price;

			public ViewHolder(View view) {
				circleIcon = (ImageView) view.findViewById(R.id.circle);
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
