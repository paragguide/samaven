package testcases;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import core.Page;
import java.sql.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.DataProvider;

public class FacebookTest extends Page
{
	@FindBy(xpath = xpath.AllProjectXpath.uid)
	WebElement userid;
	
	@FindBy(xpath = xpath.AllProjectXpath.pwd)
	WebElement password;
	
	@FindBy(xpath = xpath.AllProjectXpath.submit)
	WebElement submit;

	
  @Test(dataProvider = "dp")
  public void login(String uid, String pwd) 
  {
	  log.debug("checking uid "+uid+" pwd "+pwd);
	  test.log(LogStatus.INFO, "checking uid "+uid+" pwd "+pwd);
	  userid.sendKeys(uid);
	  password.sendKeys(pwd);
	  submit.click();
  }

  @DataProvider
  public Object[][] dp() throws Exception
  {
	  Object data[][] = {}; // empty 
	  if(rs != null)
	  {
	  ResultSetMetaData rsmt=rs.getMetaData();
	  int columncount=rsmt.getColumnCount();

	  rs.last(); // place on last record
	  int rowcount=rs.getRow(); // get position of last record

	  System.out.println(columncount+" , "+rowcount);
	  rs.beforeFirst(); // reset

	  data = new Object[rowcount][columncount]; //-> size of array 
	  			
	  for(int rowNum = 1 ; rowNum <= rowcount ; rowNum++)
	      { 
	  				
	  for(int colNum=1 ; colNum<= columncount; colNum++)
	       {
	                   rs.absolute(rowNum); // point to row  
	  	//String data1= rs.getString(colNum); // getting values from excel
	                   Object data1 = rs.getObject(colNum);
	  	//System.out.println(data1);
	  		data[rowNum-1][colNum-1]= data1 ; //adding table data in  array , array starts from 0
	  				}
	  			}
	  }  // end of if	
	  //System.out.println(data[0][0]);
	  return data;

  }
}
