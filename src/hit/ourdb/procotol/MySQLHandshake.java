package hit.ourdb.procotol;

import java.nio.ByteBuffer ;

public class MySQLHandshake extends MySQLPacket {

  public byte protocolVersion;
  public byte[] serverVersion;
  public long threadId;
  public byte[] seed;
  public int serverCapabilities;
  public byte serverCharsetIndex;
  public int serverStatus;
  public byte[] restOfScrambleBuff;

  public MySQLHandshake(ByteBuffer buffer)
  {
    super(buffer);
  }
  public void unpackBody()
  {
    protocolVersion = packet.read();
    serverVersion = packet.readBytesWithNull();
    threadId = packet.readUB4();
    seed = packet.readBytesWithNull();
    serverCapabilities = packet.readUB2();
    serverCharsetIndex = packet.read();
    serverStatus = packet.readUB2();
    packet.move(13);
    restOfScrambleBuff = packet.readBytesWithNull();
    logger.debug("unpack MySQLHandshake");
  }
}
