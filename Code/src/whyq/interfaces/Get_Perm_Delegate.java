package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Whyq;


public interface Get_Perm_Delegate {
	void onSuccess(ArrayList<Whyq> whyqs);
	void onError();
}
