package message_system.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressService {
   
   private Map<Class<?>, Address> addresses = new ConcurrentHashMap<>();
   
   public Address getAddress(Class<?> abonentClass) {
      return addresses.get(abonentClass);
   }

   public void setAddress(Abonent abonent) {
      addresses.put(abonent.getClass(), abonent.getAddress());
   }
}
