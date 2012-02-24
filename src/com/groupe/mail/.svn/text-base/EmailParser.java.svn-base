package com.groupe.mail;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.groupe.types.Group;
import com.groupe.types.Mail;
import com.groupe.types.Person;

/**
 * @author ontl
 */

public class EmailParser extends DefaultHandler{

	static String grp = "";
	static String courseName = "";
	static String info = "";
	static String grpHead = "";
	static String grpHashId = "";

	static String name = "";
	static String email = "";
	static String tele = "";
	static String hash = "";
	static String gruppe = "";

	static String type = "";

	static Group group = new Group();
	static Person person = new Person();
	static ArrayList<Group> groups = new ArrayList<Group>();
	static ArrayList<Person> persons = new ArrayList<Person>();

	public static boolean openGroupMail(LinkedList<Mail> mails, Context context) {

		for(int i = 0;i < mails.size();i++) {
			if(!mails.get(i).getSubject().contains("Group E message from")) {
				EmailParser.parseForGroup(mails.get(i).getBody());
				EmailParser.parseForPerson(mails.get(i).getBody());
			
			// reset list
			persons.clear();
			}
		}	
		if(mails.size() > 0) {
			return true;
		}else{
			return false;
		}
	}

	public static ArrayList<Person> parseForPerson(String body) {
		persons.clear();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean memberName 		= false;
				boolean emailAddress	= false;
				boolean telephone		= false;
				boolean hashid 			= false;
				boolean check			= false;


				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Name")) {
						memberName = true;
					}
					if (qName.equalsIgnoreCase("Email")) {
						emailAddress = true;
					}
					if (qName.equalsIgnoreCase("Telefon")) {
						telephone = true;
					}
					if (qName.equalsIgnoreCase("HashId")) {
						hashid = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (memberName) {
						name = new String(ch, start, length);
						person.setName(name);
						memberName = false;
					}
					if (emailAddress) {
						email = new String(ch, start, length);
						person.setMailAddress(email);
						emailAddress = false;
					}
					if (telephone) {
						tele = new String(ch, start, length);
						person.setPhoneNumber(tele);						
						telephone = false;
					}					
					if (hashid) {
						hash = new String(ch, start, length);
						person.setHashId(hash);
						hashid = false;
						if(persons.size()==0) {

						}else{
							for(int i = 0;i < persons.size();i++) {

								if(persons.get(i).getHashId().equals(hash)) {
									check = true;
								}
								
							}
							
						}
						if (!check){
							persons.add(new Person(name,email,tele,hash));
						}else{
							check = false;
						}
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				}
			};

			saxParser.parse(new InputSource(new StringReader(body)), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return persons;
	}


	public static Group parseForGroup(String body) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean grpName 		= false;
				boolean course		 	= false;
				boolean information		= false;
				boolean head 			= false;
				boolean hashId			= false;
				

				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Veranstaltung")) {
						course = true;
					}
					if (qName.equalsIgnoreCase("Gruppe")) {
						grpName = true;
					}
					if (qName.equalsIgnoreCase("Information")) {
						information = true;
					}
					if (qName.equalsIgnoreCase("Head")) {
						head = true;
					}
					if (qName.equalsIgnoreCase("GrpHash")) {
						hashId = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (grpName) {
						grp = new String(ch, start, length);
						group.setGrpName(grp);
						grpName = false;
					}
					if (course) {
						courseName = new String(ch, start, length);
						group.setCourse(courseName);
						course = false;
					}
					if (information) {
						info = new String(ch, start, length);
						group.setInformation(info);
						information = false;
					}
					if (head) {
						grpHead = new String(ch, start, length);
						group.setHead(grpHead);
						head = false;
					}
					if (hashId) {
						grpHashId = new String(ch, start, length);
						group.setHashId(grpHashId);
						hashId = false;
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				}
			};

			saxParser.parse(new InputSource(new StringReader(body)), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}	
	
	public static ArrayList<Group> parseForGroups(String body) {
		groups.clear();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean grpName 		= false;
				boolean course		 	= false;
				boolean information		= false;
				boolean head 			= false;
				boolean hashId			= false;
				boolean check 			= false;
				
				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Veranstaltung")) {
						course = true;
					}
					if (qName.equalsIgnoreCase("Gruppe")) {
						grpName = true;
					}
					if (qName.equalsIgnoreCase("Information")) {
						information = true;
					}
					if (qName.equalsIgnoreCase("Head")) {
						head = true;
					}
					if (qName.equalsIgnoreCase("GrpHash")) {
						hashId = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (grpName) {
						grp = new String(ch, start, length);
						group.setGrpName(grp);
						grpName = false;
					}
					if (course) {
						courseName = new String(ch, start, length);
						group.setCourse(courseName);
						course = false;
					}
					if (information) {
						info = new String(ch, start, length);
						group.setInformation(info);
						information = false;
					}
					if (head) {
						grpHead = new String(ch, start, length);
						group.setHead(grpHead);
						head = false;
					}
					if (hashId) {
						grpHashId = new String(ch, start, length);
						group.setHashId(grpHashId);
						hashId = false;
					}
					if(groups.size()==0) {
					}else{
						for(int i = 0;i < groups.size();i++) {
							if(groups.get(i).getHashId().equals(grpHashId)) {
								check = true;
							}
						}
					}
					if (!check){
						groups.add(new Group(grp,courseName,info,grpHead,grpHashId));
					}else{
						check = false;
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				}
			};

			saxParser.parse(new InputSource(new StringReader(body)), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return groups;
	}
	
	public static ArrayList<Person> parseForGroupsPerson(String body) {
		persons.clear();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean memberName 		= false;
				boolean emailAddress	= false;
				boolean telephone		= false;
				boolean hashid 			= false;
				boolean check			= false;
				boolean grp				= false;


				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Name")) {
						memberName = true;
					}
					if (qName.equalsIgnoreCase("Email")) {
						emailAddress = true;
					}
					if (qName.equalsIgnoreCase("Telefon")) {
						telephone = true;
					}
					if (qName.equalsIgnoreCase("HashId")) {
						hashid = true;
					}
					if (qName.equalsIgnoreCase("Group")) {
						grp = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (memberName) {
						name = new String(ch, start, length);
						person.setName(name);
						memberName = false;
					}
					if (emailAddress) {
						email = new String(ch, start, length);
						person.setMailAddress(email);
						emailAddress = false;
					}
					if (telephone) {
						tele = new String(ch, start, length);
						person.setPhoneNumber(tele);						
						telephone = false;
					}	
					if (hashid) {
						hash = new String(ch, start, length);
						person.setHashId(hash);
						hashid = false;
					}
					if (grp) {
						gruppe = new String(ch, start, length);
						person.setSyncGroup(gruppe);					
						grp = false;
						if(persons.size()==0) {

						}else{
							for(int i = 0;i < persons.size();i++) {

								if(persons.get(i).getHashId().equals(hash)) {
									check = true;
								}
								
							}
							
						}
						if (!check){
							persons.add(new Person(name,email,tele,hash,gruppe));
						}else{
							check = false;
						}
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				}
			};

			saxParser.parse(new InputSource(new StringReader(body)), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return persons;
	}
	
	public static String parseForType(String body) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean bType 		= false;

				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Type")) {
						bType = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {

					if (bType) {
						type = new String(ch, start, length);
						bType = false;
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
				}
			};

			saxParser.parse(new InputSource(new StringReader(body)), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return type;
	}	
}