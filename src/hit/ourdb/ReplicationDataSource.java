package hit.ourdb;

public class ReplicationDataSource extends RWSplitDataSource {
  public ReplicationDataSource(String name, DataSource read_source, DataSource write_source) {
    super(name, read_source, write_source);
  }
}
