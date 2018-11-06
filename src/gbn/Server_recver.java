package gbn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server_recver implements Runnable {
  DatagramSocket datagramSocket;
  private Server_sender server_sender;

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

    if (WinMgr.isgbn==true) {
      if(Integer.parseInt(receStr.split(" ")[1])==WinMgr.queue.peek()) {
        WinMgr.used_window-=WinMgr.ack_size;
        WinMgr.queue.remove();
        int i;
        for(i=0;i<WinMgr.ack_size;i++) {
          WinMgr.pool.remove();   //清空已确认的包
        }
      }
    } else {

      if (receStr.equals("-time")) {
        server_sender.sendTime(address, port);
      } else if (receStr.equals("-quit")) {
        server_sender.sendBye(address, port);
      } else if (receStr.equals("-testgbn")) {
        WinMgr.isgbn=true;   //进入gbn测试模式
        Server_sender gbn_sender=new Server_sender(datagramSocket,address,port);  //新线程模拟gbn
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
