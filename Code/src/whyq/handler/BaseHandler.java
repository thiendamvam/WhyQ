package whyq.handler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BaseHandler extends DefaultHandler {
	protected StringBuilder builder;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
	}

	protected String getString() {
		return builder.toString();
	}
	
	protected int getInt() {
		return Integer.parseInt(builder.toString());
	}
	
	protected float getFloat() {
		return Float.parseFloat(builder.toString());
	}
	
	protected boolean getBoolean() {
		return builder.toString().equals("1") ? true : false;
	}


	protected double getDouble() {
		return Double.parseDouble(builder.toString());
	}
}
