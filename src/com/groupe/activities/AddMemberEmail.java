package com.groupe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.mail.InviteListener;
import com.groupe.types.Group;
import com.groupe.types.MailAccount;

/**
 * @author ontl
 *
 */
public class AddMemberEmail extends Activity {

	@Override 
	public void onCreate(Bundle icicle) { 
		super.onCreate(icicle); 
		setContentView(R.layout.addmemberemail); 

		Bundle bundle = getIntent().getExtras();
		final Group group = bundle.getParcelable("com.groupe.types.Group");

		Button sendMail = (Button)findViewById(R.id.SendMailInvite); 
		sendMail.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View view) { 
				EditText reciever = (EditText)findViewById(R.id.emailAdresseInvite);
				group.invite(group, reciever.getText().toString(), AddMemberEmail.this);
				Toast.makeText(AddMemberEmail.this, "Sending invite...", Toast.LENGTH_LONG).show();
				finish();
			} 
		}); 

		Button startListening = (Button)findViewById(R.id.ListenInvite); 
		startListening.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View view) { 
				MailAccount mailAccount = FileAccess.getMailAccountFromFile(AddMemberEmail.this);
				Intent intent = new Intent(AddMemberEmail.this, InviteListener.class);
				intent.putExtra("com.groupe.types.MailAccount", mailAccount);
				
				startService(intent);
			}
		});

		Button stopListening = (Button)findViewById(R.id.StopListenInvite); 
		stopListening.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View view) { 
				stopService(new Intent(AddMemberEmail.this,InviteListener.class));
			}
		});
	} 
}	