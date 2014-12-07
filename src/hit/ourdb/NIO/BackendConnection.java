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
    public BackendConnection(InetSocketAddress address) throws IOException {
        super(address);
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
        MySQLPacket packet = new MySQLHandshake(buffer);
        packet.unpack();
      }
      if (count >= 1024) {
        logger.error("clientbuffer is too small.[1024]");
      }
    }
}
