package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class DBMethods {

	Statement stmt;
	static Connection	myConn;
	ResultSet rs;
	String password = null;
	PropertiesLoadWrite prw = new PropertiesLoadWrite();
	DriverManager driverManager;
	

	
	public DBMethods() throws SQLException{
		myConn = driverManager.getConnection(prw.loadProperties("dbHost")+"/"+prw.loadProperties("dbName"),prw.loadProperties("dbUser"),prw.loadProperties("dbPass"));
		stmt = myConn.createStatement();
	}
	
	public static ArrayList<String> getAllusers() throws SQLException{
		
		ArrayList<String> users = new ArrayList<String>();
		
		String sql = "Select username from Users";
		PreparedStatement queryStatement = myConn.prepareStatement(sql);
		ResultSet rs = queryStatement.executeQuery();
		
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount();

		while (rs.next()) {
		    Object[] values = new Object[columnCount];
		    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
			    users.add(rs.getString(i));
		    }
		}
		return users;
	}
	
	public String getPassword(){
		return password;
	}
	
	
	public String hashPass(String pass) throws NoSuchAlgorithmException {
	    MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
		mdEnc.update(pass.getBytes(), 0, pass.length());
	    String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
	    return md5;
	}
	
	public int getLastId() throws SQLException {
		int id = 0;
		String queryPattern = "select * from Users";
		PreparedStatement queryStatement = myConn.prepareStatement(queryPattern);
	    ResultSet rs = queryStatement.executeQuery();
	    if(rs.next()) {
	    	
	    	queryPattern = "SELECT idusers FROM Users WHERE idusers=(SELECT max(idusers) FROM Users)";
	    	queryStatement = myConn.prepareStatement(queryPattern);
	    	rs = queryStatement.executeQuery();
	    	if(rs.next()) {
	    		id = rs.getInt(1);
	    	}
	    }
	    return id;
	}
	public void insertFields(String username, String password, String email) throws NoSuchAlgorithmException, SQLException {
	    String queryPattern = "select * from Users where email=?";
	    PreparedStatement queryStatement = myConn.prepareStatement(queryPattern);
	    queryStatement.setString(1, email);
	    ResultSet rs = queryStatement.executeQuery();
	    if(!rs.next())
	    {
	    	int id = getLastId()+1;
	    	queryPattern = "insert into Users (idusers,username,pass,email) values(?,?,?,?)";
	    	queryStatement = myConn.prepareStatement(queryPattern);
	    	queryStatement.setInt(1, id);
	    	queryStatement.setString(2 , username.toLowerCase());
	    	queryStatement.setString(3 , hashPass(password));
	    	queryStatement.setString(4 , email.toLowerCase());
	    	queryStatement.executeUpdate();
	    }
	}
	
	public boolean checkCredentials(String login, String pass) throws SQLException, NoSuchAlgorithmException {
		String query = "select * from Users where email=? or username=? and pass=?";
		PreparedStatement queryStatement = myConn.prepareStatement(query);
		queryStatement.setString(1, login.toLowerCase());
		queryStatement.setString(2, login.toLowerCase());
		queryStatement.setString(3, hashPass(pass));
		System.out.println(hashPass(pass));
		ResultSet rs = queryStatement.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
	}


	public String getUserEmail(String credentials) throws SQLException {
		String email = null;
		if(credentials.contains("@")) {
			return credentials;
		}else {
			String query = "select email from Users where username = ?";
			PreparedStatement queryStatement = myConn.prepareStatement(query);
			queryStatement.setString(1, credentials);
			ResultSet rs = queryStatement.executeQuery();
			if(rs.next()) {
				email = rs.getString(1);
			}
		}
		return email;
	}
}
