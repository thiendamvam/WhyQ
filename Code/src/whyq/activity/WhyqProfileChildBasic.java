package whyq.activity;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WhyqProfileChildBasic extends Activity {

	private TextView tvTitle;
	private String title;
	private TextView tvContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile_child_basic);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvContent = (TextView)findViewById(R.id.tvContent);
		title = getIntent().getStringExtra("title");
		if(title!=null){
			tvTitle.setText(title);
		}else{
			tvTitle.setText("");
		}
		if(title.equals(WhyqProflleFAQActivity.HOW_WHYQ_WORK)){
			tvContent.setText(getResources().getString(R.string.About));
		}else if(title.equals(WhyqProflleFAQActivity.WHAT_DOTS_MEAN)){
			tvContent.setText(getResources().getString(R.string.About));
		}else if(title.equals(WhyqProflleFAQActivity.WHY_AM_NOT_FINISH)){
			tvContent.setText(getResources().getString(R.string.About));
		}else if(title.equals(WhyqProflleFAQActivity.HOW_AUTHORISIZE)){
			tvContent.setText(getResources().getString(R.string.About));
		}else if(title.equals(WhyqProflleFAQActivity.HOW_MAKE_BAR_FAVOURITE)){
			tvContent.setText(getResources().getString(R.string.About));
		}else if(title.equals(WhyqProflleFAQActivity.WHEN_WILL_I_KNOW_ORDER)){
			tvContent.setText(getResources().getString(R.string.About));
		}
	}
	public void onBack(View v) {
		// WhyqMain.back();
		finish();
	}
}
