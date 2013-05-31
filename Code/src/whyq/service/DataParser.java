package whyq.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class DataParser {


	public static String issueIntro="";
	private String issueString;
	private JSONObject _root;
	// private StoreIssueDataControler issueControler;
	public DataParser() {
//		issueController.resetStorySetData();
	}

	public boolean parse(String input) {
		try {
			issueString = input;
			_root = new JSONObject(input);
			return true;
		} catch (JSONException e) {
			return false;
		}
	}

	public Object parseRetaurentList() {
		// TODO Auto-generated method stub
		return null;
	}

}
