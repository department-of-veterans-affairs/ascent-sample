package gov.va.ascent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RESTConfig  {

	Properties prop = null;
	InputStream input = null;

	public void openPropertyFile(File path) throws IOException {
		prop = new Properties();
		input = new FileInputStream(path);
		// load a properties file
		prop.load(input);
		log.info("property file for the script found");

	}

	public String getPropertyName(String pName) {
		String value = prop.getProperty(pName);
		if (null == value) {
			value = "";
		}
		return value;
	}

	public void closePropertyFile() {
		try {
			input.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
