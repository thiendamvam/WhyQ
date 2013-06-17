/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;

/**
 * @author giangnguyen.tale@gmail.com
 * 
 */
public class FriendFacebookHandler extends DefaultHandler implements FriendFacebookController {

	private static final String TAG_LIST_NOT_JOIN_WHYQ = "list_not_join_whyq";
	private static final String TAG_DATA = "data";
	private static final String TAG_FACEBOOK_ID = "facebook_id";
	private static final String TAG_ITUNE_URL = "ituneUrl";
	private static final String TAG_FIRST_NAME = "first_name";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_FRIEND = "is_friend";
	private static final String TAG_AVATAR = "avatar";

	private FriendFacebook friendfacebook;
	private List<FriendFacebook> listNotJoinWhyq;
	private boolean isListNotJoinWhyq;

	private StringBuffer buffer = new StringBuffer();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (localName.equals(TAG_FACEBOOK_ID)) {
			friendfacebook.setFacebookId(buffer.toString());
		} else if (localName.equals(TAG_FIRST_NAME)) {
			friendfacebook.setFirstName(buffer.toString());
		} else if (localName.equals(TAG_ID)) {
			friendfacebook.setId(buffer.toString());
		} else if (localName.equals(TAG_ID_FRIEND)) {
			friendfacebook.setIsFriend(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(TAG_AVATAR)) {
			friendfacebook.setAvatar(buffer.toString());
		} else if (localName.equals(TAG_ITUNE_URL)) {
			friendfacebook.setItuneUrl(buffer.toString());
		} else if (localName.equals(TAG_DATA)){
			if (isListNotJoinWhyq) {
				listNotJoinWhyq.add(friendfacebook);
			}
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		buffer.setLength(0);

		if (localName.equals(TAG_DATA)) {
			friendfacebook = new FriendFacebook();
		} else if (localName.equals(TAG_LIST_NOT_JOIN_WHYQ)) {
			isListNotJoinWhyq = true;
			listNotJoinWhyq = new ArrayList<FriendFacebook>();
		} 
	}

	@Override
	public List<FriendFacebook> getListNotJoinWhyq() {
		return listNotJoinWhyq;
	}

	@Override
	public List<FriendFacebook> getListWhyq() {
		// TODO Auto-generated method stub
		return null;
	}

}
