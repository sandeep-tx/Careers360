package restutil;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.module.jsv.JsonSchemaValidator;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import org.apache.commons.codec.binary.Base64;
import utilities.ConfigReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
    RestAssured restAssured;
    RequestSpecification requestSpecification;
    String resourceBaseURI;
    String resourceEndpoint;
    String endPoint;
    HashMap<String, String> dataMap = null;
    private String testCase_id;
    private String environment;

    /**
     *
     */
    public RequestUtil(HashMap<String, String> map, String tc_id, String env) {
        restAssured = new RestAssured();
        requestSpecification = RestAssured.given();
        dataMap = map;
        testCase_id = tc_id;
        environment = env;
    }

    /*
     * Method used to set Endpoint Value
     */
    public void setEndpointValue() {
        this.endPoint = getApiBaseUrl(environment, dataMap.get("API_TYPE")) + dataMap.get("Endpoint");
        restAssured.baseURI = getApiBaseUrl(environment, dataMap.get("API_TYPE")) + dataMap.get("Endpoint");
    }

    public void generateToken() {
        try {
            String username = ConfigReader.getValue(environment + "_" + dataMap.get("API_TYPE") + "_Username");
            String password = ConfigReader.getValue(environment + "_" + dataMap.get("API_TYPE") + "_Password");
            String input = null;
            input = readFile("\\src\\test\\resources\\token\\token_payload.txt");
            Response response = RestAssured.given().auth().preemptive().basic(username, password).body(input)
                    .contentType("application/x-www-form-urlencoded").when()
                    .post(ConfigReader.getValue(environment + "_" + dataMap.get("API_TYPE") + "_Token_URL")).then()
                    .extract().response();
            String token = response.jsonPath().getString("access_token");
            setBearerAuthentication(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Method used to set BaseURI Value
     */
    public void setResourceBaseURI(String baseURI) {
        System.out.println("Base URI: " + baseURI);
        resourceBaseURI = baseURI;
    }

    /*
     * Method used to set BaseURI Value
     */
    public void setResourceEndpoint(String endpoint) {
        System.out.println("Base URI: " + endpoint);
        resourceEndpoint = endpoint;

    }

    /**
     * Method used to set bearer authentication
     *
     * @param token Bearer token
     */
    public void setBearerAuthentication(String token) {
        requestSpecification.header("Authorization", "Bearer " + token);
//                .header("Content-Type","application/json");
    }

    public void setBasicAuthentication(String userName, String password) {
        String credentials = userName + ":" + password;
        byte[] encodedCredentials = Base64.encodeBase64(credentials.getBytes());
        String encodedCredentialsAsString = new String(encodedCredentials);
        requestSpecification.header("Authorization", "Basic " + encodedCredentialsAsString);
    }

    public void settingParameter(String parameter, String value) {
        requestSpecification.queryParam(parameter, value);
    }

    public Response executeGETAPIRequest() {
        Response response = requestSpecification.get(endPoint);
//        Allure.addAttachment("Response", response.asInputStream());
        return response;
    }

    public void executeGETAPIRequestAndVerifySchema() {
        try {
            requestSpecification.get(endPoint).then()
                    .assertThat().
                    body(JsonSchemaValidator.matchesJsonSchemaInClasspath("SchemaValidation\\" + testCase_id + "Schema.json"));
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Response executePOSTAPIRequest(String payload) {
        Response response = requestSpecification.body(payload).post(endPoint);
//        Allure.addAttachment("Response", response.asInputStream());
        return response;
    }

    public Response executePOSTAPIRequest() {
        Response response = requestSpecification.post(endPoint);
        return response;
    }

    public Response executePUTAPIRequest() {
        Response response = requestSpecification.put(endPoint);
        return response;
    }

    public void setContentType(String contentType) {
        requestSpecification.header("Content-Type", contentType);
    }

    public void setPayload(String payload) {
        requestSpecification.body(payload);
    }

    public String getApiBaseUrl(String env, String type) {
        try {
            return ConfigReader.getValue(env + "_" + type + "_API");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public String getPayLoad() {
        try {
            String input = readFile("\\src\\test\\resources\\" + dataMap.get("API_TYPE") + "\\" + testCase_id + ".txt");
            return input;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void applyParameters() {
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (entry.getKey().contains("Parameter"))
                settingParameter(entry.getKey().replace("Parameter_", ""), entry.getValue());
        }
    }

}