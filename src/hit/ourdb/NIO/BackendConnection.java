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

import hit.ourdb.NIO.procotol.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;


/**
 * @author xianmao.hexm
 */
public class BackendConnection extends AbstractConnection {

    public BackendConnection(InetSocketAddress address) throws IOException {
        super(address);
    }
    public boolean finishConnect() throws IOException {
      if (channel.isConnectionPending()) {
        channel.finishConnect();
        System.out.println("finish connct in BackendConnection");
        return true;
      } else {
        return false;
      }
    }
    @Override
    public void read() throws IOException {
      System.out.println("begin read data in BackendCon");
      ByteBuffer buffer = super.readBuffer;
      buffer.clear();
      int count = channel.read(buffer);
      if (count > 0) {
        MySQLPacket packet = new MySQLHandshake(buffer);
        packet.unpack();
      }
      if (count >= 1024) {
        System.out.println("clientbuffer is too small.[1024]");
      }
      System.out.println();
    }
}
