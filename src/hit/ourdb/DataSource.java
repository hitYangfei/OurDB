
package hit.ourdb;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import hit.ourdb.NIO.BackendConnection;

public abstract class DataSource {
  protected DataServer master;
  protected DataSourceWorkingState state;
  protected DataSourceType type;
  protected DataSource parent;
  private String name;
  protected List<BackendConnection> backendConnPool = new ArrayList<BackendConnection>(10);
  public DataSource(String name)
  {
    this.name = name;
    this.state = DataSourceWorkingState.WORKING;
    this.type = DataSourceType.UNKNOWN;
  }
  public DataSource(String name, DataServer master)
  {
    this(name);
    this.master = master;
  }
  public String getName()
  {
    return name;
  }
  public void setParent(DataSource parnet)
  {
    this.parent = parent;
  }
  public DataSource getParent()
  {
    return parent;
  }
  public void setState(DataSourceWorkingState state)
  {
    this.state = state;
  }
  public DataSourceWorkingState getState()
  {
    return state;
  }
  public void setMaster(DataServer master)
  {
    this.master = master;
  }
  public DataServer getMaster()
  {
    return doGetMaster();
  }
  public DataSourceType getType()
  {
    return type;
  }
  public void handleAbnormal(DataSource source)
  {
    doHandleAbnormal(source);
  }
  public void handleWakeup(DataSource source)
  {
    doHandleWakeup(source);
  }
  public Connection getConnection(boolean isReadOnly)
  {
    return doGetConnection(isReadOnly);
  }
  public abstract void initConnPool();
  public abstract DataServer doGetMaster();
  public abstract void doHandleAbnormal(DataSource source);
  public abstract void doHandleWakeup(DataSource source);
  public abstract Connection doGetConnection(boolean isReadOnly);
}
