package whyq.activity;

import java.util.ArrayList;

import why.adapter.InvitationAdapter;
import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class InvitationActivity extends Activity implements IServiceListener{

	private ListView lvInvitation;
	private InvitationAdapter adapter;
	private TextView btnHeader;
	private ProgressBar prbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_screen);
		btnHeader = (TextView) findViewById(R.id.permpingTitle);
		btnHeader.setText("Invitations");
		lvInvitation = (ListView)findViewById(R.id.lvInvitation);
		prbar = (ProgressBar)findViewById(R.id.prgBar);
		getInvitationData("1");
	}

	private void getInvitationData(String listInvited) {
		// TODO Auto-generated method stub
		setLoading(true);
		Service service  = new Service(InvitationActivity.this);
		service.getInvitation(WhyqApplication.Instance().getRSAToken(), listInvited);
	}

	private void bindData(ArrayList<User> userList) {
		// TODO Auto-generated method stub
		adapter = new InvitationAdapter(this, userList);
		lvInvitation.setAdapter(adapter);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		setLoading(false);
		if(result.isSuccess()&& result.getAction() == ServiceAction.ActionGetInvitations){
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				ArrayList<User> userList = (ArrayList<User>)data.getData();
				if(userList!=null){
					bindData(userList);
				}
			}else if(data.getStatus().equals("401")){
				Util.loginAgain(getParent(), data.getMessage());
			}else if(data.getStatus().equals("204")){

			}else{
			}
		} 
	}
	
	public void setLoading(boolean isShowing){
		if(isShowing){
			prbar.setVisibility(View.VISIBLE);
		}else{
			prbar.setVisibility(View.INVISIBLE);
		}
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
		
	}
}
