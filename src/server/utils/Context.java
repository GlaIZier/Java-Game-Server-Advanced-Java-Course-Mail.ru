package server.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Context pattern to save all necessary methods in one instance
public class Context {
   
   private final Map<Class<?>, Object> context;
   
   @SafeVarargs
   public Context(AbstractMap.SimpleEntry<Class<?>, Object> ... servicesToImpl) {
      this.context = new ConcurrentHashMap<>();
      for (AbstractMap.SimpleEntry<Class<?>, Object> sToI : servicesToImpl) {
         if (!this.context.containsKey(sToI.getKey() ) )
               this.context.put(sToI.getKey(), sToI.getValue());
      }
   }

   public Object getImplementation(Class<?> clazz) {
      return context.get(clazz);
   }

}
