 import org.apache.log4j.Logger;
 import org.apache.log4j.BasicConfigurator;


public class HelloLog{
  
  private static final Logger logger = Logger.getLogger(HelloLog.class);
  public static void main(String[] argvs)
  {
    BasicConfigurator.configure();
    logger.debug("Hello world.");
    logger.info("What a beatiful day.");
  }
}
