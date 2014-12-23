
package hit.ourdb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


public class DataServer implements IDataServer{
  private String name;
  private String username;
  private String password;
  private String ip;
  private int port;
  private Connection conn;
  public DataServer(String name, String username, String password, String ip, int port)
  {
    this.name = name;
    this.username = username;
    this.password = password;
    this.ip = ip;
    this.port = port;
 //   initConnectionPool();
  }
  public int getPort() {
    return port;
  }
  public String getIP() {
    return ip;
  }
  public String getUsername()
  {
    return username;
  }
  public String getPassword()
  {
    return password;
  }
  public String getName()
  {
    return name;
  }
  public void initConnectionPool()
  {
    try {
      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        // todo auto-generated catch block
        e.printStackTrace();
      }
      this.conn = DriverManager.getConnection(generateConnectionUrl());
    } catch (SQLException e) {
      // todo auto-generated catch block
      e.printStackTrace();
    }
  }
  public Connection getConn()
  {
    return conn;
  }
  public void setConn(Connection conn)
  {
    this.conn = conn;
  }
  public void monitor()
  {
    // to do monitor the server every interval time
    System.out.println("monitor");
    return;
  }
  public String generateConnectionUrl()
  {
    return "jdbc:mysql://"+this.ip+":"+this.port+"/test?user="+this.username+"&password="+this.password;
  }
}
