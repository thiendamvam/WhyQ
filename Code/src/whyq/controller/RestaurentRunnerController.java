package whyq.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.activity.ListDetailActivity;
import whyq.adapter.ExpandMenuAdapterV2;
import whyq.model.Bill;
import whyq.model.ExtraItem;
import whyq.model.OptionItem;
import whyq.model.SizeItem;
import whyq.model.Store;

public class RestaurentRunnerController{
	
	public static Map<String, Map<String, List<Bill>>> restaurentList = new HashMap<String, Map<String,List<Bill>>>();
	public static Map<String, Store> storeList = new HashMap<String, Store>();
	public static String restaurentRRID;
	public static String restaurentRRNote;
	public static float deliveryFee;
	
	public RestaurentRunnerController() {
		// TODO Auto-generated constructor stub
	}
		
	public static String getListItem() {
		// TODO Auto-generated method stub
		String result = "";
		for (String key : ListDetailActivity.billList.keySet()) {
			List<Bill> list = ListDetailActivity.billList.get(key);
			for (Bill bill : list) {
				if (bill != null) {
					if (result.equals("")) {
						result += bill.getProductId() + ":" + bill.getUnit()
								+ ":" + bill.getPrice()+":"+getSizeExtraOptionId(bill)+":"+getNote(bill);
					} else {
						result += "|" + bill.getProductId() + ":"
								+ bill.getUnit() + ":" + bill.getPrice()+":"+getSizeExtraOptionId(bill)+":"+getNote(bill);
					}

				}

			}
		}
		return result;
	}
	private static String getNote(Bill bill) {
		// TODO Auto-generated method stub
		return ExpandMenuAdapterV2.noteList.get(bill.getId()) ==null? "": ExpandMenuAdapterV2.noteList.get(bill.getId());
	}
	
	private static String getSizeExtraOptionId(Bill bill) {
		// TODO Auto-generated method stub
		String result = "";
		List<SizeItem> sizeList = bill.getSizeList();
		if(sizeList !=null){
			for(SizeItem item: sizeList){
				result+=result.equals("")? item.getId():","+item.getId();
			}
		}
		
		List<OptionItem> optionList = bill.getOptionList();
		if(optionList !=null){
			for(OptionItem item: optionList){
				result+=result.equals("")? item.getId():","+item.getId();
			}
		}
		
		List<ExtraItem> extraList = bill.getExtraList();
		if(extraList !=null){
			for(ExtraItem item: extraList){
				result+=result.equals("")? item.getId():","+item.getId();
			}
		}
		return result;
	}
	
}
