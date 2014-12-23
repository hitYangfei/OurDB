package hit.outdb.NIO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.apache.log4j.Logger;

public abstract class FrontendConnection extends AbstractConnection {
    private static final Logger logger = Logger.getLogger(FrontendConnection.class);

  
    public FrontendConnection(InetSocketAddress address) {
        super(address);
    }


}
