package whyq.model;

import java.io.Serializable;

public class TransferData implements Serializable{

	private Object data;

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	} 
}
