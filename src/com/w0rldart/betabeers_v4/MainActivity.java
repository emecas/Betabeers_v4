package com.w0rldart.betabeers_v4;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.w0rldart.commons.w0rldartHelper;

public class MainActivity extends Activity {

	private String selectedCategories;
	private w0rldartHelper helper;
	
	private ListView mainListView;
	private mItems[] itemss;
	private ArrayAdapter<mItems> listAdapter;
	ArrayList<String> checked = new ArrayList<String>();
	Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        helper = new w0rldartHelper(this);
        
        mainListView = (ListView) findViewById(R.id.mainListView);
        
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
        		mItems category = listAdapter.getItem(position);
        		category.toggleChecked();
        		SelectViewHolder viewHolder = (SelectViewHolder) item.getTag();
        		viewHolder.getCheckBox().setChecked(category.isChecked());
        	}
		});
        
        itemss = (mItems[]) getLastNonConfigurationInstance();
        
        ArrayList<mItems> categoryList = new ArrayList<mItems>();
        
        categoryList.add(new mItems("Astronomy"));
        categoryList.add(new mItems("Physics"));
        categoryList.add(new mItems("Animals"));
        
        
        listAdapter =  new SelectArralAdapter(this, categoryList);
        mainListView.setAdapter(listAdapter);
        
        go = (Button) findViewById(R.id.loadContent);
        go.setOnClickListener(clickHandler);
        
    }
    
    View.OnClickListener clickHandler = new View.OnClickListener() {
    	public void onClick(View v) {
    		
    		CheckBox myCheck = null;
    		RelativeLayout myLayout = null;
    		selectedCategories = "";
    		
    		
    		for(int i = 0; i < mainListView.getCount(); i++) {
    			
    			myLayout = (RelativeLayout) mainListView.getChildAt(i);
    			myCheck =  (CheckBox) myLayout.getChildAt(1);
    			
    			if(myCheck.isChecked()) {
    				String myText = "" + ((TextView) myLayout.getChildAt(0)).getText();
    				selectedCategories += (selectedCategories.length()>0 ? "," : "") + myText;
    			}
    		}
    		
    		helper.sGet("?categories="+selectedCategories, new JsonHttpResponseHandler(){
				@Override
	            public void onSuccess(JSONArray results) {
					try{
						for(int i = 0; i < results.length(); i++ ) {
							System.out.println(results.getJSONObject(i).getString("photoName"));
						}
					} catch (Exception exc) {
						Log.d("fPost-success-exception", exc.toString());
					}
					// System.out.println("results succes: " + results);
	            }
				@Override
				public void handleFailureMessage(Throwable e, String responseBody) {
					System.out.println(responseBody);
					try {					
						JSONObject error = new JSONObject(responseBody);
					    JSONObject errorobj = error.getJSONObject("error");	
					} catch(Exception exc) {
						Log.d("fPost-failure-exception", exc.toString());
						// exc.printStackTrace();
					}
				}
    		});
    		System.out.println(selectedCategories);
        }
	};
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:

			for (int i = 0; i < checked.size(); i++) {
				Log.d("pos : ", "" + checked.get(i));
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    /** Holds category data. */
	private static class mItems {
		private String name = "";
		private boolean checked = false;

		public mItems() {
		}

		public mItems(String name) {
			this.name = name;
		}

		public mItems(String name, boolean checked) {
			this.name = name;
			this.checked = checked;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String toString() {
			return name;
		}

		public void toggleChecked() {
			checked = !checked;
		}
	}

	/** Holds child views for one row. */
	private static class SelectViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public SelectViewHolder() {}

		public SelectViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}

	/** Custom adapter for displaying an array of Category objects. */
	private static class SelectArralAdapter extends ArrayAdapter<mItems> {
		private LayoutInflater inflater;

		public SelectArralAdapter(Context context, List<mItems> categoryList) {
			super(context, R.layout.listrow, R.id.rowTextView, categoryList);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Category to display
			mItems category = (mItems) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textView;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listrow, null);

				// Find the child views.
				textView = (TextView) convertView
						.findViewById(R.id.rowTextView);
				checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
				convertView.setTag(new SelectViewHolder(textView, checkBox));
				// If CheckBox is toggled, update the category it is tagged with.
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						mItems category = (mItems) cb.getTag();
						category.setChecked(cb.isChecked());
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				SelectViewHolder viewHolder = (SelectViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			// Tag the CheckBox with the Category it is displaying, so that we can
			// access the category in onClick() when the CheckBox is toggled.
			checkBox.setTag(category);
			// Display category data
			checkBox.setChecked(category.isChecked());
			textView.setText(category.getName());
			return convertView;
		}
	}

	public Object onRetainNonConfigurationInstance() {
		return itemss;
	}
    
}
