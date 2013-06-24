package whyq.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.whyq.R;

public class WhyQDineInFragment extends DialogFragment implements OnClickListener{
	private String storeId;
	private Display display;
	private Activity context;
	private Button btnCancel;
	private Button btnOk;
	private EditText etTableNumber;
	private EditText etNoNumber;
	private CheckBox cbxNonumber;

	public WhyQDineInFragment(String storeId){
		this.storeId = storeId;
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
		View v = LayoutInflater.from(context).inflate(R.layout.whyq_dine_in, null);
		// v.setBackgroundResource(android.R.color.transparent);
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		getDialog().getWindow().setLayout(50,50);//display.getWidth()/3, display.getHeight() / 4
		getDialog().setCanceledOnTouchOutside(true);
//		LayoutParams params = (ViewGroup.LayoutParams)v.getLayoutParams();
//		params.height = 100;
//		params.width = 100;
//		
//		v.setLayoutParams(params);
		btnCancel = (Button)v.findViewById(R.id.tbnCancel);
		btnOk = (Button)v.findViewById(R.id.btnOk);
		etTableNumber = (EditText)v.findViewById(R.id.etTableNumber);
		etNoNumber = (EditText)v.findViewById(R.id.etNoNumber);
		cbxNonumber = (CheckBox)v.findViewById(R.id.cbNoNumber);
				
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		return v;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.tbnCancel:
			exeCancel();
			break;
		case R.id.btnOk:
			exeOk();
			break;

		default:
			break;
		}
	}
	private void exeCancel() {
		// TODO Auto-generated method stub
		dismiss();
	}
	private void exeOk() {
		// TODO Auto-generated method stub
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        String user = etTableNumber.getText().toString();
//        String pass = password.getText().toString();
//        if (user.length() == 0)
//        {
//        	email.setFocusable(true);
//        	email.startAnimation(shake);
//        	email.requestFocus();
//        	return false;
//        } else if (pass.length() == 0)
//        {
//        	password.setFocusable(true);
//        	password.startAnimation(shake);
//        	password.requestFocus();
//        	return false;
//        } else
//        {
//        	return true;
//        }
	}

}
