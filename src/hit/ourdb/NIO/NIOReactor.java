package hit.ourdb.NIO;

import java.io.IOException;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;


/**
 * 网络事件反应器
 *
 * @author xianmao.hexm
 */
public final class NIOReactor extends Thread{
    private static final Logger logger = Logger.getLogger(NIOReactor.class);

    private final String name;
    private final Selector selector;
    private final BlockingQueue<NIOConnection> registerQueue;

    public NIOReactor(String name) throws IOException {
      this.name = name;
      this.selector = Selector.open();
      this.registerQueue = new LinkedBlockingQueue<NIOConnection>();
    }

    final void postRegister(NIOConnection c) {
        registerQueue.offer(c);
        selector.wakeup();
    }

    final BlockingQueue<NIOConnection> getRegisterQueue() {
        return registerQueue;
    }

    @Override
    public void run() {
      final Selector selector = this.selector;
      for (;;) {
          try {
              selector.select(1000L);
              register(selector);
              Set<SelectionKey> keys = selector.selectedKeys();
              try {
                  for (SelectionKey key : keys) {
                      Object att = key.attachment();
                      if (att != null && key.isValid()) {
                          int readyOps = key.readyOps();
                          if ((readyOps & SelectionKey.OP_READ) != 0) {
                              read((NIOConnection) att);
                          } else if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                              write((NIOConnection) att);
                          } else {
                              key.cancel();
                          }
                      } else {
                          key.cancel();
                      }
                  }
              } finally {
                  keys.clear();
              }
          } catch (Throwable e) {
              logger.warn(name, e);
          }
      }

    }

    private void register(Selector selector) {
      NIOConnection c = null;
      while ((c = registerQueue.poll()) != null) {
          try {
              c.register(selector);
          } catch (Throwable e) {
            logger.error("register in reactor occur error");
          }
      }
    }

    private void read(NIOConnection c) {
        try {
            c.read();
            c.register(selector);
        } catch (Throwable e) {
          logger.error("read error");
          logger.error(e);
        }
    }

    private void write(NIOConnection c) {
        try {
            c.write();
        } catch (Throwable e) {
          logger.error("write error");
        }
    }


}
