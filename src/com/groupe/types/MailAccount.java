package com.groupe.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.groupe.mail.MailAuthenticator;

/**
 * @author ontl
 */
 
public class MailAccount implements Parcelable{
		
    private String outgoingServer;
    private String outgoingPort;
    
    private String incomingServer;
    private String incomingPort;
    
    private String user;
    private String password;
    
    /**
     * @param outgoingServer
     * @param outgoingPort
     * @param incomingServer
     * @param incomingPort
     * @param user
     * @param password
     * @param email
     */
    
    public MailAccount() {
    	super();
    }
    public MailAccount(String outgoingServer, String outgoingPort, 
    					String incomingServer, String incomingPort,
    					String user, String password) {
    	
        this.outgoingServer = outgoingServer;
        this.outgoingPort = outgoingPort;
        
        this.incomingServer = incomingServer;
        this.incomingPort = incomingPort;
        
        this.user = user;
        this.password = password;
    }
 
    public String getOutgoingPort() {
        return outgoingPort;
    }
    
    public void setOutgoingPort(String outgoingPort) {
    	this.outgoingPort = outgoingPort;
    }
 
    public String getOutgoingServer() {
        return outgoingServer;
    }
    
    public void setOutgoingServer(String outgoingServer) {
    	this.outgoingServer = outgoingServer;
    }

    public String getIncomingPort() {
        return incomingPort;
    }
    
    public void setIncomingPort(String incomingPort) {
    	this.incomingPort = incomingPort;
    }
 
    public String getIncomingServer() {
        return incomingServer;
    }
    
    public void setIncomingServer(String incomingServer) {
    	this.incomingServer = incomingServer;
    }
    
    public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MailAuthenticator getPasswordAuthentication() {
        return new MailAuthenticator(user, password);
    }
	
	@Override
	public int describeContents() {
		return 0;
	}
	public MailAccount(Parcel in) {
		//		this();
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(user);
		dest.writeString(password);
		dest.writeString(outgoingServer);
		dest.writeString(outgoingPort);
		dest.writeString(incomingServer);
		dest.writeString(incomingPort);
	}

	/**
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		user = in.readString();
		password = in.readString();
		outgoingServer = in.readString();
		outgoingPort = in.readString();
		incomingServer = in.readString();
		incomingPort = in.readString();
	}

	public static final Parcelable.Creator<MailAccount> CREATOR = new Parcelable.Creator<MailAccount>() {
		@Override
		public MailAccount createFromParcel(Parcel in) {
			return new MailAccount(in);
		}

		@Override
		public MailAccount[] newArray(int size) {
			return new MailAccount[size];
		}
	};
	
}