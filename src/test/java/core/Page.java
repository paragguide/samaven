package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import java.sql.*;
import java.time.Duration;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Page {
	
	public Logger log = null;
    public ExtentReports report = null;
    public ExtentTest test = null;
    public Connection con = null;
    public Statement stm = null;
    public ResultSet rs = null;
    public WebDriver driver = null;
    
    
  @Parameters({"browser","url"})  
  @BeforeMethod
  public void openBrowser(String browser,String url)
  {
	  if(browser.equals("chrome"))
		{
			driver = new ChromeDriver(); // predefined class
		}
		else if(browser.equals("edge"))
		{
			driver = new EdgeDriver();
		}
		else if(browser.equals("firefox"))
		{
			driver = new FirefoxDriver();
		}
		log.debug("lauched browser "+browser);
		test.log(LogStatus.PASS, "launched browser "+browser);
		driver.navigate().to(url); // better -> back, forward, refresh
		
		log.debug("url to tesr "+url);
		test.log(LogStatus.PASS, "url to test "+url);
		PageFactory.initElements(driver, this); // to read xpath from external file using @FindBy
		// implicit wait
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		
		
		
		 // long way
		     /*
		WebDriver.Options o= driver.manage();
		WebDriver.Window w = o.window();
		w.maximize();
		    */
		// short way
		driver.manage().window().maximize();

  }
  
  @Parameters({"scrshot"})
  @BeforeMethod(dependsOnMethods= "openBrowser")
  public void screenshot(String scrshot) throws Exception
  {
	  File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); // store img in tmp location
		String path = System.getProperty("user.dir")+"\\src\\test\\java\\screenshot\\"+scrshot+".jpg";
	FileUtils.copyFile(src, new File(path));
  }

  @AfterMethod
  public void closeBrowser() 
  {
	  driver.quit();
  }

  @Parameters({"workbook","sheet"})
  @BeforeClass
  public void makeWBConnection(String workbook,String sheet) throws Exception
  {
	  Class.forName("com.googlecode.sqlsheet.Driver");
	  String wbpath = System.getProperty("user.dir")+"//src//test//java//excel//"+workbook;
		con = DriverManager.getConnection("jdbc:xls:file:"+wbpath);
		stm = con.createStatement();
		rs = stm.executeQuery("select * from "+sheet);
  }

  @AfterClass
  public void closeWBConnection() throws Exception 
  {
	  con.commit();
	  con.close();
  }

  @Parameters({"file","dest","key"})
  @BeforeTest
  public void generateLogReport(String file,String dest,String key) throws Exception 
  {
	  if(!Boolean.parseBoolean(key))
	  {
		  throw new SkipException("skip test");
	  }
	  else
	  {
		  // Log
		  Properties p = new Properties();
		  FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+file+".properties");
	      p.load(fis);
	      p.put("log4j.appender."+dest+".File", System.getProperty("user.dir")+"//src//test//java//logs//"+file+".log");
	      PropertyConfigurator.configure(p);
	      log = Logger.getLogger(file);
	      
	      // report
	 report = new ExtentReports(System.getProperty("user.dir")+"//src//test//java//reports//"+file+".html");
	test = report.startTest(file);
	  }
  }

  @AfterTest
  public void closeReport() 
  {
	  report.endTest(test);
	  report.flush();
  }

}
