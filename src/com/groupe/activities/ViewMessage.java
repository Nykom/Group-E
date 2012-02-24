package com.groupe.activities;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Mail;

/**
 * @author ontl
 *
 */
public class ViewMessage extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewmessage); 
		
		Bundle extras = getIntent().getExtras();
		final Mail mail = extras.getParcelable("com.groupe.types.Mail");
		
		TextView sender = (TextView)findViewById(R.id.textView2);
		sender.setText("From: " + mail.getSender().toString());
		
		TextView body = (TextView)findViewById(R.id.textView1);
		body.setText("Text: " +"\n" + mail.getBody().toString());
		
		
		Button reply = (Button)findViewById(R.id.reply);
		reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String groupName = mail.getSubject();
				 
				String pattern = "message from ";
				 
				Pattern p = Pattern.compile(pattern);
				String[] strData = p.split(groupName, 0 );
				 
				ArrayList<Group> groups = DBAccess.getAllGroups(ViewMessage.this);

				Group group = new Group();
				
				for(int i = 0;i < groups.size();i++) {
					if(strData[1].equals(groups.get(i).getGrpName()));
					group = groups.get(i);
				}
								
				Intent intent = new Intent(ViewMessage.this, WriteMessage.class);
				intent.putExtra("com.groupe.types.Mail", mail);	
				intent.putExtra("com.groupe.types.Group", group);		
				startActivity(intent);
			}             
		});
		
		Button close = (Button)findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				finish();
			}             
		});
	}	
}
