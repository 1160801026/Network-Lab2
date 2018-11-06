package sr_withFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

public class Client_recver implements Runnable{
  // 数据报套接字
  private DatagramSocket datagramSocket;
  // 用以接收数据报
  private DatagramPacket datagramPacket;
  private Client_sender client_sender;
  private int expected_seq=1;  //初始期待值显然为1

  public Client_recver(Client_sender client_sender) throws IOException {
    this.datagramSocket = client_sender.getSocket();
    this.client_sender=client_sender;
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
    
      if(receStr.contains(" | ")) {   //说明开始进入gbn测试状态
        Frame frame=new Frame(receStr);
        int seq = frame.getSeq();
        if(seq!=expected_seq) {
          WinMgr.buffer.add(frame);
        }else {
          WinMgr.result.add(frame);
          expected_seq++;
          int size=WinMgr.buffer.size();
          int i;
          for(i=0;i<size;i++) {
            Iterator<Frame> iter=WinMgr.buffer.iterator();
            while(iter.hasNext()){
              Frame f=iter.next();
              if(f.getSeq()==expected_seq+1) {
                WinMgr.result.add(f);   //假装读出
                iter.remove();  
                expected_seq++;
              }
            }
          }
        }
        
        WinMgr.recved_window++;
        double rand=Math.random();
        if(rand>WinMgr.ack_loss) {  //ack随机丢失
          client_sender.ack(seq);
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
    while(true) {
//      if(WinMgr.recved_window<WinMgr.window_size) {
        receive();
        
        if(WinMgr.recved_window==WinMgr.window_size) {
          System.out.println("接受窗口满");
        }
      }
//    }
  }
}
