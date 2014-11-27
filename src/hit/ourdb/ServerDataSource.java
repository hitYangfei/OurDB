
package hit.ourdb;


import java.sql.Connection;

public class ServerDataSource extends DataSource {
  public ServerDataSource(String name, DataServer master)
  {
    super(name, master);
    this.type = DataSourceType.SERVER_DATASOURCE;
  }
  public DataServer doGetMaster()
  {
    return master;
  }
  public void doHandleAbnormal(DataSource target)
  {
    this.state = DataSourceWorkingState.SERVER_STOP;
    // todo notify parent;
  }
  public void doHandleWakeup(DataSource target)
  {
    this.state = DataSourceWorkingState.WORKING;
    // todo notify parent;
  }
  public Connection doGetConnection(boolean isReadOnly)
  {
    return master.getConn();
  }
}
