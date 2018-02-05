package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.ParseException;
import com.sun.mail.pop3.POP3SSLStore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class getMailBox {

	private Session session = null;
	private Store store = null;
	private String username, password;
	private Folder folder;
	public static ArrayList<String> messageContainer = new ArrayList<>();
	PropertiesLoadWrite psw = new PropertiesLoadWrite();
	static ArrayList<String> msgsBOX = new ArrayList<>();
	public getMailBox() {}
	
	public void setUserPass(String username,String password) {
		this.username = psw.loadProperties("smtpEmail");
		this.password = psw.loadProperties("smtpPass");
	}
	public ArrayList<String> getMSG() throws Exception {
		connect();
        openFolder("INBOX");
        printAllMessages();
		return messageContainer;
	}
	
	public void getAllMSG() throws Exception {
		connect();
        openFolder("INBOX");
        printAllMessageEnvelopes();
	}

	public String[] getMSGContent(String From, String Subject) throws Exception {
		getAllMSG();
		String[] msgContent = new String[4];
		for(int i = 0; i< msgsBOX.size(); i+=4) {
			if((msgsBOX.get(i).contains(From)) && (msgsBOX.get(i+1).contains(Subject))) {
				msgContent[0] = msgsBOX.get(i);
				msgContent[1] = msgsBOX.get(i+1);
				msgContent[2] = msgsBOX.get(i+2);
				msgContent[3] = msgsBOX.get(i+3);
			}
		}
		return msgContent;
	}
	
	public void downloadFile(String From, String Title) throws Exception {
		connect();
		openFolder("INBOX");
        Address[] a;
		String attachFiles = "";
		String from = "";
		Message [] msg = folder.getMessages();
		for(int i = 0; i < msg.length; i++) {
	        	if ((a = msg[i].getFrom()) != null) {
	        		for (int j = 0; j < a.length; j++) {
	        			from = from.concat(a[j].toString());
	        		}
	        	}
	        	if((from.contains(From)) && (msg[i].getSubject().toString().contains(Title))) {
	        		System.out.println("True");
	        		String contentType = msg[i].getContentType();
	        		if(contentType.contains("multipart")) {
	        			Multipart multiPart  = (Multipart) msg[i].getContent();
	        			int partsNumber = multiPart.getCount();
	        			for(int j = 0; j < partsNumber; j++) {
	        				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(j);
	        				if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	        					String fileName = part.getFileName();
	        					attachFiles += fileName + ", ";
	        					part.saveFile(new PropertiesLoadWrite().loadProperties("defDir") + File.separator + fileName);
	        				}
	        			}
	        		}
	        }
		}
	}
	
    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message)
            dumpEnvelope((Message)p);
       
        String ct = p.getContentType();

        
        /*
         * Using isMimeType to determine the content type avoids
         * fetching the actual content data until we need it.
         */
        if (p.isMimeType("text/plain")) {  
        } else {
        }
    }
	
	public void connect() throws MessagingException {
		 String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	        
	        Properties pop3Props = new Properties();
	        
	        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
	        pop3Props.setProperty("mail.pop3.port",  "995");
	        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
	        
	        URLName url = new URLName("pop3", "pop.gmail.com", 995, "",
	                psw.loadProperties("smtpEmail"), psw.loadProperties("smtpPass"));
	        
	        session = Session.getInstance(pop3Props, null);
	        store = new POP3SSLStore(session, url);
	        store.connect();
	}
	 public void openFolder(String folderName) throws Exception {
	        
	        // Open the Folder
	        folder = store.getDefaultFolder();
	        
	        folder = folder.getFolder(folderName);
	        
	        if (folder == null) {
	            throw new Exception("Invalid folder");
	        }
	        
	        // try to open read/write and if that fails try read-only
	        try {
	            
	            folder.open(Folder.READ_WRITE);
	            
	        } catch (MessagingException ex) {
	            
	            folder.open(Folder.READ_ONLY);
	            
	        }
	    }
	    
	    public void closeFolder() throws Exception {
	        folder.close(false);
	    }
	    
	    public int getMessageCount() throws Exception {
	        return folder.getMessageCount();
	    }
	    
	    public int getNewMessageCount() throws Exception {
	        return folder.getNewMessageCount();
	    }
	    
	    public void disconnect() throws Exception {
	        store.close();
	    }
	    
	    public void printMessage(int messageNo) throws Exception {
	        
	        Message m = null;
	        
	        try {
	            m = folder.getMessage(messageNo);
	            dumpPart(m);
	        } catch (IndexOutOfBoundsException iex) {
	        }
	    }
	    
	    public void printAllMessageEnvelopes() throws Exception {
	        
	        // Attributes & Flags for all messages ..
	        Message[] msgs = folder.getMessages();
	        
	        // Use a suitable FetchProfile
	        FetchProfile fp = new FetchProfile();
	        fp.add(FetchProfile.Item.ENVELOPE);        
	        folder.fetch(msgs, fp);
	        
	        for (int i = 0; i < msgs.length; i++) {

	            dumpEnvelope(msgs[i]);
	            
	        }
	        
	    }
	    
	    public void printAllMessages() throws Exception {
	     
	        // Attributes & Flags for all messages ..
	        Message[] msgs = folder.getMessages();
	        
	        // Use a suitable FetchProfile
	        FetchProfile fp = new FetchProfile();
	        fp.add(FetchProfile.Item.ENVELOPE);        
	        folder.fetch(msgs, fp);
	        messageContainer.clear();
	        msgsBOX.clear();
	        for (int i = 0; i < msgs.length; i++) {
	            dumpPart(msgs[i]);
	        }
	    }
	    

	    
	    public static void dumpEnvelope(Message m) throws Exception {        
	        Address[] a;

	        // FROM
            String subs = "";
            String from = "";
	        if ((a = m.getFrom()) != null) {
	            for (int j = 0; j < a.length; j++) {
	                
	                
	               from = from.concat(a[j].toString());

	            }
	               from = from.replace("<", " ").replace(">", "");

	            messageContainer.add(from);
	            msgsBOX.add(from);
	        }
	        


	        
	        // SUBJECT
	        messageContainer.add(m.getSubject());
	        msgsBOX.add(m.getSubject());
	        if(m.isMimeType("text/plain")) {
	        	msgsBOX.add(m.getContent().toString());
	        }else {
	        	msgsBOX.add("Unknows message content type");
	        }
	        	
	        msgsBOX.add(m.getSentDate().toString());
	        // DATE
	        Date d = m.getSentDate();

	        

	    }
	    
	    static String indentStr = "                                               ";
	    static int level = 0;

	    
	    /**
	     * Print a, possibly indented, string.
	     */
	    
	}
	

