package whyq.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import whyq.WhyqApplication;
import whyq.adapter.WhyQBillAdapter;
import whyq.controller.WhyqListController;
import whyq.interfaces.IServiceListener;
import whyq.model.Bill;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.service.paypal.PayPalUI;
import whyq.utils.Util;
import whyq.view.Whyq;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

public class WhyQBillScreen extends FragmentActivity implements IServiceListener{
	
	private TextView tvTitle;
	private ListView lvBill;
	private ArrayList<Bill> listBill;
	private float valueDiscount = 0;
	private TextView tvTotal;
	private TextView tvDiscount;
	private TextView tvTotalAfterDiscount;
	private float totalValue = 0;
	private float totalafterDiscount = 0;
	private Button btnDone;
	private Bundle bundle;
	private String pushNotificationData;
	private boolean isFromPushNotification = false;
	private boolean isOrdered;
	private String billId;
	public static int LOGIN_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_bill_screen);
		bundle = getIntent().getBundleExtra("data");
		if(bundle==null)
			bundle = getIntent().getExtras();
		pushNotificationData =  getIntent().getExtras().getString( "com.parse.Data" );
		if(pushNotificationData!=null){
			isFromPushNotification  = true;
			Log.d("pushNotificationData","data:"+pushNotificationData);
		}
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Bills");
		lvBill = (ListView)findViewById(R.id.lvBill);
		listBill = new ArrayList<Bill>();
		isOrdered = bundle.getBoolean("is_ordered", false);
		btnDone = (Button)findViewById(R.id.btnDone);
		if(!isOrdered){
			listBill = getBillList(ListDetailActivity.billList);
			btnDone.setText("Order");
		}else{
			listBill = WhyqCheckedBillActivity.listBill;
			String billStatus = bundle.getString("bill_status");
			if(billStatus!=null){
				if(billStatus.equalsIgnoreCase("1")){
					btnDone.setText("Paypal");		
				}else{
					btnDone.setVisibility(View.INVISIBLE);
				}
			}
			
			billId = bundle.getString("bill_id");
			exeGetBillDetail(billId);
		}
		tvTotal = (TextView)findViewById(R.id.tvTotal);
		tvDiscount = (TextView)findViewById(R.id.tvDiscount);
		tvTotalAfterDiscount = (TextView)findViewById(R.id.tvTotalafterDiscount);
		
		
		bindDatatoListview();
		bindBillValue();
	}
	private void exeGetBillDetail(String billId) {
		// TODO Auto-generated method stub
		Service service = new Service(WhyQBillScreen.this);
		service.getBillDetail(billId, WhyqApplication.Instance().getRSAToken());
		
	}
	private void bindBillValue() {
		// TODO Auto-generated method stub
		
		tvTotal.setText("$"+totalValue);
		if(valueDiscount!=0)
			tvDiscount.setText("%"+ valueDiscount*100);
		if(valueDiscount!=0)
			totalafterDiscount = (float)(totalValue*valueDiscount);
		else
			totalafterDiscount = totalValue;
		tvTotalAfterDiscount.setText("$"+totalafterDiscount);
	}
	private void getValue(ArrayList<Bill> listBill2) {
		// TODO Auto-generated method stub
		totalafterDiscount = 0;
		totalValue = 0;
		for(Bill item: listBill2){
			try {
				float unit = item.getUnit()!=null?Float.parseFloat(item.getUnit()):0;
				float price = item.getPrice()!=null?Float.parseFloat(item.getPrice()):0;
				float discount = item.getDiscount()!=null?Float.parseFloat(item.getDiscount()):0;
				totalValue += price*unit;
				valueDiscount = 0;
				valueDiscount = discount*price*unit;
				totalafterDiscount=totalValue - valueDiscount;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
	}
	private void bindDatatoListview() {
		// TODO Auto-generated method stub
		WhyQBillAdapter adapter = new WhyQBillAdapter(WhyQBillScreen.this, listBill);
		lvBill.setAdapter(adapter);
	}
	private ArrayList<Bill> getBillList(HashMap<String, Bill> billList) {
		// TODO Auto-generated method stub
		totalValue = 0;
		totalafterDiscount = 0;

		if(billList!=null){
			Collection<Bill> c = billList.values();
			if(c!=null){
				for (Iterator collectionItr = c.iterator(); collectionItr.hasNext(); ) {
					try {

						  Bill item = (Bill)collectionItr.next();
						  this.listBill.add(item);
						  totalValue+= Float.parseFloat(item.getPrice())*Float.parseFloat(item.getUnit());
					
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}
		
		return this.listBill;
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
		if(isOrdered){
			if(billId!=null){

				PayPalUI paypalUI = new PayPalUI();
				Bundle bundle = new Bundle();
				bundle.putString("bill_id", billId);
				paypalUI.setArguments(bundle);
				getSupportFragmentManager().beginTransaction().add(paypalUI, "").commit();
				
			
			}
		}else{
			WhyqOrderMenuActivity frag = new WhyqOrderMenuActivity();	
			frag.show(getFragmentManager(), "WhyqOrderMenuActivity");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
			Toast.makeText(getApplicationContext(), R.string.toast_login_ok,
					Toast.LENGTH_LONG).show();

			// Set the raw json representation as content of the TextView
//			profileText.setText(data
//					.getStringExtra(AccessHelperConnect.DATA_PROFILE));
		} else {
			Toast.makeText(getApplicationContext(),
					R.string.toast_login_failed, Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		if(result.isSuccess()&& result.getAction() == ServiceAction.ActionGetBillDetail){
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				listBill = (ArrayList<Bill>)data.getData();
				if(listBill!=null){
					
					bindDatatoListview();
					getValue(listBill);
					bindBillValue();
				}
			}else if(data.getStatus().equals("401")){
				Util.loginAgain(getParent(), data.getMessage());
			}else if(data.getStatus().equals("204")){
			}else{
			}
		} 
	}
}
