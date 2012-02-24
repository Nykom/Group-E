package com.groupe.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.mail.MailTransport;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;
import com.groupe.types.Person;
/**
 * @author ontl
 */
public class GroupView extends Activity{
	private static final int MENU1 = Menu.FIRST;
	private static final int MENU2 = Menu.FIRST + 1;
	private static final int MENU3 = Menu.FIRST + 2;
	private static final int MENU4 = Menu.FIRST + 3;
	private static final int MENU5 = Menu.FIRST + 4;
	Group group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupview);  
	  
		Bundle bundle = getIntent().getExtras();
		group = bundle.getParcelable("com.groupe.types.Group");

		TextView tvName = (TextView)findViewById(R.id.gruppe);
		tvName.setText("Group: " + group.getGrpName());
		
		TextView tvCourse = (TextView)findViewById(R.id.veranstaltung);
		tvCourse.setText("Course: " + group.getCourse());
		
		TextView tvInfo = (TextView)findViewById(R.id.info);
		tvInfo.setText("Information: " + group.getInformation());
		
		TextView member = (TextView)findViewById(R.id.member);
		String memberText = "Member:" + "\n";
		
		for(int i = 0;i < group.getMembers(group, this).size();i++) {
			memberText += group.getMembers(group, this).get(i).getName() + "\n";
		}
		member.setText(memberText);
      
		Button showPersons = (Button)findViewById(R.id.ShowAllPersons);
		showPersons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				startActivity(new Intent(GroupView.this, ShowAllPersons.class));
			}             
		});
		Button editGroup = (Button)findViewById(R.id.EditGroup);
		editGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupView.this, EditGroup.class);
				intent.putExtra("com.groupe.types.Group", group);
				startActivity(intent);
				finish();
			}             
		});
      
		Button deleteGroup = (Button)findViewById(R.id.DeleteGroup);
		deleteGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {         	             	 
				AlertDialog.Builder alertbox = new AlertDialog.Builder(GroupView.this);
				alertbox.setMessage("Disband Group?");
				alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						DBAccess.deleteGroup(group, GroupView.this);
						finish();
					}
				});
   
				alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {   
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
				alertbox.show();
			}             
		});
	
	
		Button leaveGroup = (Button)findViewById(R.id.LeaveGroup);
		leaveGroup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {  
				
				final String head = group.getHead();
				final Person user = FileAccess.getUserFromFile(GroupView.this);
								
				AlertDialog.Builder alertbox = new AlertDialog.Builder(GroupView.this);
				alertbox.setMessage("Leave Group?");
				alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ArrayList<Person> persons = group.getMembers(group, GroupView.this);
						if(head.equals(user)) {
							
							String reci = "";
							for(int i = 0;i < persons.size();i++) {
								
								if(persons.get(i).isHeadFunc()) {
									reci = persons.get(i).getMailAddress();
									break;
								}
							}
							
							Mail mail = new Mail();
							
							mail.setSubject("Group E Head leaving group");
							
							mail.setSender(user.getMailAddress());
							
							ArrayList<String> reciever = new ArrayList<String>();
							
							if(reci.equals("")) {
								mail.setBody("The actual Head is leaving the group. Since there is no other " +
										"person who can send mails, the communication will not be available anymore.");
								for(int i = 0;i < persons.size();i++) {
									reciever.add(persons.get(i).getMailAddress());
								}
							}else{			
								mail.setBody("The actual Head has left the group. You are now the new head!");
								reciever.add(reci);
								mail.setReciever(reciever);
							}
							
							MailAccount mailAccount = FileAccess.getMailAccountFromFile(GroupView.this);
							try {
								MailTransport.send(mailAccount, mail, GroupView.this);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							Mail mail = new Mail();
							
							mail.setSubject("Group E Member leaving group");
							
							mail.setSender(user.getMailAddress());
							
							ArrayList<String> reciever = new ArrayList<String>();
							reciever.add(group.getHead());
														
							mail.setBody(user.acknowledge(user, group));
																
							mail.setReciever(reciever);
							
							
							MailAccount mailAccount = FileAccess.getMailAccountFromFile(GroupView.this);
							try {
								MailTransport.send(mailAccount, mail, GroupView.this);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						DBAccess.deleteGroup(group, GroupView.this);
						finish();
					}
				});
 
				alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {   
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
				alertbox.show();
			}             
		});
		Button writeMess = (Button)findViewById(R.id.WriteMessage);
		writeMess.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(GroupView.this, WriteMessage.class);
				intent.putExtra("com.groupe.types.Group", group);
				startActivity(intent);
				finish();
			}             
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		menu.add(0, MENU1, 0 , "Add Member").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU2, 0 , "Add Member NFC").setIcon(android.R.drawable.ic_menu_add);
		
		String head = group.getHead();
		Person user = FileAccess.getUserFromFile(GroupView.this);
		
		if(user.getMailAddress().equals(head)) {			
			menu.add(0, MENU3, 0 , "Apply to Course");
			if(group.getGrpName().contains("*")) {
				menu.add(0, MENU4, 0 , "Submit");
			}
			
		}else{
			if(group.getGrpName().contains("*")) {
				menu.add(0, MENU5, 0 , "Inform head");
			}
		}
				
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		
	    switch (item.getItemId()) {
	    case MENU1:
	    	Intent intent1 = new Intent(GroupView.this, AddMember.class);
			intent1.putExtra("com.groupe.types.Group", group);
			startActivity(intent1);
	        return true;
	    case MENU2:
	    	Intent intent2 = new Intent(GroupView.this, AddMemberNFC.class);
			intent2.putExtra("com.groupe.types.Group", group);
			startActivity(intent2);
	    	return true;
	    case MENU3:
	    	String courseName = group.getCourse();
	    	ArrayList<Course> courses = DBAccess.getAllCourses(GroupView.this);
	    	for(int i = 0;i < courses.size();i++) {
	    		if(courseName.equals(courses.get(i).getCourseVAK())) {
	    			Mail mail = new Mail();
	    			mail.setSubject("Group E apply to Course " + courseName);
	    			mail.setBody(group.generateInformation(group, GroupView.this));
	    			
	    			ArrayList<String> reciever = new ArrayList<String>();
	    			reciever.add(courses.get(i).getLecturerMail());
	    			mail.setReciever(reciever);
	    			
	    			try {
						MailTransport.send(FileAccess.getMailAccountFromFile(GroupView.this), mail, GroupView.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	}
	    	
	    	return true;
	    
	    case MENU4:
	    	group.sendStatus(group, GroupView.this);
	    	String grpName = group.getGrpName();
	    	grpName = grpName.replace("*", "");
	    	group.setGrpName(grpName);
	    	DBAccess.updateGroup(DBAccess.getGroupId(group, GroupView.this), group, GroupView.this);
	    	return true;
	    	
	    case MENU5:
	    	group.informHead(group, GroupView.this);
	    	String grp = group.getGrpName();
	    	grp = grp.replace("*", "");
	    	group.setGrpName(grp);
	    	DBAccess.updateGroup(DBAccess.getGroupId(group, GroupView.this), group, GroupView.this);
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}