package stop_wait;

import java.io.IOException;
import java.net.DatagramSocket;

public class Server {

  // ���Դ�Ž������ݵ��ֽ�����
  public static void main(String args[]) {
    DatagramSocket socket;
    try {
      socket = new DatagramSocket(8081);
      ServerPlatform ackserver=new ServerPlatform(socket);
      while(true) {
        ackserver.ack();
      }
    } catch (IOException e) {
      
    }
  }
}
