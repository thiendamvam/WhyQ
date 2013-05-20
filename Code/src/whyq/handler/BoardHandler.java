/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.Comment;
import whyq.model.Perm;
import whyq.model.PermImage;
import whyq.model.User;
import whyq.utils.Constants;


/**
 * @author Linh Nguyen
 *
 */
public class BoardHandler extends DefaultHandler {

	private StringBuffer buffer = new StringBuffer();
	
	private List<Perm> perms;
	
	private Perm perm;
	
	private User user;
	
	private List<Comment> comments;
	
	private Comment comment;
	
	private User commentAuthor;
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length); 
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName != null && localName.equals(Constants.ITEM)) {
			perms.add(perm);
		} else if (localName != null && localName.equals(Constants.PERM_ID)) {
			perm.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_DESCRIPTION)) {
			perm.setDescription(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_CATEGORY)) {
			perm.setCategory(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_IMAGE)) {
			PermImage permImage = new PermImage(buffer.toString());
			perm.setImage(permImage);
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTS)) {
			perm.setComments(comments);
		} else if (localName != null && localName.equals(Constants.COMMENT)) {
			comments.add(comment);
		} else if (localName != null && localName.equals(Constants.ID)) {
			comment.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER)) {
			perm.setAuthor(user);
		} else if (localName != null && localName.equals(Constants.L_USER)) {
			comment.setAuthor(commentAuthor);
		} else if (localName != null && localName.equals(Constants.USER_ID)) {
			user.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_USER_ID)) {
			commentAuthor.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER_NAME)) {
			user.setName(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_USER_NAME)) {
			commentAuthor.setName(buffer.toString());
		} else if (localName != null && localName.equals(Constants.STATUS)) {
			user.setStatus(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_STATUS)) {
			commentAuthor.setStatus(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER_AVATAR)) {
			PermImage avatar = new PermImage(buffer.toString());
			user.setAvatar(avatar);
		} else if (localName != null && localName.equals(Constants.L_USER_AVATAR)) {
			PermImage avatar = new PermImage(buffer.toString());
			commentAuthor.setAvatar(avatar);
		} else if (localName != null && localName.equals(Constants.CONTENT)) {
			comment.setContent(buffer.toString());
		} else if (localName != null && localName.equals(Constants.IS_MORE)) {
			comment.setIsMore(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_REPINCOUNT)) {
			perm.setPermRepinCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_LIKECOUNT)) {
			perm.setPermLikeCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTCOUNT)) {
			perm.setPermCommentCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_USERLIKECOUNT)) {
			perm.setPermUserLikeCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.NEXT_ITEM)) {
			perm.setNextItem(buffer.toString());
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer.setLength(0);
		
		if (localName != null && localName.equals(Constants.PERMS)) {
			perms = new ArrayList<Perm>();
		} else if (localName != null && localName.equals(Constants.ITEM)) {
			perm = new Perm();
		} else if (localName != null && localName.equals(Constants.USER)) {
			user = new User();
		} else if (localName != null && localName.equals(Constants.L_USER)) {
			commentAuthor = new User();
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTS)) {
			comments = new ArrayList<Comment>();
		} else if (localName != null && localName.equals(Constants.COMMENT)) {
			comment = new Comment();			
		} else if (localName != null && localName.equals(Constants.USER)) {
			commentAuthor = new User();
		}
	}
	
	public List<Perm> getPerms() {
		return perms;
	}
}