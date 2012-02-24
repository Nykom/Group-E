package com.groupe.activities;

import com.groupe.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @author ontl
 */
public class ConfigMain extends TabActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configmain);
		
		TabHost tabHost = getTabHost();  
		TabHost.TabSpec spec;  

		spec = tabHost.newTabSpec("User").setIndicator("User").setContent(new Intent(this, ConfigUser.class));
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("Email").setIndicator("Email").setContent(new Intent(this, ConfigEmail.class));
		tabHost.addTab(spec);		
	}
}
