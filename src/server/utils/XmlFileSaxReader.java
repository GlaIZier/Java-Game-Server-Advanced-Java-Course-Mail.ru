package server.utils;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XmlFileSaxReader {
   
   private final Object instance;
   
   private final Class<?> clazz;
   
   public XmlFileSaxReader(String xmlFile) {
      instance = read(xmlFile);
      clazz = instance.getClass();
   }
   
   private static Object read(String xmlFile) {
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser parser;

         parser = factory.newSAXParser();

         SaxHandler handler = new SaxHandler();
         parser.parse(xmlFile, handler);
         return handler.getInstance();
      } 
      catch (ParserConfigurationException | SAXException | IOException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   public Object getInstance() {
      return instance;
   }
   
   public Class<?> getInstanceClass() {
      return clazz;
   }

}
