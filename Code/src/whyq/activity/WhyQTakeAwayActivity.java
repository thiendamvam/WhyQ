package whyq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.whyq.R;

public class WhyQTakeAwayActivity extends Activity implements OnClickListener {

	private EditText etHours;
	private EditText etMinutes;
	private CheckBox cbLeaveNow;
	private TextView tvCarTime;
	private TextView tvBycicalTime;
	private TextView tvWalkTime;
	private Button btnDone;
	public WhyQTakeAwayActivity(){
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_take_away_screen);
		etHours = (EditText)findViewById(R.id.etHours);
		etMinutes = (EditText)findViewById(R.id.etMinutes);
		cbLeaveNow = (CheckBox)findViewById(R.id.cbLeaveNow);
		tvCarTime = (TextView)findViewById(R.id.tvCarTime);
		tvBycicalTime = (TextView)findViewById(R.id.tvBycicalTime);
		tvWalkTime = (TextView)findViewById(R.id.tvWalkTime);
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btnDone:		
			exeDone();
			break;

		default:
			break;
		}
	}
	private void exeDone() {
		// TODO Auto-generated method stub
		
	}
}
