package com.groupe.persistence;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.groupe.config.FileAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;


/**
 * @author ontl
 */
public class DBAccess {

	public static long getPersonId(Person person, Context context) {
		DBAdapter db = new DBAdapter(context);
		db.open();	
		Cursor cursor = db.getAllPersons();
		if (cursor.moveToFirst()) {
			do { 
				if(person.getHashId().equals(cursor.getString(5))) {
					db.close();
					return cursor.getLong(0);
				}				
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return -1;
	}

	public static void updatePerson(Person person, Context context) {
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.updatePerson(person);
		db.close();
	}

	public static void deletePerson(long personId, Context context) {
		DBAdapter db = new DBAdapter(context);

		db.open();
		db.deletePerson(personId);
		db.close();
	}

	public static ArrayList<Person> getAllPersons(Context context) {
		ArrayList<Person> members = new ArrayList<Person>();

		DBAdapter db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllPersons();

		if (cursor.moveToFirst()) {
			do { 
				Person person = new Person(cursor.getLong(0),cursor.getString(1),
						cursor.getString(2),cursor.getString(3),cursor.getString(4),
						cursor.getString(5));
				members.add(person);
			} while (cursor.moveToNext());
		}
		db.close();
		return members;
	}


	public static ArrayList<Group> getAllGroups(Context context) {

		DBAdapter db = new DBAdapter(context);
		ArrayList<Group> groups = new ArrayList<Group>();
		db.open();

		Cursor cursor = db.getAllGroups();

		if(cursor.getCount() == 0) {
		}else{ 		
			if (cursor.moveToFirst()) {
				do {
					Group group = new Group(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
					group.setHashId(cursor.getString(5));
					groups.add(group);

				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		db.close();
		return groups;
	}

	public static void updateGroup(Long grpId, Group group, Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.updateGroup(grpId, group.getGrpName(), group.getCourse(),group.getInformation(),group.getHead());
		db.close();
	}
	public static void updateCourse(Long courseId, Course course, Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.updateCourse(courseId, course);
		db.close();
	}

	public static void saveCourseGroups(Group group, Context context) {
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.insertGroup(group);
		db.close();
	}

	public static void saveGroup(Group group, Context context) {

		DBAdapter db = new DBAdapter(context);
		Person person = FileAccess.getUserFromFile(context);

		if(new File("/data/data/com.groupe/files/groupeConfigEmail").exists()) {
			person.setHeadFunc(true);
		}

		if(group.getHashId() == null){
			group.setHashId(group.calculateHash());
		}
		db.open();
		db.insertGroup(group);

		db.close();

		if(person.getHashId() == null){
			person.setHashId(person.getMailAddress());
		}
		if(!person.checkPersonInGroup(person, group, context)){
			DBAccess.insertPerson(group, person, context);
		}
		Toast.makeText(context, "Group created", Toast.LENGTH_LONG).show();

	}

	public static long getGroupId(Group group, Context context) {
		long ret;
		DBAdapter db = new DBAdapter(context);
		db.open();	
		Cursor cursor = db.getAllGroups();
		if (cursor.moveToFirst()) {
			do { 

				if(group.getHashId().equals(cursor.getString(5))) {
					ret = cursor.getLong(0);

					cursor.close();
					db.close();
					return ret;					
				}				
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return -1;
	}

	public static Boolean checkExistingGroup(Group group, Context context){
		Boolean check = false;
		DBAdapter db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllGroups();

		if (cursor.moveToFirst()) {
			do { 
				if(group.getHashId().equals(cursor.getString(5))) {
					check = true;
				}				

			} while (cursor.moveToNext());
		}
		db.close();
		return check;
	}

	public static void deleteGroup(Group group, Context context) {
		ArrayList<Person> members = group.getMembers(group,context);
		for(int i = 0;i < members.size();i++) {
			members.get(i).removeMemberOfGroup(members.get(i), new Long(getGroupId(group, context)).toString(), context);
		}
		long id = getGroupId(group, context);
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.deleteGroup(id);
		db.close();
	}


	public static ArrayList<Group> getGroups(Course course, Context context){
		ArrayList<Group> groups = new ArrayList<Group>();
		DBAdapter db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllGroups();
		if (cursor.moveToFirst()) {
			do { 
				if(course.getCourseVAK().equals(cursor.getString(2))) {
					groups.add(new Group(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4), cursor.getString(5)));
				}

			} while (cursor.moveToNext());
		}
		db.close();
		return groups;
	}

	public static ArrayList<Course> getAllCourses(Context context) {
		ArrayList<Course> courses = new ArrayList<Course>();

		DBAdapter db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllCourses();

		if (cursor.moveToFirst()) {
			do { 
				Course course = new Course(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
				courses.add(course);
			} while (cursor.moveToNext());
		}
		db.close();
		return courses;
	}

	public static void deleteCourse(Long courseID, Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.deleteCourse(courseID);
		db.close();
	}

	public static long getCourseId(Course course, Context context) {
		DBAdapter db = new DBAdapter(context);
		long id = -1;
		db.open();
		Cursor cursor = db.getAllCourses();
		if (cursor.moveToFirst()) {
			do { 
				if(course.getCourseVAK().equals(cursor.getString(2))) {
					id = cursor.getLong(0);
				}				
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return id;
	}

	public static void saveCourse(Course course, Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.insertCourse(course);
		db.close();		
	}

	public static Course getCourse(Group group, Context context){
		DBAdapter db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllCourses();

		if (cursor.moveToFirst()) {
			do {
				if (cursor.getString(2).equals(group.getCourse())){
					Course course = new Course(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
					db.close();
					return course;
				}
			} while (cursor.moveToNext());
		}
		db.close();
		return null;
	}

	public static void saveCourseWithoutNewGroup(Course course, Context context, String groupName){
		DBAdapter db = new DBAdapter(context);
		//		Person person = FileAccess.getUserFromFile(context);

		ArrayList<Group> groups = DBAccess.getAllGroups(context);

		Group newGroup = new Group();

		for(int i = 0;i < groups.size();i++) {
			if(groups.get(i).getGrpName().equals(groupName)) {
				groups.get(i).setCourse(course.getCourseName());
				newGroup.setInformation(course.getCourseName());
				newGroup.setCourse(course.getCourseVAK());
				newGroup.setGrpName(groups.get(i).getGrpName());
				newGroup.setHashId(groups.get(i).getHashId());
				newGroup.setHead(groups.get(i).getHead());
			}
		}

		long id = getGroupId(newGroup,context);

		db.open();
		db.insertCourse(course);
		db.updateGroup(id,newGroup.getGrpName(),newGroup.getCourse(),newGroup.getInformation(),newGroup.getHead());
		db.close();		
	}

	public static Boolean checkExistingCourse(Course course, Context context){
		DBAdapter db;
		Boolean check = false;
		db = new DBAdapter(context);
		db.open();
		Cursor cursor = db.getAllCourses();

		if (cursor.moveToFirst()) {
			do { 
				if(course.getCourseName().equals(cursor.getString(1))) {
					check = true;
				}				
			} while (cursor.moveToNext());
		}
		db.close();
		return check;
	}

	public static void createData(Group group, ArrayList<Person> persons, Context context) {

		Person person = FileAccess.getUserFromFile(context);
		person.setHashId(person.calculateHash(person.getMailAddress()));

		persons.add(person);

		group.getMembers(group, context).add(person);
		DBAdapter db = new DBAdapter(context);
		db.open();

		long grpId = db.insertGroup(group);
		String idString	= (new Long(grpId)).toString();

		for(int k = 0;k < persons.size();k++) {
			db.insertPerson(persons.get(k), idString);
		}		
		db.close();
	}

	public static void insertPerson(Group group, Person person, Context context) {
		ArrayList<Person> allPersons = getAllPersons(context);
		String id = new Long(getGroupId(group, context)).toString();

		Person user = FileAccess.getUserFromFile(context);

		if(person.getName().equals(user.getName())&& new File("/data/data/com.groupe/files/groupeConfigEmail").exists()) {
			person.setHeadFunc(true);
		}
		if(allPersons.size() == 0){
			DBAdapter db = new DBAdapter(context);
			db.open();

			db.insertPerson(person, id);
			db.close();
		}else{
			for(int i = 0;i < allPersons.size();i++) {
				if(allPersons.get(i).getName().equals(person.getName()) &&
						allPersons.get(i).getMailAddress().equals(person.getMailAddress()) &&
						allPersons.get(i).getPhoneNumber().equals(person.getPhoneNumber())) {

					person.addMemberOfGroup(person, id, context);
					break;
				}
				if(i == allPersons.size()-1){
					DBAdapter db = new DBAdapter(context);
					db.open();
					db.insertPerson(person, id);
					db.close();
					break;
				}
			}
		}
	}
}
