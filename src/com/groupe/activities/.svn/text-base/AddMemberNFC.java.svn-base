/**
 * 
 */
package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.mail.EmailParser;
import com.groupe.nfc.TagParser;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class AddMemberNFC extends Activity {
	NfcAdapter myNfc;
	PendingIntent nfcPendingIntent;
	IntentFilter[] exchangeFilters;
	Group group = new Group();
	Course course = new Course();
	boolean groupInCourse = false;
	
	ArrayList<Person> members = new ArrayList<Person>();

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		Bundle bundle = getIntent().getExtras();

		myNfc = NfcAdapter.getDefaultAdapter(this);
		if ( myNfc == null ){
			Toast.makeText(this, "No NFC Adapter found!", Toast.LENGTH_SHORT).show();
			finish();
		}
		group = bundle.getParcelable("com.groupe.types.Group");
		course = DBAccess.getCourse(group, this);
		if (course != null){
			groupInCourse = true;
		}
		setContentView(R.layout.buildgroupnfc);
		nfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) { }
		exchangeFilters = new IntentFilter[] { ndef };

	}

	@Override
	protected void onResume() {
		super.onResume();
		enableNdefExchange();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableNdefExchange();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = getNdefMessages(intent);

			final String incoming = new String(msgs[0].getRecords()[0].getPayload());
			String type = EmailParser.parseForType(incoming);
			if (type.equals("Group")){
				AlertDialog.Builder alertbox = new AlertDialog.Builder(AddMemberNFC.this);
				alertbox.setMessage("Merge Groups?");
				alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {							
						final Group groupnfc = EmailParser.parseForGroup(incoming);
						final Course coursenfc = TagParser.parseForCourse(incoming);
						if(!groupnfc.getCourse().equals(group.getCourse()) && !group.getCourse().equals("") && !groupnfc.getCourse().equals("")){
							Toast.makeText(AddMemberNFC.this, "Groups aren't in the same Course", Toast.LENGTH_SHORT).show();
						}else{
							members.clear();
							members = EmailParser.parseForPerson(incoming);
							group.mergeGroupsNfc(group, groupnfc, members, coursenfc, AddMemberNFC.this);
						}
						
						finish();
					}
				});

				alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {   
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
				alertbox.show();
				
			}else if(type.equals("Person")){
				members.clear();
				members = EmailParser.parseForPerson(incoming);
				for (int i = 0; i < members.size(); i++){
					group.addMember(group, members.get(i), AddMemberNFC.this);
					
				}
				Toast.makeText(AddMemberNFC.this, "New Member added! ", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void enableNdefExchange() {
		myNfc.enableForegroundDispatch(AddMemberNFC.this, nfcPendingIntent, exchangeFilters, null);
		myNfc.enableForegroundNdefPush(AddMemberNFC.this, generateNdef());
	}

	private void disableNdefExchange() {
		myNfc.disableForegroundNdefPush(AddMemberNFC.this);
		myNfc.disableForegroundDispatch(AddMemberNFC.this);
	}

	protected NdefMessage generateNdef() {
		String ndef;
		if (groupInCourse){
			ndef = group.generateInformationWithCourse(group, course, AddMemberNFC.this);
		}else{
			ndef = group.generateInformation(group, AddMemberNFC.this);
		}
		byte[] textBytes = ndef.getBytes();
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
				new byte[] {}, textBytes);
		return new NdefMessage(new NdefRecord[] {
				ndefRecord
		});
	}
	NdefMessage[] getNdefMessages(Intent intent) {
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] {
						record
				});
				msgs = new NdefMessage[] {
						msg
				};
			}
		} else {
			finish();
		}
		return msgs;
	}
}
