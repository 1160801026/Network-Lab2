package sr_withFrame;

import java.io.IOException;
import java.net.DatagramSocket;

public class Server {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      DatagramSocket socket = new DatagramSocket(8081);
      Server_recver server_recver=new Server_recver(socket);
      Thread thread=new Thread(server_recver);
      thread.start();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
