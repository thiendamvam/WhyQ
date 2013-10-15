package whyq.activity;

import java.util.ArrayList;

import why.adapter.InvitationAdapter;
import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class InvitationActivity extends Activity implements IServiceListener{

	private ListView lvInvitation;
	private InvitationAdapter adapter;
	private TextView btnHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_screen);
		btnHeader = (TextView) findViewById(R.id.permpingTitle);
		btnHeader.setText("Invitations");
		lvInvitation = (ListView)findViewById(R.id.lvInvitation);
		String listId = getIntent().getStringExtra("id");
		getInvitationData(listId);
	}

	private void getInvitationData(String listInvited) {
		// TODO Auto-generated method stub
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
		
	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){
		
	}
}
