package com.groupe.mail;


import java.util.ArrayList;
import java.util.LinkedList;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.groupe.activities.CreateCourseGroups;
import com.groupe.activities.GroupUpdate;
import com.groupe.activities.HandleAck;
import com.groupe.activities.HandleInvite;
import com.groupe.activities.HandleManuelInv;
import com.groupe.activities.MemberLeftGroup;
import com.groupe.activities.NewCourseGroup;
import com.groupe.activities.ViewMessage;
import com.groupe.config.FileAccess;
import com.groupe.nfc.TagParser;
import com.groupe.types.Course;
import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;
import com.groupe.types.Person;

/**
 * @author ontl
 */
public class InviteListener extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	static HandlerThread thread;
	public static boolean end = false;

	String user = "";
	String server = "";
	String port = "";
	String pass = "";
	
	private final class ServiceHandler extends Handler {

		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			LinkedList<String> settings = FileAccess.getMailSettings(InviteListener.this);
			int interval = Integer.parseInt(settings.getLast());
			while(!end) {
				checkForMails(getBaseContext());

				try {
					if(interval != 0) {
						Thread.sleep(1000*60*60*interval);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onCreate() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		end = false;
		thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);

		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		MailAccount mailAccount = FileAccess.getMailAccountFromFile(this);
		user = mailAccount.getUser();
		server = mailAccount.getIncomingServer();
		port = mailAccount.getIncomingPort();
		pass = mailAccount.getPassword();

		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		end = true;
	}

	private void notifyUser(Mail mail, int notifyID) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);

		int icon = R.drawable.stat_notify_chat;
		CharSequence tickerText = "New Group E Message!";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;				

		Context c = getApplicationContext();

		if(mail.getSubject().contains("Group E message from")) {
			CharSequence contentTitle = "Group message";
			CharSequence contentText = "Group message";
			Intent notificationIntent = new Intent(this, ViewMessage.class);
			notificationIntent.putExtra("com.groupe.types.Mail", mail);

			PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

			notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
			mNotificationManager.notify(notifyID, notification);

		}else{
			Group group = EmailParser.parseForGroup(mail.getBody());
			ArrayList<Group> groups = EmailParser.parseForGroups(mail.getBody());
			ArrayList<Person> members = EmailParser.parseForGroupsPerson(mail.getBody());
			Course course = TagParser.parseForCourse(mail.getBody());
			
			if(mail.getSubject().contains("Group E ack")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "New Group E ack";
				CharSequence contentText = "New Group E ack";
				Intent notificationIntent = new Intent(this, HandleAck.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();

			}
			if(mail.getSubject().contains("Group E invite")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "New Group E Invite";
				CharSequence contentText = "New Group E Invite";
				Intent notificationIntent = new Intent(InviteListener.this, HandleInvite.class);

				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);
				notificationIntent.putExtra("com.groupe.types.Course", course);
				
				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			if(mail.getSubject().contains("Group E new Member")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "New Group E Member";
				CharSequence contentText = "New Group E Member";
				Intent notificationIntent = new Intent(this, HandleManuelInv.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			if(mail.getSubject().contains("Group E status message")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "Group Update";
				CharSequence contentText = "Group Update";
				Intent notificationIntent = new Intent(this, GroupUpdate.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			if(mail.getSubject().contains("Group E course groups")) {
//				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "Course Groups";
				CharSequence contentText = "Course Groups";
				Intent notificationIntent = new Intent(this, CreateCourseGroups.class);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Group", groups);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putExtra("com.groupe.types.Course",course);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", members);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
//				persons.clear();
			}
			if(mail.getSubject().contains("Group E apply to Course")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "New Course Group";
				CharSequence contentText = "New Course Group";
				Intent notificationIntent = new Intent(this, NewCourseGroup.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			
			
			if(mail.getSubject().contains("Group E Member leaving group")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "Member left group";
				CharSequence contentText = "Member left group";
				Intent notificationIntent = new Intent(this, MemberLeftGroup.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putExtra("head", false);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			if(mail.getSubject().contains("Group E Head leaving group")) {
				ArrayList<Person> persons = EmailParser.parseForPerson(mail.getBody());
				CharSequence contentTitle = "Member left group";
				CharSequence contentText = "Member left group";
				Intent notificationIntent = new Intent(this, MemberLeftGroup.class);
				notificationIntent.putExtra("com.groupe.types.Group", group);
				notificationIntent.putExtra("com.groupe.types.Mail", mail);
				notificationIntent.putExtra("head", true);
				notificationIntent.putParcelableArrayListExtra("com.groupe.types.Person", persons);

				PendingIntent contentIntent = PendingIntent.getActivity(this, notifyID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
				mNotificationManager.notify(notifyID, notification);
				persons.clear();
			}
			
		}
	}
	
	public static void stopThread() {
		if (thread.isAlive()) {
			thread.destroy();       
        }
     }
	
	public void checkForMails(Context context) {
		try {
			LinkedList<Mail> list = MailTransport.getMail(this);

			boolean newInvite = EmailParser.openGroupMail(list,context);
			if(newInvite) {

				for(int i = 0;i < list.size();i++) {
					notifyUser(list.get(i),i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}