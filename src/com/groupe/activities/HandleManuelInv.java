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
import com.groupe.types.MailAccount;
import com.groupe.types.Person;
/**
 * 
 * @author ontl
 *
 */
public class HandleManuelInv extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handleack);  

		Bundle bundle = getIntent().getExtras();

		final Group group = bundle.getParcelable("com.groupe.types.Group");
		final ArrayList<Person> persons = bundle.getParcelableArrayList("com.groupe.types.Person");
		
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
				
				for(int i = 0; i < persons.size();i++) {
					if(!persons.get(i).checkPersonInGroup(persons.get(i), group, HandleManuelInv.this)) {
						long mog = DBAccess.getGroupId(group, HandleManuelInv.this);
						persons.get(i).setMemberOfGroup(new Long(mog).toString());
						DBAccess.insertPerson(group, persons.get(i), HandleManuelInv.this);
						group.setGrpName(group.getGrpName()+"*");
						DBAccess.updateGroup(DBAccess.getGroupId(group, HandleManuelInv.this), group, HandleManuelInv.this);
						
					}				
				}				
				finish();
			}

		});
		final Button decline = (Button) findViewById(R.id.addPersonInform);
		decline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Person person = new Person();
				long mog = DBAccess.getGroupId(group, HandleManuelInv.this);
				
                person.setMemberOfGroup(new Long(mog).toString());
                                
                DBAccess.insertPerson(group, persons.get(0), HandleManuelInv.this);
				DBAccess.updateGroup(DBAccess.getGroupId(group, HandleManuelInv.this), group, HandleManuelInv.this);
				MailAccount mailAccount = FileAccess.getMailAccountFromFile(HandleManuelInv.this);
				if(mailAccount.getUser().equals(group.getHead())) {
					group.sendStatus(group, HandleManuelInv.this);
				}
				
				finish();
			}
		});
	}
}