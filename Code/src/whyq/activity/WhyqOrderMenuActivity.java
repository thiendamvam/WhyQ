package whyq.activity;

import whyq.WhyqApplication;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.whyq.R;

public class WhyqOrderMenuActivity extends DialogFragment implements OnClickListener{
	private String storeId;
	private Display display;
	private Context context;
	private Button btnHomeDelivery;
	private Button btnTakeAway;
	private Button btnHotelDelivery;
	private Button btnDinein;
	private Button btnCancel;
	public WhyqOrderMenuActivity(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.whyq_store_order_menu);
		display = getActivity().getWindowManager().getDefaultDisplay();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View v = LayoutInflater.from(WhyqApplication.Instance().getApplicationContext()).inflate(R.layout.whyq_store_order_menu, null);
//		setContentView(v);
//		context = this;
		btnHomeDelivery =(Button)v.findViewById(R.id.btnHomeDelivery);
		btnTakeAway = (Button)v.findViewById(R.id.btnHomeTakeAway);
		btnHotelDelivery = (Button)v.findViewById(R.id.btnHotelRoomDelivery);
		btnDinein = (Button)v.findViewById(R.id.btnDineIn);
		btnCancel = (Button)v.findViewById(R.id.btnCancel);
		
		btnHomeDelivery.setOnClickListener(this);
		btnTakeAway.setOnClickListener(this);
		btnHotelDelivery.setOnClickListener(this);
		btnDinein.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		 v.setBackgroundResource(android.R.color.transparent);
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		getDialog().getWindow().setLayout(100,300);//display.getWidth()/3, display.getHeight() / 4
		getDialog().setCanceledOnTouchOutside(true);
		return v;
	}
	public void homeDeliveryClicked() {
		
		Intent intent = new Intent(context,WhyQHomeDeliveryActivity.class );
		intent.putExtra("store_id", storeId);
		startActivity(intent);

	}

	public void homeTakeawayClicked() {
		Intent intent = new Intent(context,WhyQTakeAwayActivity.class );
		intent.putExtra("store_id", storeId);
		startActivity(intent);
	}

	public void hotelRoomDeliveryClicked() {
		Intent intent = new Intent(context,WhyQHotelRoomDelivery.class );
		intent.putExtra("store_id", ""+storeId);
		startActivity(intent);
	}

	public void dineinClicked() {
		WhyQDineInFragment dineIn = new WhyQDineInFragment(storeId);
		 dineIn.show(getFragmentManager(), "orderMenuFrag");
	}

	public void cancelClicked() {
		dismiss();
//		finish();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btnHomeDelivery:
			homeDeliveryClicked();
			break;
		case R.id.btnHomeTakeAway:
			homeTakeawayClicked();
			break;
		case R.id.btnHotelRoomDelivery:
			hotelRoomDeliveryClicked();
			break;
		case R.id.btnDineIn:
			dineinClicked();
			break;
		case R.id.btnCancel:
			cancelClicked();
			break;
		default:
			break;
		}
		
	}
}
