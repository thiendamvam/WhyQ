package whyq.model;

import java.io.Serializable;

public class Comment implements Serializable{

	private String id;
	private String content;
	private User author;
	
	// TODO :This is not good but for now, I will do it
	// This is for keeping the value of <nextItem> in the response when get the list of Perm
	private String isMore;
	
	private static final long serialVersionUID = 3234423477L;
	
	public Comment(){
		
	}
	
	public Comment(String id){
		
	}
	
	public Comment( String id, String content ){
		this.id = id;
		this.content = content;
	}
	
	public void setAuthor( User author ){
		this.author = author;
	}
	
	public User getAuthor(){
		return this.author;
	}
	
	
	public String getId(){
		return this.id;
	}
	
	public String getContent(){
		return this.content;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsMore() {
		return isMore;
	}

	public void setIsMore(String isMore) {
		this.isMore = isMore;
	}

}
