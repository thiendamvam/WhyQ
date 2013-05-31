package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Whyq;


public interface Get_Board_delegate {
	void onSuccess(ArrayList<Whyq> whyqs);
	void onError();
}
