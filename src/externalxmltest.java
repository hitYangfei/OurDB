import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
public class externalxmltest {
   static Logger logger = Logger.getLogger(externalxmltest.class);
   public static void main(String args[]) {
      DOMConfigurator.configure("/home/yangfei/dbscale/ourdb/src/log4j.xml");
      logger.debug("Here is some DEBUG");
      logger.info("Here is some INFO");
      logger.warn("Here is some WARN");
      logger.error("Here is some ERROR");
      logger.fatal("Here is some FATAL");
   }
}
