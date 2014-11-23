package tests.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.db.mock.AccountService;
import server.message_system.base.MessageSystem;
import server.users.User;

public class AccountServiceTest {
   
   private AccountService as;
   
   private MessageSystem ms;

   @Before
   public void setUp() throws Exception {
      ms = MessageSystem.getInstance();
      as = new AccountService(ms);
   }

   @Test
   public void testGetAddress() {
      assertEquals(as.getAddress().hashCode(), 1);
   }

   @Test
   public void testGetUser() {
      User testUser = as.getUser("TestUser");
      assertEquals(testUser.getId(), 1);
      assertEquals(testUser.getName(), "TestUser");
   }

   @Test
   public void testGetMessageSystem() {
      assertEquals(as.getMessageSystem(), ms);
   }

}
