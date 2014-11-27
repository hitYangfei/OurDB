
package hit.ourdb;

import java.sql.Connection;

public abstract class DataSource {
  protected DataServer master;
  protected DataSourceWorkingState state;
  protected DataSourceType type;
  protected DataSource parent;
  private String name;
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
  public void handleAbnormal(DataSource target)
  {
    System.out.println("aaa");
    doHandleAbnormal(target);
  }
  public void handleWakeup(DataSource target)
  {
    System.out.println("aaa");
    doHandleWakeup(target);
  }
  public Connection getConnection(boolean isReadOnly)
  {
    return doGetConnection(isReadOnly);
  }
  public abstract DataServer doGetMaster();
  public abstract void doHandleAbnormal(DataSource target);
  public abstract void doHandleWakeup(DataSource target);
  public abstract Connection doGetConnection(boolean isReadOnly);
}
