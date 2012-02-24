package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.persistence.DBAccess;
import com.groupe.persistence.DBAdapter;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class CourseView extends Activity{
	private static final int MENU1 = Menu.FIRST;

	DBAdapter myDB = new DBAdapter(CourseView.this);
	Course course;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);  

		Bundle bundle = getIntent().getExtras();

		course = bundle.getParcelable("com.groupe.types.Course");

		TextView tv1 = (TextView) findViewById(R.id.veranstaltung);
		tv1.setText("Course: " + course.getCourseName());

		TextView tv2 = (TextView) findViewById(R.id.info);
		tv2.setText("VAK: " + course.getCourseVAK());

		TextView tv3 = (TextView) findViewById(R.id.lecturername);
		tv3.setText("Lecturer: " + course.getLecturerName());

		TextView tv4 = (TextView) findViewById(R.id.lecturermail);
		tv4.setText("Lecturer Email: " + course.getLecturerMail());

		TextView tv5 = (TextView)findViewById(R.id.courseMember);
		String groups = "Groups:" + "\n";
		final ArrayList<Group> groupList = DBAccess.getGroups(course, this);
		for(int j = 0;j < groupList.size();j++) {
			groups += groupList.get(j).getGrpName() + "  Number of Members: " + groupList.get(j).getMemberCount(groupList.get(j), this) + "\n";
		}
		tv5.setText(groups);

		Button writeToTag = (Button)findViewById(R.id.writeCourseToTag);
		writeToTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CourseView.this, WriteToTag.class);
				intent.putExtra("com.groupe.types.Course", course);
				startActivity(intent);
			}
		});

		Button showGroups = (Button)findViewById(R.id.showAllCourseGroups);
		showGroups.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (groupList.size()== 0){
					Toast.makeText(CourseView.this, "No Groups!", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(CourseView.this, ShowCourseGroups.class);
					intent.putExtra("com.groupe.types.Course", course);
					startActivity(intent);
				}
			}             
		});
		
		Button editCourse = (Button)findViewById(R.id.editCourse);
		editCourse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CourseView.this, EditCourse.class);
				intent.putExtra("com.groupe.types.Course", course);
				startActivity(intent);
			}
		});

		Button showPersons = (Button)findViewById(R.id.showAllCourseMember);
		showPersons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (groupList.size()== 0){
					Toast.makeText(CourseView.this, "No Member!", Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(CourseView.this, ShowAllCourseMember.class);
					intent.putExtra("com.groupe.types.Course", course);
					startActivity(intent);
				}
			}             
		});

		Button deleteCourse = (Button)findViewById(R.id.deleteCourse);
		deleteCourse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {         	             	 
				AlertDialog.Builder alertbox = new AlertDialog.Builder(CourseView.this);
				alertbox.setMessage("Disband Course?");
				alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						long id = DBAccess.getCourseId(course, CourseView.this);
						DBAccess.deleteCourse(id, CourseView.this);
						finish();
					}
				});

				alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {   
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
				alertbox.show();
			}             
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		
		String lecturer = course.getLecturerMail();
		Person user = FileAccess.getUserFromFile(CourseView.this);
		
		if(user.getMailAddress().equals(lecturer)) {			
			menu.add(0, MENU1, 0 , "Synchronize");
		}
				
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
	    switch (item.getItemId()) {
	    case MENU1:
	    	course.syncCourse(course, CourseView.this);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
