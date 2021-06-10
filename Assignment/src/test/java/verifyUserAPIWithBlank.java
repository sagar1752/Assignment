import org.testng.annotations.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class verifyUserAPIWithBlank  extends userLibrary  implements ObjectRepositry{
	
	@Test
	public void verifyUserCreateWithBlankData() {
		
		RequestSpecification  httpRequest = createConnection(uri);
		
		getUserRequest(httpRequest,getData("name.N2"),getData("job.L2"));
		
		Response response = sendRequestType(httpRequest,Method.POST);
		
		printResponse(response);
		
		System.out.println(getData("name.N2"));
		
	}

}
