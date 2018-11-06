package sr_FileTrans;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server_recver implements Runnable {
  DatagramSocket datagramSocket;
  private Server_sender server_sender;
  private List<Integer> received_ack=new ArrayList<>();

  public Server_recver(DatagramSocket datagramSocket) {
    this.datagramSocket = datagramSocket;
    server_sender = new Server_sender(datagramSocket);
  }

  public void receive() throws IOException {
    byte[] receBuf = new byte[1024];
    DatagramPacket recPacket = new DatagramPacket(receBuf, receBuf.length);
    datagramSocket.receive(recPacket);

    String receStr = new String(recPacket.getData(), 0, recPacket.getLength());
    System.out.println("Server Received:" + receStr);
    System.out.println("Client Port:" + recPacket.getPort());

    InetAddress address = recPacket.getAddress();
    int port = recPacket.getPort();

    if (WinMgr.issr==true) {
      System.out.println("��ǰmaxseqΪ"+WinMgr.maxseq);
      ACK ack=new ACK(receStr);
      int seq=ack.getSeq();
      if(WinMgr.unacked.contains(seq)) {
//        WinMgr.used_window--;
        WinMgr.unacked.remove(seq);   //�յ�ack����б���ȥ����
        if(seq==WinMgr.maxseq-WinMgr.window_size+1) {
          WinMgr.maxseq++;
          int size=received_ack.size();
          int i;
          for(i=0;i<size;i++) {
            for(Integer integer:received_ack) {
              if(integer==WinMgr.maxseq-WinMgr.window_size+1) {
                WinMgr.maxseq++;   //��������
              }
            }
          }
        }else {
          received_ack.add(seq);
        }
      }
    } else {

      if (receStr.equals("-time")) {
        server_sender.sendTime(address, port);
      } else if (receStr.equals("-quit")) {
        server_sender.sendBye(address, port);
      } else if (receStr.contains("-testsr")) {
        String[] command=receStr.split(" ");
        if(command.length>1) {
          double x=Double.parseDouble(command[1]);
          if(x<=1 && x>=0) {
            WinMgr.packet_loss=x;
            if(command.length>2) {
              double y=Double.parseDouble(command[2]);   //��Ҫ�ǳ�ע����ǣ���仰ʵ��û�ã�
              if(y<=1 && y>=0) {                         //��Ϊ����Ϣ��Clientʹ�ã��ǲ�ͬ��WinMgr��
                WinMgr.ack_loss=y;                       //��Ҫ�ͻ������Լ���WinMgr���ø���ֵ
              }else {
                System.out.println("Illegal ack loss rate. Nothing changed.");
              }
            }
          }else {
            System.out.println("Illegal packet loss rate. Nothing changed.");
          }
        }
        WinMgr.issr=true;   //����sr����ģʽ
        Server_sender gbn_sender=new Server_sender(datagramSocket,address,port);  //���߳�ģ��sr
        Thread thread=new Thread(gbn_sender);
        thread.start();
      } else {
        server_sender.sendBack(receStr.getBytes(), address, port);
      }
    }
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      while (true) {
        receive();
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
