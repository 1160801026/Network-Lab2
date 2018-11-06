package stop_wait;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerPlatform {
  // 数据报套接字
  private DatagramSocket datagramSocket;
  // 用以接收数据报
  private DatagramPacket datagramPacket;

  public ServerPlatform(DatagramSocket socket) throws IOException {
    this.datagramSocket = socket;
  }

  public void ack() {
    // TODO Auto-generated method stub
    /****** 解析数据报 ****/
    try {
      byte[] receMsgs = new byte[65535];
      datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
      // receive()来等待接收UDP数据报
      datagramSocket.receive(datagramPacket);
      String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
      System.out.println("Server Receive:" + receStr);
      System.out.println("Client Port:" + datagramPacket.getPort());

      /***** 返回ACK消息数据报 */
      // 组装数据报
      byte[] buf = "Received".getBytes();
      DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(),
          datagramPacket.getPort());
      // 发送消息

      datagramSocket.send(sendPacket);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
