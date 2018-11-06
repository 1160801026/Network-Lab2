package gbn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client_recver implements Runnable {
  // ���ݱ��׽���
  private DatagramSocket datagramSocket;
  // ���Խ������ݱ�
  private DatagramPacket datagramPacket;
  private Client_sender client_sender;
  private int expected_seq=1;

  public Client_recver(Client_sender client_sender) throws IOException {
    this.datagramSocket = client_sender.getSocket();
    this.client_sender = client_sender;
  }

  public void receive() {
    // TODO Auto-generated method stub
    /****** �������ݱ� ****/
    try {
      byte[] receMsgs = new byte[65535];
      datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
      // receive()���ȴ�����UDP���ݱ�
      datagramSocket.receive(datagramPacket);

      String receStr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
      System.out.println("Client Receive:" + receStr);
      System.out.println("Server Port:" + datagramPacket.getPort());

      if (receStr.contains("Hello")) { // ˵����ʼ����gbn����״̬
        int seq = Integer.parseInt(receStr.split(" ")[3]);
        if(seq==expected_seq) {
          expected_seq++;
          if(seq%WinMgr.ack_size==0) {   //�ۼ�ȷ��
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
