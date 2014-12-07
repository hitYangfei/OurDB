
package hit.ourdb.NIO.procotol;

/*
 * see http://dev.mysql.com/doc/internals/en/capability-flags.html#packet-Protocol::CapabilityFlags
 * Protocol::CapabilityFlags
 * Protocal Version:10
 */

public interface CapabilityFlags {
  int CLIENT_LONG_PASSWORD        = 0x1;
  int CLIENT_FOUND_ROWS           = 0x2;
  int CLIENT_LONG_FLAG            = 0x4;
  int CLIENT_CONNECT_WITH_DB      = 0x8;
  int CLIENT_NO_SCHEMA            = 0x10;
  int CLIENT_COMPRESS             = 0x20;
  int CLIENT_PLUGIN_AUTH          = 0x80000;
  int CLIENT_SECURE_CONNECTION    = 0x8000 ;

}
