package hit.ourdb;

public enum DataSourceWorkingState {
  UNKNOWN,
  WORKING,
  SLAVE_STOP,
  SERVER_STOP,
  CHANGING;
}
