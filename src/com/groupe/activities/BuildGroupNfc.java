package com.groupe.activities;

import java.math.BigInteger;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
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
import com.groupe.config.FileAccess;
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
public class BuildGroupNfc extends Activity{
	NfcAdapter myNfc;
	PendingIntent nfcPendingIntent;
	IntentFilter[] exchangeFilters;
	Person person = new Person();


	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		person = FileAccess.getUserFromFile(this);
		myNfc = NfcAdapter.getDefaultAdapter(this);
		if ( myNfc == null ){
			Toast.makeText(this, "No NFC Adapter found!", Toast.LENGTH_SHORT).show();
			finish();
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
		ArrayList<Person> members = new ArrayList<Person>();
		members.clear();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = getNdefMessages(intent);
			String incoming = "";
			incoming = new String(msgs[0].getRecords()[0].getPayload());
			// incoming String verarbeiten

			String type = EmailParser.parseForType(incoming);
			if (type.equals("Group")){
				Group groupnfc = EmailParser.parseForGroup(incoming);

				Course coursenfc = TagParser.parseForCourse(incoming);
				if(!DBAccess.checkExistingGroup(groupnfc, BuildGroupNfc.this)){
					DBAccess.saveGroup(groupnfc, BuildGroupNfc.this);
				}
				members.clear();
				members = EmailParser.parseForPerson(incoming);
				for (int i = 0; i < members.size(); i++){
					groupnfc.addMember(groupnfc, members.get(i), BuildGroupNfc.this);
				}

				if (coursenfc != null && !DBAccess.checkExistingCourse(coursenfc, BuildGroupNfc.this)){
					DBAccess.saveCourseWithoutNewGroup(coursenfc, BuildGroupNfc.this, groupnfc.getGrpName());
				}

			}else if(type.equals("Person")){
				members.clear();
				members = EmailParser.parseForPerson(incoming);
				int cmp = members.get(0).getHashId().compareTo(person.getHashId());
				BigInteger hash1 = (new BigInteger(members.get(0).getHashId(), 16)).add(new BigInteger(person.getHashId(), 16));
				BigInteger hash2 = (new BigInteger(members.get(0).calculateHash(members.get(0).getName()), 16)).add(new BigInteger(person.calculateHash(person.getName()), 16));
				hash1.add(hash2);
				String hash = hash1.toString(16);
				
				Group group = new Group();
				if (cmp < 0){
					group.setGrpName(person.getName()+"rename please!");
					group.setInformation(" ");
					group.setCourse(" ");
					group.setHead(person.getMailAddress());
					group.setHashId(hash);
				}else if(cmp > 0){
					group.setGrpName(members.get(0).getName()+"rename please!");
					group.setInformation(" ");
					group.setCourse(" ");
					group.setHead(members.get(0).getMailAddress());
					group.setHashId(hash);
				}
				while(DBAccess.checkExistingGroup(group, BuildGroupNfc.this)){
					hash = (new BigInteger(hash, 16)).add(new BigInteger(hash, 16)).toString(16);
					group.setHashId(hash);
				}
				if(!DBAccess.checkExistingGroup(group, BuildGroupNfc.this)){
					DBAccess.saveGroup(group, BuildGroupNfc.this);

					for (int i = 0; i < members.size(); i++){
						group.addMember(group, members.get(i), BuildGroupNfc.this);
					}

					ArrayList<Person> memberlog = group.getMembers(group, BuildGroupNfc.this);
					for (int j = 0; j < memberlog.size(); j++){
					}
				
				}
			}
		}
	}

	private void enableNdefExchange() {
		myNfc.enableForegroundDispatch(BuildGroupNfc.this, nfcPendingIntent, exchangeFilters, null);
		myNfc.enableForegroundNdefPush(BuildGroupNfc.this, generateNdef());
	}

	private void disableNdefExchange() {
		myNfc.disableForegroundNdefPush(BuildGroupNfc.this);
		myNfc.disableForegroundDispatch(BuildGroupNfc.this);
	}

	protected NdefMessage generateNdef() {
		String ndef = person.generateInformation(person, BuildGroupNfc.this);
		byte[] ndefBytes = ndef.getBytes();
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
				new byte[] {}, ndefBytes);
		return new NdefMessage(new NdefRecord[] {
				ndefRecord
		});
	}
	NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
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
