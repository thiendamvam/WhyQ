package whyq.service;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import whyq.handler.FriendFacebookHandler;
import whyq.handler.UserHandler;
import whyq.interfaces.FriendFacebookController;

public class DataParser {

	public static String issueIntro = "";

	// private StoreIssueDataControler issueControler;
	public DataParser() {
		// issueController.resetStorySetData();
	}

	public Object parseRetaurentList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object parseLogout() {
		// TODO Auto-generated method stub
		// result.setSuccess(_root.optBoolean("success", false));
		// return null
		return null;
	}

	public Object parserLoginData(String inputString) {
		// TODO Auto-generated method stub
		try {
			XMLReader xmlReader = initializeReader();
			UserHandler userHandler = new UserHandler();
			// assign the handler
			xmlReader.setContentHandler(userHandler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return userHandler.getUser();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static final FriendFacebookController parseFriendFacebook(
			String inputString) {
		try {
			XMLReader xmlReader = initializeReader();
			FriendFacebookHandler handler = new FriendFacebookHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(inputString)));
			return handler;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static XMLReader initializeReader()
			throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// Create a parser
		SAXParser parser = factory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
		return xmlReader;
	}
}
