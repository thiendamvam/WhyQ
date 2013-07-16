package whyq.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import whyq.adapter.WhyQBillAdapter;
import whyq.model.Bill;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class WhyQBillScreen extends Activity{
	
	private TextView tvTitle;
	private ListView lvBill;
	private ArrayList<Bill> listBill;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_bill_screen);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Bills");
		lvBill = (ListView)findViewById(R.id.lvBill);
		listBill = new ArrayList<Bill>();
		listBill = getBillList(ListDetailActivity.billList);
		bindDatatoListview();
		
	}
	private void bindDatatoListview() {
		// TODO Auto-generated method stub
		WhyQBillAdapter adapter = new WhyQBillAdapter(WhyQBillScreen.this, listBill);
		lvBill.setAdapter(adapter);
	}
	private ArrayList<Bill> getBillList(HashMap<String, Bill> billList) {
		// TODO Auto-generated method stub

		Collection<Bill> c = billList.values();
		for (Iterator collectionItr = c.iterator(); collectionItr.hasNext(); ) {
			  Bill item = (Bill)collectionItr.next();
			  this.listBill.add(item);
		}
		
		return this.listBill;
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
		
	}
}
