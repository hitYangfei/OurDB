import org.apache.log4j.*;

// How to use log4j
public class HelloLog {

    // Initialize a logging category.  Here, we get THE ROOT CATEGORY
    //static Category cat = Category.getRoot();
    // Or, get a custom category
    static Category cat = Category.getInstance(HelloLog.class);

    // From here on, log away!  Methods are: cat.debug(your_message_string),
    // cat.info(...), cat.warn(...), cat.error(...), cat.fatal(...)

    public static void main(String args[]) {
        // Try a few logging methods
        cat.debug("Start of main()");
        cat.info("Just testing a log message with priority set to INFO");
        cat.warn("Just testing a log message with priority set to WARN");
        cat.error("Just testing a log message with priority set to ERROR");
        cat.fatal("Just testing a log message with priority set to FATAL");

        // Alternate but INCONVENIENT form
        cat.log(Priority.DEBUG, "Calling init()");

        new HelloLog().init();
    }

    public void init() {
        java.util.Properties prop = System.getProperties();
        java.util.Enumeration enum1 = prop.propertyNames();

        cat.info("***System Environment As Seen By Java***");
        cat.debug("***Format: PROPERTY = VALUE***");

        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();
            cat.info(key + " = " + System.getProperty(key));
        }
    }

}
