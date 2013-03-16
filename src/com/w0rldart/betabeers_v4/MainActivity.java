package com.w0rldart.betabeers_v4;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private ListView mainListView;
	private mItems[] itemss;
	private ArrayAdapter<mItems> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mainListView = (ListView) findViewById(R.id.mainListView);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
        		mItems category = listAdapter.getItem(position);
        		category.toggleChecked();
        		CategoryViewHolder viewHolder = (CategoryViewHolder) item.getTag();
        		viewHolder.getCheckBox().setChecked(category.isChecked());
        	}
		});
        itemss = (mItems[]) getLastNonConfigurationInstance();
        ArrayList<mItems> categoryList = new ArrayList<mItems>();
        categoryList.add(new mItems("Astronomy"));
        categoryList.add(new mItems("Fisics"));
        categoryList.add(new mItems("Animals"));
        categoryList.add(new mItems("Human-Body"));
        listAdapter =  new CategoryArrayAdapter(this, categoryList);
        mainListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
