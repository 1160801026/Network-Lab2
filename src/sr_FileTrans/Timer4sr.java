package sr_FileTrans;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Timer4sr implements Runnable {
  private int seq; // �ü�ʱ�������ʱ�İ����
  byte[] content;
  DatagramSocket datagramSocket;
  InetAddress address;
  int port;

  public Timer4sr(byte[] content, int seq, DatagramSocket datagramSocket, InetAddress address,
      int port) {
    this.content = content;
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
          if (!WinMgr.unacked.isEmpty()) {
            if (WinMgr.unacked.contains(seq)) { // ˵���������ʱ�ˣ����������������κβ������߳��Զ���ֹ
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
    DatagramPacket packet = new DatagramPacket(content, content.length, address, port);
    System.out.println("��鵽���" + seq + "������ʱ�����·���");

    Timer4sr timer = new Timer4sr(content, seq, datagramSocket, address, port); // ���·��͵İ�Ҳ��Ҫ��ʱ
    Thread thread = new Thread(timer);
    thread.start();

    datagramSocket.send(packet);
//    WinMgr.used_window++;
  }
}
