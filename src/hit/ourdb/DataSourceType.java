package hit.ourdb;

public enum DataSourceType {
  UNKNOWN,
  SERVER_DATASOURCE,
  LOAD_BALANCE_DATASOURCE,
  RWSPLIT_DATASOURCE,
  HA_DATASOURCE,
  REPLICATION_DATASOURCE;
}
