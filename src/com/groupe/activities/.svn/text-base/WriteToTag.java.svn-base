package com.groupe.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.types.Course;

/**
 * @author ontl
 *
 */
public class WriteToTag extends Activity{
	
	Course course = new Course();
	NfcAdapter myNfc;
	IntentFilter [] writeToTagFilters;
	PendingIntent nfcPendingIntent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writetotag);
		Bundle bundle = getIntent().getExtras();
		course = bundle.getParcelable("com.groupe.types.Course");
		myNfc = NfcAdapter.getDefaultAdapter(this);
		if ( myNfc == null ){
			Toast.makeText(this, "No NFC Adapter found!", Toast.LENGTH_SHORT).show();
			finish();
		}
		nfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeToTagFilters = new IntentFilter[] {
            tagDetected
        };
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        myNfc.enableForegroundDispatch(this, nfcPendingIntent, writeToTagFilters, null);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		myNfc.disableForegroundDispatch(this);
	}
	@Override
	protected void onNewIntent(Intent intent){
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(generateNdef(), detectedTag);
        }
	}
	
	protected NdefMessage generateNdef() {
		String ndef = course.generateInformation(course, WriteToTag.this);
		byte[] textBytes = ndef.getBytes();
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
				new byte[] {}, textBytes);
		return new NdefMessage(new NdefRecord[] {
				ndefRecord
		});
	}
	
	void writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndefTag = Ndef.get(tag);
            if (ndefTag != null) {
            	ndefTag.connect();

                if (!ndefTag.isWritable()) {
                	Toast.makeText(this, "Tag is read-only.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ndefTag.getMaxSize() < size) {
                    Toast.makeText(this, "Tag capacity is to low.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ndefTag.writeNdefMessage(message);
                Toast.makeText(this, "Wrote course to Tag", Toast.LENGTH_SHORT).show();
                return;
            } else {
                NdefFormatable ndefFormat = NdefFormatable.get(tag);
                if (ndefFormat != null) {
                    try {
                    	ndefFormat.connect();
                    	ndefFormat.format(message);
                    	Toast.makeText(this, "Formated Tag and wrote course to Tag", Toast.LENGTH_SHORT).show();
                        return;
                    } catch (IOException e) {
                    	Toast.makeText(this, "Format failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                	Toast.makeText(this, "Tag does not support NDef", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
        	Toast.makeText(this, "Failed to write course to Tag", Toast.LENGTH_SHORT).show();
        }

        return;
    }
}
