package com.groupe.nfc;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.groupe.types.Course;

/**
 * @author ontl
 *
 */
public class TagParser extends DefaultHandler{
	
	static String crs = "";
	static String vak = "";
	static String lecName = "";
	static String lecMail = "";
	
	static Course course = new Course();
	
	public static Course parseForCourse(String body) {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
			
				boolean courseName 				= false;
				boolean courseVAK				= false;
				boolean lecturerName			= false;
				boolean lecturerMail			= false;
				
				@Override
				public void startElement(String uri, String localName, String qName,
						Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("Kursname")) {
						courseName = true;
					}
					if (qName.equalsIgnoreCase("VAK")) {
						courseVAK = true;
					}
					if (qName.equalsIgnoreCase("LecturerName")) {
						lecturerName = true;
					}
					if (qName.equalsIgnoreCase("LecturerMail")) {
						lecturerMail = true;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {
						
					if (courseName) {
						crs = new String(ch, start, length);
						course.setCourseName(crs);
						courseName = false;
					}
					if (courseVAK) {
						vak = new String(ch, start, length);
						course.setCourseVAK(vak);
						courseVAK = false;
					}
					if (lecturerName) {
						lecName = new String(ch, start, length);
						course.setLecturerName(lecName);
						lecturerName = false;
					}
					if (lecturerMail) {
						lecMail = new String(ch, start, length);
						course.setLecturerMail(lecMail);
						lecturerMail = false;
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
		return course;
	}
}
