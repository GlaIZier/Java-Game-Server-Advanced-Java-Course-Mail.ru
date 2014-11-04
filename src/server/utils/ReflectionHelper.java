package server.utils;

import java.lang.reflect.Field;

/**
 * Class to reflect objects. Uses to create resource instances from xml files
 * 
 * @author Khokhlushin Mikhail
 * 
 */

public class ReflectionHelper {

   public static Object createInstance(String className) {
      try {
         return Class.forName(className).newInstance();
      } catch (InstantiationException | IllegalAccessException
            | ClassNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static void setFieldValue(Object instance, String fieldName, String fieldValue) {
      try {
         Field field = instance.getClass().getDeclaredField(fieldName);
         field.setAccessible(true);

         if (field.getType().equals(String.class)) 
            field.set(instance, fieldValue);
         else if (field.getType().equals(int.class)) 
            field.setInt(instance, Integer.parseInt(fieldValue));
         else {
            throw new IllegalArgumentException("Unknown type of field "
                  + fieldName + ": " + field.getType()
                  + " . While trying to set up field of instance of type "
                  + instance.getClass());
         }
      } 
      catch (NoSuchFieldException | SecurityException
            | IllegalAccessException e) {
         e.printStackTrace();
      }
   }
   
}
