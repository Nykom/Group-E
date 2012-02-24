package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class NewCourseGroup extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcoursegroup);  
		Bundle bundle = getIntent().getExtras();
		final ArrayList<Person> members = bundle.getParcelableArrayList("com.groupe.types.Person");
		final Group group = bundle.getParcelable("com.groupe.types.Group");

		if (!DBAccess.checkExistingGroup(group, NewCourseGroup.this)){
			DBAccess.saveCourseGroups(group, NewCourseGroup.this);
		}
		for (int j = 0 ; j < members.size(); j++){
			group.addMember(group,members.get(j), NewCourseGroup.this);
		}
		Toast.makeText(NewCourseGroup.this, "Course groups syncronized!", Toast.LENGTH_SHORT).show();
		finish();
	}


	@Override
	public void onResume(){
		super.onResume();
	}

}
