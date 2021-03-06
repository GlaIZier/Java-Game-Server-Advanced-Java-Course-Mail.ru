package test.java.functional;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import server.main.ServerSettings;
import server.main.ServerStarter;

public class GameEnteringTest {

   private static final String URL = "http://localhost:8080";
   
   private static final String USER_NAME = "a";
   
   private static final int BD_SEARCH_DELAY_IN_SECONDS = 10;
   
   @Before
   public void setUp() {
      String[] params = {ServerSettings.START_WITHOUT_JOIN_ARG};
      ServerStarter.main(params);
   }
   
   @Test
   public void enterToGame() {
      WebDriver driver = new HtmlUnitDriver(true); // true to enable js
      driver.get(URL);
      
      WebElement element = driver.findElement(By.id("login"));
      element.clear();
      element.sendKeys(USER_NAME);
      element.submit();
   
      new WebDriverWait(driver, BD_SEARCH_DELAY_IN_SECONDS).until(new ExpectedCondition<Boolean>() {
         @Override
         public Boolean apply(WebDriver d) {
            WebElement id = d.findElement(By.id("userId"));
            System.out.println("userId = " + id.getText());
            return Integer.parseInt(id.getText() ) != 0 ;
         }
         
      });

      System.out.println(driver.getPageSource());
      driver.close();
   }
   
   @AfterClass
   public static void cleanUp() {
      ServerStarter.stopServer();
      ServerStarter.joinServer();
   }
   
}
