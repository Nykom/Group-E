package com.groupe.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.groupe.persistence.DBAccess;
import com.groupe.types.Person;
/**
 * 
 * @author ontl
 *
 */

public class ShowAllPersons extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

	    final String[] columns = { "Zeile1", "Zeile2" };
	    final String[] matrix  = { "_id", "Zeile1", "Zeile2" };
	    final int[] layouts = new int[] { android.R.id.text1, android.R.id.text2 };
	    MatrixCursor cursor = new MatrixCursor(matrix);
	    int key = 0;
	    
	    final ArrayList<Person> members = DBAccess.getAllPersons(this);
		for(int n = 0;n < members.size();n++){
			cursor.addRow(new Object[] { key++, members.get(n).getName(), members.get(n).getMailAddress()});
		}
	    
		SimpleCursorAdapter data = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_2, 
				cursor, columns, layouts);
		setListAdapter( data );
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	
		    	Intent intent = new Intent(ShowAllPersons.this,EditPerson.class);
				intent.putExtra("com.groupe.types.Person", members.get((int) id));
				startActivity(intent);
		    }
		  });		
	}
	@Override
	public void onResume() {
		super.onResume();
		onCreate(null);
	}
	
}