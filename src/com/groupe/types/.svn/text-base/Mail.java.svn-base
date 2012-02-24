package com.groupe.types;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ontl
 *
 */
public class Mail implements Parcelable {
	private String sender;
	private ArrayList<String> reciever;
	private String subject;
	private String body;
	private Date date;
	
	public Mail(){
		reciever = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param host
	 * @param sender
	 * @param reciever
	 * @param subject
	 * @param body
	 * @param date
	 * @param password
	 */
	public Mail(String sender, ArrayList<String> reciever, String subject, String body, Date date){
		this.sender = sender;
		this.reciever = reciever;
		this.subject = subject;
		this.body = body;
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public ArrayList<String> getReciever() {
		return reciever;
	}

	public void setReciever(ArrayList<String> reciever) {
		this.reciever = reciever;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	private Mail(Parcel in) {
		this();
		readFromParcel(in);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sender);
		dest.writeStringList(reciever);
		dest.writeString(subject);
		dest.writeString(body);
	}
	
	/**
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		sender = in.readString();
		in.readStringList(reciever);
		subject = in.readString();
		body = in.readString();
	}
	
	public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
		@Override
		public Mail createFromParcel(Parcel in) {
			return new Mail(in);
		}
 
		@Override
		public Mail[] newArray(int size) {
			return new Mail[size];
		}
	};
}