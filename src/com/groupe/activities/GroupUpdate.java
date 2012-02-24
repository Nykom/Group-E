package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class GroupUpdate extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupupdate);  

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
		
		final Button accept = (Button) findViewById(R.id.confirmUpdate);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ArrayList<Group> myGroups = DBAccess.getAllGroups(GroupUpdate.this);
				
				for(int i = 0; i < myGroups.size();i++) {
					if(myGroups.get(i).getHashId().equals(group.getHashId())) {
						ArrayList<Person> members = myGroups.get(i).getMembers(myGroups.get(i), GroupUpdate.this);
						long id = DBAccess.getGroupId(myGroups.get(i), GroupUpdate.this);
						String idstr = new Long(id).toString();
						for (int n = 0; n < members.size(); n++){
							members.get(n).removeMemberOfGroup(members.get(n), idstr, GroupUpdate.this);
						}
						for(int j = 0; j < persons.size(); j++){
							myGroups.get(i).addMember(myGroups.get(i), persons.get(j), GroupUpdate.this);
						}
						myGroups.get(i).setHead(group.getHead());
						DBAccess.updateGroup(id, myGroups.get(i), GroupUpdate.this);
					}
				}
			}
		});
	}
}
