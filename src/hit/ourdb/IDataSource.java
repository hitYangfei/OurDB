
package hit.ourdb;

public interface IDataSource {
  void handleAbnormal(DataSource source);
  void handleWakeup(DataSource source);
}
