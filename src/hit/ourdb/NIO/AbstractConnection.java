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

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.apache.log4j.Logger;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * @author xianmao.hexm
 */
public abstract class AbstractConnection implements NIOConnection {
    private static final Logger logger = Logger.getLogger(AbstractConnection.class);

    protected final SocketChannel channel;
    protected SelectionKey processKey;
    protected ByteBuffer readBuffer;
    protected boolean isConnect;

    public AbstractConnection() throws IOException {
       //打开Socket通道
       channel = SocketChannel.open();
       //设置为非阻塞模式
       channel.configureBlocking(false);
       readBuffer = ByteBuffer.allocate(1024);
       isConnect = false;
    }

    public SocketChannel getChannel() {
        return channel;
    }


    @Override
    public void register(Selector selector) throws IOException {
        try {
            if (isConnect) {
              processKey = channel.register(selector, SelectionKey.OP_READ, this);
              isConnect = true;
            }
            else
              processKey = channel.register(selector, SelectionKey.OP_CONNECT, this);
        } finally {
          logger.debug("register");
        }
    }

    @Override
    public void read() throws IOException {
        ByteBuffer buffer = this.readBuffer;
        int got = channel.read(buffer);

        // 处理数据
        logger.debug("read data");

    }
    public void connect(InetSocketAddress address) throws IOException {
      channel.connect(address);
    }

    @Override
    public void write() {
      logger.debug("write something");
    }


    @Override
    public boolean close() {
        SocketChannel channel = this.channel;
        isConnect = false;
        if (channel != null) {
            boolean isSocketClosed = true;
            Socket socket = channel.socket();
            if (socket != null) {
                try {
                    socket.close();
                } catch (Throwable e) {
                }
                isSocketClosed = socket.isClosed();
            }
            try {
                channel.close();
            } catch (Throwable e) {
            }
            return isSocketClosed && (!channel.isOpen());
        } else {
            return true;
        }
    }

}
