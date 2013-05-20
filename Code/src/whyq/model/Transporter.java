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
	
	private ArrayList<Perm> perms;
	private List<PermBoard> permsBoard;
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
	 * @param perms the list of perms
	 */
	public Transporter(ArrayList<Perm> perms) {
		this.perms = perms;
	}

	public ArrayList<Perm> getPerms() {
		return perms;
	}

	public void setPerms(ArrayList<Perm> perms) {
		this.perms = perms;
	}
	public List<PermBoard> getPermsBoard() {
		return permsBoard;
	}

	public void setPermsBoard(List<PermBoard> perms) {
		this.permsBoard = perms;
	}

}
