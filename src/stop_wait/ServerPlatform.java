package stop_wait;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerPlatform {
  // ���ݱ��׽���
  private DatagramSocket datagramSocket;
  // ���Խ������ݱ�
  private DatagramPacket datagramPacket;

  public ServerPlatform(DatagramSocket socket) throws IOException {
    this.datagramSocket = socket;
  }

  public void ack() {
    // TODO Auto-generated method stub
    /****** �������ݱ� ****/
    try {
      byte[] receMsgs = new byte[65535];
      datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
      // receive()���ȴ�����UDP���ݱ�
      datagramSocket.receive(datagramPacket);
      String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
      System.out.println("Server Receive:" + receStr);
      System.out.println("Client Port:" + datagramPacket.getPort());

      /***** ����ACK��Ϣ���ݱ� */
      // ��װ���ݱ�
      byte[] buf = "Received".getBytes();
      DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, datagramPacket.getAddress(),
          datagramPacket.getPort());
      // ������Ϣ

      datagramSocket.send(sendPacket);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
