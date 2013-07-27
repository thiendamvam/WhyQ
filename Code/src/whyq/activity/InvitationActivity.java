package whyq.activity;

import java.util.ArrayList;

import why.adapter.InvitationAdapter;
import whyq.model.User;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class InvitationActivity extends Activity{

	private ListView lvInvitation;
	private InvitationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_screen);
		lvInvitation = (ListView)findViewById(R.id.lvInvitation);
		ArrayList<User> userList = new ArrayList<User>();
		adapter = new InvitationAdapter(this, userList);
		lvInvitation.setAdapter(adapter);
	}
}
