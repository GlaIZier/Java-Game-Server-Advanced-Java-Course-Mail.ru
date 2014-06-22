package main;

import java.util.concurrent.atomic.AtomicInteger;

public class Address {
   
   private static AtomicInteger lastCreatedAddress = new AtomicInteger();
   
   private final int abonentID;
   
   public Address() {
      abonentID = lastCreatedAddress.incrementAndGet();
   }
   
   // will use address as key in collection 
   @Override
   public int hashCode() {
      return abonentID;
   }

}
