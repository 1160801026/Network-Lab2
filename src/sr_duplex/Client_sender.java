package sr_duplex;

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
    /***** ����ACK��Ϣ���ݱ� */
    // ��װ���ݱ�
    ACK ack=new ACK(seq);
    byte[] buf = ack.getBytes();
    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
    // ������Ϣ
    System.out.println("ACK " + seq + " sent");

    datagramSocket.send(sendPacket);
    WinMgr.recved_window--;
  }

  public void send(byte[] content) throws IOException {

    DatagramPacket sendPacket = new DatagramPacket(content, content.length, address, port);
    // ������Ϣ
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

        if(temp.contains("-send")) {
          WinMgr.recvmode=true;  //��Է������˵�recvmode
          System.out.println("Send mode enabled. Waiting.....");
          Thread.sleep(1000);
          int i=1;
          try {
            while (true) {
              
              if (i <= WinMgr.maxseq) {   //���ʹ���
                
                double rand = Math.random();
                if (rand > WinMgr.packet_loss) {   //�������
                  Frame frame = new Frame(i, "Message " + i + " from Client");
                  System.out.println("Sending packet " + i);
                  WinMgr.unacked.add(i);

                  send(frame.getBytes());

                  Timer4sr timer = new Timer4sr(frame.getBytes(), i, datagramSocket, address, port); // ��ʼ��ʱ
                  Thread thread = new Thread(timer);
                  thread.start();
                }
                i++;
                Thread.sleep(100);
              }
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
         }
        
        
        if (temp.contains("-testsr")) {
          WinMgr.issr=true;
          String[] command=temp.split(" ");
          if(command.length>1) {
            double x=Double.parseDouble(command[1]);
            if(x<=1 && x>=0) {
              WinMgr.packet_loss=x;
              if(command.length>2) {
                double y=Double.parseDouble(command[2]);   //��Ҫ�ǳ�ע����ǣ��ͻ��˶Ծ�̬�����ĸı䷢�ͷ���֪��
                if(y<=1 && y>=0) {                         
                  WinMgr.ack_loss=y;                       
                }else {
                  System.out.println("Illegal ack loss rate. Nothing changed.");
                }
              }
            }else {
              System.out.println("Illegal packet loss rate. Nothing changed.");
            }
          }
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

