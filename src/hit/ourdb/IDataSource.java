
package hit.ourdb;

public interface IDataSource {
  void handleAbnormal(DataSource target);
  void handleWakeup(DataSource target);
}
