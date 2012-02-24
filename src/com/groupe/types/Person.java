package com.groupe.types;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.groupe.persistence.DBAccess;
import com.groupe.persistence.DBAdapter;

/**
 * @author ontl
 */
public class Person implements Parcelable {

	private long id;
	private String name;
	private String mailAddress;
	private String phoneNumber;
	private String hashId;
	private String memberOfGroup;
	private String syncGroup;
	private boolean headFunc;

	public boolean checkExistingPerson(Person person, Context context){
		ArrayList<Person> allPersons = DBAccess.getAllPersons(context); 
		for(int i = 0; i < allPersons.size();i++) {
			if(allPersons.get(i).getHashId().equals(person.getHashId())) {
				return true;
			}
		}
		return false;
	}

	
	
	public boolean checkPersonInGroup(Person person, Group group, Context context){
		boolean check = false;
		ArrayList<Person> members = group.getMembers(group, context);
					for(int j = 0; j < members.size(); j++){
						if(members.get(j).getHashId().equals(person.getHashId())){
							String[] mogPerson = members.get(j).getMemberOfGroup().split("\\,");
							for(int k = 0; k < mogPerson.length; k++){
								if(mogPerson[k].equals((new Long (DBAccess.getGroupId(group, context))).toString())){
									check = true;
								}
							}
			}
		}
		return check;
	}
	

	public String calculateHash(String mail) {
		int value;
		String ret;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(mail.getBytes());
			byte[] result = md5.digest();
			StringBuffer hexString = new StringBuffer(32);
			for (int i = 0; i < result.length; i++) {
				value = (result[i] & 0x7F) + (result[i] < 0 ? 128 : 0);
				ret = (value < 16 ? "0" : "");
				hexString.append(ret += Integer.toHexString(value));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String generateInformation(Person person, Context context) {

		String body = "<?xml version='1.0'?>" + "\n";
		body += " <Type>" + "Person" + "\n";
		body += "  <Name>" + person.getName() + "\n";
		body += "  <Email>" + person.getMailAddress() + "\n";
		body += "  <Telefon>" + person.getPhoneNumber() + "\n";
		body += "  <HashId>" + person.getHashId() + "\n";

		body += "  </HashId>" + "\n";
		body += "  </Telefon>" + "\n";
		body += "  </Email>" + "\n";
		body += "</Name>";
		body += "</Type>";

		return body;
	}


	public String acknowledge(Person person, Group group) {
		String ackValue = "<?xml version='1.0'?>" + "\n";
		ackValue += "<Veranstaltung>" + group.getCourse() + "\n";
		ackValue += "  <Gruppe>" + group.getGrpName() + "\n";
		ackValue += "  <Information>" + group.getInformation() + "\n";
		ackValue += "  <Head>" + group.getHead() + "\n";
		ackValue += "   <GrpHash>" + group.getHashId() + "\n";
		ackValue += "<Member>" + "\n";
		ackValue += "  <Name>" + person.getName() + "</Name>" + "\n";
		ackValue += "  <Email>" + person.getMailAddress().toString() + "</Email>" + "\n";
		ackValue += "  <Telefon>" + person.getPhoneNumber() + "</Telefon>" + "\n";
		ackValue += "  <HashId>" + person.getHashId() + "\n";
		ackValue += "  </HashId>" + "\n";
		ackValue += "</Member>";
		ackValue += "   </GrpHash>" + "\n";
		ackValue += "</Head>";
		ackValue += "  </Information>" + "\n";
		ackValue += "  </Gruppe>" + "\n";
		ackValue += "</Veranstaltung>";

		return ackValue;
	}

	public Person(){
	}

	public Person(String name, String mailAddress, String phoneNumber) {

		this.name = name;
		this.mailAddress = mailAddress;
		this.phoneNumber = phoneNumber;
	}
	public Person(String name, String mailAddress, String phoneNumber,String hashId) {
		this.name = name;
		this.mailAddress = mailAddress;
		this.phoneNumber = phoneNumber;
		this.hashId = hashId;
	}
	public Person(String name, String mailAddress, String phoneNumber,String hashId,String syncGroup) {
		this.name = name;
		this.mailAddress = mailAddress;
		this.phoneNumber = phoneNumber;
		this.hashId = hashId;
		this.syncGroup = syncGroup;
	}
	public Person(long id, String memberOfGroup, String name, String mailAddress, String phoneNumber, 
			String hashId) {
		this.id = id;
		this.name = name;
		this.mailAddress = mailAddress;
		this.phoneNumber = phoneNumber;
		this.hashId = hashId;
		this.memberOfGroup = memberOfGroup;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setHashId(){
		this.hashId = calculateHash(this.mailAddress);
	}

	/**
	 * @param hashId the hashId to set
	 */
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}
	/**
	 * @return the hashId
	 */
	public String getHashId() {
		return hashId;
	}

	/**
	 * @param headFunc the headFunc to set
	 */
	public void setHeadFunc(boolean headFunc) {
		this.headFunc = headFunc;
	}

	/**
	 * @return the headFunc
	 */
	public boolean isHeadFunc() {
		return headFunc;
	}



	public void addMemberOfGroup(Person person, String memberOfGroup, Context context) {
		long id = DBAccess.getPersonId(person, context);
		ArrayList<Person> allPersons = DBAccess.getAllPersons(context);
		String mog = "";
		for(int i = 0;i < allPersons.size();i++) {
			if(id == allPersons.get(i).getId()){
				mog = allPersons.get(i).getMemberOfGroup();
			}
		}
		String newMog = mog+","+memberOfGroup;
		person.setMemberOfGroup(newMog);
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.updateMemberOfGroup(id, newMog);

		db.close();
	}

	public void removeMemberOfGroup(Person person, String memberOfGroup, Context context) {
		long id = person.getId();
		String mogPerson = person.getMemberOfGroup();

		String[] grps = mogPerson.split("\\,");
		ArrayList<String> mogGrps = new ArrayList<String>();
		
		for(int i = 0;i < grps.length;i++) {
			if(!grps[i].equals("")) {
				mogGrps.add(grps[i]);
			}
		}
		
		String members = "";
		for(int i = 0;i < mogGrps.size();i++) {
			if(!mogGrps.get(i).equals(memberOfGroup)) {
				if(i < mogGrps.size()-1) {
					members += mogGrps.get(i)+",";
				}else{
					members += mogGrps.get(i);
				}
			}
		}
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.updateMemberOfGroup(id, members);
		db.close();
	}
	public Group getCourseGroup(Person person, Course course, Context context){
		ArrayList<Group> groups = DBAccess.getGroups(course, context);
		String[] mog = person.getMemberOfGroup().split("\\,");
		
		for(int i = 0;i < mog.length ;i++) {
			for(int j = 0;j < groups.size();j++){
				if(mog[i].equals((new Long(DBAccess.getGroupId(groups.get(j), context)).toString()))){
					return groups.get(j);
				}
			}
		}
		return null;
	}
	
	/**
	 * @param memberOfGroup the memberOfGroup to set
	 */
	public void setMemberOfGroup(String memberOfGroup) {
		this.memberOfGroup = memberOfGroup;
	}

	/**
	 * @return the memberOfGroup
	 */
	public String getMemberOfGroup() {
		return memberOfGroup;
	}
	public void setSyncGroup(String syncGroup) {
		this.syncGroup = syncGroup;
	}



	public String getSyncGroup() {
		return syncGroup;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	public Person(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(mailAddress.toString());
		dest.writeString(phoneNumber);
		dest.writeString(hashId);
		dest.writeString(memberOfGroup);
		dest.writeString(syncGroup);
	}

	/**
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		name = in.readString();
		mailAddress = in.readString();
		phoneNumber = in.readString();
		hashId = in.readString();
		memberOfGroup = in.readString();
		syncGroup = in.readString();
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		@Override
		public Person createFromParcel(Parcel in) {
			return new Person(in);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};
}
