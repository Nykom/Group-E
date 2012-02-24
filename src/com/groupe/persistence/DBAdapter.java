package com.groupe.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 *
 */
public class DBAdapter {

	// Db und tables
	private static final String DATABASE_NAME = "group.db";
	private static final String TABLE_GROUPS  = "table_group";
	private static final String TABLE_PERSONS = "table_persons";
	private static final String TABLE_COURSE = "table_course";
	private static final int DATABASE_VERSION = 1;

	//Spalten TABLE_GROUPS
	public static final String KEY_GROUPID ="_idGroup";
	public static final String KEY_GROUPNAME = "groupname";
	public static final String KEY_COURSE = "course";
	public static final String KEY_INFO = "info";
	public static final String KEY_HEAD = "head";
	public static final String KEY_GROUPHASH = "grpHash";

	//Spalten TABLE_PERSONS
	public static final String KEY_PERSONID = "_idPerson";
	public static final String KEY_MEMBER_OF_GROUP = "member_of_group";
	public static final String KEY_PERSONNAME = "personname";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_TELEPHONE = "telephone";
	public static final String KEY_HASHID = "hashid";
	public static final String KEY_HEADFUNC = "headfunc";

	//Spalten TABLE_COURSE
	public static final String KEY_COURSEID = "_idCourse";
	public static final String KEY_COURSENAME = "coursename";
	public static final String KEY_COURSE_VAK = "course_vak";
	public static final String KEY_COURSE_LECTURERNAME = "courselecturername";
	public static final String KEY_COURSE_LECTUREREMAIL = "courselectureremail";

	//SQL-Statement create
	private static final String CREATE_GROUP_TABLE = 
			"create table " + TABLE_GROUPS +
			" (_idGroup integer primary key autoincrement, " +
			"groupname not null, course not null, info not null, " +
			"head not null, grpHash not null);";

	//SQL-Statement create
	private static final String CREATE_PERSON_TABLE = 
			"create table " + TABLE_PERSONS +
			" (_idPerson integer primary key autoincrement, " +
			"member_of_group not null, personname not null, email not null, telephone not null, " +
			"hashid not null, headfunc not null);";

	//SQL-Statement create
	private static final String CREATE_COURSE_TABLE = 
			"create table " + TABLE_COURSE + 
			" (_idCourse integer primary key autoincrement, " +
			"coursename not null, course_vak not null, courselecturername not null, " +
			"courselectureremail not null);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context con) {
		this.context = con;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_GROUP_TABLE);
			db.execSQL(CREATE_PERSON_TABLE);
			db.execSQL(CREATE_COURSE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS table_groups");
			db.execSQL("DROP TABLE IF EXISTS table_persons");
			db.execSQL("DROP TABLE IF EXISTS table_course");
			onCreate(db);
		}

	}

	// opens writable Database
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// closes Database
	public void close() {
		DBHelper.close();
	}
	/**
	 * Insert Groups in the tables
	 * 
	 * @param groupname
	 * @param course
	 * @param information
	 * @return
	 */
	public long insertGroup(Group group) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_GROUPNAME, group.getGrpName());
		initialValues.put(KEY_COURSE, group.getCourse());
		initialValues.put(KEY_INFO, group.getInformation());
		initialValues.put(KEY_HEAD, group.getHead());
		initialValues.put(KEY_GROUPHASH, group.getHashId());

		return db.insert(TABLE_GROUPS, null, initialValues);
	}	

	/**
	 * @param personname
	 * @param email
	 * @param telephone
	 * @param member_of_group = member of which group //id von Gruppe
	 * @return
	 */
	public long insertPerson(Person person, String member_of_group) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PERSONNAME, person.getName());
		initialValues.put(KEY_EMAIL, person.getMailAddress());
		initialValues.put(KEY_TELEPHONE, person.getPhoneNumber());
		initialValues.put(KEY_MEMBER_OF_GROUP, member_of_group);
		initialValues.put(KEY_HASHID, person.getHashId());
		initialValues.put(KEY_HEADFUNC, person.isHeadFunc());

		return db.insert(TABLE_PERSONS, null, initialValues);
	}

	public long insertCourse(Course course) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_COURSENAME, course.getCourseName());
		initialValues.put(KEY_COURSE_VAK, course.getCourseVAK());
		initialValues.put(KEY_COURSE_LECTURERNAME, course.getLecturerName());
		initialValues.put(KEY_COURSE_LECTUREREMAIL, course.getLecturerMail());
		return db.insert(TABLE_COURSE, null, initialValues);
	}

	public Cursor getCourse(long courseID) throws SQLException {
		Cursor mCursor =
				db.query(true, TABLE_COURSE, new String[] {
						KEY_COURSEID,
						KEY_COURSENAME, 
						KEY_COURSE_VAK
				}, 
				KEY_COURSEID + "=" + courseID, 
				null,
				null, 
				null, 
				null, 
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getGroup(Group group) throws SQLException {
		Cursor mCursor =
				db.query(true, TABLE_GROUPS, new String[] {
						KEY_GROUPID,
						KEY_GROUPNAME, 
						KEY_COURSE,
						KEY_INFO,
						KEY_HEAD,
						KEY_GROUPHASH
				}, 
				KEY_GROUPHASH + "=" + group.getHashId(), 
				null,
				null, 
				null, 
				null, 
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getPerson(long personID) throws SQLException {
		Cursor mCursor =
				db.query(true, TABLE_PERSONS, new String[] {
						KEY_PERSONID,
						KEY_MEMBER_OF_GROUP,
						KEY_PERSONNAME, 
						KEY_EMAIL,
						KEY_TELEPHONE,
						KEY_HASHID,
						KEY_HEADFUNC
				}, 
				KEY_PERSONID + "=" + personID, 
				null,
				null, 
				null, 
				null, 
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor getAllCourses() {
		return db.query(TABLE_COURSE, new String[] {
				KEY_COURSEID,
				KEY_COURSENAME, 
				KEY_COURSE_VAK,
				KEY_COURSE_LECTURERNAME,
				KEY_COURSE_LECTUREREMAIL
		}, 
		null, 
		null, 
		null, 
		null, 
		null);
	}

	public Cursor getAllGroups() {
		return db.query(TABLE_GROUPS, new String[] {
				KEY_GROUPID,
				KEY_GROUPNAME, 
				KEY_COURSE,
				KEY_INFO,
				KEY_HEAD,
				KEY_GROUPHASH
		}, 
		null, 
		null, 
		null, 
		null, 
		null);
	}
	public Cursor getAllPersons() {
		return db.query(TABLE_PERSONS, new String[] {
				KEY_PERSONID,
				KEY_MEMBER_OF_GROUP,
				KEY_PERSONNAME, 
				KEY_EMAIL,
				KEY_TELEPHONE,
				KEY_HASHID,
				KEY_HEADFUNC
		}, 
		null, 
		null, 
		null, 
		null, 
		null);
	}
	public boolean deleteCourse(long crsID) {
		return db.delete(TABLE_COURSE, KEY_COURSEID + 
				"=" + crsID, null) > 0;
	}

	public boolean deleteGroup(long grpID) {
		return db.delete(TABLE_GROUPS, KEY_GROUPID + 
				"=" + grpID, null) > 0;
	}

	public boolean deletePerson(long personID) {
		return db.delete(TABLE_PERSONS, KEY_PERSONID + 
				"=" + personID, null) > 0;
	}
	public boolean updateHead(long grpID, String newHead){
		ContentValues head = new ContentValues();
		head.put(KEY_HEAD, newHead);
		return db.update(TABLE_GROUPS, head, KEY_GROUPID + "=" + grpID, null) > 0;
	}
	public int updateMemberOfGroup(long personID, String newMemberOfGroup){
		ContentValues memberOfGroup = new ContentValues();
		memberOfGroup.put(KEY_MEMBER_OF_GROUP, newMemberOfGroup);
		return db.update(TABLE_PERSONS, memberOfGroup, KEY_PERSONID +"="+personID,null);

	}
	public boolean updateGroup(long grpID, String grpName, String course, String info, String head) {
		ContentValues args = new ContentValues();
		args.put(KEY_GROUPNAME, grpName);
		args.put(KEY_COURSE, course);
		args.put(KEY_INFO, info);
		args.put(KEY_HEAD, head);
		return db.update(TABLE_GROUPS, args, 
				KEY_GROUPID + "=" + grpID, null) > 0;
	}

	public boolean updatePerson(Person person) {

		ContentValues args = new ContentValues();
		args.put(KEY_PERSONNAME, person.getName());
		args.put(KEY_EMAIL, person.getMailAddress());
		args.put(KEY_TELEPHONE, person.getPhoneNumber());
		args.put(KEY_PERSONID, person.getId());
		return db.update(TABLE_PERSONS, args, 
				KEY_PERSONID + " = " + person.getId(), null) > 0;
	}
	public boolean updateCourse(long courseID, Course course) {
		ContentValues args = new ContentValues();
		args.put(KEY_COURSENAME, course.getCourseName());
		args.put(KEY_COURSE_VAK, course.getCourseVAK());
		args.put(KEY_COURSE_LECTURERNAME, course.getLecturerName());
		args.put(KEY_COURSE_LECTUREREMAIL, course.getLecturerMail());
		return db.update(TABLE_COURSE, args, 
				KEY_COURSEID + " = " + courseID, null) > 0;
	}
}