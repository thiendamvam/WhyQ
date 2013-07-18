package whyq.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import whyq.WhyqApplication;
import whyq.handler.ActivityHandler;
import whyq.handler.BillHandler;
import whyq.handler.CommentHandler;
import whyq.handler.FriendFacebookHandler;
import whyq.handler.FriendWhyqHandler;
import whyq.handler.PhotoHandler;
import whyq.handler.UserHandler;
import whyq.interfaces.FriendFacebookController;
import whyq.model.ActivityItem;
import whyq.model.BillItem;
import whyq.model.Comment;
import whyq.model.FriendWhyq;
import whyq.model.Location;
import whyq.model.Menu;
import whyq.model.Photo;
import whyq.model.ProductTypeInfo;
import whyq.model.Store;
import whyq.model.StoreInfo;
import whyq.model.User;
import whyq.model.UserCheckBill;
import whyq.model.WhyqImage;
import whyq.utils.Util;

public class DataParser {

	public static String issueIntro = "";

	// private StoreIssueDataControler issueControler;
	public DataParser() {
		// issueController.resetStorySetData();
	}

	public Object parseRetaurentList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object parseLogout() {
		// TODO Auto-generated method stub
		// result.setSuccess(_root.optBoolean("success", false));
		// return null
		return null;
	}

	public Object parserLoginData(String inputString) {
		// TODO Auto-generated method stub
		try {
			XMLReader xmlReader = initializeReader();
			UserHandler userHandler = new UserHandler();
			// assign the handler
			xmlReader.setContentHandler(userHandler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return userHandler.getUser();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static final List<ActivityItem> parseActivities(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			ActivityHandler handler = new ActivityHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getActivities();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final List<BillItem> parseBills(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			BillHandler handler = new BillHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getBills();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final List<Photo> parsePhotos(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			PhotoHandler handler = new PhotoHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getComments();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final List<Comment> parseComments(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			CommentHandler handler = new CommentHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getComments();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final FriendFacebookController parseFriendFacebook(
			String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			FriendFacebookHandler handler = new FriendFacebookHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final List<FriendWhyq> parseFriendWhyq(String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			FriendWhyqHandler handler = new FriendWhyqHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler.getFriends();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static XMLReader initializeReader()
			throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Create a parser
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}

	public Object parseBusinessList(String result) {
		// TODO Auto-generated method stub
		Document doc = XMLfromString(result);
		ArrayList<Store> permList = new ArrayList<Store>();
		String statusResponse = doc.getElementsByTagName("Status").item(0).getFirstChild().getNodeValue();
		if(statusResponse.equals("200")){
			NodeList permNodeList =  doc.getElementsByTagName("obj");
			
			for( int i = 0; i< permNodeList.getLength(); i++ ){
				try {

					Element permElement = (Element ) permNodeList.item(i);
					Store item = new Store();
					String id = getValue(permElement, "id");
					String storeId = getValue(permElement, "store_id");
					String address = getValue(permElement, "address");
					String longitude = getValue(permElement, "longitude");
					String latitude = getValue(permElement, "latitude");
					String status = getValue(permElement, "status");
					String radius = getValue(permElement, "radius_allow_deliver");
					String feeChargeOutRadiesDeliverPerKm = getValue(permElement, "fee_charge_out_radius_allow_deliver");
					String  createdate = getValue(permElement, "createdate");
					String updatedate = getValue(permElement, "updatedate");
					String cateId = getValue(permElement, "cate_id");
					String userId =getValue(permElement, "user_id");
					String nameStore =getValue(permElement, "name_store");
					String introStore = getValue(permElement, "intro_store");
					String phoneStore =getValue(permElement, "phone_store");
					String logi =getValue(permElement, "logo");;
					String style =getValue(permElement, "style");
					String startTime =getValue(permElement, "start_time");
					String endTime =getValue(permElement, "end_time");
					boolean isHomeDeliver = getValue(permElement, "is_home_deliver").equals("1")?true:false;
					boolean isHotelDeliver = getValue(permElement, "is_hotel_deliver").equals("1")?true:false;
					boolean isTakeAway = getValue(permElement, "is_take_away").equals("1")?true:false;
					boolean isPlace = getValue(permElement, "is_at_place").equals("1")?true:false;
					String minimunTimeDeliver = getValue(permElement, "minimun_time_deliver");
					String tableQuantity = getValue(permElement, "table_quantity");
					String countFavouriteMemebr = getValue(permElement, "count_favourite_member");
					boolean isFavourite = getValue(permElement, "is_favourite").equals("1")?true:false;
					String coutBill =getValue(permElement, "count_bill");
					String inCome = getValue(permElement, "income");
					String nameCate = getValue(permElement, "name_cate");
					
					NodeList userNode = (NodeList) permElement.getElementsByTagName("user_check_bill");
					int size  = userNode.getLength();
					for(int i2=0;i2< size;i2++){
						Element element = (Element)userNode.item(i2);
						UserCheckBill userCheckBill= new UserCheckBill();
						userCheckBill.setTotalFriend(getValue(element, "TotalFriend"));
						userCheckBill.setTotalMember(getValue(element, "TotalMember"));
						userCheckBill.setId(getValue(element, "id"));
						userCheckBill.setFirstName(getValue(element, "first_name"));
						userCheckBill.setLastName(getValue(element, "last_name"));
						userCheckBill.setAvatar(getValue(element, "avatar"));
						userCheckBill.setStoreId(getValue(element, "store_id"));
						userCheckBill.setStatusBill(getValue(element, "status_bill"));
						userCheckBill.setInteractionPoint(getValue(element, "interaction_point"));
						item.setUserCheckBill(userCheckBill);
					}
					item.setId(id);
					item.setStoreId(storeId);
					item.setAddress(address);
					item.setLongitude(longitude);
					item.setLatitude(latitude);
					item.setStatus(status);
					item.setRadius(radius);
					item.setIsFavourite(isFavourite);
					item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
					item.setCreatedate(createdate);
					item.setUpdatedate(updatedate);
					item.setCateid(cateId);
					item.setUserId(userId);
					item.setNameStore(nameStore);
					item.setIntroStore(introStore);
					item.setPhoneStore(phoneStore);
					item.setLogo(logi);
					item.setStyle(style);
					item.setStartTime(startTime);
					item.setEndTime(endTime);
					item.setHomeDeliver(isHomeDeliver);
					item.setHotelDeliver(isHotelDeliver);
					item.setTakeAway(isTakeAway);
					
					item.setTableQuantity(tableQuantity);
					item.setCountFavaouriteMember(countFavouriteMemebr);
					item.setCoutBill(coutBill);
					item.setIncome(inCome);
					item.setNameCate(nameCate);
					item.setMinimum(minimunTimeDeliver);
					item.setPlace(isPlace);
					
					permList.add(item);
				
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}else if(statusResponse.equals("401")){
			Util.loginAgain(WhyqApplication.Instance().getApplicationContext());
		}

		return permList;
	}
	public Object parseBusinessDetail(String result) {
		// TODO Auto-generated method stub
		Document doc = XMLfromString(result);
		ArrayList<Store> permList = new ArrayList<Store>();
		NodeList permNodeList = doc.getElementsByTagName("obj");

		for (int i = 0; i < permNodeList.getLength(); i++) {
			Element permElement = (Element) permNodeList.item(i);
			Store item = new Store();
			String id = getValue(permElement, "id");
			String storeId = getValue(permElement, "store_id");
			String address = getValue(permElement, "address");
			String longitude = getValue(permElement, "longitude");
			String latitude = getValue(permElement, "latitude");
			String status = getValue(permElement, "status");
			String radius = getValue(permElement, "radius_allow_deliver");
			String feeChargeOutRadiesDeliverPerKm = getValue(permElement,
					"fee_charge_out_radius_deliver_per_km");
			String createdate = getValue(permElement, "createdate");
			String updatedate = getValue(permElement, "updatedate");
			String cateId = getValue(permElement, "cate_id");
			String userId = getValue(permElement, "user_id");
			String nameStore = getValue(permElement, "name_store");
			String introStore = getValue(permElement, "intro_store");
			String phoneStore = getValue(permElement, "phone_store");
			String logo = getValue(permElement, "logo");
			String photos = getValue(permElement, "photos");
			String style = getValue(permElement, "style");
			;
			String startTime = getValue(permElement, "start_time");
			String endTime = getValue(permElement, "end_time");
			boolean isHomeDeliver = getValue(permElement, "is_home_deliver")
					.equals("1") ? true : false;
			boolean isHotelDeliver = getValue(permElement, "is_hotel_deliver")
					.equals("1") ? true : false;
			boolean isTakeAway = getValue(permElement, "is_take_away").equals(
					"1") ? true : false;
			boolean isPlace = getValue(permElement, "is_at_place").equals("1") ? true
					: false;
			String minimunTimeDeliver = getValue(permElement,
					"minimun_time_deliver");
			String tableQuantity = getValue(permElement, "table_quantity");
			String countFavouriteMemebr = getValue(permElement,
					"count_favourite_member");
			String coutBill = getValue(permElement, "count_bill");
			String inCome = getValue(permElement, "income");
			String nameCate = getValue(permElement, "name_cate");

			item.setId(id);
			item.setStoreId(storeId);
			item.setAddress(address);
			item.setLongitude(longitude);
			item.setLatitude(latitude);
			item.setStatus(status);
			item.setRadius(radius);
			item.setFreeChargeOutRadiusDelieverPerKm(feeChargeOutRadiesDeliverPerKm);
			item.setCreatedate(createdate);
			item.setUpdatedate(updatedate);
			item.setCateid(cateId);
			item.setUserId(userId);
			item.setNameStore(nameStore);
			item.setIntroStore(introStore);
			item.setPhoneStore(phoneStore);
			item.setLogo(logo);
			item.setStyle(style);
			item.setStartTime(startTime);
			item.setEndTime(endTime);
			item.setHomeDeliver(isHomeDeliver);
			item.setHotelDeliver(isHotelDeliver);
			item.setTakeAway(isTakeAway);
			item.setPhotos(photos);
			
			item.setTableQuantity(tableQuantity);
			item.setCountFavaouriteMember(countFavouriteMemebr);
			item.setCoutBill(coutBill);
			item.setIncome(inCome);
			item.setNameCate(nameCate);
			item.setPlace(isPlace);
			item.setMinimum(minimunTimeDeliver);
			NodeList nodes = permElement.getElementsByTagName("user");
			int lengthUser = nodes.getLength();
			ArrayList<User> userList = new ArrayList<User>();
			for (int j = 0; j < lengthUser; j++) {
				Element element = (Element) nodes.item(j);
				User user = new User();
				user.setId(getValue(element, "id"));
				user.setEmail(getValue(element, "email"));
				user.setRoleId(getValue(element, "role_id"));
				user.setAcive(getValue(element, "is_active").equals("1") ? true
						: false);
				user.setTotalMoney(getValue(element, "total_saving_money"));
				user.setTotalSavingMoney(getValue(element, "total_comment"));
				user.setTotalComment(getValue(element, "total_comment"));
				user.setTotalCommentLike(getValue(element, "total_comment_like"));
				user.setTotalFriend(getValue(element, "total_friend"));
				user.setTotalFavourite(getValue(element, "total_favourite"));
				user.setTotalCheckBill(getValue(element, "total_check_bill"));
				user.setStatus(getValue(element, "status"));
				user.setCreateDate(getValue(element, "createdate"));
				user.setUpdateDate(getValue(element, "updatedate"));
				user.setFirstName(getValue(element, "first_name"));
				user.setLastName(getValue(element, "last_name"));
				user.setGender(Integer.parseInt(getValue(element, "gender")));
				WhyqImage image = new WhyqImage(getValue(element, "avatar"));
				user.setAvatar(image);
				user.setAddress(getValue(element, "address"));
				userList.add(user);
			}
			item.setUserList(userList);

			NodeList menuNodes = permElement.getElementsByTagName("menu");
			int lengthMenuNode = nodes.getLength();
			ArrayList<Menu> menuList = new ArrayList<Menu>();

			for (int c = 0; c < lengthMenuNode; c++) {
				Element element = (Element) menuNodes.item(c);
				Menu menu = new Menu();
				menu.setId(getValue(element, "id"));
				menu.setStoreId(getValue(element, "store_id"));
				menu.setNameProduct(getValue(element, "name_product"));
				menu.setValue(getValue(element, "value"));
				menu.setValuePromotion(getValue(element, "value_promotion"));
				menu.setImageProduct(getValue(element, "image_product"));
				menu.setImageThumb(getValue(element, "image_thumb"));
				menu.setStatus(getValue(element, "status"));
				menu.setTypeProductId(getValue(element, "type_product_id"));
				menu.setCreateDate(getValue(element, "createdate"));
				menu.setUpdateData(getValue(element, "updatedate"));
				menu.setSort(getValue(element, "sort"));

				NodeList productTypeInfoNodes = permElement
						.getElementsByTagName("product_type_info");
				int length = productTypeInfoNodes.getLength();
				ArrayList<ProductTypeInfo> productTypeInfoList = new ArrayList<ProductTypeInfo>();
				for (int y = 0; y < length; y++) {
					Element inElement = (Element) productTypeInfoNodes.item(y);
					ProductTypeInfo product = new ProductTypeInfo();
					product.setId(getValue(inElement, "id"));
					product.setNameProductType(getValue(inElement,
							"name_product_type"));
					product.setCreateDate(getValue(inElement, "createdate"));
					product.setSort(getValue(inElement, "sort"));
					productTypeInfoList.add(product);
				}
				menu.setProductTypeInfoList(productTypeInfoList);

				NodeList storeInfoNodes = permElement
						.getElementsByTagName("store_info");
				int storeInfoLength = productTypeInfoNodes.getLength();
				for (int w = 0; w < storeInfoLength; w++) {
					Element infoElement = (Element) storeInfoNodes.item(w);
					StoreInfo storeInfo = new StoreInfo();
					storeInfo.setId(getValue(infoElement, "id"));
					storeInfo.setCateid(getValue(infoElement, "cate_id"));
					storeInfo.setUserId(getValue(infoElement, "user_id"));
					storeInfo.setNameSore(getValue(infoElement, "name_store"));
					storeInfo
							.setIntroStore(getValue(infoElement, "intro_store"));
					storeInfo
							.setPhoneStore(getValue(infoElement, "phone_store"));
					storeInfo.setLogo(getValue(infoElement, "logo"));
					storeInfo.setStyle(getValue(infoElement, "style"));
					storeInfo.setStartTime(getValue(infoElement, "start_time"));
					storeInfo.setEndTime(getValue(infoElement, "end_time"));
					storeInfo.setHomeDeliver(getValue(infoElement,
							"is_home_deliver").equals("1") ? true : false);
					storeInfo.setHotelDeliver(getValue(infoElement,
							"is_hotel_deliver").equals("2") ? true : false);
					storeInfo.setTakeAray(getValue(infoElement, "is_take_away")
							.equals("1") ? true : false);
					storeInfo.setAtPlace(getValue(infoElement, "is_at_place")
							.equals("2") ? true : false);
					storeInfo.setStartTimeDeliver(getValue(infoElement,
							"start_time_deliver"));
					storeInfo.setEndtimeDeliver(getValue(infoElement,
							"end_time_deliver"));
					storeInfo.setMinimunTimeDeliever(getValue(infoElement,
							"minimun_time_deliver"));
					storeInfo.setValueConditionDeliver(getValue(infoElement,
							"value_condition_deliver"));
					storeInfo.setTableWuantity(getValue(infoElement,
							"table_quantity"));
					storeInfo.setCountFavouriteMember(getValue(infoElement,
							"count_favourite_member"));
					storeInfo.setCountBill(getValue(infoElement, "count_bill"));
					storeInfo.setIncome(getValue(infoElement, "income"));
					storeInfo.setStatus(getValue(infoElement, "status"));
					storeInfo
							.setCreateDate(getValue(infoElement, "createdate"));
					storeInfo
							.setUpdateDate(getValue(infoElement, "updatedate"));
					menu.setStoreInfo(storeInfo);

				}

				menuList.add(menu);
			}
			item.setMenuList(menuList);

			NodeList tagNodes = permElement.getElementsByTagName("tags");
			int tagLenth = nodes.getLength();
			ArrayList<whyq.model.Tag> tagList = new ArrayList<whyq.model.Tag>();
			for (int k = 0; k < tagLenth; k++) {
				Element element2 = (Element) tagNodes.item(k);
				whyq.model.Tag tag = new whyq.model.Tag();
				tag.setId(getValue(element2, "id"));
				tag.setNameTag(getValue(element2, "name_tag"));
				if(!getValue(element2,
						"count_used").equals(""))tag.setCountUsed(Integer.parseInt(getValue(element2,
						"count_used")));
				tag.setStoreId(getValue(element2, "store_id"));
				tag.setCreateDate(getValue(element2, "createdate"));
				tagList.add(tag);
			}
			item.setTagList(tagList);

			return item;
		}
		return null;
	}

	public String getValue(Element e, String tag) {
		try {
			if (e != null) {
				Node node = e.getElementsByTagName(tag).item(0).getFirstChild();
				if (node != null) {
					return node.getNodeValue();
				}
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		return "";
	}

	public Document XMLfromString(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			if (is != null)
				doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}

	public Object parseCommentResult(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Location> parseLCationResult(String result) {
		// TODO Auto-generated method stub
		Document doc = XMLfromString(result);
		ArrayList<Store> permList = new ArrayList<Store>();
		NodeList nodeList = doc.getElementsByTagName("geoname");
		ArrayList<Location> locationList = new ArrayList<Location>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Location location = new Location();
			Element infoElement = (Element) nodeList.item(i);
			location.setLat(getValue(infoElement, "lat"));
			location.setLon(getValue(infoElement, "lng"));
			location.setName(getValue(infoElement, "name"));
			location.setCountry(getValue(infoElement, "countryName"));
			locationList.add(location);
		}
		
		return locationList;
	}

}
