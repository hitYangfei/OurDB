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
string[NUL]    schema
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
  public static final byte CLIENT_CHARSET            = 8;
  public static final int CLIENT_MAX_PACKET_SIZE     = 1024*1024;
  public static final long defaultClientCapabilities = CapabilityFlags.CLIENT_LONG_PASSWORD
                                                | CapabilityFlags.CLIENT_LONG_FLAG
                                                | CapabilityFlags.CLIENT_CONNECT_WITH_DB
                                                | CapabilityFlags.CLIENT_LOCAL_FILES
                                                | CapabilityFlags.CLIENT_PROTOCOL_41
                                                | CapabilityFlags.CLIENT_TRANSACTIONS
                                                | CapabilityFlags.CLIENT_SECURE_CONNECTION
                                                | CapabilityFlags.CLIENT_MULTI_RESULTS
                                                | CapabilityFlags.CLIENT_PLUGIN_AUTH
                                                | CapabilityFlags.CLIENT_MULTI_STATEMENTS;

  private static final byte[] FILLER = new byte[23];

  public long clientFlags;
  public long maxPacketSize;
  public byte charsetIndex;
  public byte[] extra;// from FILLER(23)
  public String user;
  public byte[] password;
  public String schema;
  public byte[] scramble;
  public MySQLAuthPacket(long clientFlags, byte[] scramble, String user, String password, String schema)
  {
    super();
    this.clientFlags = clientFlags;
    this.scramble = scramble;
    this.user = user;
    this.password = password.getBytes();
    this.schema = schema;
    this.maxPacketSize = CLIENT_MAX_PACKET_SIZE;
    this.charsetIndex = 8;
  }
  public void unpackBody()
  {
    logger.debug("not support now");
  }
  public void packBody()
  {
    logger.debug("begin to packBody for AuthPacket");
    logger.debug("only support no password now");
    packet.writeUB4(buffer, clientFlags);
    packet.writeUB4(buffer, maxPacketSize);
    packet.writeByte(buffer, charsetIndex);
    packet.writeBytes(buffer, FILLER);
    packet.writeStringWithNULL(buffer, user);
    // no password
    packet.writeByte(buffer, (byte)0);

    packet.writeStringWithNULL(buffer, schema);
    packet.writeStringWithNULL(buffer, "mysql_native_password");

  }
  public int getPacketLength()
  {
    return 4 + 4 + 1 + 23 + user.length() + 1 + 1 + schema.length() + 1 + 22;
  }
}
