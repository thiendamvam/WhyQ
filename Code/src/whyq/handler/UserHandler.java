/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.WhyqBoard;
import whyq.model.WhyqImage;
import whyq.model.User;
import whyq.utils.Constants;


/**
 * @author Linh Nguyen
 *
 */
public class UserHandler extends DefaultHandler {
	
	private User user;
	private List<WhyqBoard> boards;
	private WhyqBoard whyqBoard;
	
	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length); 
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	
		if (localName.equals(Constants.USER_ID)) {
			user.setId(buffer.toString());
		} else if (localName.equals(Constants.USER_NAME)) {
			user.setName(buffer.toString());
		} else if (localName.equals(Constants.USER_AVATAR)) {
			WhyqImage avatar = new WhyqImage(buffer.toString());
			user.setAvatar(avatar);
		} else if (localName.equals(Constants.FOLLOWING_COUNT)) {
			user.setFollowings(Integer.parseInt(buffer.toString()));			
		} else if (localName.equals(Constants.FOLLOWER_COUNT)) {
			user.setFriends(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(Constants.PIN_COUNT)) {
			user.setPin(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(Constants.LIKE_COUNT)) {
			user.setLike(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(Constants.BOARD_COUNT)) {
			user.setBoard(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(Constants.BOARDS)) {
			user.setBoards(boards);
		} else if (localName.equals(Constants.ITEM)) {
			boards.add(whyqBoard);
		} else if (localName.equals(Constants.ID)) {
			whyqBoard.setId(buffer.toString());
		} else if (localName.equals(Constants.NAME)) {
			whyqBoard.setName(buffer.toString());
		} else if (localName.equals(Constants.DESCRIPTION)) {
			whyqBoard.setDescription(buffer.toString());
		} else if (localName.equals(Constants.FOLLOWERS)) {
			whyqBoard.setFollowers(Integer.parseInt(buffer.toString()));
		} else if (localName.equals(Constants.PINS)) {
			whyqBoard.setPins(Integer.parseInt(buffer.toString()));
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		buffer.setLength(0);
		
		if (localName.equals(Constants.USER)) {
			user = new User();
		} else if (localName.equals(Constants.BOARDS)) {
			boards = new ArrayList<WhyqBoard>();
		} else if (localName.equals(Constants.ITEM)) {
			whyqBoard = new WhyqBoard();
		}
	}
	
	public User getUser() {
		return user;
	}
}
