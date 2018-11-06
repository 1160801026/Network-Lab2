package sr_FileTrans;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Timer4sr implements Runnable {
  private int seq; // 该计时器负责计时的包序号
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
            if (WinMgr.unacked.contains(seq)) { // 说明这个包超时了，否则跳出不进行任何操作，线程自动终止
              this.resend();
            }
          }
          break; // 不管是否要重传都应该跳出死循环了
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
    System.out.println("检查到序号" + seq + "发生超时，重新发送");

    Timer4sr timer = new Timer4sr(content, seq, datagramSocket, address, port); // 重新发送的包也需要计时
    Thread thread = new Thread(timer);
    thread.start();

    datagramSocket.send(packet);
//    WinMgr.used_window++;
  }
}
