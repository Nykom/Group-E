package com.groupe.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.groupe.config.FileAccess;
import com.groupe.types.Mail;
import com.groupe.types.MailAccount;

/**
 * @author ontl
 */
public class MailTransport {

	private static boolean isConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo == null ? false : networkInfo.isConnected();
	}


	public static LinkedList<Mail> getMail(Context context) throws Exception {

		MailAccount mailAccount = FileAccess.getMailAccountFromFile(context);
		LinkedList<String> settings = FileAccess.getMailSettings(context);
		LinkedList<Mail> groupMess = new LinkedList<Mail>();
		if(settings.get(0).equals("true")) { 
			if((settings.get(1).equals("true") && (isConnected(context))||settings.get(1).equals("false"))) {

				Properties p = new Properties();

				p.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				p.setProperty("mail.imap.socketFactory.fallback", "false");
				p.setProperty("mail.imap.port", "993");
				p.setProperty("mail.imap.socketFactory.port", "993");

				p.put("mail.imap.host", mailAccount.getIncomingServer());
				p.put("mail.store.protocol", "imaps"); //s

				MailAuthenticator auth = new MailAuthenticator(mailAccount.getUser(),mailAccount.getPassword());

				Session session = Session.getInstance(p,auth);
				Store store = session.getStore("imaps");

				store.connect(mailAccount.getIncomingServer(), mailAccount.getUser().toString(), mailAccount.getPassword());
				
				try {
					Message[] message;
					Folder folder = store.getFolder("INBOX");
					folder.open(Folder.READ_ONLY);
					message = folder.getMessages();

					File file = context.getFileStreamPath("groupeMailTimeFile");
					GregorianCalendar cal = new GregorianCalendar();
					Date date = new Date();
					if(file.exists()) {
						String dateString = FileAccess.getTimeLastMail(context);
						date = new Date(dateString);
						cal.setTime(date);
						cal.set(Calendar.YEAR, date.getYear()+1900);
												
					}else{
						date = new Date();
						date.setDate(1);
						date.setHours(14);
						date.setMinutes(14);
						date.setMonth(1);
						date.setSeconds(30);
						date.setYear(110);
						cal.setTime(date);
					}
					for(int i = 0; i < message.length; i++) {
						if(date != null) {
							if(message[i].getSubject().contains("Group E" ) && message[i].getReceivedDate().after(cal.getTime())) {
								
								Multipart mp = (Multipart) message[i].getContent();

								for ( int j = 0; j < mp.getCount(); j++ ) {
									Part part = mp.getBodyPart( j );

									String disposition = part.getDisposition();

									if ( disposition == null ) {
										MimeBodyPart mimePart = (MimeBodyPart)part;

										if(mimePart.isMimeType("text/plain")) {
											BufferedReader in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()) );

											Address[] recipients = message[i].getFrom();
											Address address = recipients[0];

											Address[] reciever = message[i].getAllRecipients();

											ArrayList<String> re = new ArrayList<String>();
											for(int k = 0;k < reciever.length; k++) {
												re.add(reciever[k].toString());
											}

											Mail email = new Mail(address.toString(),re,message[i].getSubject(),"",message[i].getSentDate());

											String body ="";
											for ( String line; (line=in.readLine()) != null; ) {
												body = body + line + "\n";
											}
											email.setBody(body);
											email.setDate(message[i].getReceivedDate());
											groupMess.add(email);
										}else{
											Toast.makeText(context, "No new messages", Toast.LENGTH_LONG).show();
										}
									}
								}
								FileAccess.writeFile(groupMess.getLast().getDate().toGMTString(),"groupeMailTimeFile",context);
								
								InputStream stream = message[i].getInputStream();
								while (stream.available() != 0) {
								}
							}
						}
					}
					folder.close(true);
					store.close();

				} catch(Exception e) {
				}
			}
		}
		return groupMess;
	}


	public static void send(MailAccount mailAccount, Mail mail, Context context) throws Exception {

		LinkedList<String> settings = FileAccess.getMailSettings(context);
		if(settings.get(0).equals("true")) {
			if((settings.get(1).equals("true") && isConnected(context))||settings.get(1).equals("false")) {
				Properties properties = System.getProperties();

				properties.setProperty("mail.smtp.host", mailAccount.getOutgoingServer());

				properties.setProperty("mail.smtp.port", String.valueOf(mailAccount.getOutgoingPort()));

				properties.setProperty("mail.smtp.auth", "true");

				Session session = Session.getDefaultInstance(properties, mailAccount.getPasswordAuthentication());

				properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 


				MimeMessage msg = new MimeMessage(session);

				msg.setSubject(mail.getSubject());

				Multipart multipart = new MimeMultipart();

				BodyPart xmlBody = new MimeBodyPart();
				xmlBody.setText(mail.getBody());

				multipart.addBodyPart(xmlBody);

				msg.setFrom(new InternetAddress(mailAccount.getUser()));

				InternetAddress reci[] = new InternetAddress[mail.getReciever().size()];
				for(int i = 0;i < mail.getReciever().size();i++) {
					reci[i] = new InternetAddress(mail.getReciever().get(i));
				}
				msg.setRecipients(RecipientType.TO, reci); 

				msg.setContent(multipart);
				final MimeMessage mess = msg;
				new Thread(new Runnable() {
					public void run() {
						try {
							Transport.send(mess);
						} catch (MessagingException e) {
							e.printStackTrace();
						}				      
					}
				}).start();

			}
		}
	}
}