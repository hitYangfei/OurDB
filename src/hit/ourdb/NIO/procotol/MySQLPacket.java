package hit.ourdb.NIO.procotol;

import java.nio.ByteBuffer ;
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
  }
  protected void unpackHeader()
  {
    this.packetLength = packet.readUB3();
    logger.info("length of the packet is "+  packetLength);
    this.packetId = packet.read();
    logger.info("num of the packet is " + packetId);
  }
  public void unpack()
  {
    logger.info("Begin unpack MySQLPacket");
    unpackHeader();
    unpackBody();
    logger.info("finish unpack MySQLPacket");
  }
  protected void packHeader()
  {
  }
  public void pack()
  {
    logger.info("begin pack MySQLPacket");
    packHeader();
    packBody();
    logger.info("finish pack MySQLPacet");
  }
  public abstract void unpackBody();
  public abstract void packBody();
}

