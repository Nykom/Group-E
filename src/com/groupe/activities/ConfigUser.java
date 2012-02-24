package com.groupe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class ConfigUser extends Activity {

	static String configUserFile  = "groupeConfigUser";

	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.configappuser);

		final Person person = FileAccess.getUserFromFile(this);

		EditText editName  = (EditText)findViewById(R.id.EditUserName);
		EditText editMail  = (EditText)findViewById(R.id.EditUserMail);
		EditText editPhone = (EditText)findViewById(R.id.EditUserPhone);


		editName.setText(person.getName());
		editMail.setText(person.getMailAddress());
		editPhone.setText(person.getPhoneNumber());

		final Button updateEmail = (Button)findViewById(R.id.save);
		updateEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText name  = (EditText)findViewById(R.id.EditUserName);
				EditText mail  = (EditText)findViewById(R.id.EditUserMail);
				EditText phone = (EditText)findViewById(R.id.EditUserPhone);

				Person personTmp = new Person(name.getText().toString(),mail.getText().toString(),phone.getText().toString());
				String hash;
				if (person.getHashId() != null){
					hash = person.getHashId();
				}else{
					hash = personTmp.calculateHash(mail.getText().toString());
				}
				String userInfo = 	name.getText().toString() + "\n" +
						mail.getText().toString() + "\n" +
						phone.getText().toString()+ "\n" +
						hash;

				FileAccess.writeFile(userInfo, configUserFile, ConfigUser.this);
				finish();
			}             
		});
	}
}