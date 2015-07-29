package com.lemania.sis.server.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ContactDao extends MyDAOBase {
	
	public void sendEmail (String firstName, String lastName, String email, String message ) {
		//
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Email : " + email + "\n\nMessage : " + message;

        try {
	        Message msg = new MimeMessage(session);
	        //
	        msg.setFrom(new InternetAddress("thuannn@gmail.com", "Lemania eProfil"));
	        //
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("Thuan.Nguyen@lemania.ch", "Thuan NGUYEN"));
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("Cindy.Clemence@lemania.ch", "Cindy CLEMENCE"));
	        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("Olga.Theofanidis@lemania.ch", "Olga THEOFANIDIS"));
	        //
	        // Reply to
 	        Address[] rraa = new Address[1];
 	        rraa[0] = new InternetAddress( email, email );
	        msg.setReplyTo( rraa );
	        //
	        msg.setSubject("Nouveau message de "+ firstName + " " + lastName +" depuis eProfil");
	        msg.setText( msgBody );
	        //
	        Transport.send( msg );
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	
	/*
	 * 
	 * */
	public void sendEmail ( 
			String messageSubject,
			String from, String to, String replyto, String cc,
			String messageBody ) {
		//
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
	        Message msg = new MimeMessage(session);
	        //
	        // From
	        String[] fromEmails = from.split(",");
	        msg.setFrom(new InternetAddress( fromEmails[0].trim(), fromEmails[1].trim() ));
	        //
	        // To
	        String[] toto;
 	        String[] toPairs = to.split("/");
	        for ( String toMail : toPairs ) {
	        	toto = toMail.split(",");
	        	msg.addRecipient( Message.RecipientType.TO, new InternetAddress( toto[0].trim(), toto[1].trim() ));
	        }
	        //
	        // CC
	        String[] cccc;
 	        String[] ccPairs = cc.split("/");
	        for ( String ccMail : ccPairs ) {
	        	cccc = ccMail.split(",");
	        	msg.addRecipient( Message.RecipientType.TO, new InternetAddress( cccc[0].trim(), cccc[1].trim() ));
	        }
	        //
	        // Reply to
	        String[] rrrr;
 	        String[] rrPairs = replyto.split("/");
 	        Address[] rraa = new Address[ rrPairs.length ];
	        for ( int i = 0; i < rrPairs.length; i++ ) {
	        	//
	        	rrrr = rrPairs[i].split(",");
	        	rraa[i] = new InternetAddress( rrrr[0].trim(), rrrr[1].trim() );
	        }
	        msg.setReplyTo( rraa );
	        // 
	        // Message
	        msg.setSubject( messageSubject );
	        msg.setText( messageBody );
	        //
	        // Send
	        Transport.send(msg);
	        //
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	
	
	/*
	 * */
	public void sendSMS( String number, String message ) {
		//
		//
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        String from = "thuannn@gmail.com, Ecole Lémania";
        String to = number + "@neprlp85uw7k65lf.sms.vtx.ch,Mobile/";
        String subject = "Absence";

        try {
	        Message msg = new MimeMessage(session);
	        //
	        String[] fromEmails = from.split(",");
	        msg.setFrom(new InternetAddress( fromEmails[0].trim(), fromEmails[1].trim() ));
	        //
	        String[] toto;
 	        String[] toPairs = to.split("/");
	        for ( String toMail : toPairs ) {
	        	toto = toMail.split(",");
	        	msg.addRecipient( Message.RecipientType.TO, new InternetAddress( toto[0].trim(), toto[1].trim() ));
	        }
	        //
	        msg.setSubject( subject );
	        msg.setText( message );
	        //
	        Transport.send(msg);
	        //
	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
}