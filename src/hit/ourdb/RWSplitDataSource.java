package hit.ourdb;

import java.sql.Connection;

public class RWSplitDataSource extends DataSource {
  private DataSource write_source;
  private DataSource read_source;
  public RWSplitDataSource(String name, DataSource read_source, DataSource write_source) 
  {
    super(name);
    this.write_source = write_source;
    this.read_source = read_source;
  }
  public DataServer doGetMaster()
  {
    return write_source.getMaster();
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
    if (isReadOnly) {
      return read_source.getConnection(isReadOnly);
    }
    return write_source.getConnection(isReadOnly);
  }
  public void initConnPool() {
    write_source.initConnPool();
    //read_source.initConnPool();
  }

}
