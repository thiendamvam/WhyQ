package whyq.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import whyq.adapter.WhyQBillAdapter;
import whyq.model.Bill;
import whyq.paypal.LoginPaypalActivity;
import whyq.paypal.helper.AccessHelperConnect;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

public class WhyQBillScreen extends FragmentActivity{
	
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
	public static int LOGIN_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_bill_screen);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Bills");
		lvBill = (ListView)findViewById(R.id.lvBill);
		listBill = new ArrayList<Bill>();
		listBill = getBillList(ListDetailActivity.billList);
		tvTotal = (TextView)findViewById(R.id.tvTotal);
		tvDiscount = (TextView)findViewById(R.id.tvDiscount);
		tvTotalAfterDiscount = (TextView)findViewById(R.id.tvTotalafterDiscount);
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setText("Order");
		bindDatatoListview();
		bindBillValue();
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
	private void bindDatatoListview() {
		// TODO Auto-generated method stub
		WhyQBillAdapter adapter = new WhyQBillAdapter(WhyQBillScreen.this, listBill);
		lvBill.setAdapter(adapter);
	}
	private ArrayList<Bill> getBillList(HashMap<String, Bill> billList) {
		// TODO Auto-generated method stub
		totalValue = 0;
		totalafterDiscount = 0;
		Collection<Bill> c = billList.values();
		if(c!=null){
			for (Iterator collectionItr = c.iterator(); collectionItr.hasNext(); ) {
				try {

					  Bill item = (Bill)collectionItr.next();
					  this.listBill.add(item);
					  totalValue+= totalValue+Float.parseFloat(item.getPrice())*Float.parseFloat(item.getUnit());
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		return this.listBill;
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
//		final Intent loginIntent = new Intent(this, LoginPaypalActivity.class);
//		startActivityForResult(loginIntent, LOGIN_REQUEST);
		WhyqOrderMenuActivity frag = new WhyqOrderMenuActivity();	
		frag.show(getFragmentManager(), "WhyqOrderMenuActivity");
//		Intent intent = new Intent(WhyQBillScreen.this, WhyqOrderMenuActivity.class	);
//		startActivity(intent);
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
}
