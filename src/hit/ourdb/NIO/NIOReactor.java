/*
 * Copyright 1999-2012 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.apache.log4j.BasicConfigurator;


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
      BasicConfigurator.configure();
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
                              System.out.println("read data");
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
            logger.debug("register in reactor occur error");
          }
      }
    }

    private void read(NIOConnection c) {
        try {
            c.read();
        } catch (Throwable e) {
          logger.debug("read error");
        }
    }

    private void write(NIOConnection c) {
        try {
            c.write();
        } catch (Throwable e) {
          logger.debug("write error");
        }
    }


}
