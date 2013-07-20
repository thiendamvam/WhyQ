package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.model.BillItem;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.whyq.R;

public class WhyqCheckedBillActivity extends ImageWorkerActivity {

	public static final String ARG_USER_ID = "userid";

	protected static final String ARG_MODE = "mode";

	protected static final String SAVING = "saving";

	private BillAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_checked_bill);

		setTitle(R.string.checked_bills);

		RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
		group.getLayoutParams().width = WhyqApplication.sScreenWidth * 3 / 5;
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.viewPlace) {
					mAdapter.switchPlaceMode(true);
				} else {
					mAdapter.switchPlaceMode(false);
				}
			}
		});

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
		final String userId = getIntent().getExtras().getString(ARG_USER_ID);
		getService().getOrder(getEncryptedToken(), userId);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null && result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetOrder) {
			mAdapter.setItems(DataParser.parseBills(String.valueOf(result
					.getData())));
		}
	}

	static class BillAdapter extends BaseAdapter {

		private static final int PHOTO_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<BillItem> mItems;
		private ImageViewHelper mImageWorker;
		private boolean isPlaceMode;

		public BillAdapter(Context context, ImageViewHelper imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<BillItem>();
			this.mImageWorker = imageWorker;
		}
		
		public void switchPlaceMode(boolean placeMode) {
			isPlaceMode = placeMode;
			notifyDataSetChanged();
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
			if (isPlaceMode) {
				int count  = item.getBusiness_info().getCount_favourite_member();
				holder.unit.setText("Visit " + count + (count > 1 ? " times" : " time"));
			} else {
				holder.unit.setText("Bill normal");
			}
			holder.name.setText(item.getBusiness_info().getName_store());
			holder.price.setText("$ " + item.getTotal_value());

			mImageWorker.downloadImage(item.getBusiness_info().getLogo(),
					holder.photo);

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
