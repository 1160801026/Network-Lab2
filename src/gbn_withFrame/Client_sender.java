package gbn_withFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client_sender implements Runnable {
  DatagramSocket datagramSocket = new DatagramSocket();
  InetAddress address;
  int port;
  Scanner in = new Scanner(System.in);

  public Client_sender(String netAddress, int port) throws IOException {
    this.port = port;
    address = InetAddress.getByName(netAddress);
  }

  public DatagramSocket getSocket() {
    return datagramSocket;
  }

  public void ack(int seq) throws IOException {
    /***** 返回ACK消息数据报 */
    // 组装数据报
    ACK ack=new ACK(seq);
    byte[] buf = ack.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
    // 发送消息
    System.out.println("ACK " + seq + " sent");

    datagramSocket.send(sendPacket);
  }

  public void send(byte[] content) throws IOException {

    DatagramPacket sendPacket = new DatagramPacket(content, content.length, address, port);
    // 发送消息
    System.out.println("Message Sent");

    datagramSocket.send(sendPacket);
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      while (true) {
        System.out.println("Type in the content you want to send:");
        String temp = in.nextLine();
        byte[] content = (temp).getBytes();
        send(content);

        if (temp.equals("-testgbn")) {
          WinMgr.isgbn=true;
          break;
        }
        Thread.sleep(100);
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

