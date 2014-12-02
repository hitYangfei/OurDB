
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

import hit.ourdb.*;
import hit.ourdb.NIO.*;

public class MainTest
{
  public static void main(String[] argvs)
  {
    try {
    InetSocketAddress ip = new InetSocketAddress("192.168.122.13", 3306);
    NIOConnection conn = new BackendConnection();
    NIOReactor worker = new NIOReactor("worker1");
    conn.connect(ip);
    worker.start();
    } catch(IOException e) {
      System.out.println(e);
    }
  }
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
