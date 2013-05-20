package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Perm;


public interface Get_Board_delegate {
	void onSuccess(ArrayList<Perm> perms);
	void onError();
}
