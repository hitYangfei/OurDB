package hit.ourdb.NIO.procotol;

import java.nio.ByteBuffer ;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public abstract class MySQLPacket {
  public int packetLength;
  public byte packetId;
  protected PacketUtils packet;
  protected static final Logger logger = Logger.getLogger(MySQLPacket.class);
  public MySQLPacket()
  {
  }
  public MySQLPacket(ByteBuffer buffer)
  {
    packet = new PacketUtils(buffer);
    BasicConfigurator.configure();
  }
  protected void unpackHeader()
  {
    this.packetLength = packet.readUB3();
    System.out.println("length is " + packetLength);
    this.packetId = packet.read();
    System.out.println("packet num is " + packetId);
  }
  public void unpack()
  {
 //   logger.debug("Begin unpack MySQLPacket");
    System.out.println("Begin unpack MySQLPacket");
    unpackHeader();
    unpackBody();
    logger.debug("finish unpack MySQLPacket");
  }
  public abstract void unpackBody();
}

