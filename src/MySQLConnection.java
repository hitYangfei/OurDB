
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import hit.ourdb.procotol.*;

public class MySQLConnection {

    private static final Logger logger = Logger.getLogger(MySQLConnection.class);
    static int SIZE = 1;
    static InetSocketAddress ip = new InetSocketAddress("192.168.122.13", 3306);

    static class Message implements Runnable {
        protected String name;
        String msg = "";

        public Message(String index) {
            this.name = index;
        }

        public void run() {
            try {
                long start = System.currentTimeMillis();
                //打开Socket通道
                SocketChannel client = SocketChannel.open();
                //设置为非阻塞模式
                client.configureBlocking(false);
                //打开选择器
                Selector selector = Selector.open();
                //注册连接服务端socket动作
                client.register(selector, SelectionKey.OP_CONNECT);
                //连接
                client.connect(ip);
                //分配内存
                //may be problem if the packet.length > 1024
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int total = 0;

                _FOR: for (;;) {
                        System.out.println(name + " is in for loop");
                        selector.select();
                        Iterator iter = selector.selectedKeys().iterator();
                        while (iter.hasNext()) {
                          SelectionKey key = (SelectionKey) iter.next();
                          iter.remove();
                          if (key.isConnectable()) {
                            System.out.println(name + " is connecting");
                            SocketChannel channel = (SocketChannel) key.channel();
                            if (channel.isConnectionPending())
                                channel.finishConnect();
                            channel.register(selector, SelectionKey.OP_READ);
                          } else if (key.isReadable()) {
                            System.out.println(name + " is reading");
                            SocketChannel channel = (SocketChannel) key.channel();
                            buffer.clear();
                            int count = channel.read(buffer);
                            if (count > 0) {
                              MySQLPacket packet = new MySQLHandshake(buffer);
                              packet.unpack();
                            }
                            if (count == 1024) {
                              logger.error("clientbuffer is too small.[1024]");
                            }
                            System.out.println();
                            break _FOR;
                          }
                        }
                      }
                      double last = (System.currentTimeMillis() - start) * 1.0 / 1000;
                      System.out.println(msg + "used time :" + last + "s.");
                      msg = "";
                      client.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {

      String names[] = new String[SIZE];

      for (int index = 0; index < SIZE; index++) {
        names[index] = "jeff[" + index + "]";
        new Thread(new Message(names[index])).start();
      }
    }
}
