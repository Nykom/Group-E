package com.groupe.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.groupe.R;
import com.groupe.types.Group;

/**
 * @author ontl
 *
 */
public class AddMember extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmember);

		Bundle bundle = getIntent().getExtras();
        final Group group = bundle.getParcelable("com.groupe.types.Group");
		
		TabHost tabHost = getTabHost();  
		TabHost.TabSpec spec;  

		Intent intent = new Intent(this, AddMemberManuel.class);
		intent.putExtra("com.groupe.types.Group", group);			
		
		spec = tabHost.newTabSpec("Manuell").setIndicator("Manuell").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, AddMemberEmail.class);
		intent.putExtra("com.groupe.types.Group", group);			
		spec = tabHost.newTabSpec("Email").setIndicator("Email").setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}