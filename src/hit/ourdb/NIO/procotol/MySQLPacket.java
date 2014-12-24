package hit.ourdb.NIO.procotol;

import java.nio.ByteBuffer ;
import org.apache.log4j.Logger;


public abstract class MySQLPacket {
  public int packetLength;
  public byte packetId;
  protected PacketUtils packet ;
  protected ByteBuffer buffer;
  protected static final Logger logger = Logger.getLogger(MySQLPacket.class);
  public void setPacketId(byte id) {
    this.packetId = id;
  }
  public byte getPacketId() {
    return this.packetId;
  }
  public MySQLPacket()
  {
    buffer = ByteBuffer.allocate(1024);
    packet = new PacketUtils();
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
    this.packetLength = 4 + getPacketLength();
 //   buffer.allocate(this.packetLength + 4 );
    packet.writeUB3(buffer, this.packetLength);
    packet.writeUB1(buffer, this.packetId);
    this.packetId++;
  }
  public static String ByteToHexString(byte b) {
    int i = b;
    byte low = (byte)(i%16);
    byte high = (byte)(i/16);
    char[] tran = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    String rtn = "0x";
    rtn += tran[high];
    rtn += tran[low];
    return rtn;
  }
  public ByteBuffer pack()
  {

    logger.info("begin pack MySQLPacket");
    buffer.clear();
    packHeader();
    packBody();
  //  buffer.flip();

    logger.info("finish pack MySQLPacet");

 /*   while (buffer.hasRemaining()) {
      System.out.print(buffer.get());
      System.out.print(" ");
    }
    System.out.println("\n");*/
    return buffer;
  }
  public abstract void unpackBody();
  public abstract void packBody();
  public int getPacketLength()
  {
    /*
     * 3 packet legnth
     * 1 packet num
     */
    return 4;
  }
}

