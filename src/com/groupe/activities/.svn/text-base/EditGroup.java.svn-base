package com.groupe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;

/**
 * @author ontl
 *
 */
public class EditGroup extends Activity{
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.editgroup);
		
		Bundle extras = getIntent().getExtras();
		final Group group = extras.getParcelable("com.groupe.types.Group");
		
		EditText grpName  	= (EditText)findViewById(R.id.editGroupName);
		EditText info  		= (EditText)findViewById(R.id.editGroupInfo);
		EditText course 	= (EditText)findViewById(R.id.editGroupCourse);
		
		grpName.setText(group.getGrpName());
		info.setText(group.getInformation());
		course.setText(group.getCourse());
		
        final Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Group updatedGroup = new Group();
            	EditText grpName  	= (EditText)findViewById(R.id.editGroupName);
        		EditText info  		= (EditText)findViewById(R.id.editGroupInfo);
        		        		
        		updatedGroup.setGrpName(grpName.getText().toString());
        		updatedGroup.setCourse(group.getCourse());
        		updatedGroup.setInformation(info.getText().toString());
        		
        		DBAccess.updateGroup(DBAccess.getGroupId(group, EditGroup.this),updatedGroup, EditGroup.this);
            	finish();
            }
        });
	}
}
