package tests.load;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import server.main.ServerSettings;
import server.main.ServerStarter;

public class GameEnteringLoadTest implements Runnable{
   
   private static final int THREADS_NUMBER = 50;
   
   private static final int NUMBER_CONNECTIONS_IN_THREAD = 2;
   
   private static final int MEMORY_SHOWS_DELAY_IN_MILLIS = 1000;
   
   private static final int TEST_TIME_AFTER_CON_ESTABLISHED_IN_MILLIS = 10000;

   private static final String URL = "http://localhost:8080";
   
   private static final String USER_NAME = "TestUser";
   
   private static AtomicInteger lastEnteredUser;;
   
   @Before
   public void setUp() {
      lastEnteredUser = new AtomicInteger();
      new Thread(new GameEnteringLoadTest()).start();
      String[] params = {ServerSettings.START_WITHOUT_JOIN_ARG};
      ServerStarter.main(params);
   }
   
   @Test
   public void enterToGame() {
      
      List<Thread> threads = new ArrayList<>();
      for(int i = 0; i < THREADS_NUMBER; i++) {
         threads.add(new Thread() {
            @Override
            public void run() {
               List<WebDriver> drivers = new ArrayList<>();
               
               for (int j = 0; j < NUMBER_CONNECTIONS_IN_THREAD; j++) {
                  WebDriver driver = new HtmlUnitDriver(true);
                  driver.get(URL);

                  WebElement element = driver.findElement(By.id("login"));
                  element.clear();
                  element.sendKeys(USER_NAME + lastEnteredUser.incrementAndGet() );
                  element.submit();
                  drivers.add(driver);
               }
               
               try {
                  Thread.sleep(TEST_TIME_AFTER_CON_ESTABLISHED_IN_MILLIS);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               for (WebDriver driver : drivers)
                  driver.close();
            } 
         } );
      }
      
      for (Thread t : threads) {
         t.start();
      }
      
      for (Thread t : threads) {
         try {
            t.join();
         } 
         catch (InterruptedException e) {
            e.printStackTrace();
         }
      }      
      
//      WebDriver driver = new HtmlUnitDriver(true);
//      driver.get(URL);
//
//      WebElement element = driver.findElement(By.id("login"));
//      element.clear();
//      element.sendKeys(USER_NAME + lastEnteredUser.incrementAndGet() );
//      element.submit();
//
//      try {
//         Thread.sleep(TEST_TIME_AFTER_CON_ESTABLISHED_IN_MILLIS);
//      } catch (InterruptedException e) {
//         e.printStackTrace();
//      }
//      driver.close();
               
//List<WebDriver> drivers = new ArrayList<>();
//               
//               for (int j = 0; j < NUMBER_CONNECTIONS_IN_THREAD; j++) {
//                  WebDriver driver = new HtmlUnitDriver(true);
//                  driver.get(URL);
//
//                  WebElement element = driver.findElement(By.id("login"));
//                  element.clear();
//                  element.sendKeys(USER_NAME + lastEnteredUser.incrementAndGet() );
//                  element.submit();
//                  drivers.add(driver);
//               }
//               
//               try {
//                  Thread.sleep(TEST_TIME_AFTER_CON_ESTABLISHED_IN_MILLIS);
//               } catch (InterruptedException e) {
//                  e.printStackTrace();
//               }
//               for (WebDriver driver : drivers)
//                  driver.close();
   }
   
   @AfterClass
   public static void cleanUp() {
      System.out.println("Final max mem: " + Runtime.getRuntime().maxMemory() );
      System.out.println("Final free mem: " + Runtime.getRuntime().freeMemory() );
      ServerStarter.stopServer();
      ServerStarter.joinServer();
   }

   // shows 
   @Override
   public void run() {
      while (true) {
         System.out.println("Free mem: " + Runtime.getRuntime().freeMemory() );
         try {
            Thread.sleep(MEMORY_SHOWS_DELAY_IN_MILLIS);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      
   }
}
