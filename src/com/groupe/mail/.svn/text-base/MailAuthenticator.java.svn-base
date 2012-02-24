package com.groupe.mail;
 
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
/**
 * @author ontl
 */
public class MailAuthenticator extends Authenticator {
    private String user;
    private String password;
 
    public MailAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }
 
    @Override
	public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
}
