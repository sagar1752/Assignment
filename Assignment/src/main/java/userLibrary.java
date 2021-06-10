import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.testng.annotations.BeforeSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class userLibrary {

	//private Utilities utilities = new Utilities();
	protected static Map<String, String> dataMap;
//1
	public RequestSpecification createConnection(String Uri) {

		RestAssured.baseURI=Uri;

		//Create Connection With server
		RequestSpecification httpRequest = RestAssured.given();
		return httpRequest;

	}
//2
	public RequestSpecification createHeader(RequestSpecification httpRequest,String key, String value) {


		return httpRequest.header(key,value);

	}
//2.1
	public JSONObject getUserRequest(RequestSpecification httpRequest , String nameValue, String jobValue) {
		JSONObject obj = new JSONObject();

		obj.put("name", nameValue);
		obj.put("job", jobValue);
		httpRequest.body(obj);
		return obj;

	}
	//3
	public Response sendRequestType(RequestSpecification httpRequest, Method POST) {
	Response response = httpRequest.request(POST);
	return response;
	
}
	//4
	public Response printResponse(Response response) {
		response.prettyPrint();
		System.out.println(response);
		return response;
	}
	
	@BeforeSuite(alwaysRun = true)
	public void loadXMLData() {
		String key = "";
		String value = "";

		try {
			File e = null;
			String testDataFileName = getTestDataFileName();
			System.out.println("------------- Caching Test Data! File :" + testDataFileName + " ------------");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			String testDataFolder = checkIfFolderExistsInClassPath("TestDataXml")
					? "TestDataXml/"
					: (checkIfFolderExists("resources/TestDataXml")
							? "resources/TestDataXml/"
							: "resources/");
			if (testDataFolder.equals("resources/TestDataXml/")) {
				
				e = new File("/Users/sagarshinde/workspace/Assignment/"+testDataFolder + testDataFileName);
				
			}

			Document document = docBuilder.parse(e);
			NodeList nodeList = document.getElementsByTagName("*");

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == 1) {
					int noOfChildNodes = node.getChildNodes().getLength();

					for (int j = 0; j < noOfChildNodes; ++j) {
						if (node.getChildNodes().item(j).getNodeType() == 1
								&& node.getChildNodes().item(j).getNodeType() != 3
								&& node.getChildNodes().item(j).getChildNodes().getLength() <= 1) {
							key = node.getChildNodes().item(j).getNodeName().trim();
							value = node.getChildNodes().item(j).getTextContent().trim();
							dataMap.put(node.getNodeName() + "." + key, value);
						}
					}
				}
			}
		} catch (FileNotFoundException arg13) {
			System.out.println("WARNING: No test data XML found to cache.");
		} catch (Exception arg14) {
			arg14.printStackTrace();
		}

	}
	
	/////////
	
	public boolean checkIfFolderExistsInClassPath(String folderName) {
		return this.getClass().getClassLoader().getResource(folderName) != null;
	}
	
	public boolean checkIfFolderExists(String folderName) {
		boolean found = false;

		try {
			File e = new File(folderName);
			if (e.exists() && e.isDirectory()) {
				found = true;
			}
		} catch (Exception arg3) {
			arg3.printStackTrace();
		}

		return found;
	}
		
	public static String getTestDataFileName() {
		return System.getProperty("user.dir"+"\\target"+"\\cofig.properties", getConfig("testDataFile"));
	}

	public static String getConfig(String configName) {
		String configValue = "";
			try {
				configValue = getProperty("/Users/sagarshinde/workspace/Assignment/target/config.properties", configName);
			} catch (Exception arg3) {
				arg3.printStackTrace();
			}
			return configValue;
		}

	
	
	public static String getProperty(String fileName, String propertyName) {
		Properties property = new Properties();
		FileInputStream input = null;
		String propertyValue = "";

		try {
			input = new FileInputStream(fileName);
			property.load(input);
			propertyValue = property.getProperty(propertyName);
		} catch (Exception arg5) {
			arg5.printStackTrace();
		}

		return propertyValue;
	}
	
	public String getData(String data) {
		return (String) dataMap.get(data);
	}
	
	
	static {
		dataMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
	}
}

