package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.Person;
/**
 * 
 * @author ontl
 *
 */
public class MemberLeftGroup extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberleftgroup);  

		Bundle bundle = getIntent().getExtras();

		final Group group = bundle.getParcelable("com.groupe.types.Group");
		final ArrayList<Person> persons = bundle.getParcelableArrayList("com.groupe.types.Person");
		final boolean ishead = bundle.getBoolean("head");
		
		TextView grp = (TextView) findViewById(R.id.inviteGrp);
		grp.setText(group.getGrpName().toString() +"");

		TextView course = (TextView) findViewById(R.id.inviteCourse);
		course.setText(group.getCourse().toString() +"");

		TextView infos = (TextView) findViewById(R.id.inviteInformation);
		infos.setText(group.getInformation().toString() +"");

		String memberNames = "";
		for(int i = 0;i < persons.size();i++) {

			if(persons.size()-1 == i) {
				memberNames += persons.get(i).getName();
			}else{
				memberNames += persons.get(i).getName() + ", ";
			}
		}
		TextView members = (TextView) findViewById(R.id.invitePersons);
		members.setText(memberNames);
		

		final Button accept = (Button) findViewById(R.id.addPerson);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<Person> pers = DBAccess.getAllPersons(MemberLeftGroup.this);
				Person person = new Person();
				for (int i = 0; i < pers.size(); i++){
					if (pers.get(i).getHashId().equals(persons.get(0).getHashId())){
						person = pers.get(i);
					}
				}
				person.removeMemberOfGroup(person, new Long(DBAccess.getGroupId(group, MemberLeftGroup.this)).toString(), MemberLeftGroup.this);
			
				group.setGrpName(group.getGrpName()+"*");
				
				DBAccess.updateGroup(DBAccess.getGroupId(group, MemberLeftGroup.this), group, MemberLeftGroup.this);
				
				if(ishead) {
										
					Person user = FileAccess.getUserFromFile(MemberLeftGroup.this);
					group.setHead(user.getMailAddress());
					DBAccess.updateGroup(DBAccess.getGroupId(group, MemberLeftGroup.this), group, MemberLeftGroup.this);
					
					Mail mail = new Mail();
					ArrayList<String> reci = new ArrayList<String>();
					ArrayList<Person> members = group.getMembers(group, MemberLeftGroup.this);
					for(int i = 0; i < members.size();i++) {
						reci.add(members.get(i).getMailAddress());
					}
					mail.setReciever(reci);
					mail.setBody(group.generateInformation(group, MemberLeftGroup.this));
					mail.setSubject("Group E status message");
				}
				finish();
			}

		});
	}
}