package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;


/**
 * @author ontl
 *
 */
public class CreateCourse extends Activity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		final Person person = FileAccess.getUserFromFile(this);
		setContentView(R.layout.createcourse);
		
		ArrayList<Group> groups = DBAccess.getAllGroups(this);
		
		ArrayAdapter <CharSequence> adapter =
			new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter.add("Create new Group");
		adapter.add("Without Group");
		for(int n = 0;n < groups.size();n++){			
			adapter.add(groups.get(n).getGrpName());			
		}			
				
		final Spinner groupSpinner = (Spinner)findViewById(R.id.groupSpinner);
		groupSpinner.setAdapter(adapter);
		
		final Button button = (Button) findViewById(R.id.okay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
             EditText courseName = (EditText)findViewById(R.id.course_name);
           	 EditText courseInfo = (EditText)findViewById(R.id.information);
           	 EditText lecturerName = (EditText)findViewById(R.id.lecturername);
           	 EditText lecturerEmail = (EditText)findViewById(R.id.lectureremail);

           	Toast.makeText(CreateCourse.this, "Create Course: \n" + courseName.getText().toString() + "\n" +
      				 courseInfo.getText().toString(),Toast.LENGTH_LONG).show();
      		 
      		 Course course = new Course(courseName.getText().toString(), courseInfo.getText().toString(), 
      				 lecturerName.getText().toString(), lecturerEmail.getText().toString());
           	 
           	 if(groupSpinner.getSelectedItem().toString().equals("Create new Group")) {       		 
           		DBAccess.saveCourse(course, CreateCourse.this);
           		Group group = new Group(person.getName() + " " + course.getCourseName(),course.getCourseVAK(), " ",person.getMailAddress());
        		group.setHashId(group.calculateHash());
        		DBAccess.saveGroup(group, CreateCourse.this);
           	 }else if(groupSpinner.getSelectedItem().toString().equals("Without Group")){
           		DBAccess.saveCourse(course, CreateCourse.this);
           	 }else{
           		 DBAccess.saveCourseWithoutNewGroup(course, CreateCourse.this,groupSpinner.getSelectedItem().toString());           		 
           	 }   	 
             
           	 finish();
            }
        });
        
        final Button button1 = (Button) findViewById(R.id.show);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            }             
        });  

	}
}
