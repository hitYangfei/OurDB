
package hit.ourdb.worker;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import hit.ourdb.*;
import hit.ourdb.NIO.*;
import java.util.ArrayList;
import java.io.IOException;

public class BackendWorker {
  private static final Logger logger = Logger.getLogger(BackendWorker.class);
  private static BackendWorker worker = new BackendWorker();
  private Map<String, DataServer> dataServerMap = new HashMap<String, DataServer>();
  private Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
  private Catalog catalog;
  private NIOWorker nioworker;
  private BackendWorker() {
    try {
      nioworker = new NIOWorker();
      nioworker.startup();
    } catch(IOException e) {
      logger.info("error happend in BackendWorker");
    }
  }
  public static BackendWorker instance() {
    return worker;
  }
  public NIOWorker getNIOWorker() {
    return nioworker;
  }
  public void init() {
    /*
     *  TODO read config file to init data driver level
     */
    

    // now just build a 1->2 1->3 master-slave replication datasource
    DataServer s1 = new DataServer("master", "root", "", "172.16.90.12", 13001);
    DataServer s2 = new DataServer("slave1", "root", "", "172.16.90.12", 13002);
    DataServer s3 = new DataServer("slave2", "root", "", "172.14.90.12", 13003);
    dataServerMap.put(s1.getName(), s1);
    dataServerMap.put(s2.getName(), s2);
    dataServerMap.put(s3.getName(), s3);
    DataSource ds1 = new ServerDataSource("rep_master", s1);
    DataSource ds2 = new ServerDataSource("rep_read_slave1", s2);
    DataSource ds3 = new ServerDataSource("rep_read_slave2", s3);
    ArrayList<DataSource> sources = new ArrayList<DataSource>();
    sources.add(ds2);
    sources.add(ds3);
    DataSource ds4 = new LoadBalanceDataSource("rep_read", sources);
    DataSource ds5 = new ReplicationDataSource("rep", ds4, ds1);
    dataSourceMap.put(ds1.getName(), ds1);
    dataSourceMap.put(ds2.getName(), ds2);
    dataSourceMap.put(ds3.getName(), ds3);
    dataSourceMap.put(ds4.getName(), ds4);
    dataSourceMap.put(ds5.getName(), ds5);
    catalog = new Catalog(ds5);
    ds5.initConnPool();
  }
}
