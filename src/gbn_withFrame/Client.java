package gbn_withFrame;

import java.io.IOException;

public class Client {
  static int window_size=4;
  // 用以存放接收数据的字节数组
  public static void main(String args[]) {
    try {   
      Client_sender client_sender = new Client_sender("localhost",8081);
      Thread thread = new Thread(client_sender);
      Client_recver client_recver=new Client_recver(client_sender);
      Thread thread2 = new Thread(client_recver);
      thread.start(); // 发送请求
      thread2.start(); // 接收报文 
    } catch (IOException e) {
      
    }
  }
}
