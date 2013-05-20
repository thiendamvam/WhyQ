package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Perm;


public interface Get_Perm_Delegate {
	void onSuccess(ArrayList<Perm> perms);
	void onError();
}
