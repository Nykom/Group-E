package com.groupe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;

/**
 * @author ontl
 *
 */
public class EditCourse extends Activity{
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.editcourse);
		
		Bundle extras = getIntent().getExtras();
		final Course course = extras.getParcelable("com.groupe.types.Course");
		
		
		EditText courseName  	= (EditText)findViewById(R.id.editCourseName);
		EditText vak  			= (EditText)findViewById(R.id.editVAK);
		EditText lecturerName 	= (EditText)findViewById(R.id.editLecturerName);
		EditText lecturerEmail 	= (EditText)findViewById(R.id.editLecturerEmail);
		
		courseName.setText(course.getCourseName());
		vak.setText(course.getCourseVAK());
		lecturerName.setText(course.getLecturerName());
		lecturerEmail.setText(course.getLecturerMail());
		
        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Course updatedCourse = new Course();
                
            	EditText courseName  	= (EditText)findViewById(R.id.editCourseName);
        		EditText vak  			= (EditText)findViewById(R.id.editVAK);
        		EditText lecturerName 	= (EditText)findViewById(R.id.editLecturerName);
        		EditText lecturerEmail 	= (EditText)findViewById(R.id.editLecturerEmail);
        		
        		updatedCourse.setCourseName(courseName.getText().toString());
        		updatedCourse.setCourseVAK(vak.getText().toString());
        		updatedCourse.setLecturerName(lecturerName.getText().toString());
        		updatedCourse.setLecturerMail(lecturerEmail.getText().toString());
        		
        		DBAccess.updateCourse(DBAccess.getCourseId(course, EditCourse.this),updatedCourse, EditCourse.this);
            	finish();
            }
        });
	}
}
