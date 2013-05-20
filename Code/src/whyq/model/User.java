package whyq.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4778518614772031293L;
	private String id;
	private String name;
	private String status;
	private PermImage avatar;
	private int followings;
	private int friends;	
	private int pin;
	private int like;
	private int board;
	List<PermBoard> boards;

	/**
	 * Default constructor
	 */
	public User() {
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public User( String id){
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param avatar
	 * @param friends
	 * @param followings
	 * @param pin
	 * @param like
	 * @param board
	 * @param boards
	 */
	public User(String id, String name, PermImage avatar, int friends, int followings, 
			int pin, int like, int board, List<PermBoard> boards) {
		this.setId(id);
		this.setName(name);
		this.setAvatar(avatar);
		this.setFriends(friends);
		this.setFollowings(followings);
		this.setPin(pin);
		this.setLike(like);
		this.setBoard(board);
		this.setBoards(boards);
	}


	/**
	 * @return the boards
	 */
	public List<PermBoard> getBoards() {
		return boards;
	}

	/**
	 * @param boards the boards to set
	 */
	public void setBoards(List<PermBoard> boards) {
		this.boards = boards;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public PermImage getAvatar() {
		return avatar;
	}


	public void setAvatar(PermImage avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the friends
	 */
	public int getFriends() {
		return friends;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(int friends) {
		this.friends = friends;
	}

	
	/**
	 * @return the pin
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(int pin) {
		this.pin = pin;
	}

	/**
	 * @return the like
	 */
	public int getLike() {
		return like;
	}

	/**
	 * @param like the like to set
	 */
	public void setLike(int like) {
		this.like = like;
	}

	/**
	 * @return the board
	 */
	public int getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(int board) {
		this.board = board;
	}

	/**
	 * @return the followings
	 */
	public int getFollowings() {
		return followings;
	}

	/**
	 * @param followings the followings to set
	 */
	public void setFollowings(int followings) {
		this.followings = followings;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
