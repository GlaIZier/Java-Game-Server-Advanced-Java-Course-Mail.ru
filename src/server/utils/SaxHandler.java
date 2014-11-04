package server.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {
   
   private static String CLASS_ELEMENT = "class";
   
   private String element;
   
   private Object instance;

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes)
         throws SAXException {
      if (qName.equals(CLASS_ELEMENT))
         instance = ReflectionHelper.createInstance(attributes.getValue(0));
      element = qName;
   }
   

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      if (!element.equals(CLASS_ELEMENT)) {
         ReflectionHelper.setFieldValue(instance, element, new String(ch, start, length));
      }
   }


   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      element = CLASS_ELEMENT;
   }
   
   public Object getInstance() {
      return instance;
   }

}
