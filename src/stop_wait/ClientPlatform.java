package stop_wait;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientPlatform {
  DatagramSocket datagramSocket = new DatagramSocket();
  InetAddress address;
  int port;
  
  public ClientPlatform(String netAddress, int port) throws IOException {
    this.port=port;
    address= InetAddress.getByName(netAddress);
  }
  
  public void send(byte[] content) throws IOException, InterruptedException {
    DatagramPacket packet=new DatagramPacket(content,content.length,address,port);
    
    datagramSocket.send(packet);
    
    byte[] receBuf = new byte[1024];
    DatagramPacket recPacket = new DatagramPacket(receBuf, receBuf.length);
    datagramSocket.receive(recPacket);
   
    String receStr = new String(recPacket.getData(), 0 , recPacket.getLength());
    System.out.println("ACK Received:" + receStr);
    System.out.println("Server Port:"+recPacket.getPort());
    
    Thread.sleep(1000);
  }
}