package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.mail.MailTransport;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class CreateCourseGroups extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcoursegroup);  
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(this);
		ArrayList<String> syncReceiver = new ArrayList<String>();
		 
		Bundle bundle = getIntent().getExtras();
		Mail mail = bundle.getParcelable("com.groupe.types.Mail");
		final ArrayList<Person> members = bundle.getParcelableArrayList("com.groupe.types.Person");
		final ArrayList<Group> groups = bundle.getParcelableArrayList("com.groupe.types.Group");
		for (int i = 0 ; i < groups.size(); i++){
			if (!DBAccess.checkExistingGroup(groups.get(i), CreateCourseGroups.this)){
				DBAccess.saveCourseGroups(groups.get(i), CreateCourseGroups.this);
			}
			if (groups.get(i).getHead().equals(mailAccount.getUser())){
				ArrayList<Person> syncReceiverPersons = groups.get(i).getMembers(groups.get(i), CreateCourseGroups.this);
				for (int n = 0 ; n < syncReceiverPersons.size() ; n++){
					if(!syncReceiverPersons.get(n).getMailAddress().equals(mailAccount.getUser())){
						syncReceiver.add(syncReceiverPersons.get(n).getMailAddress());
					}
				}
			}
			for (int j = 0 ; j < members.size(); j++){
				if (members.get(j).getSyncGroup().equals(groups.get(i).getHashId())){
					groups.get(i).addMember(groups.get(i),members.get(j), CreateCourseGroups.this);
				}
			}
		}
		Toast.makeText(CreateCourseGroups.this, "Course groups syncronized!", Toast.LENGTH_SHORT).show();
		mail.setReciever(syncReceiver);
		try {
			MailTransport.send(mailAccount, mail, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}
	@Override
	public void onResume(){
		super.onResume();
	}
	
}
