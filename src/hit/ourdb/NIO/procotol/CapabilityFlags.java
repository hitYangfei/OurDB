
package hit.ourdb.NIO.procotol;

/*
 * see http://dev.mysql.com/doc/longernals/en/capability-flags.html#packet-Protocol::CapabilityFlags
 * Protocol::CapabilityFlags
 * Protocal Version:10
 */

public interface CapabilityFlags {
  long CLIENT_LONG_PASSWORD = 1;  /* new more secure passwords */
  long CLIENT_FOUND_ROWS  = 2;  /* Found instead of affected rows */
  long CLIENT_LONG_FLAG         = 4;  /* Get all column flags */
  long CLIENT_CONNECT_WITH_DB = 8;  /* One can specify db on connect */
  long CLIENT_NO_SCHEMA         = 16; /* Don't allow database.table.column */
  long CLIENT_COMPRESS    = 32; /* Can use compression protocol */
  long CLIENT_ODBC    = 64; /* Odbc client */
  long CLIENT_LOCAL_FILES = 128;  /* Can use LOAD DATA LOCAL */
  long CLIENT_IGNORE_SPACE  = 256;  /* Ignore spaces before '(' */
  long CLIENT_PROTOCOL_41 = 512;  /* New 4.1 protocol */
  long CLIENT_INTERACTIVE = 1024; /* This is an longeractive client */
  long CLIENT_SSL               = 2048; /* Switch to SSL after handshake */
  long CLIENT_IGNORE_SIGPIPE    = 4096; /* IGNORE sigpipes */
  long CLIENT_TRANSACTIONS  = 8192; /* Client knows about transactions */
  long CLIENT_RESERVED          = 16384;   /* Old flag for 4.1 protocol  */
  long CLIENT_SECURE_CONNECTION = 32768;  /* New 4.1 authentication */
  long CLIENT_MULTI_STATEMENTS  = (1 << 16); /* Enable/disable multi-stmt support */
  long CLIENT_MULTI_RESULTS     = (1 << 17); /* Enable/disable multi-results */
  long CLIENT_PS_MULTI_RESULTS  = (1 << 18); /* Multi-results in PS-protocol */
  long CLIENT_PLUGIN_AUTH       = (1 << 19); /* Client supports plugin authentication */
  long CLIENT_CONNECT_ATTRS = (1 << 20); /* Client supports connection attributes */
  long CLIENT_SSL_VERIFY_SERVER_CERT = (1 << 30); /* SSL verify server certification */
  long CLIENT_REMEMBER_OPTIONS  = (1 << 31); /* Remember client options */
}
