package gov.va.ascent.util;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Assert;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RESTUtil  {

	protected String serviceURL;

	HashMap<String, String> mapQS = new HashMap<String, String>(); // stores
																	// query
																	// strings
	HashMap<String, String> mapReqHeader = new HashMap<String, String>(); // stores
																			// request
																			// headers
	HashMap<String, String> mapResponse = new HashMap<String, String>(); // stores
																			// expected
																			// response
																			// parameters
	HashMap<String, String> mapPathParam = new HashMap<String, String>(); // stores
																			// Query
																			// String
																			// parameters
	HashMap<String, String> mapFormParam = new HashMap<String, String>(); // stores
																			// Form
	String contentType = new String();
	String jsonText = new String();
	File requestFile = null;
	File responseFile = null;
	PrintStream requestStream = null;
	Response response = null; // stores response from rest
		String strEnv = "baseURL";
	/**
	 * Reads file content for a given file resource using URL object. 
	 * @param strRequestFile
	 * @param mapHeader
	 * @throws Exception
	 */
	public void setUpRequest(String strRequestFile,
			HashMap<String, String> mapHeader) throws Exception {
		try {
			mapReqHeader = mapHeader;
			if (!strRequestFile.equals("NA")) {
				URL urlFilePath = RESTUtil.class.getClassLoader().getResource(
						"Request/" + strRequestFile);
				requestFile = new File(urlFilePath.toURI());

				// Note - Enhance the code so if Header.Accept is xml, then it
				// should use something like convertToXML function
				jsonText = readFile(requestFile);
			}
		} catch (Exception ex) {
			log.info("Unable to do setUpRequest");
			ex.printStackTrace();
		}
	}
	/**
	 * Assigns given header object into local header map.
	 * @param mapHeader
	 * @throws Exception
	 */
	public void setUpRequest(HashMap<String, String> mapHeader)
			throws Exception {
		try {
			mapReqHeader = mapHeader;
		} catch (Exception ex) {
			log.info("Unable to do setUpRequest");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Invokes REST end point for a GET method using REST assured API and return response json object.
	 * @param serviceURL
	 * @return
	 */
	public String GETResponse(String serviceURL) {
		RestAssured.useRelaxedHTTPSValidation();	
		response =
				given().log().all().headers(mapReqHeader).urlEncodingEnabled(false)
						.when().get(serviceURL);
				String json = response.asString();
				
				return json;	
		}


	public String DELETEResponse(String serviceURL) {
		response =

		given().log().all().headers(mapReqHeader).urlEncodingEnabled(false)
				.when().delete( serviceURL);
		String json = response.asString();
		return json;
	}

	public String POSTResponse(String serviceURL) {
		response = given().urlEncodingEnabled(false).log().all()
				.headers(mapReqHeader).body(jsonText).when()
				.post( serviceURL);

		// System.out.println(response.asString());
		String json = response.asString();
		return json;
	}
	


	public String PUTResponse(String serviceURL) {
		response =
		// given
		given().urlEncodingEnabled(false).log().all().headers(mapReqHeader)
				.body(jsonText).when().put( serviceURL);

		// System.out.println(response.asString());
		String json = response.asString();
		return json;
	}

	/**
	 * Parses json object for a given key and match with given expected value.
	 * @param json
	 * @param strRoot
	 * @param strField
	 * @param strExpectedValue
	 * @return
	 */
	public String parseJSON(String json, String strRoot, String strField,
			String strExpectedValue) {
		String strResult = null;
		JsonPath jsonPath = new JsonPath(json).setRoot(strRoot);
		List<String> lstField = jsonPath.get(strField);
		if (lstField.contains(strExpectedValue)) {

			strResult = lstField.toString();
			System.out.println("Passed:Field=" + strField
					+ " matched the expected value=" + strExpectedValue);
			log.info("Passed:Field=" + strField
					+ " matched the expected value=" + strExpectedValue);
		} else {
			strResult = lstField.toString();
			log.info("Failed:Field=" + strField + " expected value="
				+ strExpectedValue + " and actual value="
					+ lstField.toString());
		}

		return strResult;

	}

	public String parseJSON(String json, String strRoot, String strField,
			int strExpectedValue) {
		String strResult = null;
		JsonPath jsonPath = new JsonPath(json).setRoot(strRoot);
		List<String> lstField = jsonPath.get(strField);
		if (lstField.contains(strExpectedValue)) {

			strResult = lstField.toString();
			System.out.println("Passed:Field=" + strField
					+ " matched the expected value=" + strExpectedValue);
			log.info("Passed:Field=" + strField
				+ " matched the expected value=" + strExpectedValue);
		} else {
			strResult = lstField.toString();
			log.info("Failed:Field=" + strField + " expected value="
					+ strExpectedValue + " and actual value="
					+ lstField.toString());
		}

		return strResult;
	}

	public String parseJSON(String json, String strField,
			String strExpectedValue) {
		String strResult = null;
		try {
			JsonPath jsonPath = new JsonPath(json);
			strResult = jsonPath.get(strField).toString();
			if (strResult.contains(strExpectedValue)) {
				
				
				log.info("Passed:Field=" + strField
					+ " matched the expected value=" + strExpectedValue);
			} else {
				log.info("Failed:Field=" + strField
				 + " expected value=" + strExpectedValue
				+ " and actual value=" + strResult);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			strResult = "Failed";
		}
		return strResult;
	}
	
	public String parseJSONroot(String json, String strRoot,
			String strExpectedValue) {
		String strResult = null;
		try {
			strResult =  new JsonPath(json).get(strRoot).toString();
			if (strResult.contains(strExpectedValue)) {
				System.out.println("Passed:Field=" + strRoot
						+ " matched the expected value=" + strExpectedValue);
				//this.logger.info("Passed:Field=" + strRoot
				//		+ " matched the expected value=" + strExpectedValue);
			} else {
				log.info("Failed:Field=" + strRoot
					+ " expected value=" + strExpectedValue
					+ " and actual value=" + strResult);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			strResult = "Failed";
		}
		return strResult;
	}

	public String parseXML(String xml, String strFieldName,
			String strExpectedValue) {
		String strResult = null;

		XmlPath xmlPath = new XmlPath(xml);
		String strField = xmlPath.get(strFieldName).toString();
		if (strField.contains(strExpectedValue)) {
			strResult = strField.toString();
			log.info("Passed:Field=" + strFieldName
					+ " matched the expected value=" + strExpectedValue);
		} else {
			strResult = strField.toString();
			log.info("Failed:Field=" + strFieldName + " expected value="
					+ strExpectedValue + " and actual value="
					+ strField.toString());
		}
		return strResult;
	}

	public String parseXML(String xml, String strFieldName) {
		XmlPath xmlPath = new XmlPath(xml);
		String strFieldValue = xmlPath.get(strFieldName).toString();
		return strFieldValue;
	}

	public String parseXML(String xml, String strRoot, String strFieldName,
			String strExpectedValue) {
		String strResult = null;

		XmlPath xmlPath = new XmlPath(xml).setRoot(strRoot);

		String strField = xmlPath.get(strFieldName);
		if (strField.contains(strExpectedValue)) {
			strResult = strField.toString();
			//this.logger.info("Passed:Field=" + strField
				//	+ " matched the expected value=" + strExpectedValue);
		} else {
			strResult = strField.toString();
             log.info("Failed:Field=" + strField + " expected value="
				+ strExpectedValue + " and actual value="
				+ strField.toString());
		}
		return strResult;
	}

	

	public String prettyFormatXML(String strXml) {
		String xml = strXml;
		String result = null;
		try {
			Document doc = DocumentHelper.parseText(xml);
			StringWriter sw = new StringWriter();
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter xw = new XMLWriter(sw, format);
			xw.write(doc);
			System.out.print(xw.toString());
			result = sw.toString();
		} catch (Exception ex) {
			log.error("Unable to pretty format XML");
			ex.printStackTrace();
		}
		return result;
	}

	public String CompareResponse(String strExpectedResponseFile,
			String strActualResponse) {
		String strResult = null;
		try {
			URL urlFilePath = RESTUtil.class.getClassLoader().getResource(
					"Response/" + strExpectedResponseFile);
			responseFile = new File(urlFilePath.toURI());
			String strExpectedResponse = readFile(responseFile);

			if (strActualResponse.contains(strExpectedResponse)) {
				log.info("Passed:Actual response matched the expected response file="
							+ strExpectedResponseFile);
				strResult = "Passed";
			} else {
				System.out
						.println("Failed:Actual response did not matched the expected response file");
				log.info("Failed:Actual response =" + strActualResponse
					+ " and \n expected response=" + strExpectedResponse);
				strResult = "Failed";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			strResult = "Failed";
		}
		return strResult;

	}

	public String readExpectedResponse(String filename) {
		String strExpectedResponse = null;
		try {
			URL urlFilePath = RESTUtil.class.getClassLoader().getResource(
					"Response/" + filename);
			File strFilePath = new File(urlFilePath.toURI());
			strExpectedResponse = FileUtils.readFileToString(strFilePath,
					"ASCII");
		} catch (Exception ex) {
			strExpectedResponse = "";
			ex.printStackTrace();
		}

		return strExpectedResponse;
	}

	protected String readFile(File filename) {
		String content = null;
		// File file = new File(filename); //for ex foo.txt
		File file = filename;
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	@SuppressWarnings("deprecation")
	public void ValidateStatusCode(int intStatusCode) {

		int actStatusCode = response.getStatusCode();
		try {
			Assert.assertEquals(intStatusCode, actStatusCode);
			log.info("Passed:Response status code matched the expected value="
				+ intStatusCode);

		} catch (Exception ex) {
			ex.printStackTrace();

			log.info("Failed:Expected status code=" + intStatusCode
				+ " did not match actual status code=" + actStatusCode);
		}

	}


	
}
