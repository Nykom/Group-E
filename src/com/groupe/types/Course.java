package com.groupe.types;

import java.util.ArrayList;

import com.groupe.config.FileAccess;
import com.groupe.mail.MailTransport;
import com.groupe.persistence.DBAccess;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ontl
 */
public class Course implements Parcelable {

	private String courseName;
	private String courseVAK;

	private String lecturerName;
	private String lecturerMail;

	public Course(String courseName, String courseVAK) {
		this.courseName = courseName;
		this.courseVAK = courseVAK;
	}

	public Course(String courseName, String courseVAK, String lecturerName, String lecturerMail) {
		this.courseName = courseName;
		this.courseVAK = courseVAK;
		this.lecturerName = lecturerName;
		this.lecturerMail = lecturerMail;
	}

	public String generateInformation(Course course, Context context) {

		String body = "  <Kursname>" + course.getCourseName() + "\n";
		body += "  <VAK>" + course.getCourseVAK() + "\n";
		body += "  <LecturerName>" + course.getLecturerName() + "\n";
		body += "  <LecturerMail>" + course.getLecturerMail() + "\n";

		body += "  </LecturerMail>" + "\n";
		body += "  </LecturerName>" + "\n";
		body += "  </VAK>" + "\n";
		body += "</Kursname>";

		return body;
	}

	public String generateSyncInformation(Course course, ArrayList<Group> groups, Context context) {
		ArrayList<Person> members = new ArrayList <Person>();
		ArrayList<Group> grps = groups;

		String body = "<?xml version='1.0'?>" + "\n";
		body += "  <Kursname>" + course.getCourseName() + "\n";
		body += "  <VAK>" + course.getCourseVAK() + "\n";
		body += "  <LecturerName>" + course.getLecturerName() + "\n";
		body += "  <LecturerMail>" + course.getLecturerMail() + "\n";
		for(int j = 0; j < grps.size(); j++){
			body += "<Veranstaltung>" + grps.get(j).getCourse() + "\n";
			body += "  <Gruppe>" + grps.get(j).getGrpName() + "\n";
			body += "  <Information>" + grps.get(j).getInformation() + "\n";
			body += "  <Head>" + grps.get(j).getHead() + "\n";
			if (grps.get(j).getHashId() == null){
				body += "   <GrpHash>" + grps.get(j).calculateHash() + "\n";
			}else{
				body += "   <GrpHash>" + grps.get(j).getHashId() + "\n";
			}
			members = grps.get(j).getMembers(grps.get(j), context);
			for(int i = 0; i < members.size();i++) {
				body += "    <Member>" + "\n";
				body += "      <Name>" + members.get(i).getName() + "</Name>" + "\n";
				body += "      <Email>" + members.get(i).getMailAddress().toString() + "</Email>" + "\n";
				body += "      <Telefon>" + members.get(i).getPhoneNumber() + "</Telefon>" + "\n";
				body += "      <HashId>" + members.get(i).getHashId() + "</HashId>" + "\n";
				body += "      <Group>" + grps.get(j).getHashId() + "</Group>" + "\n";	
				body += "    </Member>" + "\n";
			}
			members.clear();
			body += "   </GrpHash>" + "\n";
			body += "  </Head>" + "\n";
			body += "  </Information>" + "\n";
			body += "  </Gruppe>" + "\n";
			body += "</Veranstaltung>";
		}
		body += "  </LecturerMail>" + "\n";
		body += "  </LecturerName>" + "\n";
		body += "  </VAK>" + "\n";
		body += "</Kursname>";

		return body;
	}


	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	/**
	 * @return the courseInformation
	 */
	public String getCourseVAK() {
		return courseVAK;
	}

	/**
	 * @param courseInformation the courseInformation to set
	 */
	public void setCourseVAK(String courseVAK) {
		this.courseVAK = courseVAK;
	}

	public void syncCourse(Course course, Context context){
		Mail mail = new Mail();
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);
		ArrayList<String> syncReceiver = new ArrayList<String>();
		ArrayList<Group> groups = DBAccess.getGroups(course, context);
		for (int i = 0; i < groups.size(); i++){
			syncReceiver.add(groups.get(i).getHead());
		}
		mail.setReciever(syncReceiver);
		mail.setSubject("Group E course groups to Course " + course.getCourseName());
		mail.setBody(course.generateSyncInformation(course, groups, context));
		try {
			MailTransport.send(mailAccount, mail, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(courseName);
		dest.writeString(courseVAK);
		dest.writeString(lecturerName);
		dest.writeString(lecturerMail);
	}

	/**
	 * @param lecturerName the lecturerName to set
	 */
	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	/**
	 * @return the lecturerName
	 */
	public String getLecturerName() {
		return lecturerName;
	}

	/**
	 * @param lecturerMail the lecturerMail to set
	 */
	public void setLecturerMail(String lecturerMail) {
		this.lecturerMail = lecturerMail;
	}

	/**
	 * @return the lecturerMail
	 */
	public String getLecturerMail() {
		return lecturerMail;
	}
	public Course(Parcel in) {
		readFromParcel(in);
	}

	public Course() {
	}


	private void readFromParcel(Parcel in) {
		courseName = in.readString();
		courseVAK = in.readString();
		lecturerName = in.readString();
		lecturerMail = in.readString();
	}

	public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
		@Override
		public Course createFromParcel(Parcel in) {
			return new Course(in);
		}

		@Override
		public Course[] newArray(int size) {
			return new Course[size];
		}
	};

}
