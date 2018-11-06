package sr_FileTrans;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server_sender implements Runnable {
  private DatagramSocket datagramSocket;
  InetAddress address;
  int port;

  public Server_sender(DatagramSocket datagramSocket) {
    this.datagramSocket = datagramSocket;
  }

  public Server_sender(DatagramSocket datagramSocket, InetAddress dest_address, int dest_port) {
    this.datagramSocket = datagramSocket;
    this.address = dest_address;
    this.port = dest_port;
  }

  public void sendTime(InetAddress address, int port) throws IOException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

    byte[] buf = ("Current Time: " + df.format(new Date())).getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
    // 发送消息
    System.out.println("System time sent");

    datagramSocket.send(sendPacket);
  }

  public void sendBye(InetAddress address, int port) throws IOException {
    byte[] buf = ("Goodbye!").getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
    // 发送消息
    System.out.println("Stop request accepted.");

    datagramSocket.send(sendPacket);
  }

  public void sendBack(byte[] content, InetAddress address, int port) throws IOException {
    DatagramPacket packet = new DatagramPacket(content, content.length, address, port);
    System.out.println("Content sent back.");
    datagramSocket.send(packet);
  }

  private void gbn_send(byte[] content, InetAddress address, int port)
      throws IOException, InterruptedException {
    DatagramPacket packet = new DatagramPacket(content, content.length, address, port);
    datagramSocket.send(packet);
//    WinMgr.used_window++;

    Thread.sleep(100);
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    int i = 1;
    System.out.println("SR test starting.");
    try {
      while (i<WinMgr.input.size()) {
        
        if (i <= WinMgr.maxseq) {   //发送窗口
          
          double rand = Math.random();
          if (rand > WinMgr.packet_loss) {   //随机丢包
            Frame frame = new Frame(i, WinMgr.input.get(i));
            System.out.println("Sending packet " + i);
            WinMgr.unacked.add(i);

            gbn_send(frame.getBytes(), address, port);

            Timer4sr timer = new Timer4sr(frame.getBytes(), i, datagramSocket, address, port); // 开始计时
            Thread thread = new Thread(timer);
            thread.start();
          }
//
//          if (WinMgr.used_window == 4) {
//            System.out.println("发送窗口满");
//            Thread.sleep(1000);
//          }
          
          i++;
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
