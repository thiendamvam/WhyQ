package whyq.model;

import java.io.Serializable;

public class PermImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private String url;
	private String fileName;
	
	
	public PermImage( String url ){
		this.setUrl(url);
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
