package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.mail.MailTransport;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class WriteMessage extends Activity {
	boolean reply;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writemessage); 
		
		Bundle extras = getIntent().getExtras();
		final Group group = extras.getParcelable("com.groupe.types.Group");
		final Mail mail = extras.getParcelable("com.groupe.types.Mail");
				
		ArrayAdapter <CharSequence> adapter =
			new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final ArrayList<Person> members = group.getMembers(group,this);
		final ArrayList<Person> spinnerMembers = new ArrayList<Person>();
		final Person user = FileAccess.getUserFromFile(this);
				
		
		if(mail == null){
			reply = false;
			for(int n = 0;n < members.size();n++){			
				
				if(!user.getName().equals(members.get(n).getName())) {			
					adapter.add(members.get(n).getName());	
					spinnerMembers.add(members.get(n));
				}
			}			
			adapter.add("all members");
		}else{
			reply = true;
			adapter.add(mail.getSender());
		}
		
		
		final Spinner reci = (Spinner)findViewById(R.id.reciSpinner);
		reci.setAdapter(adapter);
		
		
		Button sendMessage = (Button)findViewById(R.id.buttonSend);
		sendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MailAccount mailAccount = FileAccess.getMailAccountFromFile(WriteMessage.this);
				
				Mail mail = new Mail();
				mail.setSender(mailAccount.getUser());
				
				ArrayList<String> re = new ArrayList<String>();
				
				if(!reply) {
					if(reci.getSelectedItem().equals("all members")) {
						for(int i = 0;i < members.size();i++) {
							if(!user.getName().equals(members.get(i).getName())) {	
								re.add(members.get(i).getMailAddress());
							}
						}
					}else{
						re.add(spinnerMembers.get(reci.getSelectedItemPosition()).getMailAddress());
					}
				}else{
					re.add(reci.getSelectedItem().toString());
				}				
				mail.setReciever(re);
				
				EditText message = (EditText)findViewById(R.id.editText2);
				
				mail.setBody(message.getText().toString());
				
				mail.setSubject("Group E message from " + group.getGrpName());
				
				
				try {
					MailTransport.send(mailAccount, mail, WriteMessage.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finish();
			}             
		});
		
		Button abort = (Button)findViewById(R.id.buttonAbort);
		abort.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				finish();
			}             
		});
	   
	}
}
