package server.message_system.base;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.utils.TimeHelper;

// Lazy concurrent Singlton
public class MessageSystem {
   
   private final Map<Address, ConcurrentLinkedQueue<Msg>> messages = new ConcurrentHashMap<>();
   
   private final AddressService addressService = new AddressService();
   
   public static MessageSystem getInstance() {
      return InstanceHolder.INSTANCE;
   }
   
   private static class InstanceHolder {
      private static final MessageSystem INSTANCE = new MessageSystem();
      private InstanceHolder() {};
   }
   
   private MessageSystem() {};
   
   public void addService(Abonent abonent) {
      addressService.setAddress(abonent);
      messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<Msg>() );
   }
   
   public void sendMessage(Msg msg) {
      Queue<Msg> queueToSend = messages.get(msg.getTo());
      queueToSend.add(msg);
   }
   
   public void execFor(Abonent abonent) {
      Address address = abonent.getAddress();
      Queue<Msg> queueForExec = messages.get(address);
      while (!queueForExec.isEmpty() ) {
         Msg msg = queueForExec.poll();
         msg.exec(abonent);
      }
   }

   public AddressService getAddressService() {
      return addressService;
   }   

   public void execWithDynamicSleepFor(Abonent abonent) {
      long startTime = System.currentTimeMillis();
      execFor(abonent);
      long deltaTime = System.currentTimeMillis() - startTime;
      float load = deltaTime / TimeHelper.SERVER_TICK_IN_MILLIS;
      if (load < 1)
         TimeHelper.sleep(TimeHelper.SERVER_TICK_IN_MILLIS);
   }
}
