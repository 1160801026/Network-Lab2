package gbn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client_recver implements Runnable {
  // 数据报套接字
  private DatagramSocket datagramSocket;
  // 用以接收数据报
  private DatagramPacket datagramPacket;
  private Client_sender client_sender;
  private int expected_seq=1;

  public Client_recver(Client_sender client_sender) throws IOException {
    this.datagramSocket = client_sender.getSocket();
    this.client_sender = client_sender;
  }

  public void receive() {
    // TODO Auto-generated method stub
    /****** 解析数据报 ****/
    try {
      byte[] receMsgs = new byte[65535];
      datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
      // receive()来等待接收UDP数据报
      datagramSocket.receive(datagramPacket);

      String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
      System.out.println("Client Receive:" + receStr);
      System.out.println("Server Port:" + datagramPacket.getPort());

      if (receStr.contains("Hello")) { // 说明开始进入gbn测试状态
        int seq = Integer.parseInt(receStr.split(" ")[3]);
        if(seq==expected_seq) {
          expected_seq++;
          if(seq%WinMgr.ack_size==0) {   //累计确认
            client_sender.ack(seq);
          }
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    while (true) {
      receive();
    }
  }
}
