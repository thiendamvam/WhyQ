/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */
package whyq.activity;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import whyq.model.Location;
import whyq.service.ParseChangedLocationXMLFile;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

/**
 * Class description: TODO
 * 
 * @created Jan 12, 2011
 * @version v2.0
 * @author dvthien
 * @copyright TMA Solutions, www.tmasolutions.com
 */
public class ChangeLocationActivity extends Activity {

	private EditText etSearchLocation;
	private String locationToChange;

	public ParseChangedLocationXMLFile tfh;
	private ListView lvLocation;
	private EfficientAdapter adapter;

	ArrayList<Location> locationList = new ArrayList<Location>();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.changelocation);
		etSearchLocation = (EditText) findViewById(R.id.etTextSearch);
		lvLocation = (ListView) findViewById(R.id.lwLocationToChange);
		etSearchLocation
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						// TODO Auto-generated method stub
						return false;
					}
				});

		// End update new location

		lvLocation.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Location location = (Location)view.getTag();
				etSearchLocation.setText(location.getName());

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeLocationActivity.this);
				builder.setMessage(
						"              Confirm\nAre you sure you want to change your location?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										ChangeLocationActivity.this.finish();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.show();
			}
		});

	}

	public void performSearch() {
		try {
			URL url;
			String queryString1 = "http://ws.geonames.org/search?q="
					+ etSearchLocation.getText().toString()
					+ "&style=full&maxRows=10";
			String queryString = queryString1.replace(" ", "+");
			url = new URL(queryString);
			tfh = new ParseChangedLocationXMLFile();
			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(tfh);
			xr.parse(new InputSource(url.openStream()));
			ParseChangedLocationXMLFile parseLocation = new ParseChangedLocationXMLFile();
			locationList = tfh.getResults();
			adapter = new EfficientAdapter(ChangeLocationActivity.this, locationList);
			lvLocation.setAdapter(adapter);
		}

		catch (Exception e) {
			etSearchLocation.setText(e.getMessage());
		}

	}

	public static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<Location> locationList; 
		public EfficientAdapter(Context context, ArrayList<Location> list) {
			mInflater = LayoutInflater.from(context);
			locationList = list;
		}

		public int getCount() {
			return locationList.size()
					;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Location location = locationList.get(position);
			ViewHolder holder;
			if (convertView == null) {
				
				convertView = mInflater.inflate(
						R.layout.listviewchangelocation, null);
				holder = new ViewHolder();
				holder.name = (TextView)convertView.findViewById(R.id.tvName);
				holder.name.setText(location.getName());
				convertView.setTag(location);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		static class ViewHolder {
			TextView name;
		}
	}

	/**
	 * @Description TODO
	 * 
	 */
	public String locationToChange(String newLocation) {
		// TODO Auto-generated method stub

		setLocationToChange(newLocation);
		return newLocation;
	}

	public String getLocationToChange() {
		return locationToChange;
	}

	public void setLocationToChange(String locationToChange) {
		this.locationToChange = locationToChange;
	}

}
