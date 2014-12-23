/*
 *
4              capability flags, CLIENT_PROTOCOL_41 always set
4              max-packet size
1              character set
string[23]     reserved (all [0])
string[NUL]    username
  if capabilities & CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA {
lenenc-int     length of auth-response
string[n]      auth-response
  } else if capabilities & CLIENT_SECURE_CONNECTION {
1              length of auth-response
string[n]      auth-response
  } else {
string[NUL]    auth-response
  }
  if capabilities & CLIENT_CONNECT_WITH_DB {
string[NUL]    database
  }
  if capabilities & CLIENT_PLUGIN_AUTH {
string[NUL]    auth plugin name
  }
  if capabilities & CLIENT_CONNECT_ATTRS {
lenenc-int     length of all key-values
lenenc-str     key
lenenc-str     value
   if-more data in 'length of all key-values', more keys and value pairs
  }
 */
package hit.ourdb.NIO.procotol;

public class MySQLAuthPacket extends MySQLPacket {
  public static final long defaultClientCapabilities = CapabilityFlags.CLIENT_LONG_PASSWORD
                                                | CapabilityFlags.CLIENT_PROTOCOL_41
                                                | CapabilityFlags.CLIENT_SECURE_CONNECTION
                                                | CapabilityFlags.CLIENT_MULTI_RESULTS
                                                | CapabilityFlags.CLIENT_REMEMBER_OPTIONS
                                                | CapabilityFlags.CLIENT_FOUND_ROWS
                                                | CapabilityFlags.CLIENT_MULTI_STATEMENTS;

  private static final byte[] FILLER = new byte[23];

  public long clientFlags;
  public long maxPacketSize;
  public int charsetIndex;
  public byte[] extra;// from FILLER(23)
  public String user;
  public byte[] password;
  public String database;
  public byte[] scramble;
  public MySQLAuthPacket(long clientFlags, byte[] scramble, String user, String password, String database)
  {
    this.clientFlags = clientFlags;
    this.scramble = scramble;
    this.user = user;
    this.password = password.getBytes();
    this.database = database;
  }
  public void unpackBody()
  {
    logger.debug("not support now");
  }
  public void packBody()
  {
   logger.debug("sss");
  }
  public int getPacketLength()
  {
    return 4;
  }
}
