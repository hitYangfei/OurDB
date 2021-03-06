package hit.ourdb.NIO;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


/**
 * @author xianmao.hexm
 */
public final class NIOConnector extends Thread {
    private static final Logger logger = Logger.getLogger(NIOConnector.class);

    private final String name;
    private final Selector selector;
    private final BlockingQueue<BackendConnection> connectQueue;
    private NIOWorker worker;

    public NIOConnector(String name) throws IOException {
        super.setName(name);
        this.name = name;
        this.selector = Selector.open();
        this.connectQueue = new LinkedBlockingQueue<BackendConnection>();
    }

    public void setWorker(NIOWorker worker) {
      this.worker = worker;
    }
    public void addConnect(BackendConnection c) {
        connectQueue.offer(c);
        selector.wakeup();
    }

    @Override
    public void run() {
        final Selector selector = this.selector;
        for (;;) {
            try {
                selector.select(1000L);
                connect(selector);
                Set<SelectionKey> keys = selector.selectedKeys();
                try {
                    for (SelectionKey key : keys) {
                        Object att = key.attachment();
                        if (att != null && key.isValid() && key.isConnectable()) {
                            finishConnect(key, att);
                        } else {
                            key.cancel();
                        }
                    }
                } finally {
                    keys.clear();
                }
            } catch (Throwable e) {
              logger.error("error in NIConnctor main run");
            }
        }
    }

    private void connect(Selector selector) {
      BackendConnection c = null;
      while ((c = connectQueue.poll()) != null) {
          try {
              c.connect(selector);
          } catch (Throwable e) {
             // c.error(ErrorCode.ERR_CONNECT_SOCKET, e);
             logger.error("connect error in NIOConnector");
          }
      }
    }

    private void finishConnect(SelectionKey key, Object att) {
        BackendConnection c = (BackendConnection) att;
        try {
            if (c.finishConnect()) {
                clearSelectionKey(key);
 /*               NIOProcessor processor = nextProcessor();
                c.setProcessor(processor);
                processor.postRegister(c);*/
                worker.postRegister(c);
            }
        } catch (Throwable e) {
            clearSelectionKey(key);
            logger.error("finish connect error in NIOConnector");
        }
    }

    private void clearSelectionKey(SelectionKey key) {
        if (key.isValid()) {
            key.attach(null);
            key.cancel();
        }
    }

 /*   private NIOProcessor nextProcessor() {
        if (++nextProcessor == processors.length) {
            nextProcessor = 0;
        }
        return processors[nextProcessor];
    }
*/
    /**
     * 后端连接ID生成器
     *
     * @author xianmao.hexm
     */
  /*  private static class ConnectIdGenerator {

        private static final long MAX_VALUE = Long.MAX_VALUE;

        private long connectId = 0L;
        private final Object lock = new Object();

        private long getId() {
            synchronized (lock) {
                if (connectId >= MAX_VALUE) {
                    connectId = 0L;
                }
                return ++connectId;
            }
        }
    }*/

}
