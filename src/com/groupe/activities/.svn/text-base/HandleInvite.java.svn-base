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
import com.groupe.mail.MailTransport;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;
import com.groupe.types.Person;
/**
 * 
 * @author ontl
 *
 */
public class HandleInvite extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handleinvite);  

		Bundle bundle = getIntent().getExtras();
		
		final Group group = bundle.getParcelable("com.groupe.types.Group");
		final Mail email = bundle.getParcelable("com.groupe.types.Mail");
		final ArrayList<Person> persons = bundle.getParcelableArrayList("com.groupe.types.Person");
		final Course course = bundle.getParcelable("com.groupe.types.Course");

		TextView grp = (TextView) findViewById(R.id.inviteGrp);
		grp.setText(group.getGrpName().toString() +"");

		TextView courseName = (TextView) findViewById(R.id.inviteCourse);
		courseName.setText(group.getCourse().toString() +"");

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


		final Button accept = (Button) findViewById(R.id.acceptInv);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(course.getCourseName() != null && DBAccess.checkExistingCourse(course, HandleInvite.this)) {
					DBAccess.saveCourse(course, HandleInvite.this);
				}
				DBAccess.createData(group, persons, HandleInvite.this);

				MailAccount ma = FileAccess.getMailAccountFromFile(HandleInvite.this);
				Person person = FileAccess.getUserFromFile(HandleInvite.this);
				Mail mail = new Mail();
				String body = person.acknowledge(person, group);
				
				mail.setSender(ma.getUser());
				mail.setSubject("Group E ack");
				
				mail.setBody(body);
				ArrayList<String> re = new ArrayList<String>();
				re.add(email.getSender());
				mail.setReciever(re);
				
				try {
					MailTransport.send(ma, mail, HandleInvite.this);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		final Button decline = (Button) findViewById(R.id.declineInv);
		decline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MailAccount ma = FileAccess.getMailAccountFromFile(HandleInvite.this);
				Mail mail = new Mail();
				mail.setSender(ma.getUser());
				mail.setSubject("Group E deny");
				
				mail.setBody("Invite denied");
				ArrayList<String> re = new ArrayList<String>();
				re.add(email.getSender());
				mail.setReciever(re);
				try {
					MailTransport.send(ma, mail, HandleInvite.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finish();
			}
		});
	}
}