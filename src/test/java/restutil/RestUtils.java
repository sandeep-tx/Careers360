package restutil;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;

import runner.MyTestRunner;
import utilities.HTMLReportUtil;

import static com.jayway.restassured.RestAssured.*;

import org.testng.Assert;

public class RestUtils {
    // Global Setup Variables
    public static String path;
    public static String jsonPathTerm;
    static RequestSpecification rs;
    // public static

    // Sets Base URI
    public static void setBaseURI(String baseURI, String logStep) {
        RestAssured.baseURI = baseURI;
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    // Sets base path
    public static void setBasePath(String basePathTerm, String logStep) {
        RestAssured.basePath = basePathTerm;
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    // Reset Base URI (after test)
    public static void resetBaseURI(String logStep) {
        RestAssured.baseURI = null;
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    // Reset base path
    public static void resetBasePath(String logStep) {
        RestAssured.basePath = null;
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    // Sets ContentType
    public static void setContentBodyType(ContentType Type, String body, String logStep) {
        rs = given().contentType(Type).body(body);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    public static void setContentBodyType(ContentType Type, String accessToken, String body, String logStep) {
        rs = given().contentType(Type).header("Authorization", accessToken).body(body).log().all();
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
    }

    // Returns response
    public static Response getResponse(String logStep) {
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return rs.when().get();
    }

    // Returns response
    public static Response PostResponse(String type, String logStep) {
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return rs.when().post("");
    }

    // Returns JsonPath object
    public static JsonPath getJsonPath(Response res, String logStep) {
        String json = res.asString();
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return new JsonPath(json);
    }

    // Verify the http response status returned. Check Status Code is 200?
    public static void checkStatusIs200(Response res) {
        Assert.assertEquals(200, res.getStatusCode(), "Status Check Failed!");
    }

    public static Object getValueFromJson(Response res, String path, String logStep) {
        String json = res.asString();
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return new JsonPath(json).getString(path);

    }

    public static void setHeader(String headerName, String headerValue, String logStep) {
        rs.header(headerName, headerValue);
        if (logStep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, "Set header " + headerName + "with Value " + headerValue);
    }
}