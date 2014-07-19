package tests.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.frontend.Logon;
import server.message_system.base.AddressService;
import server.message_system.base.MessageSystem;

public class AddressServiceTest {

   private AddressService as;
   
   @Before
   public void setUp() throws Exception {
      as = new AddressService();
   }
   
   @Test
   public void testGetAddress() {
      Logon logon = new Logon(new MessageSystem() );
      as.setAddress(logon);
      assertEquals(as.getAddress(Logon.class), logon.getAddress() );
   }

}
