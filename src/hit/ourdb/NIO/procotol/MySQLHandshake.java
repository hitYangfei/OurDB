package hit.ourdb.NIO.procotol;

import java.nio.ByteBuffer ;
import org.apache.log4j.Logger;

public class MySQLHandshake extends MySQLPacket {

  private static final Logger logger = Logger.getLogger(MySQLHandshake.class);
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
    logger.info("protocal version is 0x" + Integer.toHexString(protocolVersion));
    serverVersion = packet.readBytesWithNull();
    logger.info("server version is " + (new String(serverVersion)));
    threadId = packet.readUB4();
    logger.info("thread id is " + threadId);
    seed = packet.readBytesWithNull();
    logger.info("auth data part 1 is " + (new String(seed)));
    serverCapabilities = packet.readUB2();
    serverCharsetIndex = packet.read();
    serverStatus = packet.readUB2();
    serverCapabilities |= (packet.readUB2() << 16);
    logger.info("server capablilities is " + Integer.toHexString(serverCapabilities));
    int length = 0;
    if ((serverCapabilities & CapabilityFlags.CLIENT_PLUGIN_AUTH) != 0) {
      logger.info("CLIENT_PLUGIN_AUTH enable");
      length = (int)packet.readUB4();
      logger.info("length of auth-plugin-data is " + length);
    } else {
      logger.info("CLIENT_PLUGIN_AUTH disable");
      logger.info("reversed 1 byte 00");
      packet.move(1);
    }
    packet.move(10);
    if ((serverCapabilities & CapabilityFlags.CLIENT_SECURE_CONNECTION) !=0 ) {
      logger.info("CLIENT_SECURE_CONNECTION enable");

      logger.info("auth-plugin-data-part-2 is " + (new String(packet.readBytes(Math.max(13, length - 8)))));
    } else {
      logger.info("CLIENT_SECURE_CONNECTION disable");
    }

    if ((serverCapabilities & CapabilityFlags.CLIENT_PLUGIN_AUTH) != 0) {
      restOfScrambleBuff = packet.readBytesWithNull();
      logger.info("auth-plugin-name is " + (new String(restOfScrambleBuff)));
    }
    logger.info("packet position is " + packet.position());
    logger.info("packet length is " + packet.length());
    logger.info("unpack MySQLHandshake");
  }
}
