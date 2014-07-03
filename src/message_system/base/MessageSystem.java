package message_system.base;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem {
   
   private final Map<Address, ConcurrentLinkedQueue<Msg>> messages = new ConcurrentHashMap<>();
   
   private final AddressService addressService = new AddressService();
   
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

}
