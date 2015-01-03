package whyq.adapter;

import java.util.List;

import whyq.interfaces.OnnOptionItemSelected;
import whyq.model.OptionItem;
import android.content.Context;

public class OptionItemAdapter extends OptionItemBasicAdapter{

	private OnnOptionItemSelected mOnOptionItemSelected;

	public OptionItemAdapter(Context context, int resource, List<OptionItem> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	public void setDelegate(OnnOptionItemSelected listener){
		mOnOptionItemSelected = listener;
	}
	
	@Override
	protected void onItemClicked(OptionItem item) {
		// TODO Auto-generated method stub
		super.onItemClicked(item);
		mOnOptionItemSelected.onSelected(1, item);
		
		notifyDataSetChanged();
	}
}
