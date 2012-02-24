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
public class AddMemberManuel extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmembermanuel);
        
        Bundle bundle = getIntent().getExtras();
        final Group group = bundle.getParcelable("com.groupe.types.Group");
        
        final Button addMember = (Button)findViewById(R.id.AddMemberButton);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	EditText name  = (EditText)findViewById(R.id.AddPersonName);
                EditText mail  = (EditText)findViewById(R.id.AddPersonMail);
                EditText phone = (EditText)findViewById(R.id.AddPersonPhone);
                
                Person person = new Person(name.getText().toString(),mail.getText().toString(),phone.getText().toString());
                person.setHashId(person.calculateHash(mail.getText().toString()));
                long mog = DBAccess.getGroupId(group, AddMemberManuel.this);
                person.setMemberOfGroup(new Long(mog).toString());
                
                DBAccess.insertPerson(group, person, AddMemberManuel.this);
                
                Person user = FileAccess.getUserFromFile(AddMemberManuel.this);
                String head = user.getMailAddress();
                if(!group.getHead().equals(head)) {
                	group.updateGroupInformation(group, AddMemberManuel.this);
                }else{
                	group.sendStatus(group, AddMemberManuel.this);
                }
                finish();
            }             
        });
	}
} 