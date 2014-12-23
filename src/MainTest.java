
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.net.InetSocketAddress;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import hit.ourdb.*;
import hit.ourdb.NIO.*;
import hit.ourdb.worker.*;

public class MainTest
{
  private static final Logger logger = Logger.getLogger(MainTest.class);
  public static void main(String[] argvs) {
    DOMConfigurator.configure("/home/yangfei/dbscale/ourdb/src/log4j.xml");
    logger.info("start test");
    BackendWorker worker = BackendWorker.instance();
    worker.init();
    /*FrontendWorker front = new FrontendWorker();
    front.NIOAcceptor.start();*/
  }
 /* public static void main(String[] argvs)
  {
    try {
        InetSocketAddress ip = new InetSocketAddress("127.0.0.1", 3306);
    BackendConnection conn = new BackendConnection(ip);
    logger.info("start a worker");
    NIOWorker  worker = new NIOWorker();
    worker.addConnect(conn);
    
    worker.startup();
    
    } catch(IOException e) {
      System.out.println(e);
    }
  }*/
  public static void DataSource()
  {
    System.out.println("ServerDataSource test........");
    DataServer s1 = new DataServer("server1", "root", "", "192.168.122.13", 3306);
    DataServer s2 = new DataServer("server2", "root", "", "192.168.122.13", 3307);
    DataSource ds1= new ServerDataSource("ServerDS1", s1);
    DataSource ds2= new ServerDataSource("ServerDS2", s2);
    doexec(ds1.getConnection(true));
    doexec(ds2.getConnection(true));
    System.out.println("RWSplitDataSource test........");
    DataSource ds3 = new RWSplitDataSource("ds3",ds1,ds2);
    doexec(ds3.getConnection(true));
    doexec(ds3.getConnection(false));
    System.out.println("LoadBalanceDataSource test........");
    ArrayList<DataSource> sources = new ArrayList<DataSource>();
    sources.add(ds1);
    sources.add(ds2);
    DataSource ds4 = new LoadBalanceDataSource("ds4", sources);
    for (int i = 0; i < 5 ; i++)
    {
      doexec(ds4.getConnection(true));
    }
    System.out.println("HADataSource test........");
    DataSource ds5 = new HADataSource("ds5", ds1, ds2);
    doexec(ds5.getConnection(true));
    ds5.handleAbnormal(ds1);
    doexec(ds5.getConnection(true));
    ds5.handleWakeup(ds1);
    doexec(ds5.getConnection(true));
  }
  public static void doexec(Connection conn)
  {
    try {
      Statement stmt1;
      stmt1 = conn.createStatement();
      ResultSet rs1 = stmt1.executeQuery("SELECT @@SERVER_ID");
      while(rs1.next()) {
        System.out.println(rs1.getString(1));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
