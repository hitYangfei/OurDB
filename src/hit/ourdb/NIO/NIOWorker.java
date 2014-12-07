
package hit.ourdb.NIO;

import java.io.IOException;
public class NIOWorker{
  private NIOReactor reactor;
  private NIOConnector connector;
  public NIOWorker() throws IOException {
    reactor = new NIOReactor("reactor");
    connector = new NIOConnector("connector");
    connector.setWorker(this);
  }
  public void addConnect(BackendConnection c) {
    connector.addConnect(c);
  }
  public void  postRegister(NIOConnection c) {
    reactor.postRegister(c);
  }
  public void startup() {
    reactor.start();
    connector.start();
  }
}
