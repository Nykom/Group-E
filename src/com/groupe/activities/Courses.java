package com.groupe.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.mail.InviteListener;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;



/**
 * @author ontl
 *
 */
public class Courses extends ListActivity {
	private static final int MENU1 = Menu.FIRST;
	private static final int MENU2 = Menu.FIRST + 1;
	private static final int MENU3 = Menu.FIRST + 2;	
	private static final int MENU4 = Menu.FIRST + 3;
	private static final int MENU5 = Menu.FIRST + 4;
	int y = 0;
	public ArrayList<Course> courses;
	long courseId[];
	String course[];
	String nummer[];
	String info[];
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		
		
	    final String[] columns = { "Zeile1", "Zeile2" };
	    final String[] matrix  = { "_id", "Zeile1", "Zeile2" };
	    final int[] layouts = new int[] { android.R.id.text1, android.R.id.text2 };
	    int key = 0;
	                   
	    MatrixCursor cursor = new MatrixCursor(matrix);
	    
	    final ArrayList<Course> courses = DBAccess.getAllCourses(this);
	     
	    if(courses.size() != 0) {
	    	for(int n = 0;n < courses.size();n++){
	    		cursor.addRow(new Object[] { key++, courses.get(n).getCourseName(), 
	    				courses.get(n).getCourseVAK()});
	    	}
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
		    	Intent intent = new Intent(Courses.this,CourseView.class);
		    	intent.putExtra("com.groupe.types.Course", courses.get((int)id));
		    	startActivity(intent);
		    }
		});
	}
	@Override
	public void onResume(){
		super.onResume();
		onCreate(null);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		menu.add(0, MENU1, 0 , "Create Course").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU2, 0 , "Preferences").setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU3, 0 , "Check Messages").setIcon(R.drawable.ic_menu_refresh);
		menu.add(0 ,MENU4, 0 , "Close").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0 ,MENU5, 0 , "Group Mode").setIcon(android.R.drawable.ic_menu_mapmode);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU1:
	    	startActivity(new Intent(Courses.this, CreateCourse.class));
	        return true;
	    case MENU2:
	    	//TODO
	    	startActivity(new Intent(Courses.this, ConfigMain.class));
	    	return true;			
	    case MENU3:
			File userConfig = getFileStreamPath("groupeConfigUser");
			File mailConfig = getFileStreamPath("groupeConfigEmail");
			if(!userConfig.exists() || !mailConfig.exists()) {
				Toast.makeText(this, "No configuration found! Please go to preferences.", Toast.LENGTH_LONG).show();				
			}else{	    
				startService(new Intent(this,InviteListener.class));
			}
			return true;
	    case MENU4:
	    	finish();
	    	return true;
	    case MENU5:
	    	finish();
	    	startActivity(new Intent(Courses.this, GroupEActivity.class));
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
