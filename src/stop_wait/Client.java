package stop_wait;

import java.io.IOException;

public class Client {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      ClientPlatform client = new ClientPlatform("localhost", 8081);
      int i=0;
      while (true) {
        i++;
        byte[] content = ("Hello for the "+i+" time!").getBytes();
        client.send(content);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
