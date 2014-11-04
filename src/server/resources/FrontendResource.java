package server.resources;

// will reflect this class from xml file
public class FrontendResource {

   private String logonPath;
   private String logonTemplate;
   private String logonLoginParam;

   private String waitingPath;
   private String waitingTemplate;

   private String gamePath;
   private String gameTemplate;
   private String gameClicksParam;
   private String gameLoadingMsg;
   
   private String templatesDir;

   public String getTemplatesDir() {
      return templatesDir;
   }

   public String getLogonPath() {
      return logonPath;
   }

   public String getLogonTemplate() {
      return logonTemplate;
   }

   public String getLogonLoginParam() {
      return logonLoginParam;
   }

   public String getWaitingPath() {
      return waitingPath;
   }

   public String getWaitingTemplate() {
      return waitingTemplate;
   }

   public String getGamePath() {
      return gamePath;
   }
   
   public String getGameLoadingMsg() {
      return gameLoadingMsg;
   }
   
   public String getGameClicksParam() {
      return gameClicksParam;
   }

   public String getGameTemplate() {
      return gameTemplate;
   }

}
