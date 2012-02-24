package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.nfc.TagParser;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class CreateCourseNFC extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createcoursenfc);

	}
	@Override
	public void onResume(){
		super.onResume();
		final Person person = FileAccess.getUserFromFile(CreateCourseNFC.this);
		Intent intent = getIntent();
		String message = "";
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
					message = new String(msgs[i].getRecords()[0].getPayload());
				}
				ArrayList<Group> groups = DBAccess.getAllGroups(this);

				ArrayAdapter <CharSequence> adapter =
					new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				adapter.add("Create new Group");
				for(int n = 0;n < groups.size();n++){			
					adapter.add(groups.get(n).getGrpName());			
				}			

				final Spinner groupSpinner = (Spinner)findViewById(R.id.groupSpinnerNfc);
				groupSpinner.setAdapter(adapter);

				final Course course = TagParser.parseForCourse(message);
				final Button button = (Button) findViewById(R.id.okay);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(groupSpinner.getSelectedItem().toString().equals("Create new Group")) {       		 
							DBAccess.saveCourse(course, CreateCourseNFC.this);
							Toast.makeText(CreateCourseNFC.this, "Course created mit neuer Gruppe!", Toast.LENGTH_SHORT).show();
							Group group = new Group(person.getName() + " " + course.getCourseName(),course.getCourseVAK(), " ",person.getMailAddress());
							group.setHashId(group.calculateHash());
							DBAccess.saveGroup(group, CreateCourseNFC.this);
							finish();
						}else{
							DBAccess.saveCourseWithoutNewGroup(course, CreateCourseNFC.this,groupSpinner.getSelectedItem().toString());
							Toast.makeText(CreateCourseNFC.this, "Course created", Toast.LENGTH_SHORT).show();
							finish();
						}

					}
				});
			}
		}
	}
}