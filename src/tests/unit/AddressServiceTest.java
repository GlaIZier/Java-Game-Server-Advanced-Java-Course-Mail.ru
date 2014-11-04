package tests.unit;

import static org.junit.Assert.*;

import java.util.AbstractMap;

import org.junit.Before;
import org.junit.Test;

import server.frontend.Logon;
import server.message_system.base.AddressService;
import server.message_system.base.MessageSystem;
import server.utils.Context;

public class AddressServiceTest {

   private AddressService as;
   
   @Before
   public void setUp() throws Exception {
      as = new AddressService();
   }
   
   @Test
   public void testGetAddress() {
      Context context = new Context(new AbstractMap.SimpleEntry<Class<?>, Object>(
          MessageSystem.class, MessageSystem.getInstance()));
      Logon logon = new Logon(context);
      as.setAddress(logon);
      assertEquals(as.getAddress(Logon.class), logon.getAddress() );
   }

}
