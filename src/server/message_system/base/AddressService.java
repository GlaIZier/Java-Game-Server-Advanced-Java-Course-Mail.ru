package server.message_system.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressService {
   
   private Map<Class<?>, Address> addresses = new ConcurrentHashMap<>();
   
   public Address getAddress(Class<?> abonentClass) {
      return addresses.get(abonentClass);
   }

   public void setAddress(Abonent abonent) {
      for (Class<?> interfaze : abonent.getClass().getInterfaces()) {
         if (interfaze != Runnable.class && interfaze != Abonent.class) { 
            addresses.put(interfaze, abonent.getAddress());
            return;
         }
      }
      addresses.put(abonent.getClass(), abonent.getAddress());
   }
}
