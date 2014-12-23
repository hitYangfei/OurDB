package hit.ourdb;
import java.sql.Connection;

public class HADataSource extends DataSource {
  // only support SERVER_DATASOURCE as active and coldStandBy
  private DataSource active;
  private DataSource coldStandBy;
  private  boolean isActive;
  public HADataSource(String name, DataSource active, DataSource coldStandBy)
  {
    super(name);
    this.isActive = true;
    this.type = DataSourceType.HA_DATASOURCE;
    this.active = active;
    this.coldStandBy = coldStandBy;

    if (active.getType() != DataSourceType.SERVER_DATASOURCE
        || coldStandBy.getType() != DataSourceType.SERVER_DATASOURCE) {
      // should init failed and throw Execption
      System.out.println("init HADATASOURCE failed, only support SERVER_DATASOURCE");
    }
  }
  public DataServer doGetMaster()
  {
    if (isActive) {
      return active.getMaster();
    }
    return coldStandBy.getMaster();
  }
  public void doHandleAbnormal(DataSource source)
  {
    if (state == DataSourceWorkingState.SERVER_STOP)
      return;
    if (isActive && source == active) {
      isActive = false;
    }
    else if (!isActive && source == coldStandBy) {
      isActive = true;
    }
    if (active.state == DataSourceWorkingState.SERVER_STOP
        && coldStandBy.state == DataSourceWorkingState.SERVER_STOP) {
      this.state = DataSourceWorkingState.SERVER_STOP;
    }
    // todo notify parent;
  }
  public void doHandleWakeup(DataSource source)
  {
    if (this.state == DataSourceWorkingState.SERVER_STOP) {
      isActive = (source == active) ? true : false;
    }
    if (active == source) {
      isActive = true;
    }
    // todo notify parent;
  }
  public Connection doGetConnection(boolean isReadOnly)
  {
    return getMaster().getConn();
  }
  public void initConnPool() {
    active.initConnPool();
  }
}
