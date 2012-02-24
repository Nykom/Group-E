package com.groupe.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import android.content.Context;

import com.groupe.types.MailAccount;
import com.groupe.types.Person;

/**
 * @author ontl
 */
public class FileAccess {
	
	static String configEmailFile = "groupeConfigEmail";
	static String configUserFile  = "groupeConfigUser";
	static String emailTimeFile = "groupeMailTimeFile";
	
	public static LinkedList<String> getMailSettings(Context context) {
		LinkedList<String> fileInfo;
		fileInfo = readFile(configEmailFile,context);
		LinkedList<String> settings = new LinkedList<String>();
			
		if(fileInfo.size() > 0) {
			settings.add(fileInfo.get(6));
			settings.add(fileInfo.get(7));
			settings.add(fileInfo.get(8));
		}
			
		return settings;		
	}
	
	public static MailAccount getMailAccountFromFile(Context context) {
		LinkedList<String> fileInfo;
		
		fileInfo = readFile(configEmailFile,context);
		MailAccount mailAccount = new MailAccount("","","","","","");
			
		if(fileInfo.size() > 0) {
			mailAccount.setUser(fileInfo.get(0));
			mailAccount.setPassword(fileInfo.get(5));

			mailAccount.setOutgoingServer(fileInfo.get(1));
			mailAccount.setOutgoingPort(fileInfo.get(2));
		
			mailAccount.setIncomingServer(fileInfo.get(3));
			mailAccount.setIncomingPort(fileInfo.get(4));
		}			
		return mailAccount;		
	}
	
	public static Person getUserFromFile(Context context) {
		LinkedList<String> fileInfo;
		fileInfo = readFile(configUserFile,context);
			
		Person user = new Person();

		if(fileInfo.size() > 0) {
			user.setName(fileInfo.get(0));
			user.setMailAddress(fileInfo.get(1));
			user.setPhoneNumber(fileInfo.get(2));
			user.setHashId(fileInfo.get(3));
		}			
		return user;
	}
	
	public static String getTimeLastMail(Context context) {
		LinkedList<String> time;
		time = readFile(emailTimeFile,context);
		return time.get(0);		
	}
	
	public static LinkedList<String> readFile(String file, Context context) {

		LinkedList<String> list = new LinkedList<String>();
		String actLine;
		FileInputStream in;					

		boolean exists = (new File("/data/data/com.groupe/files/"+file)).exists();
		if (exists) {

			try {
				in = context.openFileInput(file);
				BufferedReader read = new BufferedReader(new InputStreamReader(in));

				for(int i = 0; (actLine = read.readLine()) != null; i++) {
					list.add(actLine);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}
	
	public static void writeFile(String text, String file, Context context) {
		FileOutputStream out = null;
		OutputStreamWriter write = null;
		try {
			out = context.openFileOutput(file,Context.MODE_PRIVATE);
			write = new OutputStreamWriter(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			write.write(text);
			write.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				write.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
