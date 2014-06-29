package frontend;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PageGenerator {
   
   private static final String TEMPLATES_DIR = "templates";
   
   private static final Configuration CFG = new Configuration();
   
   public static String getPage(String fileName, Map<String, Object> data) {
      Writer writer = new StringWriter();
      try {
         Template template = CFG.getTemplate(TEMPLATES_DIR + File.separator + fileName);
         template.process(data, writer);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return writer.toString();
   }

}
