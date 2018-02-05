package application;

public class Settings {

	String dbHost = null;
	String dbName = null;
	String dbUser = null;
	String dbPass = null;
	String smtpPass = null;
	String smtpEmail = null;
	
	PropertiesLoadWrite prw = new PropertiesLoadWrite();
	
	public Settings(String dbHost, String dbName, String dbUser, String dbPass, String smtpEmail, String smtpPass) {
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.dbPass = dbPass;
		this.dbUser = dbUser;
		this.smtpEmail = smtpEmail;
		this.smtpPass = smtpPass;
	}
	
	public void setSettings() {
		if(dbHost!=null) {
			prw.storeProperties("dbHost", dbHost);
		}
		if(dbName!=null) {
			prw.storeProperties("dbName", dbName);
		}
		if(dbPass!=null) {
			prw.storeProperties("dbPass", dbPass);
		}
		if(dbUser!=null) {
			prw.storeProperties("dbUser", dbUser);
		}
		if(smtpEmail!=null) {
			prw.storeProperties("smtpEmail", smtpEmail);
		}
		if(smtpPass!=null) {
			prw.storeProperties("smtpPass", smtpPass);
		}
	}
}
