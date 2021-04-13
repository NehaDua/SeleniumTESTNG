package testcases.execution;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.framework.utilities.CSVUtilities;
import com.framework.utilities.ExcelUtilities;
import com.framework.utilities.SeleniumUtilies;

public class USTCreation {

	public static String xmlFileName = System.getProperty("user.dir")
			+ "\\src\\test\\java\\object\\repositories\\USTCreation.xml";
	SeleniumUtilies selutil;

	@BeforeClass
	public void setUp() {

		selutil = new SeleniumUtilies(xmlFileName);
		// GenericComp.capturingLogs();
	}

	static List<String> arrayList = new ArrayList<String>();
	static String lanID = System.getProperty("lanID");;
	static String emailID = System.getProperty("emailID");;
	static String browserName = System.getProperty("browserName");;
	static String encryptedLanPassword = System.getProperty("encryptedLanPassword");// "UExx|twi6776iwt|xxEUUExx|twi6776iwt|xxEU";

	@BeforeMethod
	public void collectingInputs(Method m) {
		if (m.getName().equals("ustCreation")) {
			String jobNumber = "2673";
			String jenkinsURL = "http://llbida02:9381/jenkins/view/Benfits-Multiclient/job/Ben-Multiclient-jobs/job/Client-Copy-Job/"
					+ jobNumber + "/parameters/";
			selutil.launchBroswerAndURLNavigation(jenkinsURL, browserName);
			selutil.clearText("UST_Page", "Jenkins_UserIDTF");
			selutil.setValue("UST_Page", "Jenkins_UserIDTF", lanID);
			selutil.clearText("UST_Page", "Jenkins_UserPasswordTF");
			selutil.setSecureValue("UST_Page", "Jenkins_UserPasswordTF", encryptedLanPassword);
			selutil.click("UST_Page", "Jenkins_SubmitBtn");
			System.out.println(selutil.waitForElementPresent("UST_Page", "Jenkins_JobparameterTFs", 50));
			List<WebElement> elems = selutil.getAllWebElements("UST_Page", "Jenkins_JobparameterTFs");
			System.out.println(elems.size());
			for (WebElement elem : elems) {
				arrayList.add(elem.getAttribute("value").trim());
			}
			selutil.closeAllBrowsers();
		}
	}

	@Test
	public void ustCreation() {
		String clientID = arrayList.get(0);
		String serverURL = arrayList.get(2);
		String org_Name = arrayList.get(3);
		String csvFilePath = "Lifecycle_URL.csv";
		// String sheetName = "Inputs";
		int rowCount = CSVUtilities.getCSVRowCount(csvFilePath);
		System.out.println(rowCount);

		for (int rowindex = 1; rowindex < rowCount; rowindex++) {
			String excelServerURL = CSVUtilities.readRowColValueFromCSV(csvFilePath, rowindex, 2);
			//
			System.out.println("excelServerURL: " + excelServerURL);
			if (excelServerURL.contains(serverURL)) {
				String dvTUName = CSVUtilities.readRowColValueFromCSV(csvFilePath, rowindex, 3);
				System.out.println("dvTUName: " + dvTUName);
				String cluster = CSVUtilities.readRowColValueFromCSV(csvFilePath, rowindex, 1);
				System.out.println("cluster: " + cluster);

				createUST(browserName, "https://jira.alight.com/browse/UA-2112", emailID, encryptedLanPassword,
						dvTUName, clientID, org_Name);
				// createAdaMigr("chrome",
				// "https://jira.alight.com/browse/ADAMIGR-2635",
				// "paras.jain.3@alight.com",
				// "P@ssword12", dvTUName, clientID, org_Name);

				break;
			}
		}

	}

	public boolean createUST(String browsername, String url, String userEmailID, String userPassword, String dvTUName,
			String clientID, String org_Name) {
		selutil.launchBroswerAndURLNavigation(url, browsername);

		selutil.clearText("UST_Page", "userName");
		selutil.setValue("UST_Page", "userName", userEmailID);
		selutil.clearText("UST_Page", "userPassword");
		selutil.setSecureValue("UST_Page", "userPassword", userPassword);
		selutil.click("UST_Page", "loginButton");
		String createdUSTSummary = "", text = "";
		if (selutil.waitForElementPresent("UST_Page", "editButton", 50)) {
			selutil.click("UST_Page", "MoreDropDown");
			selutil.waitForElementPresent("UST_Page", "cloneLink", 50);
			selutil.click("UST_Page", "cloneLink");
			selutil.wait_Sec(2);
			selutil.waitForElementPresent("UST_Page", "subjectTextfield", 50);
			selutil.clearText("UST_Page", "subjectTextfield");
			text = "Need to update TOMI  - " + dvTUName + "-  for client id- " + clientID + "(" + org_Name + ")";
			selutil.setValue("UST_Page", "subjectTextfield", text);
			selutil.click("UST_Page", "cloneButton");
			selutil.wait_Sec(2);
			selutil.waitForElementPresent("UST_Page", "assignVal", 50);
			selutil.implicitWaitTimeOut(10);
			selutil.mouseHover("UST_Page", "assignVal_EditButton");
			selutil.wait_Sec(2);
			selutil.click("UST_Page", "assignVal_EditButton");
			selutil.wait_Sec(5);
			selutil.waitForElementPresent("UST_Page", "assigneeSelectionDropDown", 50);
			// selutil.clearText("UST_Page", "AssigneeField");
			// selutil.wait_Sec(2);
			// selutil.setValue("UST_Page", "AssigneeField",
			// "paras.jain.3@alight.com");
			// selutil.wait_Sec(4);
			// selutil.setValue("UST_Page", "AssigneeField",
			// Keys.ENTER);

			selutil.click("UST_Page", "assigneeSelectionDropDown");

			selutil.waitForClickableElementPresent("UST_Page", "chooseAutomaticAssigneeOption", 50);
			selutil.wait_Sec(5);
			selutil.click("UST_Page", "chooseAutomaticAssigneeOption");
			selutil.wait_Sec(2);
			selutil.click("UST_Page", "updateButton");
			selutil.waitForElementPresent("UST_Page", "CreatedUSTSummary", 50);
			createdUSTSummary = selutil.getElementText("UST_Page", "CreatedUSTSummary");
			// String createdUSTSummaryInnerText =
			// selutil.getElementInnerText("UST_Page", "CreatedUSTSummary");

			System.out.println("Input Text:" + text);
			System.out.println("createdUSTSummaryText:" + createdUSTSummary);
			selutil.closeAllBrowsers();

		}
		return createdUSTSummary.trim().contains(text) && createdUSTSummary.length() > 0 && text.length() > 0;
	}

	public boolean createAdaMigr(String browsername, String url, String userEmailID, String userPassword,
			String dvTUName, String clientID, String org_Name) {
		selutil.launchBroswerAndURLNavigation(url, browsername);

		selutil.clearText("UST_Page", "userName");
		selutil.setValue("UST_Page", "userName", userEmailID);
		selutil.clearText("UST_Page", "userPassword");
		selutil.setSecureValue("UST_Page", "userPassword", userPassword);
		selutil.click("UST_Page", "loginButton");
		String createdUSTSummary = "", text = "";
		if (selutil.waitForElementPresent("UST_Page", "editButton", 50)) {
			selutil.click("UST_Page", "MoreDropDown");
			selutil.waitForElementPresent("UST_Page", "cloneLink", 50);
			selutil.click("UST_Page", "cloneLink");
			selutil.wait_Sec(2);
			selutil.waitForElementPresent("UST_Page", "subjectTextfield", 50);
			selutil.clearText("UST_Page", "subjectTextfield");
			text = "Add HRO properties in YML for Client -" + clientID + "(org name-" + org_Name + ")";
			// "Need to update TOMI - " + dvTUName + "- for client id- " +
			// clientID + "(" + org_Name + ")";
			selutil.setValue("UST_Page", "subjectTextfield", text);
			selutil.click("UST_Page", "cloneButton");
			selutil.wait_Sec(2);
			selutil.waitForElementPresent("UST_Page", "assignVal", 50);
			selutil.implicitWaitTimeOut(10);
			selutil.mouseHover("UST_Page", "assignVal_EditButton");
			selutil.wait_Sec(2);
			selutil.click("UST_Page", "assignVal_EditButton");
			selutil.wait_Sec(5);
			selutil.waitForElementPresent("UST_Page", "assigneeSelectionDropDown", 50);
			// selutil.clearText("UST_Page", "AssigneeField");
			// selutil.wait_Sec(2);
			// selutil.setValue("UST_Page", "AssigneeField",
			// "paras.jain.3@alight.com");
			// selutil.wait_Sec(4);
			// selutil.setValue("UST_Page", "AssigneeField",
			// Keys.ENTER);

			selutil.click("UST_Page", "assigneeSelectionDropDown");
			selutil.waitForClickableElementPresent("UST_Page", "chooseAutomaticAssigneeOption", 50);
			selutil.wait_Sec(5);
			selutil.click("UST_Page", "chooseAutomaticAssigneeOption");
			selutil.wait_Sec(2);
			selutil.click("UST_Page", "updateButton");
			selutil.waitForElementPresent("UST_Page", "CreatedUSTSummary", 50);
			createdUSTSummary = selutil.getElementText("UST_Page", "CreatedUSTSummary");
			String createdUSTSummaryInnerText = selutil.getElementInnerText("UST_Page", "CreatedUSTSummary");

			System.out.println("Input Text:" + text);
			System.out.println("createdUSTSummaryText:" + createdUSTSummaryInnerText);
			selutil.closeAllBrowsers();

		}
		return createdUSTSummary.trim().contains(text) && createdUSTSummary.length() > 0 && text.length() > 0;
	}
	//

}
