package testcases.execution;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.framework.components.FrontendComp;
import com.framework.components.GenericComp;
import com.framework.utilities.SeleniumUtilies;

public class Sampletestcase {

	public static String xmlFileName = System.getProperty("user.dir")
			+ "\\src\\test\\java\\object\\repositories\\Components.xml";
	SeleniumUtilies selutil;

	@BeforeClass
	public void setUp() {

		selutil = new SeleniumUtilies(xmlFileName);
		//GenericComp.capturingLogs();
	}

	
//	@BeforeMethod
//	public void pre_Test(Method m)
//	{
//		if(m.getName().equals("frontEnd_LoginFlow")){
//			//perform the steps
//		}
//	}
	
	@Test(groups = "loginflow")
	public void frontEnd_LoginFlow() {

		
		FrontendComp.Frontend_login(selutil, "edge",
				selutil.getConfigFileValue("Frontend_Url"), selutil.getConfigFileValue("Frontend_UserId"),
				selutil.getConfigFileValue("Frontend_password"), selutil.getConfigFileValue("Frontend_trans1"),
				selutil.getConfigFileValue("Frontend_trans2"), selutil.getConfigFileValue("Frontend_trans3"),
				selutil.getConfigFileValue("Frontend_trans4"));
		try {
			FrontendComp.closeGmcFlyout(selutil);
		} catch (Exception e) {
		}
		assertTrue(selutil.waitForElementPresent("GMCObjects", "gmcIcon", 80),
				"GMC icon not viisble Login functionality fail");
		selutil.setValue("", "",Keys.ENTER);
		

	}

//	@Test(dependsOnGroups = "loginflow", priority = 1)
//	public void frontEnd_validateTiles() {
//
//		List<String> tileValue = selutil.getAllWebElementsText("Frontend_Home", "Frontend_dataTiles");
//		assertTrue(tileValue.contains("12Legal Information"), "Legal Info Tile not found");
//
//	}
//
//	@Test(dependsOnGroups = "loginflow", priority = 2)
//	public void frontEnd_logOFFFlow() {
//
//		selutil.click("Frontend_Home", "LogOFFLink");
//		assertTrue(selutil.waitForElementPresent("Frontend_Home", "LogOFFPage", 80), "Log OFF page not found");
//
//	}
//	
//	@Test(alwaysRun=true,priority = 3)
//	public void failedScenario() {
//
//		assertTrue(false);
//
//	}

//	@AfterClass
//	public void teardown() {
//		GenericComp.sendConsoleOutputMail("paras.jain.3@alight.com");
//		GenericComp.sendReportMail("paras.jain.3@alight.com");
//		if (selutil.getWebDriverInstance() != null)
//			selutil.closeAllBrowsers();
//	}
}
