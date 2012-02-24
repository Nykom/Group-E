package com.groupe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * 
 * @author ontl
 *
 */
public class CreateGroup extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
				
		setContentView(R.layout.creategroup);
		
		 final Button okay = (Button) findViewById(R.id.okay);
         okay.setOnClickListener(new View.OnClickListener() {
             @Override
			public void onClick(View v) {
            	 EditText groupName 	= (EditText)findViewById(R.id.createGroupName);
            	 EditText groupCourse 	= (EditText)findViewById(R.id.createGroupCourse);
            	 EditText groupInfo 	= (EditText)findViewById(R.id.createGroupInformation);
            	 
            	          			 				
            	 Person person = FileAccess.getUserFromFile(CreateGroup.this);
            	 Group group = new Group(groupName.getText().toString()+"",
            			 				groupCourse.getText().toString()+"",
            			 				groupInfo.getText().toString()+"",
            			 				person.getMailAddress()           			 				
            	 );
            	 
            	 DBAccess.saveGroup(group,CreateGroup.this);
            	 finish();
             }
         });   
	}
}