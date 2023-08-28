package mobileutil;

//import Module.ContentCreation.ContentCreationModule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import runner.MyTestRunner;
import utilities.ConfigReader;
import utilities.HTMLReportUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.KeywordUtil;
import static java.net.URLDecoder.decode;


public class APIHelper {
    static String baseURL;
    public static String layoutOrderItem=null;
    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/test/resources/testData/" + fileName));
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

    public static List<String> earlierTitleList = new ArrayList<>();

    private static void setBaseURL() {
        if (System.getProperty("environment") != null) {
            baseURL = ConfigReader.getValue(System.getProperty("environment") + "APIBaseURL");
        } else {
            baseURL = ConfigReader.getValue("UAT" + "APIBaseURL");

        }
    }

    public static Map<String, String> validateArticleWeb(String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("WebContentDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("inlineItems");
        Map<String, String> contentList = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            contentList.put(dataList.get(i).get("type").toString(), dataList.get(i).get("id").toString());
        }
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("SECTION", dataMap.get("section").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
        return contentList;


    }
    public static Map<String, String> validateArticleBusiness(String Testdata, String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("BusinessContentDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        switch (Testdata) {
//Without inline items
            case "Testdata10":
                contentList.put("CONTENTID", contentID);
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("AUTHOR", dataMap.get("author").toString());
                contentList.put("BODY", response.jsonPath().getJsonObject("bodyItems[0].html"));
                for (Map.Entry<String, String> entry : contentList.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }
                break;
            //Inline items
            case "Testdata_CMS004":
                contentList.put("CONTENTID", contentID);
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("AUTHOR", dataMap.get("author").toString());
                //Need List of IDs for
                List<String> Ids = new ArrayList<>();
                List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("bodyItems");
                    for (int i = 0; i < dataList.size() - 1; i++) {
                        try {
                            //for article group
                            if (dataList.get(i).get("type").toString().equalsIgnoreCase("articleGroup")) {
                                List<Map<String, String>> articleGroupList = (List<Map<String, String>>) dataList.get(i).get("items");
                                for (int j = 0; j < articleGroupList.size(); j++) {
                                    Ids.add(articleGroupList.get(j).get("id"));
                                }
                            }
                            else {
                                if (!(dataList.get(i).get("type").toString().contains("HTML")))
                                    Ids.add(dataList.get(i).get("id").toString());
                            }
                            if (dataList.get(i).get("type").toString().equalsIgnoreCase("HTML")){
                                Document doc = Jsoup.parse(response.jsonPath().getJsonObject("bodyItems["+i+"].html"));
                                Elements anchorTags = doc.select("a");
                                String bodyText = "";
                                for (Element anchorTag : anchorTags) {
                                    String text = anchorTag.text();
                                    bodyText = bodyText + text;
                                    bodyText = bodyText.replace("<p>","").replace("</p>","").trim();
                                }
                                if (!bodyText.equalsIgnoreCase(""))
                                    contentList.put("BODY", bodyText);
                            }
                        } catch (java.lang.IllegalArgumentException a) {
                            continue;
                        } catch (java.lang.NullPointerException a) {
                            continue;
                        }
                    }
                contentList.put("Ids", Ids.toString());
//                Document doc = Jsoup.parse(response.jsonPath().getJsonObject("bodyItems[0].html"));
//                Elements anchorTags = doc.select("a");
//                String bodyText = "";
//                for (Element anchorTag : anchorTags) {
//                    String text = anchorTag.text();
//                    bodyText = bodyText + text;
//                }
//                contentList.put("BODY", bodyText.trim());

                break;
        }

        for (Map.Entry<String, String> entry : contentList.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        return contentList;
    }

    public static Map<String, String> validateContentForApp(String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentDetail").replace("@ContentID", contentID));
        requestSpecification.queryParam("include_related", "true");
        requestSpecification.queryParam("client", "sna_app");
        requestSpecification.queryParam("show_body_items", "true");
        requestSpecification.queryParam("groupInlineArticle", "true");
        requestSpecification.queryParam("show_keyword", "true");
        requestSpecification.queryParam("groupInlineLiveStory", "true");
        requestSpecification.queryParam("position", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();

//        List<Map<Integer,Object>>dataList=response.jsonPath().getJsonObject("body-items");
//        List<Map<Integer,Object>> subList =new ArrayList<>();
//        --------------------
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("section").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
        contentList.put("BODY", dataMap.get("body").toString());
//        contentList.put("AUTHOR",dataMap.get("author").toString());

//        for(int i=0;i<dataList.size()-1;i++)
//        {
//            if(i==0)
//            {
//               List<Object>map= new ArrayList<>();
//                map.add(dataList.get(0).get("items"));
//                Map<String,Object>topicMap= (Map<String, Object>) ((ArrayList) map.get(0)).get(0);
//                subList.add((Map<Integer, Object>) ((ArrayList) map.get(0)).get(0));
//            }
//            else
//            subList.add(i, dataList.get(i));
//        }
//
//        Map<String,String>finalContentList=new HashMap<>();
//        for(int i=0;i<subList.size();i++)
//        {
//            finalContentList.put(subList.get(i).get("type").toString(),subList.get(i).get("id").toString());
//        }
        return contentList;
//        return finalContentList;
    }

    public static Map<String, String> validateBreakingArticleForWeb(String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("WebContentDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("SECTION", dataMap.get("section").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
        return contentList;
    }

    public static Map<String, String> validateBreakingArticleForBusiness(String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("BusinessContentDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("SECTION", dataMap.get("section").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
        return contentList;
    }


    /**
     * Method used to validate Content listing for Web
     *
     * @param contentID
     * @return
     */
    public static boolean getSNAWebContentListing(String contentID, String apiType) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("WebContentListingURL"));
        Response response = requestSpecification.get();
        List<Map<String, Object>> contentItemList = response.jsonPath().getJsonObject("contentItems");
        boolean isFound = false;
        for (int i = 0; i < contentItemList.size(); i++) {
            if (contentItemList.get(i).get("id").equals(contentID)) {
                isFound = true;
                break;
            }

        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;

    }

    /**
     * Method used to validate Content listing for Business
     *
     * @param contentID
     * @return
     */
    public static boolean getSNABusinessContentListing(String contentID, String apiType) {
        String baseURL;
        if (System.getProperty("environment") != null) {
            baseURL = ConfigReader.getValue(System.getProperty("environment") + "APIBaseURL");
        } else {
            baseURL = ConfigReader.getValue("UAT" + "APIBaseURL");

        }
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("BusinessContentListingURL"));
        Response response = requestSpecification.get();

        List<Map<String, Object>> contentItemList = response.jsonPath().getJsonObject("contentItems");
        boolean isFound = false;
        for (int i = 0; i < contentItemList.size(); i++) {
            if (contentItemList.get(i).get("id").equals(contentID)) {
                isFound = true;
                break;
            }

        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor("Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;

    }

    public static Map<String, String> validateBreakingArticle(String TestData, String contentID) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("WebContentDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        switch (TestData) {
            case "Testdata10":
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                contentList.put("BODY", dataMap.get("body").toString());
                contentList.put("AUTHOR", dataMap.get("author").toString());
                break;
            case "Testdata_CMS004":
                List<String> Ids = new ArrayList<>();
//                List<Map<String,Object>>dataList=response.jsonPath().getJsonObject("bodyItems");
                List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("inlineItems");

                for (int i = 0; i < dataList.size() - 1; i++) {
                    Ids.add(dataList.get(i).get("id").toString());
                }
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());

                Document doc = Jsoup.parse(dataMap.get("body").toString());
                Elements anchorTags = doc.select("a");
                String bodyText = "";
                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.trim());
                contentList.put("AUTHOR", dataMap.get("author").toString());
                contentList.put("Ids", Ids.toString());
                break;
            default:
                break;
        }
        return contentList;
    }

    public static Map<String, String> validateContentForAppPage(String Testdata, String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentDetail").replace("@ContentID", contentID));
        requestSpecification.queryParam("include_related", "true");
        requestSpecification.queryParam("client", "sna_app");
        requestSpecification.queryParam("show_body_items", "true");
        requestSpecification.queryParam("groupInlineArticle", "true");
        requestSpecification.queryParam("show_keyword", "true");
        requestSpecification.queryParam("groupInlineLiveStory", "true");
        requestSpecification.queryParam("position", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        switch (Testdata) {
            case "Testdata10":
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                contentList.put("BODY", dataMap.get("body").toString());
                break;
            case "Testdata_CMS004":
                List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("body-items");
                List<String> Ids = new ArrayList<>();
                for (int i = 0; i < dataList.size() - 1; i++) {
                    try {
                    if (dataList.get(i).get("type").toString().equalsIgnoreCase("articleGroup")) {
                        List<Map<String, String>> articleGroupList = (List<Map<String, String>>) dataList.get(i).get("items");
                        for (int j = 0; j < articleGroupList.size(); j++) {
                            Ids.add(articleGroupList.get(j).get("id"));
                        }
                    } else {
                        if (!(dataList.get(i).get("type").toString().contains("HTML")))
                            Ids.add(dataList.get(i).get("id").toString());
                    }}catch (java.lang.IllegalArgumentException a) {
                        continue;
                    } catch (java.lang.NullPointerException a) {
                        continue;
                    }
                }
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                Document doc = Jsoup.parse(dataMap.get("body").toString());
                Elements anchorTags = doc.select("a");
                String bodyText = "";
                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.replace("<p>","").replace("</p>","").trim());
                contentList.put("Ids", Ids.toString());

                for (Map.Entry<String, String> entry : contentList.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }
                break;
        }

        return contentList;
    }

    public static Map<String, String> validateBlogDetail(String TestData, String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SnaBlogDetail").replace("@ContentID", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        Document doc = Jsoup.parse(dataMap.get("body").toString());
        Elements anchorTags = doc.select("a");
        String bodyText = "";
        switch (TestData) {
            case "TestData18":
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("sectionUrl").toString().replace("/", ""));
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());
                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.trim());

                break;
            case "TestData11":
                List<String> Ids = new ArrayList<>();
//                List<Map<String,Object>>dataList=response.jsonPath().getJsonObject("bodyItems");
                List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("inlineItems");

                for (int i = 0; i < dataList.size(); i++) {
                    Ids.add(dataList.get(i).get("id").toString());
                }
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("sectionUrl").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                contentList.put("TOPICTITLE", dataMap.get("topicTitle").toString());


                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.trim());
                contentList.put("Ids", Ids.toString());
                break;
            default:
                break;
        }
        return contentList;
    }

    public static boolean getSNABlogListing(String contentID, String apiType) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SnaBlogListing"));
        Response response = requestSpecification.get();
        List<Map<String, Object>> contentItemList = response.jsonPath().getJsonObject("contentItems");
        boolean isFound = false;
        for (int i = 0; i < contentItemList.size(); i++) {
            if (contentItemList.get(i).get("id").equals(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean getAppBlogListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBlogListing").replace("@ContentID", contentID));
        requestSpecification.queryParam("offset", "0");
        requestSpecification.queryParam("pageSize", "15");
        Response response = requestSpecification.get();
        response.prettyPrint();
        List<Map<String, Object>> contentItemList = response.jsonPath().getJsonObject("contentItems");
        boolean isFound = false;
        for (int i = 0; i < contentItemList.size(); i++) {
            if (contentItemList.get(i).get("id").equals(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }


    public static Map<String, String> validateContentForAppBlogDetail(String Testdata, String contentID) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBlogDetail").replace("@ContentID", contentID));
        requestSpecification.queryParam("include_related", "true");
        requestSpecification.queryParam("client", "sna_app");
        requestSpecification.queryParam("show_body_items", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        Document doc = Jsoup.parse(dataMap.get("body").toString());
        Elements anchorTags = doc.select("a");
        String bodyText = "";
        switch (Testdata) {
            case "TestData18":
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.trim());
                break;
            case "TestData11":
                List<Map<String, Object>> dataList = response.jsonPath().getJsonObject("body-items");
                List<String> Ids = new ArrayList<>();
                for (int i = 0; i < dataList.size() - 1; i++) {
                    if (!dataList.get(i).get("type").toString().equalsIgnoreCase("HTML"))
                        Ids.add(dataList.get(i).get("id").toString());
                }
                contentList.put("CONTENTID", contentID);
                contentList.put("SECTION", dataMap.get("section").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
                for (Element anchorTag : anchorTags) {
                    String text = anchorTag.text();
                    bodyText = bodyText + text + "\n";
                }
                contentList.put("BODY", bodyText.trim());
                contentList.put("Ids", Ids.toString());

                for (Map.Entry<String, String> entry : contentList.entrySet()) {
                    System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                }
                break;
        }

        return contentList;
    }


    public static HashMap<String, String> AppConfiguration(String label) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAAppWidgets").replace("@SectionName", "home"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        HashMap<String, String> contentList = new HashMap<>();
        ArrayList<String> loopList = response.jsonPath().getJsonObject("widgets");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("widgets[" + i + "]");
            if (dataMap.get("type").toString().equalsIgnoreCase(label.replace(" ", "_").toUpperCase())) {
                contentList.put("TYPE", label.replace(" ", "_").toUpperCase());
                contentList.put("CONTENT_COUNT", dataMap.get("contentCount").toString());
                contentList.put("CHECKBOX_STATUS", dataMap.get("active").toString());
                contentList.put("RANK", dataMap.get("rank").toString());
                break;
            }
        }


        return contentList;
    }

    public static HashMap<String, String> appsMenu(String displayName) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAAppMenu"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        HashMap<String, String> contentList = new HashMap<>();
        ArrayList<String> loopList = response.jsonPath().getJsonObject("menuItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("menuItems[" + i + "]");
            if (dataMap.get("displayName").toString().equalsIgnoreCase(displayName)) {
                contentList.put("NAME", dataMap.get("name").toString());
                contentList.put("DISPLAY_NAME", dataMap.get("displayName").toString());
                contentList.put("URL", dataMap.get("url").toString());
                contentList.put("DISPLAY_TYPE", dataMap.get("displayType").toString());
                contentList.put("ELEMENT_TYPE", dataMap.get("type").toString());
            }
        }
        return contentList;
    }

    public static HashMap<String, String> SNAWebsiteTopicDetail(String contentId) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteTopicDetail").replace("@ContentID", contentId));
        Response response = requestSpecification.get();
        response.prettyPrint();
        HashMap<String, String> contentList = new HashMap<>();
        contentList.put("Id", response.jsonPath().getJsonObject("id").toString());
        contentList.put("TYPE", response.jsonPath().getJsonObject("type").toString());
        contentList.put("TITLE", response.jsonPath().getJsonObject("headline").toString());
        contentList.put("SUMMARY", response.jsonPath().getJsonObject("summary").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0].name").toString());
        return contentList;
    }

    public static boolean SNAWebsiteTopicListing(String contentId, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteTopicListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("topics");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("topics[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentId)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentId + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentId + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean AppTopicListing(String contentId, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppTopicListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("topics");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("topics[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentId)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentId + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentId + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static HashMap<String, String> AppTopicDetail(String contentId) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppTopicDetail"));
        requestSpecification.queryParam("topics", contentId);
        requestSpecification.queryParam("types", "ARTICLE%2CIMAGE_GALLERY%2CVIDEO");
        requestSpecification.queryParam("pageSize", "15");
        requestSpecification.queryParam("offset", "0");
        Response response = requestSpecification.get();
        response.prettyPrint();
        HashMap<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("topics[0]");
        contentList.put("Id", dataMap.get("id").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        contentList.put("TITLE", dataMap.get("headline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("topics[0].tags[0].name").toString());
        return contentList;
    }

    public static HashMap<String, String> SNABusinessTopicDetail(String contentId) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessTopicDetail").replace("@ContentId", contentId));
        Response response = requestSpecification.get();
        response.prettyPrint();
        HashMap<String, String> contentList = new HashMap<>();
        contentList.put("Id", response.jsonPath().getJsonObject("id").toString());
        contentList.put("TYPE", response.jsonPath().getJsonObject("type").toString());
        contentList.put("TITLE", response.jsonPath().getJsonObject("headline").toString());
        contentList.put("SUMMARY", response.jsonPath().getJsonObject("summary").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0].name").toString());
        return contentList;
    }

    public static Map<String, String> SNAWebContentDetail(String contentID) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetail").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static Map<String, String> SNABusinessContentDetail(String contentID) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentDetail").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static boolean SNABusinessVideoListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessVideoListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean SNAWebVideoListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebVideoListing"));
        requestSpecification.queryParam("fetchContents", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;

        ArrayList<String> loopListi = response.jsonPath().getJsonObject("sectionComponents");
        ArrayList<Integer> loopListj;
        ArrayList<Integer> loopListk = new ArrayList<>();
        for (int i = 0; i < loopListi.size(); i++) {
            loopListj = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
            loopListk.add(loopListj.size());
        }
        for (int i = 0; i < loopListi.size(); i++) {
            for (int j = 0; j < loopListk.get(i); j++) {
                Map<String, Object> dataMap = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents[" + j + "]");
                if ((contentID.equalsIgnoreCase(dataMap.get("id").toString()))) {
                    Assert.assertEquals(contentID, dataMap.get("id").toString());
                    isFound = true;
                    break;
                }
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNAWebsiteProgramDetail(String url) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteProgramDetail").replace("@ProgramURL", url));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("TITLE", dataMap.get("section-title").toString().trim());
        contentList.put("NAME", dataMap.get("name").toString());
        contentList.put("DISPLAY_NAME", dataMap.get("displayName").toString());
        contentList.put("DESCRIPTION", dataMap.get("description").toString());
        contentList.put("URL", url);
        contentList.put("ABU_DHABI_TIME", dataMap.get("timezone1Time").toString());
        contentList.put("TIME_ZONE2", dataMap.get("timezone2").toString());
        contentList.put("TIME_ZONE2_TIME", dataMap.get("timezone2Time").toString());
        return contentList;
    }

    public static Boolean SNAWebsiteProgramListing(String url, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteProgramListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("programs");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("programs[" + i + "]");
            if (dataMap.get("urlFriendlySuffix").toString().equalsIgnoreCase(url)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Program URL: '" + url + "' is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Program URL: '" + url + "' is not listed while validating through " + apiType + " API"));
        }
        return isFound;

    }

    public static Map<String, String> SNAWebsitePodcastshowDetail(String url) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsitePodcastshowDetail").replace("@URL", url));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("programs[0]");
        contentList.put("TITLE", dataMap.get("title").toString().trim());
        contentList.put("SECTION_TYPE", dataMap.get("sectionType").toString().trim());
        contentList.put("NAME", dataMap.get("name").toString());
        contentList.put("DESCRIPTION", dataMap.get("description").toString());
        contentList.put("URL", url);
        contentList.put("ABU_DHABI_TIME", dataMap.get("timezone1Time").toString());
        contentList.put("TIME_ZONE2", dataMap.get("timezone2").toString());
        contentList.put("TIME_ZONE2_TIME", dataMap.get("timezone2Time").toString());
        return contentList;
    }

    public static void getRadioStreamDisplayName(String actualDisplayName, String apiType) {
        setBaseURL();
        String displayName = "";
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("OTTMainAPI"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        List<String> loopList = response.jsonPath().getJsonObject("menuItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("menuItems[" + i + "]");
            if (dataMap.get("type").toString().equalsIgnoreCase("RADIO_STREAM")) {
                displayName = dataMap.get("displayName").toString();
            }
        }
        Assert.assertEquals(actualDisplayName, displayName);
        if (actualDisplayName.equalsIgnoreCase(displayName)) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("Display name '" + displayName + "' is same, verified using " + apiType + " API"));
        }
    }

    public static boolean AppVideoListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppVideoListing"));
        requestSpecification.queryParam("fetchContents", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopListi = response.jsonPath().getJsonObject("sectionComponents");
        ArrayList<Integer> loopListj;
        ArrayList<Integer> loopListk = new ArrayList<>();
        for (int i = 0; i < loopListi.size(); i++) {
            loopListj = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
            loopListk.add(loopListj.size());
        }
        for (int i = 0; i < loopListi.size(); i++) {
            for (int j = 0; j < loopListk.get(i); j++) {
                Map<String, Object> dataMap = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents[" + j + "]");
                if ((contentID.equalsIgnoreCase(dataMap.get("id").toString()))) {
                    Assert.assertEquals(contentID, dataMap.get("id").toString());
                    isFound = true;
                    break;
                }
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> AppVideoDetail(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppVideoDetail").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static Map<String, String> SNAWebVideoDetail(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebVideoDetail").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static boolean OTTVideoListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("OTTVideoListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("items");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("items[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNAWebContentDetailSocialVideo(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetailSocialVideo").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("CONTENT_TYPE", dataMap.get("type").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static boolean SNAWebSocialVideoListing(String contentID, String apiType) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebSocialVideoListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }


    public static boolean SNAWebsiteContentListing(String programName, String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteContentListing").replace("@programName", programName));
        requestSpecification.queryParam("offset", "0");
        requestSpecification.queryParam("pageSize", "12");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean SNABusinessContentListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("episodes");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("episodes[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean OTTEpisodeListing(String contentID, String apiType, String nameID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("OTTEpisodeListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("items");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("items[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        }
        setBaseURL();
        RequestSpecification requestSpecification2 = RestAssured.given();
        requestSpecification2.baseUri(baseURL);
        requestSpecification2.basePath(ConfigReader.getValue("OTTEpisodeDetail").replace("@ProgramName", nameID));
        Response response2 = requestSpecification2.get();
        response2.prettyPrint();
        boolean isFound2 = false;
        ArrayList<String> loopListi = response2.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopListi.size(); i++) {
            Map<String, Object> dataMap = response2.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound2 = true;
                contentList.put("CONTENTID", contentID);
                contentList.put("PROGRAM_NAME", dataMap.get("programName").toString());
                contentList.put("HEADLINE", dataMap.get("headline").toString());
                contentList.put("SUMMARY", dataMap.get("summary").toString());
                contentList.put("NAME_ID", dataMap.get("nameId").toString());
                contentList.put("TYPE", dataMap.get("type").toString());
                break;
            }
        }
//        ContentCreationModule.CompareMap(ContentCreationModule.getEpisodeContent(), contentList, "OTT Episode Detail");
        return isFound;
    }

    public static Map<String, String> SNABusinessContentDetailIqtisad(String contentID, String apiType) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentDetailIqtisad").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("AUTHOR_NAME", dataMap.get("author").toString());
        contentList.put("TYPE", dataMap.get("type").toString());

        return contentList;
    }

    public static boolean SNABusinessContentListingIqtisad(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentListingIqtisad"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNAWebContentDetailImageGallery(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetailImageGallery").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SECTION", dataMap.get("defaultSectionTitle").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static boolean SNAWebContentListingImageGallery(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentListingImageGallery"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID) && dataMap.get("type").toString().equalsIgnoreCase("IMAGE_GALLERY")) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> AppContentDetailImageGallery(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentDetailImageGallery").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("SUMMARY", dataMap.get("summary").toString());
        contentList.put("SECTION", dataMap.get("defaultSectionTitle").toString());
        contentList.put("TAGS", response.jsonPath().getJsonObject("tags[0]"));
        return contentList;
    }

    public static boolean AppContentListingImageGallery(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentListingImageGallery"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID) && dataMap.get("type").toString().equalsIgnoreCase("IMAGE_GALLERY")) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNAWebsiteSection(String sectionName) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSection").replace("@SectionName", sectionName));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("SECTION_NAME", dataMap.get("sectionName").toString());
        return contentList;
    }

    public static boolean SNABusinessContentListingDigital(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentListingDigital"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNABusinessContentDetailDigital(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentDetailDigital").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("SOCIALMEDIAPLATFORM", dataMap.get("socialMediaPlatform").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        return contentList;
    }

    public static boolean SNABusinessContentListingIqtisadBulletin(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentListingIqtisadBulletin"));
        requestSpecification.queryParam("includeVideoAsset", "true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> SNABusinessContentDetailIqtisadBulletin(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentDetailIqtisadBulletin").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", contentID);
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SOCIALHEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("AUTHOR", dataMap.get("author").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        return contentList;
    }

    public static Map<String, String> AppTopNews() {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppTopNews"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        contentList.put("CONTENTID", response.jsonPath().getJsonObject("topNewsId").toString());
        contentList.put("NAME", response.jsonPath().getJsonObject("name").toString());
        ArrayList<String> loopList = response.jsonPath().getJsonObject("news");
        List<String> draggedContentIds = new ArrayList<>();
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("news[" + i + "]");
            draggedContentIds.add(dataMap.get("id").toString());
        }
        contentList.put("DRAGGED_CONTENT_ID", draggedContentIds.toString());
        return contentList;
    }

    public static Map<String, String> OTTMainAPI() {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("OTTMainAPI"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean status =false;
        ArrayList<String> loopListi = response.jsonPath().getJsonObject("menuItems");
        ArrayList<Integer> loopListj;
        ArrayList<Integer> loopListk = new ArrayList<>();
        for (int i = 0; i < loopListi.size(); i++) {
            try {
                loopListj = response.jsonPath().getJsonObject("menuItems[" + i + "].section.components");
                loopListk.add(loopListj.size());
            } catch (java.lang.IllegalArgumentException a){
                continue;
            }catch (java.lang.NullPointerException a){
                continue;
            }

        }
        for (int i = 0; i < loopListi.size(); i++) {
            if (status!=false){
                break;
            }
            for (int j = 0; j < loopListk.get(i); j++) {
                Map<String, Object> dataMap = response.jsonPath().getJsonObject("menuItems[" + i + "].section.components[" + j + "]");
                try {
                if ((dataMap.get("railType").toString().equalsIgnoreCase("LATEST_VIDEOS"))) {
                    contentList.put("TITLE",dataMap.get("title").toString());
                    contentList.put("Show Duration",dataMap.get("showDuration").toString());
                    contentList.put("Show Date & Time",dataMap.get("showTimeStamp").toString());
                    contentList.put("Show Title",dataMap.get("showTitle").toString());
                    status=true;
                    break;
                    }
                }catch (java.lang.NullPointerException a){
                    continue;
                }
            }
        }
            return contentList;
        }

    public static Map<String, String> SNABlogDetail(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABlogUpdateDetail").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        return contentList;
    }

    public static boolean SNABlogListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABlogUpdateListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                Assert.assertEquals(dataMap.get("id").toString(),contentID);
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean AppBlogListing(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBlogUpdateListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID)) {
                Assert.assertEquals(dataMap.get("id").toString(),contentID);
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> AppBlogDetail(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBlogUpdateDetail").replace("@ContentId", contentID));
        requestSpecification.queryParam("include_related","true");
        requestSpecification.queryParam("client","sna_app");
        requestSpecification.queryParam("show_body_items","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        return contentList;
    }

    public static Map<String, String> SNAWebsiteSectionLatestNews(String sectionName) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName", sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        contentList.put("SECTION_NAME",response.jsonPath().getJsonObject("sectionName").toString());
        List<String> ids = new ArrayList<>();
        ArrayList<String> loopListi = response.jsonPath().getJsonObject("sectionComponents");
        ArrayList<Integer> loopListj;
        ArrayList<Integer> loopListk = new ArrayList<>();
        for (int i = 0; i < loopListi.size(); i++) {
            loopListj = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
            loopListk.add(loopListj.size());
        }
        for (int i = 0; i < loopListi.size(); i++) {
            for (int j = 0; j < loopListk.get(i); j++) {
                Map<String, Object> dataMap = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents[" + j + "]");
                ids.add(dataMap.get("id").toString());
                }
            break;
            }
        contentList.put("Ids",ids.toString());
        return contentList;
    }

    public static Map<String, String> AppContentDetail(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentDetailInfographics").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("INFOGRAPHIC_CONTENT", dataMap.get("isInfographicContent").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        return contentList;
    }


    public static boolean AppContentListingInfoG(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppContentListingInfoG"));
        requestSpecification.queryParam("pageSize","15");
        requestSpecification.queryParam("page","infographic");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID) && dataMap.get("type").toString().equalsIgnoreCase("IMAGE_GALLERY")) {
                Assert.assertEquals(dataMap.get("id").toString(),contentID);
                Assert.assertEquals(dataMap.get("type").toString(),"IMAGE_GALLERY");
                Assert.assertEquals(dataMap.get("isInfographicContent").toString(),"true");
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean SNAWebsiteContentListingInfoG(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteContentListingInfoG"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID) && dataMap.get("type").toString().equalsIgnoreCase("IMAGE_GALLERY")) {
                Assert.assertEquals(dataMap.get("id").toString(),contentID);
                Assert.assertEquals(dataMap.get("type").toString(),"IMAGE_GALLERY");
                Assert.assertEquals(dataMap.get("isInfographicContent").toString(),"true");
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }


    public static Map<String, String> SNABusinessContentDetailInfoG(String contentID) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentDetailInfoG").replace("@ContentId", contentID));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("CONTENTID", dataMap.get("id").toString());
        contentList.put("HEADLINE", dataMap.get("headline").toString());
        contentList.put("SECTION", dataMap.get("sectionUrl").toString());
        contentList.put("SOCIAL_HEADLINE", dataMap.get("socialHeadline").toString());
        contentList.put("TOPIC", dataMap.get("topicTitle").toString());
        contentList.put("DESCRIPTION", dataMap.get("summary").toString());
        contentList.put("INFOGRAPHIC_CONTENT", dataMap.get("isInfographicContent").toString());
        contentList.put("TYPE", dataMap.get("type").toString());
        return contentList;
    }

    public static boolean SNABusinessContentListingInfoG(String contentID, String apiType) {
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessContentListingInfoG"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        ArrayList<String> loopList = response.jsonPath().getJsonObject("contentItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("contentItems[" + i + "]");
            if (dataMap.get("id").toString().equalsIgnoreCase(contentID) && dataMap.get("type").toString().equalsIgnoreCase("IMAGE_GALLERY")) {
                Assert.assertEquals(dataMap.get("id").toString(),contentID);
                Assert.assertEquals(dataMap.get("type").toString(),"IMAGE_GALLERY");
                Assert.assertEquals(dataMap.get("isInfographicContent").toString(),"true");
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static Map<String, String> validateHeaderMenuContentThroughAPI(String name) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessHeader"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        ArrayList<String> loopList = response.jsonPath().getJsonObject("menuItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("menuItems[" + i + "]");
            if (dataMap.get("name").toString().equalsIgnoreCase(name)) {
                contentList.put("NAME",dataMap.get("name").toString());
                contentList.put("DISPLAY_NAME",dataMap.get("displayName").toString());
                contentList.put("URL",dataMap.get("url").toString());
                contentList.put("TYPE",dataMap.get("type").toString());
                break;
            }
        }
        return contentList;
    }

    public static Map<String, String> validateFooterMenuContentThroughAPI(String displayName) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNABusinessFooter"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        ArrayList<String> loopList = response.jsonPath().getJsonObject("menuItems");
        for (int i = 0; i < loopList.size(); i++) {
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("menuItems[" + i + "]");
            if (dataMap.get("displayName").toString().equalsIgnoreCase(displayName)) {
                contentList.put("NAME",dataMap.get("name").toString());
                contentList.put("DISPLAY_NAME",dataMap.get("displayName").toString());
                contentList.put("URL",dataMap.get("url").toString());
                contentList.put("TYPE",dataMap.get("type").toString());
                break;
            }
        }
        return contentList;
    }

    public static Map<String, String> validateIqtisadTickerContent(){
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("IqtisadTickerContent"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        ArrayList<String> loopList = response.jsonPath().getJsonObject("tickerData");
        Map<String,Object> dataMap1;
        for (int i=0;i< loopList.size();i++){
            Map<String, Object> dataMap = response.jsonPath().getJsonObject("tickerData[" + i + "]");
            if (dataMap.get("type").toString().equalsIgnoreCase("LATEST_NEWS")){
                contentList.put("TYPE",dataMap.get("type").toString());
            }
        }
        return contentList;
    }

    public static Map<String, String> AppBreakingNewsListing(String notificationID){
        Map<String, String> contentList1 = new HashMap<>();
        List<Map<String, Object>> dataMap;
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBreakingNewsListing"));
        requestSpecification.queryParam("page","archive");
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("pushMessages");
        for (int i=0;i< dataMap.size();i++){
            if (dataMap.get(i).get("id").toString().equalsIgnoreCase(notificationID.trim())){
                contentList1.put("ID",dataMap.get(i).get("id").toString());
                contentList1.put("MESSAGE",dataMap.get(i).get("message").toString());
            }
        }
        return contentList1;
    }

    public static Map<String, String> SNAWebsiteBreakingNewsListing(String notificationID){
        Map<String, String> contentList2 = new HashMap<>();
        List<Map<String, Object>> dataMap;
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteBreakingNewsListing"));
        requestSpecification.queryParam("page","archive");
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("pushMessages");
        for (int i=0;i< dataMap.size();i++){
            if (dataMap.get(i).get("id").toString().equalsIgnoreCase(notificationID.trim())){
                contentList2.put("ID",dataMap.get(i).get("id").toString());
                contentList2.put("MESSAGE",dataMap.get(i).get("message").toString());
                break;
            }
        }
        return contentList2;
    }

    public static Map<String, String> SNABusinessBreakingNewsListing(String notificationID){
        Map<String, String> contentList3 = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("IqtisadTickerContent"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("tickerData");
        for (int i=0;i< dataMap.size();i++){
            try{
            dataMapNew=response.jsonPath().getJsonObject("tickerData["+i+"].contentItems");
            for (int j=0;j<dataMapNew.size();j++){
                if (dataMapNew.get(j).get("id").toString().equalsIgnoreCase(notificationID)){
                    contentList3.put("ID",dataMapNew.get(j).get("id").toString());
                    contentList3.put("MESSAGE",dataMapNew.get(j).get("message").toString());
                    break;
                }
            }}catch (java.lang.IllegalArgumentException a){
                continue;
            }catch (java.lang.NullPointerException a){
                continue;
            }

        }
        return contentList3;
    }

    public static Map<String, String> validatePushNotificationArray(){
        Map<String, String> contentList = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        List<String> Ids = new ArrayList<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("IqtisadTickerContent"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("tickerData");
        for (int i=0;i< dataMap.size();i++){
                dataMapNew=response.jsonPath().getJsonObject("tickerData["+i+"].contentItems");
                for (int j=0;j<3;j++){
                    Ids.add(dataMapNew.get(j).get("id").toString());
                    }
        }
        contentList.put("CONTENTID",Ids.toString());
        return contentList;
    }

    public static Map<String, String> AppBreakingNewsListingPushNotification(String contentCount){
        Map<String, String> contentList = new HashMap<>();
        List<String> isBreaking = new ArrayList<>();
        List<Map<String, Object>> dataMap;
        List<String> Ids = new ArrayList<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppBreakingNewsListingPushNotification"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("pushMessages");
        for (int i=0;i<Integer.parseInt(contentCount);i++){
            Ids.add(dataMap.get(i).get("id").toString());
            isBreaking.add(dataMap.get(i).get("breaking").toString());
        }
        contentList.put("CONTENTID",Ids.toString());
        contentList.put("IS_BREAKING",isBreaking.toString());
        return contentList;
    }

    public static Map<String, String> SNAWebsiteBreakingNewsListingPushNotification(String contentCount){
        Map<String, String> contentList = new HashMap<>();
        List<String> isBreaking = new ArrayList<>();
        List<Map<String, Object>> dataMap;
        List<String> Ids = new ArrayList<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteBreakingNewsListingPushNotification"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("pushMessages");
        for (int i=0;i<Integer.parseInt(contentCount);i++){
            Ids.add(dataMap.get(i).get("id").toString());
            isBreaking.add(dataMap.get(i).get("breaking").toString());
        }
        contentList.put("IS_BREAKING",isBreaking.toString());
        contentList.put("CONTENTID",Ids.toString());
        return contentList;
    }

    public static Map<String, String> AppNonBreakingListing(String contentCount){
        Map<String, String> contentList = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<String> Ids = new ArrayList<>();
        List<String> isBreaking = new ArrayList<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("AppNonBreakingListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("contentItems");
        for (int i=0;i<Integer.parseInt(contentCount);i++){
            Ids.add(dataMap.get(i).get("id").toString());
            isBreaking.add(dataMap.get(i).get("isBreaking").toString());
        }
        Collections.sort(Ids);
        contentList.put("CONTENTID",Ids.toString());
        contentList.put("IS_BREAKING",isBreaking.toString());
        return contentList;
    }

    public static Map<String, String> SNAWebsiteNonBreakingListing(String contentCount){
        Map<String, String> contentList = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<String> Ids = new ArrayList<>();
        List<String> isBreaking = new ArrayList<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteNonBreakingListing"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap=response.jsonPath().getJsonObject("contentItems");
        for (int i=0;i<Integer.parseInt(contentCount);i++){
            Ids.add(dataMap.get(i).get("id").toString());
            isBreaking.add(dataMap.get(i).get("isBreaking").toString());
        }
        Collections.sort(Ids);
        contentList.put("CONTENTID",Ids.toString());
        contentList.put("IS_BREAKING",isBreaking.toString());
        return contentList;
    }

    public static Map<String, String> validateHeaderMenu(String name) {
        Map<String, String> contentList = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebHeader"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap = response.jsonPath().getJsonObject("menuItems");
        for (int i = 0; i < dataMap.size(); i++) {
            dataMapNew=response.jsonPath().getJsonObject("menuItems["+i+"].children");
            for (int j=0;j<dataMapNew.size();j++) {
                if (dataMapNew.get(j).get("name").toString().equalsIgnoreCase(name)) {
                    contentList.put("NAME", dataMapNew.get(j).get("name").toString());
                    contentList.put("DISPLAY_NAME", dataMapNew.get(j).get("displayName").toString());
                    contentList.put("TYPE", dataMapNew.get(j).get("type").toString());
                    contentList.put("TOOL_TIP", dataMapNew.get(j).get("toolTip").toString());
                    contentList.put("ACTIVE", dataMapNew.get(j).get("active").toString());
                    break;
                }
            }
                break;
        }
        return contentList;
    }

    public static Map<String, String> validateFooterMenu(Map<String,String> actualData, String name,String apiType) {
        Map<String, String> contentList = new HashMap<>();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteFooter"));
        Response response = requestSpecification.get();
        response.prettyPrint();
        dataMap = response.jsonPath().getJsonObject("menuItems");
//        for (int i = 0; i < dataMap.size(); i++) {
        for (int i = 0; i < 4; i++) {
            dataMapNew=response.jsonPath().getJsonObject("menuItems["+i+"].children");
            for (int j=0;j<dataMapNew.size();j++) {
                if (dataMapNew.get(j).get("name").toString().equalsIgnoreCase(name)) {
                    contentList.put("NAME", dataMapNew.get(j).get("name").toString());
                    contentList.put("DISPLAY_NAME", dataMapNew.get(j).get("displayName").toString());
                    contentList.put("TYPE", dataMapNew.get(j).get("type").toString());
                    contentList.put("TOOL_TIP", dataMapNew.get(j).get("toolTip").toString());
                    contentList.put("ACTIVE", dataMapNew.get(j).get("active").toString());
                    break;
                }
            }
            CompareFooterMap(actualData,contentList,apiType,String.valueOf(i+1));
        }
        return contentList;
    }

    public static void CompareFooterMap(Map<String, String> actualData, Map<String, String> APIData, String apiType, String footerCount) {
        System.out.println("Data from Actual testdata:-------------------------------------- ");
        for (Map.Entry<String, String> entry : actualData.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        System.out.println("Data from API:-------------------------------------- ");
        for (Map.Entry<String, String> entry : APIData.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        if (actualData.size() != APIData.size())
            System.out.println("Hashmaps have different sizes");
        for (Map.Entry<String, String> entry : actualData.entrySet()) {
            String key = entry.getKey();
            String value1 = entry.getValue();
            String value2 = APIData.get(key);

            if (value2 == null) {
                System.out.println("Key '" + key + "' is not present in map2");
            } else {
                // Compare the values
                if (!value1.equals(value2)) {
                    System.out.println("Value for key '" + key + "' is different: " + actualData + " = " + value1 + ", " + APIData + " = " + value2);
                }
            }
        }
        boolean isEqualFlag = actualData.equals(APIData);
        if (isEqualFlag) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("API data and actual data is same for footer "+footerCount+" using " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor("API data and actual data is not same for footer "+footerCount+" using " + apiType + " API"));
        }
    }

    public static Map<String, String> validateVideoSectionContentUsingAPI(String sectionName) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName", sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        contentList.put("SECTION_NAME",response.jsonPath().getJsonObject("sectionName").toString());
        List<String> ids = new ArrayList<>();
        ArrayList<String> loopListi = response.jsonPath().getJsonObject("sectionComponents");
        ArrayList<Integer> loopListj;
        ArrayList<Integer> loopListk = new ArrayList<>();
        for (int i = 0; i < loopListi.size(); i++) {
            loopListj = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
            loopListk.add(loopListj.size());
        }
        for (int i = 0; i < loopListi.size(); i++) {
            for (int j = 0; j < loopListk.get(i); j++) {
                Map<String, Object> dataMap = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents[" + j + "]");
                ids.add(dataMap.get("id").toString());
            }
            break;
        }
        contentList.put("Ids",ids.toString());
        return contentList;
    }

    public static boolean getPodcastListing(String contentID, String apiType) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentListing"));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        for (int i = 0; i < dataMap.size(); i++) {
            dataMapNew = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
            for (int j=0;j<dataMapNew.size();j++) {
                if (dataMapNew.get(j).get("id").toString().equalsIgnoreCase(contentID)) {
                    isFound = true;
                    break;
                }
            }
            if (isFound!=true){
                return false;
            }
        }
        Assert.assertTrue(isFound);
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }


    public static Map<String, String> validatePodcastContent(String programName,String contentId) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetailPodcast").replace("@Program", programName));
        requestSpecification.queryParam("isPodcast","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        dataMap = response.jsonPath().getJsonObject("contentItems");
        for (int i=0;i< dataMap.size();i++) {
            if (dataMap.get(i).get("id").toString().equalsIgnoreCase(contentId)) {
                contentList.put("CONTENTID", dataMap.get(i).get("id").toString().trim());
                contentList.put("TYPE", dataMap.get(i).get("type").toString());
                contentList.put("DESCRIPTION", dataMap.get(i).get("headline").toString());
                contentList.put("PROGRAM_NAME", dataMap.get(i).get("programUrl").toString().replace("/","").trim());
                break;
            }
        }
        return contentList;
    }


    public static boolean getAudioClipListing(String contentID, String apiType) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentListingAC"));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
            for (int i = 0; i < dataMap.size(); i++) {
                dataMapNew = response.jsonPath().getJsonObject("sectionComponents[" + i + "].contents");
                try {
                for (int j = 0; j < dataMapNew.size(); j++) {
                    if (dataMapNew.get(j).get("id").toString().equalsIgnoreCase(contentID)) {
                        isFound = true;
                        break;
                    }
                }
                }catch (java.lang.IllegalArgumentException a){
                    continue;
                }catch (java.lang.NullPointerException a){
                    continue;
                }
            }
        if (isFound != true) {
            return false;
        }
        Assert.assertTrue(isFound);
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is listed while validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Content ID: " + contentID + " is not listed while validating through " + apiType + " API"));
        }
        return isFound;
    }


    public static Map<String, String> validateAudioClipContent(String programName,String contentId) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetailAC").replace("@Program", programName));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        dataMap = response.jsonPath().getJsonObject("contentItems");
        for (int i=0;i< dataMap.size();i++) {
            if (dataMap.get(i).get("id").toString().equalsIgnoreCase(contentId)) {
                contentList.put("CONTENTID", dataMap.get(i).get("id").toString().trim());
                contentList.put("TYPE", dataMap.get(i).get("type").toString());
                contentList.put("DESCRIPTION", dataMap.get(i).get("headline").toString());
                contentList.put("PROGRAM_NAME", dataMap.get(i).get("programUrl").toString().replace("/","").trim());
                break;
            }
        }
        return contentList;
    }

    public static Map<String,String> validateLiveStory(String contentId, String apiType){
        setBaseURL();
        Map<String, Object> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        if (apiType.equalsIgnoreCase("SNA Web Content Detail"))
            requestSpecification.basePath(ConfigReader.getValue("SNAWebContentDetailLiveStory").replace("@ContentId", contentId));
        else {
            requestSpecification.basePath(ConfigReader.getValue("AppContentDetailLiveStory").replace("@ContentId", contentId));
        }
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, String> contentList = new HashMap<>();
        dataMap = response.jsonPath().getJsonObject("liveStory");
        contentList.put("CONTENTID", dataMap.get("id").toString().trim());
        contentList.put("TYPE", dataMap.get("type").toString().trim());
        contentList.put("SECTION", dataMap.get("section").toString().trim());
        contentList.put("DESCRIPTION", dataMap.get("headline").toString());
        contentList.put("AUTHOR", dataMap.get("author").toString().trim());
        contentList.put("TOTAL_POSTS", dataMap.get("totalPosts").toString().trim());
        dataMapNew=response.jsonPath().getJsonObject("posts");
        List<String> postsId = new ArrayList<>();
        for (int i=0;i< dataMapNew.size();i++){
            postsId.add(dataMapNew.get(i).get("contentId").toString());
        }
        String nextPageToken;
        dataMap=response.jsonPath().getJsonObject("");
        if (dataMap.get("hasMore").toString().equalsIgnoreCase("true")){
            nextPageToken=dataMap.get("nextPageToken").toString();
            setBaseURL();
            List<Map<String, Object>> dataMapNextPage;
            RequestSpecification requestSpecificationNew = RestAssured.given();
            requestSpecificationNew.baseUri(baseURL);
            requestSpecificationNew.basePath(ConfigReader.getValue("SNAWebContentDetailLiveStory").replace("@ContentId", contentId));
            requestSpecificationNew.queryParam("nextPageToken",nextPageToken);
            Response responseNew = requestSpecificationNew.get();
            responseNew.prettyPrint();
            dataMapNextPage=responseNew.jsonPath().getJsonObject("posts");
            for (int i=0;i< dataMapNextPage.size();i++){
                postsId.add(dataMapNextPage.get(i).get("contentId").toString());
            }
        }
        Collections.sort(postsId);
        contentList.put("IDs", postsId.toString());
        return contentList;
    }

    public static boolean validateCustomHtml(String contentID, String apiType) {
        setBaseURL();
        Map<String, Object> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        if (apiType.equalsIgnoreCase("Article HTML For Web & AMP"))
            requestSpecification.basePath(ConfigReader.getValue("CustomHtmlWebAMP").replace("@ArticleId",contentID));
        else if (apiType.equalsIgnoreCase("Article HTML For Web & AMP - Business")) {
            requestSpecification.basePath(ConfigReader.getValue("ArticleHTMLForWebAMPBusiness").replace("@ArticleId",contentID));
        }
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        dataMap = response.jsonPath().getJsonObject("customHtml");
        if (dataMap.get("type").toString().equalsIgnoreCase("CUSTOM_HTML")){
            isFound = true;
        }
        Assert.assertTrue(isFound);
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + contentID + "  validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + contentID + "  validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean validateCustomHtmlForApp(String contentID, String apiType) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("ArticleHTMLForApps").replace("@ArticleId",contentID));
        requestSpecification.queryParam("include_related","true");
        requestSpecification.queryParam("client","sna_app");
        requestSpecification.queryParam("show_body_items","true");
        requestSpecification.queryParam("groupInlineArticle","true");
        requestSpecification.queryParam("show_keyword","true");
        requestSpecification.queryParam("groupInlineLiveStory","true");
        requestSpecification.queryParam("position","0");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        dataMap = response.jsonPath().getJsonObject("body-items");
        for (int i=0;i< dataMap.size();i++){
            try {
                if (dataMap.get(i).get("type").toString().equalsIgnoreCase("CUSTOM_HTML")){
                    isFound=true;
                }
            } catch (java.lang.IllegalArgumentException a){
                continue;
            }catch (java.lang.NullPointerException a){
                continue;
            }
        }

        Assert.assertTrue(isFound);
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + contentID + "  validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + contentID + "  validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean validateHtmlWidgetApps(String sectionName, String apiType) {
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("HTMLWidgetForApps").replace("@section",sectionName));
        requestSpecification.queryParam("fetch_full","true");
        requestSpecification.queryParam("fetchPollWidget","true");
        requestSpecification.queryParam("pageSize","30");
        requestSpecification.queryParam("sectionVideoWidget","true");
        requestSpecification.queryParam("fetchVideoWidget","true");
        requestSpecification.queryParam("fetchTVProgWidget","true");
        requestSpecification.queryParam("fetchRadioProgWidget","true");
        requestSpecification.queryParam("fetchBlogWidget","true");
        requestSpecification.queryParam("fetchLiveEvents","true");
        requestSpecification.queryParam("fetchMatches","true");
        requestSpecification.queryParam("showAllContent","true");
        requestSpecification.queryParam("showSectionWidgets","true");
        requestSpecification.queryParam("includePodcast","true");
        String decodedSearchQuery = decode("ARTICLE%2CVIDEO%2CIMAGE_GALLERY%2CLIVE_STORY%2CPODCAST%2CAUDIO_CLIP%2CSHORTHAND_STORY", StandardCharsets.UTF_8);
        requestSpecification.queryParam("types",decodedSearchQuery);
        requestSpecification.queryParam("page","1");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        dataMap = response.jsonPath().getJsonObject("widgets");
        for (int i=0;i< dataMap.size();i++){
            if (dataMap.get(i).get("type").toString().equalsIgnoreCase("CUSTOM_HTML")){
                isFound=true;
                break;
            }
            if (isFound==true){
                break;
            }
        }
        Assert.assertTrue(isFound);
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + sectionName + " section validating through " + apiType + " API"));
        } else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Custom HTML is present for " + sectionName + " section validating through " + apiType + " API"));
        }
        return isFound;
    }

    public static boolean validateSectionLayout(String apiType, String apiCheckFor, String sectionName, Map<String,String> actualData){
        setBaseURL();
        Map<String, Object> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        if (apiCheckFor.contains("DRAGGED_COMPONENT")){
            String draggedComponentPosition= KeywordUtil.extractNumber(apiCheckFor);
            dataMapNew = response.jsonPath().getJsonObject("sectionComponents["+(Integer.parseInt(draggedComponentPosition)-1)+"]");
            String draggedComponent = dataMapNew.get("name").toString()+" - "+dataMapNew.get("title").toString();
            Assert.assertEquals(actualData.get("DRAGGED_COMPONENT"),draggedComponent);
        }
        return isFound;
    }

    public static boolean getSectionLayoutOrder(int order, String sectionName){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        dataMapNew = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMapNew.size();i++){
            earlierTitleList.add(dataMapNew.get(i).get("name").toString());
        }
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        if (layoutOrderItem!=null){
            if (layoutOrderItem.equalsIgnoreCase(dataMap.get(order-1).get("id").toString())) {
                Assert.assertEquals(layoutOrderItem, dataMap.get(order - 1).get("id").toString());
                MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                        "Dragged component to position " + order + ",'" + dataMap.get(order - 1).get("name").toString() + "' is validated through API"));
                isFound=true;
            }
            else {
                MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                        "Dragged component to position " + order + ",'" + dataMap.get(order - 1).get("name").toString() + "' is not validated through API"));
            }
            return isFound;
        }
        else {
            layoutOrderItem=dataMap.get(order-1).get("id").toString();
                isFound = true ;
        }
        return isFound;
    }

    public static void validateComponent(String sectionName, String componentTitle, String apiType, String componentStatus){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        String val = "";
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMap.size();i++){
            val = dataMap.get(i).get("title").toString();
            if (val.equalsIgnoreCase(componentTitle)){
                isFound=true;
                break;
            }
        }
        if (isFound){
            Assert.assertTrue(isFound);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    componentStatus+" component having title: '"+val+ "' is validated through "+apiType+ "API"));
        }
        else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    componentStatus+" component having title: '"+val+ "' is not validated through "+apiType+ "API"));
        }

    }

    public static void validateDeletedComponent(String sectionName, String apiType, String componentStatus){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<String> actualTitleList=new ArrayList<>();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionCMS036").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMap.size();i++){
            actualTitleList.add(dataMap.get(i).get("title").toString());
            }
        Collections.sort(earlierTitleList);
        Collections.sort(actualTitleList);
        String deletedElement="";
        for (int i = 0; i < earlierTitleList.size(); i++) {
            if (i == earlierTitleList.size() || !earlierTitleList.get(i).equals(actualTitleList.get(i))) {
                deletedElement = actualTitleList.get(i);
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Successfully deleted component having title: '" + deletedElement + "' is validated through " + apiType + "API"));
        }
        else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Successfully deleted component having title: '" + deletedElement + "' is not validated through " + apiType + "API"));
        }
    }

//    For Iqtisad

    public static boolean validateSectionLayoutForIqtisad(String apiType, String apiCheckFor, String sectionName, Map<String,String> actualData){
        setBaseURL();
        Map<String, Object> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionIqtisad").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound = false;
        if (apiCheckFor.contains("DRAGGED_COMPONENT")){
            String draggedComponentPosition= KeywordUtil.extractNumber(apiCheckFor);
            dataMapNew = response.jsonPath().getJsonObject("sectionComponents["+(Integer.parseInt(draggedComponentPosition)-1)+"]");
            String draggedComponent = dataMapNew.get("name").toString()+" - "+dataMapNew.get("title").toString();
            Assert.assertEquals(actualData.get("DRAGGED_COMPONENT"),draggedComponent);
        }
        return isFound;
    }

    public static boolean getSectionLayoutOrderForIqtisad(int order, String sectionName){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<Map<String, Object>> dataMapNew;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionIqtisad").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        dataMapNew = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMapNew.size();i++){
            earlierTitleList.add(dataMapNew.get(i).get("name").toString());
        }
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        if (layoutOrderItem!=null){
            if (layoutOrderItem.equalsIgnoreCase(dataMap.get(order-1).get("id").toString())) {
                Assert.assertEquals(layoutOrderItem, dataMap.get(order - 1).get("id").toString());
                MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                        "Dragged component to position " + order + ",'" + dataMap.get(order - 1).get("name").toString() + "' is validated through API"));
                isFound=true;
            }
            else {
                MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                        "Dragged component to position " + order + ",'" + dataMap.get(order - 1).get("name").toString() + "' is not validated through API"));
            }
            return isFound;
        }
        else {
            layoutOrderItem=dataMap.get(order-1).get("id").toString();
            isFound = true ;
        }
        return isFound;
    }

    public static void validateComponentForIqtisad(String sectionName, String componentTitle, String apiType, String componentStatus){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionIqtisad").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        String val = "";
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMap.size();i++){
            val = dataMap.get(i).get("title").toString();
            if (val.equalsIgnoreCase(componentTitle)){
                isFound=true;
                break;
            }
        }
        if (isFound){
            Assert.assertTrue(isFound);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    componentStatus+" component having title: '"+val+ "' is validated through "+apiType+ "API"));
        }
        else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    componentStatus+" component having title: '"+val+ "' is not validated through "+apiType+ "API"));
        }

    }

    public static void validateDeletedComponentForIqtisad(String sectionName, String apiType, String componentStatus){
        setBaseURL();
        List<Map<String, Object>> dataMap;
        List<String> actualTitleList=new ArrayList<>();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSectionIqtisad").replace("@SectionName",sectionName));
        requestSpecification.queryParam("fetchContents","true");
        Response response = requestSpecification.get();
        response.prettyPrint();
        boolean isFound=false;
        dataMap = response.jsonPath().getJsonObject("sectionComponents");
        for (int i=0;i< dataMap.size();i++){
            actualTitleList.add(dataMap.get(i).get("title").toString());
        }
        Collections.sort(earlierTitleList);
        Collections.sort(actualTitleList);
        String deletedElement="";
        for (int i = 0; i < earlierTitleList.size(); i++) {
            if (i == earlierTitleList.size() || !earlierTitleList.get(i).equals(actualTitleList.get(i))) {
                deletedElement = actualTitleList.get(i);
                isFound = true;
                break;
            }
        }
        if (isFound) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(
                    "Successfully deleted component having title: '" + deletedElement + "' is validated through " + apiType + "API"));
        }
        else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.passStringGreenColor(
                    "Successfully deleted component having title: '" + deletedElement + "' is not validated through " + apiType + "API"));
        }
    }

    public static Map<String, String> SNAWebsiteSectionTitle(String sectionName) {
        Map<String, String> contentList = new HashMap<>();
        setBaseURL();
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri(baseURL);
        requestSpecification.basePath(ConfigReader.getValue("SNAWebsiteSection").replace("@SectionName", sectionName));
        Response response = requestSpecification.get();
        response.prettyPrint();
        Map<String, Object> dataMap = response.jsonPath().getJsonObject("");
        contentList.put("SECTION", dataMap.get("sectionName").toString());
        contentList.put("TITLE", dataMap.get("section-title").toString());
        return contentList;
    }
}