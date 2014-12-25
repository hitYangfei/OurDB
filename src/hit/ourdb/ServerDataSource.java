
package hit.ourdb;

import java.io.IOException; 
import java.sql.Connection;
import hit.ourdb.NIO.BackendConnection;
import hit.ourdb.worker.*;
import org.apache.log4j.Logger;

public class ServerDataSource extends DataSource {
  private static final Logger logger = Logger.getLogger(ServerDataSource.class);
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
  public void initConnPool() {
    BackendWorker worker = BackendWorker.instance();
    DataServer ds = getMaster();
    String name = ds.getName();
    for (int i = 0; i < 1; i++) {
      logger.info(name + "init conn " + i); 
      try {
        BackendConnection conn = new BackendConnection(ds.getIP(), ds.getPort());
        conn.setUsername(ds.getUsername());
        conn.setPassword(ds.getPassword());
        backendConnPool.add(conn);

      } catch (IOException e) {
        logger.info("error happen in conn pool init");
      }
    }
    for (BackendConnection tmp : backendConnPool) {
      worker.getNIOWorker().addConnect(tmp);
    }
  }
}
