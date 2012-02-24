package com.groupe.types;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.groupe.config.FileAccess;
import com.groupe.mail.MailTransport;
import com.groupe.persistence.DBAccess;


/**
 * @author ontl
 *
 */
public class Group implements Parcelable {
	private String grpName;
	private String course;
	private String information;
	private String head;
	private String hashId;



	public String calculateHash() {
		Calendar cal = Calendar.getInstance();
//		Date date = cal.getTime();
		String calc = cal.toString().concat(this.grpName);

		int value;
		String ret;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(calc.getBytes());
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

	public void invite(Group group, String reciever, Context context) {

		Mail mail = new Mail();
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);

		ArrayList<String> invitePerson = new ArrayList<String>();
		invitePerson.add(reciever);
		mail.setReciever(invitePerson);

		mail.setSubject("Group E invite to "  + group.getGrpName());
		
		Course course = DBAccess.getCourse(group, context);
		String body = "";
		if (course != null){
			body = generateInformationWithCourse(group, course,context);
		}else{
			body = generateInformation(group, context);
		}
		mail.setBody(body);

		try {
			MailTransport.send(mailAccount, mail, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the head
	 */
	public String getHead() {		
		return head;
	}

	public String getHashId(){
		return hashId;
	}

	public void setHashId(String hash){
		this.hashId = hash;
	}
	/**
	 * @param head the head to set
	 */
	public void setHead(String head) {
		this.head = head;
	}

	public String generateInformation(Group group, Context context) {

		String body = "<?xml version='1.0'?>" + "\n";
		body += "<Type>" + "Group" + "\n";
		body += "<Veranstaltung>" + group.getCourse() + "\n";
		body += "  <Gruppe>" + group.getGrpName() + "\n";
		body += "  <Information>" + group.getInformation() + "\n";
		body += "  <Head>" + group.getHead() + "\n";
		if (group.getHashId() == null){
			body += "   <GrpHash>" + group.calculateHash() + "\n";
		}else{
			body += "   <GrpHash>" + group.getHashId() + "\n";
		}
		for(int i = 0; i < group.getMembers(group,context).size();i++) {
			body += "    <Member>" + "\n";
			body += "      <Name>" + group.getMembers(group,context).get(i).getName() + "</Name>" + "\n";
			body += "      <Email>" + group.getMembers(group,context).get(i).getMailAddress().toString() + "</Email>" + "\n";
			body += "      <Telefon>" + group.getMembers(group,context).get(i).getPhoneNumber() + "</Telefon>" + "\n";
			body += "      <HashId>" + group.getMembers(group,context).get(i).getHashId() + "</HashId>" + "\n";
			body += "    </Member>" + "\n";
		}
		body += "   </GrpHash>" + "\n";
		body += "  </Head>" + "\n";
		body += "  </Information>" + "\n";
		body += "  </Gruppe>" + "\n";
		body += "</Veranstaltung>" + "\n";
		body += "</Type>" + "\n";

		return body;
	}

	public String generateInformationWithCourse(Group group, Course course, Context context) {

		String body = "<?xml version='1.0'?>" + "\n";
		body += "<Type>" + "Group" + "\n";
		body += "<Veranstaltung>" + group.getCourse() + "\n";
		body += "  <Kursname>" + course.getCourseName() + "\n";
		body += "  <VAK>" + course.getCourseVAK() + "\n";
		body += "  <LecturerName>" + course.getLecturerName() + "\n";
		body += "  <LecturerMail>" + course.getLecturerMail() + "\n";

		body += "  </LecturerMail>" + "\n";
		body += "  </LecturerName>" + "\n";
		body += "  </VAK>" + "\n";
		body += "  </Kursname>";
		body += "  <Gruppe>" + group.getGrpName() + "\n";
		body += "  <Information>" + group.getInformation() + "\n";
		body += "  <Head>" + group.getHead() + "\n";
		if (group.getHashId() == null){
			body += "   <GrpHash>" + group.calculateHash() + "\n";
		}else{
			body += "   <GrpHash>" + group.getHashId() + "\n";
		}
		for(int i = 0; i < group.getMembers(group,context).size();i++) {
			body += "    <Member>" + "\n";
			body += "      <Name>" + group.getMembers(group,context).get(i).getName() + "</Name>" + "\n";
			body += "      <Email>" + group.getMembers(group,context).get(i).getMailAddress().toString() + "</Email>" + "\n";
			body += "      <Telefon>" + group.getMembers(group,context).get(i).getPhoneNumber() + "</Telefon>" + "\n";
			body += "      <HashId>" + group.getMembers(group,context).get(i).getHashId() + "</HashId>" + "\n";
			body += "    </Member>" + "\n";
		}
		body += "   </GrpHash>" + "\n";
		body += "  </Head>" + "\n";
		body += "  </Information>" + "\n";
		body += "  </Gruppe>" + "\n";
		body += "</Veranstaltung>" + "\n";
		body += "</Type>" + "\n";

		return body;
	}

	public void sendStatus(Group group, Context context) {
		Mail mail = new Mail();
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);

		ArrayList<Person> email = group.getMembers(group, context);
		ArrayList<String> reciever = new ArrayList<String>();
		for(int i = 0;i < email.size();i++) {
			if(!email.get(i).getMailAddress().equals(group.getHead())) {
				reciever.add(email.get(i).getMailAddress());
			}
		}

		mail.setReciever(reciever);

		mail.setSubject("Group E status message for "  + group.getGrpName());
		mail.setBody(group.generateInformation(group,context));

		try {
			MailTransport.send(mailAccount, mail, context);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void informHead(Group group, Context context) {
		Mail mail = new Mail();
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);

		ArrayList<String> reciever = new ArrayList<String>();
		reciever.add(group.getHead());

		mail.setReciever(reciever);

		mail.setSubject("Group E status message for "  + group.getGrpName());
		mail.setBody(group.generateInformation(group,context));

		try {
			MailTransport.send(mailAccount, mail, context);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void updateGroupInformation(Group group, Context context) {
		Mail mail = new Mail();
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);

		ArrayList<String> reciever = new ArrayList<String>();

		reciever.add(group.getHead());

		mail.setReciever(reciever);
		mail.setSubject("Group E new Member for " + group.getGrpName());
		mail.setBody(group.generateInformation(group,context));
		try {
			MailTransport.send(mailAccount,mail, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Group() {
	}
	/**
	 * 
	 * @param grpName
	 * @param course
	 * @param information
	 */
	public Group(String grpName, String course, String information, String head){
		this.grpName 	 = grpName;
		this.course  	 = course;
		this.information = information;
		this.head = head;

	}

	public Group(String grpName, String course, String information, String head, String hashId){
		this.grpName 	 = grpName;
		this.course  	 = course;
		this.information = information;
		this.head = head;
		this.hashId = hashId;

	}

	public String getGrpName() {
		return grpName;
	}
	public void setGrpName(String grpName) {
		this.grpName = grpName;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getCourseName(Context context){
		String courseName = "";
		ArrayList<Course> courses = DBAccess.getAllCourses(context);


		for (int i = 0; i < courses.size(); i++){

			if(courses.get(i).getCourseVAK().equals(getCourse())){
				courseName = courses.get(i).getCourseName();

			}
		}
		return courseName;
	}
	public void addMember(Group group, Person person, Context context) {
		if (!person.checkPersonInGroup(person, group, context)){
			DBAccess.insertPerson(group, person, context);
		}
	}

	public ArrayList<Person> getMembers(Group group, Context context) {
		ArrayList<Person> allPersons = DBAccess.getAllPersons(context);
		ArrayList<Person> members = new ArrayList<Person>();
		long id = DBAccess.getGroupId(group, context);
		String idString = new Long(id).toString();
		for(int i = 0; i < allPersons.size();i++) {
			
			String mog = allPersons.get(i).getMemberOfGroup();

			String[] grpIds = mog.split("\\,");
			for(int k = 0;k < grpIds.length;k++) {
				if(grpIds[k].equals(idString)) {
					members.add(allPersons.get(i));
				}
			}
		}
		return members;
	}

	public int getMemberCount(Group group, Context context){
		return getMembers(group,context).size();
	}

	public void mergeGroupsNfc(Group groupOne, Group groupTwo, ArrayList<Person> members, Course course, Context context){
		int cmphash = groupOne.getHashId().compareTo(groupTwo.getHashId());
		long idOne = DBAccess.getGroupId(groupOne, context);
		if (cmphash < 0){
			DBAccess.saveGroup(groupTwo, context);
			ArrayList<Person> members2 = groupOne.getMembers(groupOne, context);
			for (int i = 0; i < members.size(); i++){
				groupTwo.addMember(groupTwo, members.get(i), context);
			}
			for (int ii = 0; ii < members2.size(); ii++){
				groupTwo.addMember(groupTwo, members2.get(ii), context);
			}
			for(int iii = 0;iii < members2.size();iii++) {
				members2.get(iii).removeMemberOfGroup(members2.get(iii), new Long(idOne).toString(), context);
			}
			if (course.getCourseName()!= null){
				if(!DBAccess.checkExistingCourse(course, context)){
					DBAccess.saveCourseWithoutNewGroup(course, context, groupTwo.getGrpName());
				}
			}
			DBAccess.deleteGroup(groupOne, context);
		}else if (cmphash >= 0){
			for (int i = 0; i < members.size(); i++){
				groupOne.addMember(groupOne, members.get(i), context);
			}
			if (course.getCourseName()!= null){
				if(!DBAccess.checkExistingCourse(course, context)){
					DBAccess.saveCourseWithoutNewGroup(course, context, groupTwo.getGrpName());
				}
			}
		}
	}

	
	@Override
	public int describeContents() {
		return 0;
	}
	public Group(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(grpName);
		dest.writeString(course);
		dest.writeString(information);
		dest.writeString(head);
		dest.writeString(hashId);
	}

	/**
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		grpName = in.readString();
		course = in.readString();
		information = in.readString();
		head = in.readString();
		hashId = in.readString();

	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel in) {
			return new Group(in);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};
}
