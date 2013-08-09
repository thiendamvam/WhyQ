package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.adapter.WhyqProfileFAQItemAdapter;
import whyq.adapter.WhyqProfileFAQItemAdapter.FAQViewHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class WhyqProflleFAQActivity extends Activity{
	public static final String HOW_WHYQ_WORK = "How does WHY Q work?";
	public static final String WHAT_DOTS_MEAN = "What do the coloured dots mean?";
	public static final String WHY_AM_NOT_FINISH = "Why am I not able to finish paying for my order?";
	public static final String HOW_AUTHORISIZE = "How does Authorise and Capture work";
	public static final String HOW_MAKE_BAR_FAVOURITE = "How do I make a bar a favourite?";
	public static final String WHEN_WILL_I_KNOW_ORDER = "When will I know that my order is ready";
	private TextView tvTitle;
	private ListView lvContent;
	private List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile_faq_screen);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		lvContent = (ListView)findViewById(R.id.lvContent);
		String title = getIntent().getStringExtra("title");
		if(title!=null){
			tvTitle.setText(title);
		}else{
			tvTitle.setText("");
		}
		bindDatatoListview();
	}

	private void bindDatatoListview() {
		// TODO Auto-generated method stub
		list.add(HOW_WHYQ_WORK);
		list.add(WHAT_DOTS_MEAN);
		list.add(WHY_AM_NOT_FINISH);
		list.add(HOW_AUTHORISIZE);
		list.add(HOW_MAKE_BAR_FAVOURITE);
		list.add(WHEN_WILL_I_KNOW_ORDER);
		WhyqProfileFAQItemAdapter adapter = new WhyqProfileFAQItemAdapter(WhyqProflleFAQActivity.this, list);
		lvContent.setAdapter(adapter);
	}
	public void onItemClicked(View v){
		FAQViewHolder holder = (FAQViewHolder)v.getTag();
		if(holder.tvName.getText().equals("How does WHY Q work?")){
			Intent i = new Intent(WhyqProflleFAQActivity.this, WhyqProfileChildBasic.class);
			i.putExtra("title", holder.tvName.getText().toString());
			startActivity(i);				
		}else{
			Intent i = new Intent(WhyqProflleFAQActivity.this, WhyqProfileChildDotsMean.class);
			i.putExtra("title", holder.tvName.getText().toString());
			startActivity(i);	
		}
		
	}
	public void onBack(View v) {
		// WhyqMain.back();
		finish();
	}
}
