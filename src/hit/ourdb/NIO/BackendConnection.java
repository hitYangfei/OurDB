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
    private String username;
    private String password;
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
        MySQLAuthPacket authPacket = new MySQLAuthPacket(MySQLAuthPacket.defaultClientCapabilities,
                                                         packet.getScramble(),
                                                         username,
                                                         password,
                                                         "test");
        authPacket.pack();
      }
      if (count >= 1024) {
        logger.error("clientbuffer is too small.[1024]");
      }
    }
}
