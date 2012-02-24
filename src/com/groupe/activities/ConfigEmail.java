package com.groupe.activities;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.types.MailAccount;

/**
 * @author ontl
 *
 */
public class ConfigEmail extends Activity implements SeekBar.OnSeekBarChangeListener{
	
	static String configEmailFile = "groupeConfigEmail";
	Spinner spinner1;
	SeekBar mSeekBar;
	TextView mProgressText;
    TextView mTrackingText;
	
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.configappemail);
		
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(this);
		LinkedList<String> settings = FileAccess.getMailSettings(this);
		
		
		EditText mailAddress = (EditText)findViewById(R.id.editEmail);
		EditText mailPass = (EditText)findViewById(R.id.Pass);
		
		mailAddress.setText(mailAccount.getUser());
		mailPass.setText(mailAccount.getPassword());
		
		CheckBox access = (CheckBox)findViewById(R.id.enableMail);
		CheckBox wifi   = (CheckBox)findViewById(R.id.WifiOnly);
		
		if(settings.size() > 0) {
			access.setChecked(Boolean.parseBoolean(settings.get(0)));
			wifi.setChecked(Boolean.parseBoolean(settings.get(1)));
		}
		
		mSeekBar = (SeekBar)findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mProgressText = (TextView)findViewById(R.id.progress);
        mProgressText.setText(mSeekBar.getProgress() + " hours.");
        if(settings.size() > 0) {
        	mSeekBar.setProgress(Integer.parseInt(settings.get(2)));
        }
		Spinner s = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
        		this, R.array.provider_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
		
        final Button updateEmail = (Button)findViewById(R.id.upateEmail);
        updateEmail.setOnClickListener(new View.OnClickListener() {

        	@Override
			public void onClick(View v) {
        		
        		EditText mail = (EditText)findViewById(R.id.editEmail);
        		Spinner provider = (Spinner)findViewById(R.id.spinner1);
        		EditText password = (EditText)findViewById(R.id.Pass);
        		
        		CheckBox enableMail = (CheckBox)findViewById(R.id.enableMail);  
        		boolean enabled = false;
        		
        		CheckBox wifiOnly = (CheckBox)findViewById(R.id.WifiOnly);  
        		boolean wifi = false;
        		
        		if(enableMail.isChecked()) 
        			enabled = true;
        		
        		if(wifiOnly.isChecked()) 
        			wifi = true;
        		
        		SeekBar interval = (SeekBar)findViewById(R.id.seek_bar);
        		
        		String mailprovider = "";
        		if(provider.getSelectedItem().toString().equalsIgnoreCase("googlemail")){
        			mailprovider += "smtp.googlemail.com" + "\n" 
        							+ "465" + "\n" + 
        							"imap.googlemail.com" + "\n" 
        							+ "993";
        		}else{
        			mailprovider += "-empty-" + "\n" +
        							"-empty-" + "\n" +
        							"-empty-" + "\n" + 
        							"-empty-";
        		}
        		
        		
        		
        		String emailSettings = mail.getText().toString() + "\n" + 
        								mailprovider + "\n" +
        								password.getText().toString() + "\n" + 
        								enabled + "\n" + 
        								wifi + "\n" +
        								interval.getProgress();
        		
        		
        		
        		FileAccess.writeFile(emailSettings, configEmailFile, ConfigEmail.this);
        		finish();
        	}             
        });
	}
    @Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        mProgressText.setText(seekBar.getProgress() + " hours.");
    }

    @Override
	public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
	public void onStopTrackingTouch(SeekBar seekBar) {
    }
}