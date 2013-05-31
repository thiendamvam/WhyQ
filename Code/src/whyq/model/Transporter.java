/**
 * 
 */
package whyq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Linh Nguyen
 * This class is only for passing the contents between intents in Android.
 */
public class Transporter implements Serializable {
	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String boardName;
	
	private ArrayList<Whyq> whyqs;
	private List<WhyqBoard> permsBoard;
	/**
	 * Default constructor
	 */
	public Transporter() {
		
	}
	
	
	/**
	 * @return the boardName
	 */
	public String getBoardName() {
		return boardName;
	}


	/**
	 * @param boardName the boardName to set
	 */
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}


	/**
	 * Constructor which initializes the list of perms
	 * @param whyqs the list of perms
	 */
	public Transporter(ArrayList<Whyq> whyqs) {
		this.whyqs = whyqs;
	}

	public ArrayList<Whyq> getPerms() {
		return whyqs;
	}

	public void setPerms(ArrayList<Whyq> whyqs) {
		this.whyqs = whyqs;
	}
	public List<WhyqBoard> getPermsBoard() {
		return permsBoard;
	}

	public void setPermsBoard(List<WhyqBoard> perms) {
		this.permsBoard = perms;
	}

}
