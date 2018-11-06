package gbn;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Timer4gbn implements Runnable {
  private int seq; // �ü�ʱ�������ʱ�İ����
  DatagramSocket datagramSocket;
  InetAddress address;
  int port;

  public Timer4gbn(int seq, DatagramSocket datagramSocket, InetAddress address, int port) {
    this.seq = seq;
    this.datagramSocket = datagramSocket;
    this.address = address;
    this.port = port;
  }

  private long start_time;

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      start_time = System.currentTimeMillis();
      while (true) {
        if (System.currentTimeMillis() - start_time > WinMgr.timeout) {
          if (!WinMgr.queue.isEmpty()) {
            if (WinMgr.queue.peek() == seq) { // ˵���������ʱ�ˣ����������������κβ������߳��Զ���ֹ
              this.resend();
            }
          }
          break; // �����Ƿ�Ҫ�ش���Ӧ��������ѭ����
        }
      }
    } catch (IOException e) {

    }
  }

  /**
   * Resend packet when timeout happens
   * 
   * @throws IOException
   */
  public void resend() throws IOException {
    int i;
    for (i = 0; i < WinMgr.ack_size; i++) {
      byte[] temp=WinMgr.pool.poll();
      DatagramPacket packet = new DatagramPacket(temp, temp.length, address, port);
      System.out.println("��鵽���" + (seq-WinMgr.ack_size+1+i) + "������ʱ�����·���");
      
      WinMgr.pool.offer(temp);  //�ٰ����Ż�ȥ����Ϊ�ش�Ҳ��Ҫ��ʱ

      if(i==0) {
        Timer4gbn timer = new Timer4gbn(seq, datagramSocket, address, port); // ���·��͵İ�Ҳ��Ҫ��ʱ
        Thread thread = new Thread(timer);
        thread.start();
      }

      datagramSocket.send(packet);
      WinMgr.used_window++;
    }
  }
}
