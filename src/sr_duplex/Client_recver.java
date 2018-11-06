package sr_duplex;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client_recver implements Runnable{
  // ���ݱ��׽���
  private DatagramSocket datagramSocket;
  // ���Խ������ݱ�
  private DatagramPacket datagramPacket;
  private List<Integer> received_ack=new ArrayList<>();
  private Client_sender client_sender;
  private int expected_seq=1;  //��ʼ�ڴ�ֵ��ȻΪ1

  public Client_recver(Client_sender client_sender) throws IOException {
    this.datagramSocket = client_sender.getSocket();
    this.client_sender=client_sender;
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
      
      if(WinMgr.recvmode==true) {
        
        System.out.println("��ǰmaxseqΪ"+WinMgr.maxseq);
        ACK ack=new ACK(receStr);
        int seq=ack.getSeq();
        if(WinMgr.unacked.contains(seq)) {
//          WinMgr.used_window--;
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
        
      }else {
    
      if(receStr.contains(" | ")) {   //˵����ʼ����gbn����״̬
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
                WinMgr.result.add(f);   //��װ����
                iter.remove();  
                expected_seq++;
              }
            }
          }
        }
        
        WinMgr.recved_window++;
        double rand=Math.random();
        if(rand>WinMgr.ack_loss) {  //ack�����ʧ
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
    while(true) {
//      if(WinMgr.recved_window<WinMgr.window_size) {
        receive();
        
        if(WinMgr.recved_window==WinMgr.window_size) {
          System.out.println("���ܴ�����");
        }
      }
//    }
  }
}
