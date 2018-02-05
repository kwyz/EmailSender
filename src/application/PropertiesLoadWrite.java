package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

class PropertiesLoadWrite {

	public PropertiesLoadWrite() {}
	
	Properties prop = new Properties();
	OutputStream out = null;
	InputStream in = null;
	

	public void storeProperties(String propName, String propValue) {
		try {
			if(propName!=null && propValue!=null) {
				out = new FileOutputStream("src/application/config.properties");
				prop.setProperty(propName, propValue);
				prop.store(out, null);
			}
		} catch(IOException io) {
			io.printStackTrace();
		}finally {
			if(out!=null) {
				try {
					out.close();
				}catch(IOException io) {
					io.printStackTrace();
				}
			}
		}
	}
	
	public String loadProperties(String properties) {
		try {
			File f = new File("src/application/config.properties");
			in = new FileInputStream(f);
			prop.load(in);
		} catch (IOException ex) {
			ex.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		return prop.getProperty(properties);
	}
}



