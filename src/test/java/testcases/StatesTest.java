package testcases;

import org.testng.annotations.Test;

import core.Page;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;

public class StatesTest extends Page
{
  @Test(dataProvider = "dp")
  public void test(String s) throws Exception 
  {
	  WebElement ee=  driver.findElement(By.xpath("//*[@id=\"s\"]"));
		List <WebElement> l =  ee.findElements(By.tagName("option"));
	  Iterator <WebElement> it = l.iterator();
	  String x = null;
	  while(it.hasNext())
	  {
		  WebElement e = it.next();
		  String statepresent = e.getText().trim(); // website actual
		  
		   if(s.equals(statepresent))
		   {
			   x = s+" present";
			   break;
			    
		   }
		   else 
	       {
		   x = s+" not present";
		   }
		 }  // end of while
	  System.out.println(x);
	  
	  stm.executeUpdate("insert into Sheet2(status) values('"+x+"')");
	  con.commit();
	  con.close();

	  
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
