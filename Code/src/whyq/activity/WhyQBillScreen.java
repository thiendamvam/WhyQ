package whyq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.whyq.R;

public class WhyQBillScreen extends Activity{
	
	private TextView tvTitle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_bill_screen);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Bills");
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
		
	}
}
