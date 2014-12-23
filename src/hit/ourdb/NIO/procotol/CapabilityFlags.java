
package hit.ourdb.NIO.procotol;

/*
 * see http://dev.mysql.com/doc/internals/en/capability-flags.html#packet-Protocol::CapabilityFlags
 * Protocol::CapabilityFlags
 * Protocal Version:10
 */

public interface CapabilityFlags {
  int CLIENT_LONG_PASSWORD = 1;  /* new more secure passwords */
  int CLIENT_FOUND_ROWS  = 2;  /* Found instead of affected rows */
  int CLIENT_LONG_FLAG         = 4;  /* Get all column flags */
  int CLIENT_CONNECT_WITH_DB = 8;  /* One can specify db on connect */
  int CLIENT_NO_SCHEMA         = 16; /* Don't allow database.table.column */
  int CLIENT_COMPRESS    = 32; /* Can use compression protocol */
  int CLIENT_ODBC    = 64; /* Odbc client */
  int CLIENT_LOCAL_FILES = 128;  /* Can use LOAD DATA LOCAL */
  int CLIENT_IGNORE_SPACE  = 256;  /* Ignore spaces before '(' */
  int CLIENT_PROTOCOL_41 = 512;  /* New 4.1 protocol */
  int CLIENT_INTERACTIVE = 1024; /* This is an interactive client */
  int CLIENT_SSL               = 2048; /* Switch to SSL after handshake */
  int CLIENT_IGNORE_SIGPIPE    = 4096; /* IGNORE sigpipes */
  int CLIENT_TRANSACTIONS  = 8192; /* Client knows about transactions */
  int CLIENT_RESERVED          = 16384;   /* Old flag for 4.1 protocol  */
  int CLIENT_SECURE_CONNECTION = 32768;  /* New 4.1 authentication */
  int CLIENT_MULTI_STATEMENTS  = (1 << 16); /* Enable/disable multi-stmt support */
  int CLIENT_MULTI_RESULTS     = (1 << 17); /* Enable/disable multi-results */
  int CLIENT_PS_MULTI_RESULTS  = (1 << 18); /* Multi-results in PS-protocol */
  int CLIENT_PLUGIN_AUTH       = (1 << 19); /* Client supports plugin authentication */
  int CLIENT_CONNECT_ATTRS = (1 << 20); /* Client supports connection attributes */
  int CLIENT_SSL_VERIFY_SERVER_CERT = (1 << 30); /* SSL verify server certification */
  int CLIENT_REMEMBER_OPTIONS  = (1 << 31); /* Remember client options */
}
