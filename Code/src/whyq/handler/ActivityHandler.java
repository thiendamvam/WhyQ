/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.ActivityItem;

public class ActivityHandler extends DefaultHandler {

	private List<ActivityItem> items = new ArrayList<ActivityItem>();;
	private ActivityItem activity;

	private StringBuffer buffer = new StringBuffer();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		String value = buffer.toString();
		if (localName.equals("message")) {
			activity.setMessage(value);
		} else if (localName.equals("obj")) {
			items.add(activity);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		buffer.setLength(0);

		if (localName.equals("obj")) {
			activity = new ActivityItem();
		}
	}
	
	public List<ActivityItem> getItems () {
		return items;
	}
	
}
