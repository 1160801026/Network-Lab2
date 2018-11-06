package gbn_withFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server_sender implements Runnable{
  private DatagramSocket datagramSocket;
  InetAddress address;
  int port;
  
  public Server_sender(DatagramSocket datagramSocket) {
    this.datagramSocket=datagramSocket;
  }
  
  public Server_sender(DatagramSocket datagramSocket,InetAddress dest_address,int dest_port) {
    this.datagramSocket=datagramSocket;
    this.address=dest_address;
    this.port=dest_port;
  }
  
  public void sendTime(InetAddress address,int port) throws IOException {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    
    byte[] buf = ("Current Time: "+df.format(new Date())).getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address,port);
    // 发送消息
    System.out.println("System time sent");

    datagramSocket.send(sendPacket);
  }
  
  public void sendBye(InetAddress address,int port) throws IOException {
    byte[] buf = ("Goodbye!").getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address,port);
    // 发送消息
    System.out.println("Stop request accepted.");

    datagramSocket.send(sendPacket);
  }
  
  public void sendBack(byte[] content, InetAddress address,int port) throws IOException {
    DatagramPacket packet=new DatagramPacket(content,content.length,address,port);
    System.out.println("Content sent back.");
    datagramSocket.send(packet);
  }
  
  private void gbn_send(byte[] content,InetAddress address,int port) throws IOException, InterruptedException {
    DatagramPacket packet=new DatagramPacket(content,content.length,address,port);
    datagramSocket.send(packet);
    WinMgr.used_window++;

    Thread.sleep(100);
  }
  
  @Override
  public void run() {
 // TODO Auto-generated method stub
    int i=0;
    System.out.println("GBN test starting.");
    try {
      while(true) {   
        if(WinMgr.used_window<WinMgr.window_size) {
          i++;    
          Frame frame=new Frame(i,"Hello for the "+i+" time!");
          WinMgr.pool.offer(frame);  //待确认的包放入pool中
          System.out.println("Sending packet "+i);
          
          if(i%WinMgr.ack_size==1) {
            Timer4gbn timer=new Timer4gbn(i+WinMgr.ack_size-1,datagramSocket,address,port);   //开始计时
            Thread thread=new Thread(timer);
            thread.start();
          }
          if(i%WinMgr.ack_size==0) {
            WinMgr.queue.offer(i);   //这是期待收到的ack，也即上面开始计时时的i+ack_size-1
          }
          
          gbn_send(frame.getBytes(),address,port);

          if(WinMgr.used_window==4) {
            System.out.println("发送窗口满");
            Thread.sleep(1000);
          }
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