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
 * 
 */
package hit.ourdb.NIO;

public class NIOHandler {
  private NIOConnection conn;
  public NIOHandler(NIOConnection conn) {
    this.conn = conn;
  }
  void handle(int event) {
    switch (event) {
      case NIOEvent.HAND_SHAKE:
        handlerHandShake();
        break;
      case NIOEvent.AUTH_REQUEST:
        handlerAuthRequest();
        break;
      case NIOEvent.AUTH_RESPONSE:
        handlerAuthResponse();
        break;
      defaults:
        System.out.println("the event is not exist");
    }
  }
  void handlerHandShake() {

  }
  void handlerAuthRequest() {
  }
  void handlerAuthResponse() {
  }
}
