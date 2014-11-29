package hit.ourdb;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
public class LoadBalanceDataSource extends DataSource {
  private LBStrategyType strategyType;
  private ILBStrategy blStrategy;
  private List<DataSource> read_sources;
  public LoadBalanceDataSource(String name, List<DataSource> sources)
  {
    super(name);
    this.read_sources = sources;
    this.type = DataSourceType.LOAD_BALANCE_DATASOURCE;
    this.strategyType = LBStrategyType.RoundRobinStrategy;
    this.blStrategy = AbstractLBStrategy.strategyFactory(strategyType);
  }
  public DataServer doGetMaster()
  {
    return read_sources.get(0).getMaster();
  }
  public void doHandleAbnormal(DataSource source)
  {
    this.state = DataSourceWorkingState.SERVER_STOP;
    // todo notify parent;
  }
  public void doHandleWakeup(DataSource source)
  {
    this.state = DataSourceWorkingState.WORKING;
    // todo notify parent;
  }
  public Connection doGetConnection(boolean isReadOnly)
  {
    DataSource rtn = blStrategy.getOneDataSource(read_sources);
    return rtn.getConnection(isReadOnly);
  }


}
