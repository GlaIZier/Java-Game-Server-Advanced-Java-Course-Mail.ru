package server.frontend;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PageGenerator {
   
   private static final Configuration CFG = new Configuration();
   
   public static String getPage(String templatesDir, String fileName, Map<String, Object> data) {
      Writer writer = new StringWriter();
      try {
         Template template = CFG.getTemplate(templatesDir + File.separator + fileName);
         // merges data model with template
         template.process(data, writer);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return writer.toString();
   }

}
