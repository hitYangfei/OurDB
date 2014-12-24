package hit.ourdb.NIO;

import hit.ourdb.NIO.procotol.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import org.apache.log4j.Logger;

public class BackendConnection extends AbstractConnection {

    private static final Logger logger = Logger.getLogger(BackendConnection.class);
    private byte packetId = 0;
    private String username;
    private String password;
    public void reset() {
      packetId = 0;
    }
    public BackendConnection(String ip, int port) throws IOException {
      super(ip, port);
    }
    public BackendConnection(InetSocketAddress address) throws IOException {
      super(address);
    }
    public void setUsername(String user) {
      this.username = user;
    }
    public void setPassword(String password) {
      this.password = password;
    }
    public boolean finishConnect() throws IOException {
      if (channel.isConnectionPending()) {
        channel.finishConnect();
        logger.info("finish connect in BackendConnection");
        return true;
      } else {
        return false;
      }
    }
    @Override
    public void read() throws IOException {
      logger.info("begin read data in BackendConnection");
      ByteBuffer buffer = super.readBuffer;
      buffer.clear();
      int count = channel.read(buffer);
      if (count > 0) {
        MySQLHandshake packet = new MySQLHandshake(buffer);
        packet.unpack();
        this.packetId = packet.getPacketId();
        MySQLAuthPacket authPacket = new MySQLAuthPacket(MySQLAuthPacket.defaultClientCapabilities,
                                                         packet.getScramble(),
                                                         username,
                                                         password,
                                                         "test");
        authPacket.setPacketId(++this.packetId);
        ByteBuffer writeBuffer = authPacket.pack();
        writeBuffer.flip();
        while (writeBuffer.hasRemaining()) {
          count = channel.write(writeBuffer);
          logger.info("write to mysql server " + count);
        }
        buffer.clear();
        count = channel.read(buffer);
        logger.info("read from mysql server " + count);
        if (count > 0) {
          byte response = buffer.get(4);
          if (response == 0) {
            logger.info("auth sucesslly");
          } else if (response == 0xff) {
            logger.info("auth failed");
          } else {
            logger.error("response is not normal " + response);
          }
        }
     }
      else {
        logger.info("read nothing");
      }
      if (count >= 1024) {
        logger.error("clientbuffer is too small.[1024]");
      }
    }
}
