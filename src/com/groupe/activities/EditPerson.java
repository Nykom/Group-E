package com.groupe.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class EditPerson extends Activity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.editperson);
		
		Bundle extras = getIntent().getExtras();
		final Person person = extras.getParcelable("com.groupe.types.Person");
		
		EditText personName  = (EditText)findViewById(R.id.EditPersonName);
		EditText personMail  = (EditText)findViewById(R.id.EditPersonEmail);
		EditText personPhone = (EditText)findViewById(R.id.EditPersonPhone);
		
		personName.setText(person.getName());
		personMail.setText(person.getMailAddress());
		personPhone.setText(person.getPhoneNumber());
		
        final Button save = (Button)findViewById(R.id.EditMember);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Person updatedPerson = new Person();
            	EditText name  = (EditText)findViewById(R.id.EditPersonName);
        		EditText mail  = (EditText)findViewById(R.id.EditPersonEmail);
        		EditText phone = (EditText)findViewById(R.id.EditPersonPhone);
        		
        		updatedPerson.setName(name.getText().toString());
        		updatedPerson.setMailAddress(mail.getText().toString());
        		updatedPerson.setPhoneNumber(phone.getText().toString());
        		updatedPerson.setId(DBAccess.getPersonId(person, EditPerson.this));
        		DBAccess.updatePerson(updatedPerson, EditPerson.this);
            	finish();
            }
        });   
        
        final Button delete = (Button) findViewById(R.id.DeleteMember);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	DBAccess.deletePerson(DBAccess.getPersonId(person, EditPerson.this),EditPerson.this);
            	finish();
            }
        });
        final ImageButton call = (ImageButton)findViewById(R.id.PhoneCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_DIAL, 
            			Uri.parse("tel:" + person.getPhoneNumber()));
            	startActivity(intent);
            }
        });
        final ImageButton write = (ImageButton)findViewById(R.id.WriteEmail);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_SENDTO, 
            			Uri.parse("mailto:" + person.getMailAddress()));
            	startActivity(intent);
            }
        });
	}
}