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
import com.groupe.types.Course;
import com.groupe.types.Group;

/**
 * @author ontl
 *
 */
public class ShowCourseGroups extends ListActivity{

	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Bundle bundle = getIntent().getExtras();
		final Course course = bundle.getParcelable("com.groupe.types.Course");

	    final String[] columns = { "Zeile1", "Zeile2"};
	    final String[] matrix  = { "_id", "Zeile1", "Zeile2" };
	    final int[] layouts = new int[] { android.R.id.text1, android.R.id.text2 };
	    int key = 0;
	                   
	    MatrixCursor cursor = new MatrixCursor(matrix);
	    
	    final ArrayList<Group> groups = DBAccess.getGroups(course, this);
	    
	    if(groups.size() != 0) {
	    	for(int n = 0;n < groups.size();n++){
	    		cursor.addRow(new Object[] { key++, groups.get(n).getGrpName(),"Number of Members: " + groups.get(n).getMemberCount(groups.get(n), this)});
	    	}
	    }

		SimpleCursorAdapter data = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_2, 
				cursor, columns, layouts);
		
		setListAdapter(data);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	
		    	Intent intent = new Intent(ShowCourseGroups.this, CourseGroupView.class);
		    	intent.putExtra("com.groupe.types.Group", groups.get((int)id));
		    	intent.putExtra("com.groupe.types.Course", course);
		    	startActivity(intent);
		    }
		});
	}
}
