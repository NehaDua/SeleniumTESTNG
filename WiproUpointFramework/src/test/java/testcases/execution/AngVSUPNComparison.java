package testcases.execution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.framework.components.EcsComp;
import com.framework.components.FrontendComp;
import com.framework.components.GMCFunctionality;
import com.framework.components.GenericComp;
import com.framework.components.SSOComponents;
import com.framework.components.ToolingComp;
import com.framework.utilities.CSVUtilities;
import com.framework.utilities.SeleniumUtilies;

public class AngVSUPNComparison {

	static int implicitwait = 100;
	String xpathForNavLink = "";
	String xpathForgmcCount = "";
	String xpathForHeaderLinks = "";
	String xpathForNonDataTiles = "";
	String xpathForDataTiles = "";
	String xpathForFooterLinks = "";

	public String getdateFromFile() {
		BufferedReader br = null;
		FileReader fr = null;
		String dateWithFormat = null;
		try {
			fr = new FileReader("date.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader("date.txt"));
			String line = "";
			while ((sCurrentLine = br.readLine()) != null) {
				line = line + sCurrentLine;
			}

			dateWithFormat = line;
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return dateWithFormat;

	}

	public boolean changeAndvalidateAFEnabledExpresion(String URL, String aonId, String aonPassword, String orgName,
			boolean ahStatus) {
		boolean afEnabledStatus = false;
		boolean afHomeEnabledStatus = false;
		ToolingComp.loginWithControlPanelSelectionFunctionality(selutil, "chrome", URL, aonId, aonPassword);
		if (ToolingComp.chooseOrgFunctionality(selutil, orgName)) {

			afEnabledStatus = ToolingComp.Tooling_Expression_SetTrue(selutil, "AF_ENABLED");
			if (!ahStatus) {
				afHomeEnabledStatus = ToolingComp.Tooling_Expression_SetFalse(selutil, "AF_HOME_ENABLED");
			} else {
				afHomeEnabledStatus = ToolingComp.Tooling_Expression_SetTrue(selutil, "AF_HOME_ENABLED");
			}
			if (selutil.getWebDriverInstance() != null)
				selutil.closeAllBrowsers();
		}
		return (afEnabledStatus && afHomeEnabledStatus);

	}

	public static String xmlFileName = System.getProperty("user.dir")
			+ "\\src\\test\\java\\object\\repositories\\Components.xml";
	SeleniumUtilies selutil;

	@BeforeClass
	public void setUp() {

		selutil = new SeleniumUtilies(xmlFileName);
		// GenericComp.capturingLogs();
	}

	@Parameters("csvfileName")
	@Test
	public void AngVSUPNFunctionality(String csvfileName) {
		try {
			String csvFilePath = System.getProperty("user.dir") + "\\CsvFiles\\" + csvfileName;
			File file = new File(csvFilePath);

			try {
				csvfileName = file.getName();
				csvfileName = csvfileName.split(".csv")[0];
			} catch (Exception e) {

			}
			String serverName = "", toolingurl = "", orgName = "";
			String pptCredentials = "";
			boolean angularStatus = true;
			String clientName = "";
			String urlPath = "";
			Map<String, String> mapAng = new HashMap<String, String>();
			Map<String, String> mapUPN = null;
			for (int xx = 0; xx < 2; xx++) {
				mapUPN = mapAng;
				mapAng.clear();
				ArrayList<String> navelementsText = new ArrayList<String>();

				try {
					urlPath = CSVUtilities.readRowColValueFromCSV(csvFilePath, 1, 0);
					String lrloginUserID = CSVUtilities.readRowColValueFromCSV(csvFilePath, 1, 2);
					String lrloginPassword = CSVUtilities.readRowColValueFromCSV(csvFilePath, 1, 3);
					System.out.println("lrloginUserID: " + lrloginUserID);
					System.out.println("lrloginPassword: " + lrloginPassword);
					if (xx == 0) {
						angularStatus = false;
					} else {
						angularStatus = true;
					}
					pptCredentials = CSVUtilities.readRowColValueFromCSV(csvFilePath, 1, 1);
					System.out.println(pptCredentials);
					System.out.println(urlPath);
					if (urlPath.toLowerCase().contains("ecs")) {

						String loginUserId = pptCredentials.split("/")[0];
						String userSecurePassword = pptCredentials.split("/")[1];
						String testConf1 = pptCredentials.split("/")[2];
						String testConf2 = "";
						String testConf3 = "";
						String testConf4 = "";
						try {
							testConf2 = pptCredentials.split("/")[3];
							testConf3 = pptCredentials.split("/")[4];
							testConf4 = pptCredentials.split("/")[5];
						} catch (Exception e) {
						}
						// custID =
						// readColDataBasisonRowIndexFromExcel(excelFilePath,
						// sheetNameHome, 1, 3);
						String userPassword = GenericComp.getDecryptedVal(userSecurePassword);
						try {
							clientName = urlPath.split("\\?")[0];
							clientName = clientName.split("/")[clientName.split("/").length - 2];
							clientName = clientName.toUpperCase();
						} catch (Exception e) {
						}

						try {
							serverName = urlPath.split("/web")[0];
							toolingurl = serverName + "/web/guest/lrlogin";
							orgName = urlPath.split("/web")[1].split("/")[1];
						} catch (Exception e) {
						}
						System.out.println(toolingurl);

						System.out.println("orgName:" + orgName);

						changeAndvalidateAFEnabledExpresion(toolingurl, lrloginUserID, lrloginPassword, orgName,
								angularStatus);
						EcsComp.ECS_login(selutil, "chrome", urlPath, loginUserId, userPassword, testConf1, testConf2,
								testConf3, testConf4);
						EcsComp.Ecs_searchByCustomerId(selutil, "");
						selutil.wait_Sec(2);
						EcsComp.Ecs_clickOnAccessContinueButton(selutil);
						selutil.wait_Sec(10);
						try {
							GMCFunctionality.closeGmcFlyout(selutil);
						} catch (Exception e) {
						}

					} else if (urlPath.toLowerCase().contains("index.jsp")) {
						String loginUserId = pptCredentials.split("#")[0];
						String userPassword = pptCredentials.split("#")[1];
						String subjectID = pptCredentials.split("#")[2];
						String clientID = pptCredentials.split("#")[3];
						String modelID = pptCredentials.split("#")[4];
						String serverURL = pptCredentials.split("#")[5];
						String attributeNameVal = pptCredentials.split("#")[6];

						try {
							clientName = serverURL.split("\\?")[0];
							clientName = serverURL.split("/")[clientName.split("/").length - 2];
							clientName = clientName.toUpperCase();
							System.out.println("clientName: " + clientName);
						} catch (Exception e) {
						}

						try {
							serverName = serverURL.split("/web")[0];
							toolingurl = serverName + "/web/guest/lrlogin";
							orgName = serverURL.split("/web")[1].split("/")[1];
						} catch (Exception e) {
						}
						System.out.println(toolingurl);

						System.out.println("orgName:" + orgName);
						changeAndvalidateAFEnabledExpresion(toolingurl, lrloginUserID, lrloginPassword, orgName,
								angularStatus);
						SSOComponents.loginFunctionalityForSSO(selutil, "chrome", urlPath, loginUserId, userPassword,
								subjectID, clientID, modelID, serverURL, attributeNameVal);

					} else {

						try {
							clientName = urlPath.split("\\?")[0];
							clientName = clientName.split("/")[clientName.split("/").length - 2];
							clientName = clientName.toUpperCase();
						} catch (Exception e) {
						}
						System.out.println("clientName: " + clientName);
						String loginUserId = pptCredentials.split("/")[0];
						String userPassword = pptCredentials.split("/")[1];
						String testConf1 = pptCredentials.split("/")[2];
						String testConf2 = "";
						String testConf3 = "";
						String testConf4 = "";
						try {
							testConf2 = pptCredentials.split("/")[3];
							testConf3 = pptCredentials.split("/")[4];
							testConf4 = pptCredentials.split("/")[5];
						} catch (Exception e) {
						}

						try {
							serverName = urlPath.split("/web")[0];
							toolingurl = serverName + "/web/guest/lrlogin";
							orgName = urlPath.split("/web")[1].split("/")[1];
						} catch (Exception e) {
						}
						System.out.println(toolingurl);
						System.out.println(orgName);

						changeAndvalidateAFEnabledExpresion(toolingurl, lrloginUserID, lrloginPassword, orgName,
								angularStatus);
						FrontendComp.Frontend_login(selutil, "chrome", urlPath, loginUserId, userPassword, testConf1,
								testConf2, testConf3, testConf4);

					}
					// LogEntries loginLogs =
					// selutil.getWebDriverInstance().manage().logs().get(LogType.PERFORMANCE);
					String currentURl = selutil.getWebDriverInstance().getCurrentUrl();
					if (angularStatus && !(currentURl.contains("ah-angular-afirst-web"))) {
						String url_1 = currentURl.split("/web")[0];
						url_1 = url_1 + "/ah-angular-afirst-web/#" + "web/" + orgName + "/home";
						System.out.println(url_1);
						selutil.navigateURL(url_1);
					}

					System.out.println("Logged-IN.....");

					xpathForgmcCount = CSVUtilities.readRowColValueFromCSV(csvFilePath, 6, 1);
					System.out.println(xpathForgmcCount);

					xpathForNavLink = CSVUtilities.readRowColValueFromCSV(csvFilePath, 7, 1);
					xpathForHeaderLinks = CSVUtilities.readRowColValueFromCSV(csvFilePath, 8, 1);
					xpathForNonDataTiles = CSVUtilities.readRowColValueFromCSV(csvFilePath, 9, 1);
					xpathForDataTiles = CSVUtilities.readRowColValueFromCSV(csvFilePath, 10, 1);
					System.out.println(xpathForDataTiles);
					xpathForFooterLinks = CSVUtilities.readRowColValueFromCSV(csvFilePath, 11, 1);
					System.out.println(xpathForFooterLinks);

					boolean loginStatus = false;

					WebDriverWait wait = new WebDriverWait(selutil.getWebDriverInstance(), 80);
					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
								"//a[contains(.,'Log Off')]|//a[contains(.,'Log OFF')]|//a[contains(.,'Log off')]|//ul[contains(@id,'-primary-expand-list') or contains(@id,'-primary-main-ul')]//li[contains(@class,'-primary-moving-menu-list')]/a//a[contains(.,'Log Off')]|//a[contains(.,'Log OFF')]|//a[contains(.,'Log off')]|//ul[contains(@id,'-primary-expand-list') or contains(@id,'-primary-main-ul')]//li[contains(@class,'-primary-moving-menu-list')]/a|//*[contains(@id,'-primary-expand-list')]//li[contains(@class,'al-primary-moving-menu-list')]//a[not(@class='visuallyhidden')]")));
						loginStatus = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("loginStatus: " + loginStatus);

					if (loginStatus) {

						Thread.sleep(3000);
						navelementsText = getXpathTextInArrayList(selutil.getWebDriverInstance(), xpathForNavLink);
						System.out.println(navelementsText.toString());
						mapAng.put("Nav_Links", navelementsText.toString());

						String gmcCount = "0";
						try {
							gmcCount = selutil.getWebDriverInstance().findElement(By.xpath(xpathForgmcCount)).getText()
									.trim();
							gmcCount = gmcCount.split("[\\r\\n]+")[0];
						} catch (Exception e) {
						}
						mapAng.put("gmcCount", gmcCount);
						System.out.println("gmcCount:" + gmcCount);

						ArrayList<String> headerLinkText = getXpathTextInArrayList(selutil.getWebDriverInstance(),
								xpathForHeaderLinks);
						mapAng.put("headerLinkText", headerLinkText.toString());

						System.out.println("headerLinkText: " + headerLinkText);
						List<String> footerTextList = getXpathTextInArrayList(selutil.getWebDriverInstance(),
								xpathForFooterLinks);
						mapAng.put("footerTextList", footerTextList.toString());
						System.out.println("footerTextList: " + footerTextList);
						ArrayList<String> elemYourInfoTitleText = getXpathTextInArrayList(
								selutil.getWebDriverInstance(), xpathForDataTiles);
						mapAng.put("DataTile", elemYourInfoTitleText.toString());
						System.out.println("DataTile:" + elemYourInfoTitleText);
						ArrayList<String> HightLightTitleText = getXpathTextInArrayList(selutil.getWebDriverInstance(),
								xpathForNonDataTiles);
						mapAng.put("NonDataTile", HightLightTitleText.toString());
						System.out.println("Non-DataTile:" + HightLightTitleText);

					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (selutil.getWebDriverInstance() != null) {
						long time = System.currentTimeMillis();

						String homeScreenshotPath = "", ScreenshotPath = "";
						if (angularStatus) {
							ScreenshotPath = "/UPNONOFFScreenshot/" + file.getName().split("\\.")[0]
									+ "_HomePage_Angular_" + time + ".png";
						} else {
							ScreenshotPath = "/UPNONOFFScreenshot/" + file.getName().split("\\.")[0] + "_HomePage_UPN_"
									+ time + ".png";
						}
						homeScreenshotPath = System.getProperty("user.dir") + ScreenshotPath;
						mapAng.put("ScreenshotPath", ScreenshotPath);
						System.out.println("homeScreenshotPath: " + ScreenshotPath);
						selutil.captureScreenshot(homeScreenshotPath);

					}
				}

				selutil.getWebDriverInstance().quit();

			}
			System.out.println(mapUPN);
			System.out.println(mapAng);
			compareAndCreateHTMLFile(mapUPN, mapAng, csvfileName);

		} catch (Exception e) {
		}

	}

	public static void compareAndCreateHTMLFile(Map<String, String> mapUPN, Map<String, String> mapAng,
			String csvFileName) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csvFileName + ".html"));
			String upnHeaderLink = mapUPN.get("headerLinkText");
			String upnNavLink = mapUPN.get("Nav_Links");
			String upnFooterLink = mapUPN.get("footerTextList");
			String upnGmcCount = mapUPN.get("gmcCount");
			String upnDataTile = mapUPN.get("DataTile");
			String upnNonDataTile = mapUPN.get("NonDataTile");
			String upnScreenshotPath = mapUPN.get("ScreenshotPath");
			String angHeaderLink = mapAng.get("headerLinkText");
			String angNavLink = mapAng.get("Nav_Links");
			String angFooterLink = mapAng.get("footerTextList");
			String angGmcCount = mapAng.get("gmcCount");
			String angDataTile = mapAng.get("DataTile");
			String angNonDataTile = mapAng.get("NonDataTile");
			String angScreenshotPath = mapUPN.get("ScreenshotPath");
			String headerStatus = "FAIL", navLinkStatus = "FAIL", gmcStatus = "FAIL", footerStatus = "FAIL",
					dataTileStaus = "FAIL", nonDataTileStatus = "FAIL";
			String hdrbgcolor = "#FF0000", navbgcolor = "#FF0000", gmcbgcolor = "#FF0000", footerbgcolor = "#FF0000",
					datatilebgcolor = "#FF0000", nondatatilebgcolor = "#FF0000";

			if (upnHeaderLink.equals(angHeaderLink)) {
				headerStatus = "PASS";
				hdrbgcolor = "#00FF00";
			}
			if (upnNavLink.equals(angNavLink)) {
				navLinkStatus = "PASS";
				navbgcolor = "#00FF00";
			}
			if (upnFooterLink.equals(angFooterLink)) {
				footerStatus = "PASS";
				footerbgcolor = "#00FF00";
			}
			if (upnGmcCount.equals(angGmcCount)) {
				gmcStatus = "PASS";
				gmcbgcolor = "#00FF00";
			}
			if (upnDataTile.equals(angDataTile)) {
				dataTileStaus = "PASS";
				datatilebgcolor = "#00FF00";
			}
			if (upnNonDataTile.equals(angNonDataTile)) {
				nonDataTileStatus = "PASS";
				nondatatilebgcolor = "#00FF00";
			}
			bw.write(
					"<!DOCTYPE html><html><head><meta charset='utf-8'><title>Automation Comparison Reports</title></head><body><div class='top-banner-root' style='background-color:blue'><h1 class='top-banner-title-font' style='text-align:center;'>Test Results</h1></div><style>#customers {font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;border-collapse: collapse; width: 100%;}#customers td, #customers th {border: 1px solid #ddd;padding: 8px;}#customers tr:nth-child(even){background-color: #f2f2f2;}#customers tr:hover {background-color: #ddd;}#customers th {padding-top: 12px;padding-bottom: 12px;text-align: left;background-color: #4CAF50;color: white;}</style><table id='customers' class='table table-responsive table-hover'><thead><tr><th>Home Page</th><th>Old Auth Value</th><th>New Auth Value</th><th>Status</th><tbody>"
							+ "<tr><td>Header Links</td><td>" + upnHeaderLink + "</td><td>" + angHeaderLink
							+ "</td><td bgcolor='" + hdrbgcolor + "'>" + headerStatus + "</td></tr>"
							+ "<tr><td>Nav links</td><td>" + upnNavLink + "</td><td>" + angNavLink
							+ "</td><td bgcolor='" + navbgcolor + "'>" + navLinkStatus + "</td></tr>"
							+ "<tr><td>GMC Flyout</td><td>" + upnGmcCount + "</td><td>" + angGmcCount
							+ "</td><td bgcolor='" + gmcbgcolor + "'>" + gmcStatus + "</td></tr>"
							+ "<tr><td>Non Data tiles</td><td>" + upnNonDataTile + "</td><td>" + angNonDataTile
							+ "</td><td bgcolor='" + nondatatilebgcolor + "'>" + nonDataTileStatus + "</td></tr>"
							+ "<tr><td>Data tiles</td><td>" + upnDataTile + "</td><td>" + angDataTile
							+ "</td><td bgcolor='" + datatilebgcolor + "'>" + dataTileStaus + "</td></tr>"
							+ "<tr><td>Footer Links</td><td>" + upnFooterLink + "</td><td>" + angFooterLink
							+ "</td><td bgcolor='" + footerbgcolor + "'>" + footerStatus + "</td></tr>"
							+ "<tr><td>Screenshots</td><td><a href=\"." + upnScreenshotPath
							+ "\">Link</a></td><td><a href=\"." + angScreenshotPath + "\">Link</a></td><td></tr>"
							+ "</tbody></table></body></html>");

			bw.close();
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	public String getServiceRequestsForTiles(WebDriver driver, String serviceURL, LogEntries les,
			String tileGroupName) {
		String consumerRefID = "";

		for (LogEntry le : les) {

			String strMessage = le.getMessage();
			if (strMessage.contains(serviceURL)) {

				if (strMessage.contains("tileGroupName\":\"" + tileGroupName + "\"")
						&& strMessage.contains("consumerReferenceId")) {
					System.out.println("consumerReferenceId Founded...");
					String str = strMessage.split("consumerReferenceId")[1].split(":")[1].split("}")[0];
					str = str.replaceAll("\"", "");
					try {

						str = str.split(",")[0];

					} catch (Exception e) {
					}

					str = str.substring(1, str.length() - 1);
					consumerRefID = str;

					break;

				}

			}

		}
		return consumerRefID;
	}

	public ArrayList<String> getXpathTextInArrayList(WebDriver driver, String xpath) {

		ArrayList<String> textList = new ArrayList<String>();
		try {
			WebDriverWait wait = new WebDriverWait(driver, 80);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

			List<WebElement> listElmtsList = driver.findElements(By.xpath(xpath));

			for (WebElement elem : listElmtsList) {

				if ((elem.getAttribute("innerText").trim().length() > 0
						|| elem.getAttribute("innerHTML").trim().length() > 0)
						&& (!elem.getAttribute("innerText").trim().contains("Debug Link"))
						&& (!elem.getAttribute("innerText").trim().contains("AUI().use("))
						&& (!elem.getAttribute("innerText").trim().contains("Home"))) {
					String str = "";
					if (elem.getText().trim().length() > 0) {
						str = elem.getText().trim();
					} else if (elem.getAttribute("innerText").trim().length() > 0) {
						str = elem.getAttribute("innerText").trim();
					} else {
						str = elem.getAttribute("innerHTML").trim();
					}
					textList.add(str);
				}

			}

			Object[] st = textList.toArray();
			for (Object s : st) {
				if (textList.indexOf(s) != textList.lastIndexOf(s)) {
					textList.remove(textList.lastIndexOf(s));
				}
			}

			Collections.sort(textList, new Comparator<String>() {

				public int compare(String s1, String s2) {
					return s1.compareToIgnoreCase(s2);
				}
			});
		} catch (Exception e) {
		}

		return textList;

	}

	public String checkString(String str) {
		try {
			if (str.contains("\\")) {

				str = str.replaceAll("\\\\", "");

			}
		} catch (Exception e) {
		}
		try {
			if (str.contains("\\")) {

				str = str.replace("\\", "");
			}
		} catch (Exception e) {
		}
		try {
			if (str.contains("\"")) {

				str = str.replace("\"", "");
			}
		} catch (Exception e) {
		}
		try {
			if (str.contains("\"")) {

				str = str.replaceAll("\"", "");

			}
		} catch (Exception e) {
		}
		return str;

	}

	public boolean isElementPresent(WebDriver driver, By by, int time) {
		try {
			WebElement element = new WebDriverWait(driver, time).until(ExpectedConditions.elementToBeClickable(by));

			return element.isDisplayed();
		} catch (Exception e) {

			return false;
		}
	}

}
