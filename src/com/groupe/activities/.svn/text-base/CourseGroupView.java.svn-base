package com.groupe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.types.Course;
import com.groupe.types.Group;

/**
 * @author ontl
 *
 */
public class CourseGroupView extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursegroupview);  

		Bundle bundle = getIntent().getExtras();
		final Group group = bundle.getParcelable("com.groupe.types.Group");
		final Course course = bundle.getParcelable("com.groupe.types.Course");
		TextView tvName = (TextView)findViewById(R.id.gruppe);
		tvName.setText("Group: " + group.getGrpName());

		TextView tvCourse = (TextView)findViewById(R.id.veranstaltung);
		tvCourse.setText("Course: " + course.getCourseName());

		TextView tvInfo = (TextView)findViewById(R.id.info);
		tvInfo.setText("Information: " + group.getInformation());

		TextView member = (TextView)findViewById(R.id.member);
		String memberText = "Member:" + "\n";

		for(int i = 0;i < group.getMembers(group, this).size();i++) {
			memberText += group.getMembers(group, this).get(i).getName() + "\n";
		}
		member.setText(memberText);
		
		Button showPersons = (Button)findViewById(R.id.ContactGroup);
		showPersons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(CourseGroupView.this, WriteMessage.class);
				intent.putExtra("com.groupe.types.Group", group);
				startActivity(intent);
				finish();
			}             
		});
	}
	
}
